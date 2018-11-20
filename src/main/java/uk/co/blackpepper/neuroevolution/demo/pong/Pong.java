package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.ToIntFunction;

import uk.co.blackpepper.neuroevolution.GeneFactory;
import uk.co.blackpepper.neuroevolution.Genome;
import uk.co.blackpepper.neuroevolution.Population;

import static java.util.Comparator.comparingInt;

public class Pong {
	
	private static final boolean HEADLESS = true;
	
	private static final int HEADLESS_TICK_MILLIS = 1;
	
	static final int TICK_MILLIS = 100;
	
	private static final int MAX_GENERATIONS = 10;
	
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
		private int time = 0;
		
		@Override
		public void tick(Game game) {
			time++;
		}
		
		public int getTime() {
			return time;
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
		Population population = new Population(10, 6, 3, geneFactory);
		
		PongFrame frame = HEADLESS ? null : new PongFrame();
		if (!HEADLESS) {
			frame.setVisible(true);
		}
		
		ToIntFunction<Genome> fitness = new MemoizedToIntFunction<>(genome -> evaluateFitness(genome, frame));
		Genome fittest = null;
		
		for (int generation = 1; generation <= MAX_GENERATIONS; generation++) {
			population = population.evolve(fitness);
			
			fittest = population.getGenomes()
				.max(comparingInt(fitness))
				.orElseThrow(IllegalStateException::new);
			
			System.out.format("Generation #%d: %s%n", generation, fittest);
		}
		
		PongFrame fittestFrame = new PongFrame();
		fittestFrame.setVisible(true);
		evaluateFitness(fittest, fittestFrame);
	}
	
	private static int evaluateFitness(Genome genome, PongFrame frame) {
		ActiveListener activeListener = new ActiveListener();
		TimeAliveListener timeAliveListener = new TimeAliveListener();
		Bot bot = new Bot(genome, 1);

		Game game = new Game();
		game.addPongListener(activeListener);
		game.addPongListener(timeAliveListener);
		game.addPongListener(bot);
		
		boolean headless = (frame == null);
		if (!headless) {
			frame.setGame(game);
		}
		
		game.start(headless ? HEADLESS_TICK_MILLIS : TICK_MILLIS);
		
		activeListener.waitUntilStopped();
		
		return timeAliveListener.getTime();
	}
}
