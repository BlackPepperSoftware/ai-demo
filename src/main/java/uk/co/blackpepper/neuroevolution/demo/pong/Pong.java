package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import uk.co.blackpepper.neuroevolution.Evolver;
import uk.co.blackpepper.neuroevolution.GeneFactory;
import uk.co.blackpepper.neuroevolution.Genome;
import uk.co.blackpepper.neuroevolution.Population;

import static java.util.Comparator.comparingInt;

public class Pong {
	
	private static final int HEADLESS_TICK_MILLIS = 0;
	
	private static final int TICK_MILLIS = 100;
	
	private static final int MAX_TICKS = 1000;
	
	private static final int POPULATION_SIZE = 100;
	
	private static final int MAX_GENERATIONS = 20;
	
	private static final boolean PLAY_GENERATIONS = true;
	
	private static class ActiveListener extends PongAdapter {
		private final CountDownLatch active = new CountDownLatch(1);
		
		@Override
		public void stopped() {
			active.countDown();
		}
		
		public void waitUntilStopped() {
			try {
				active.await();
			}
			catch (InterruptedException exception) {
				throw new RuntimeException("Error waiting to stop", exception);
			}
		}
	}
	
	private static class TimeAliveListener extends PongAdapter {
		private final int maxTicks;
		
		private int ticks;
		
		public TimeAliveListener(int maxTicks) {
			this.maxTicks = maxTicks;
			ticks = 0;
		}
		
		@Override
		public void tick(Game game) {
			if (ticks++ > maxTicks && maxTicks > 0) {
				game.stop();
			}
		}
		
		public int getTicks() {
			return ticks;
		}
	}
	
	private static class MemoizedToIntFunction<T> implements ToIntFunction<T> {
		
		private final ToIntFunction<T> delegate;
		
		private final Map<T, Integer> cache;
		
		public MemoizedToIntFunction(ToIntFunction<T> delegate) {
			this.delegate = delegate;
			cache = new WeakHashMap<>();
		}
		
		@Override
		public int applyAsInt(T value) {
			if (cache.containsKey(value)) {
				return cache.get(value);
			}
			
			int result = delegate.applyAsInt(value);
			cache.put(value, result);
			return result;
		}
	}
	
	public static void main(String[] args) {
		GeneFactory geneFactory = new GeneFactory();
		Random random = new Random();
		ToIntFunction<Genome> fitness = new MemoizedToIntFunction<>(genome -> evaluateFitness(genome, random, null));
		
		Evolver evolver = new Evolver.Builder()
			.fitness(fitness)
			.geneFactory(geneFactory)
			.random(random)
			.build();
		
		Genome initialGenome = new Genome()
			.addGenes(geneFactory.newInputGenes().limit(6))
			.addGenes(geneFactory.newOutputGenes().limit(3));
		Population population = new Population(Stream.generate(initialGenome::copy).limit(POPULATION_SIZE));
		
		PongFrame frame = new PongFrame();
		frame.setVisible(true);

		AtomicInteger generation = new AtomicInteger();
		Genome fittest = evolver.evolve(population)
			.map(nextPopulation -> show(nextPopulation, generation.incrementAndGet(), fitness, random, frame))
			.limit(MAX_GENERATIONS)
			.collect(evolver.toFittest());
		
		if (!PLAY_GENERATIONS) {
			evaluateFitness(fittest, random, frame);
		}
	}
	
	private static Population show(Population population, int generation, ToIntFunction<Genome> fitness, Random random,
		PongFrame frame) {
		Genome fittest = population.getGenomes()
			.max(comparingInt(fitness))
			.orElseThrow(IllegalStateException::new);
		
		System.out.format("Generation #%d: (%d) %s%n", generation, fitness.applyAsInt(fittest), fittest.toGraphviz());

		if (PLAY_GENERATIONS) {
			evaluateFitness(fittest, random, frame);
		}
		
		return population;
	}
	
	private static int evaluateFitness(Genome genome, Random random, PongFrame frame) {
		boolean headless = (frame == null);

		ActiveListener activeListener = new ActiveListener();
		TimeAliveListener timeAliveListener = new TimeAliveListener(headless ? MAX_TICKS : 0);
		Bot bot = new Bot(genome, 1);

		Game game = new Game(random);
		game.addPongListener(activeListener);
		game.addPongListener(timeAliveListener);
		game.addPongListener(bot);
		
		if (!headless) {
			frame.setGame(game);
		}
		
		game.start(headless ? HEADLESS_TICK_MILLIS : TICK_MILLIS);
		
		activeListener.waitUntilStopped();
		
		return timeAliveListener.getTicks();
	}
}
