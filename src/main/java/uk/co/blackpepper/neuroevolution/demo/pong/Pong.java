package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.concurrent.CountDownLatch;

import uk.co.blackpepper.neuroevolution.GeneFactory;
import uk.co.blackpepper.neuroevolution.Genome;
import uk.co.blackpepper.neuroevolution.Population;

public class Pong {
	
	private static final boolean HEADLESS = true;
	
	private static final int HEADLESS_TICK_MILLIS = 1;
	
	private static final int TICK_MILLIS = 100;
	
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
	
	public static void main(String[] args) {
		GeneFactory geneFactory = new GeneFactory();
		Population population = new Population(10, 6, 3, geneFactory);
		
		PongFrame frame = HEADLESS ? null : new PongFrame();
		if (!HEADLESS) {
			frame.setVisible(true);
		}
		
		population.print(System.out);
		
		Population nextPopulation = population.evolve(genome -> evaluateFitness(genome, frame));
		nextPopulation.print(System.out);
	}
	
	private static int evaluateFitness(Genome genome, PongFrame frame) {
		ActiveListener activeListener = new ActiveListener();
		TimeAliveListener timeAliveListener = new TimeAliveListener();
		Bot bot = new Bot(genome, 1);

		Game game = new Game();
		game.addPongListener(activeListener);
		game.addPongListener(timeAliveListener);
		game.addPongListener(bot);
		
		if (frame != null) {
			frame.setGame(game);
		}
		
		game.start(HEADLESS ? HEADLESS_TICK_MILLIS : TICK_MILLIS);
		
		activeListener.waitUntilStopped();
		
		return timeAliveListener.getTime();
	}
}
