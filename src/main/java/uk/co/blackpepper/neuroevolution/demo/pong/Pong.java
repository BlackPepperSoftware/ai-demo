package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.ToIntFunction;

import uk.co.blackpepper.neuroevolution.GeneFactory;
import uk.co.blackpepper.neuroevolution.Genome;
import uk.co.blackpepper.neuroevolution.Population;

import static java.util.Comparator.comparingInt;

public class Pong {
	
	private static final int HEADLESS_TICK_MILLIS = 1;
	
	private static final int TICK_MILLIS = 100;
	
	private static final int MAX_TICKS = 1000;
	
	private static final int POPULATION_SIZE = 100;
	
	private static final int MAX_GENERATIONS = 20;
	
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
		Population population = new Population(POPULATION_SIZE, 6, 3, geneFactory, random);
		
		ToIntFunction<Genome> fitness = genome -> evaluateFitness(genome, random, null);
		
		PongFrame frame = new PongFrame();

		for (int generation = 1; generation <= MAX_GENERATIONS; generation++) {
			ToIntFunction<Genome> memoizedFitness = new MemoizedToIntFunction<>(fitness);
			
			population = population.evolve(memoizedFitness);
			
			Genome fittest = population.getGenomes()
				.max(comparingInt(memoizedFitness))
				.orElseThrow(IllegalStateException::new);
			
			System.out.format("Generation #%d: %d %s%n", generation, memoizedFitness.applyAsInt(fittest), fittest);
			
			frame.setVisible(true);
			evaluateFitness(fittest, random, frame);
			frame.setVisible(false);
		}
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
