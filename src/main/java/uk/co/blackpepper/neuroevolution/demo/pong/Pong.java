package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.Arrays;
import java.util.Random;

import uk.co.blackpepper.neuroevolution.GeneFactory;
import uk.co.blackpepper.neuroevolution.Genome;
import uk.co.blackpepper.neuroevolution.Population;

public class Pong {
	
	private static class ActiveListener extends PongAdapter {
		private boolean active = true;
		
		@Override
		public void stopped() {
			active = false;
		}
		
		public boolean isActive() {
			return active;
		}
		
		public void waitUntilStopped() {
			// TODO: Wait better
			while (active) {
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
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
		// TODO: Do we want to specify the seed? And should this be shared between populations?
		Random random = new Random();
		GeneFactory geneFactory = new GeneFactory();
		Population population = new Population(10, 6, 3, geneFactory, random);
		
		// evaluate fitness of each individual
		
		int[] fitness = population.getGenomes()
			.mapToInt(Pong::evaluateFitness)
			.toArray();

		System.out.println("Fitness: " + Arrays.toString(fitness));
	}
	
	private static int evaluateFitness(Genome genome) {
		ActiveListener activeListener = new ActiveListener();
		TimeAliveListener timeAliveListener = new TimeAliveListener();
		Bot bot = new Bot(genome, 1);
		
		PongFrame frame = new PongFrame();
		frame.addPongListener(activeListener);
		frame.addPongListener(timeAliveListener);
		frame.addPongListener(bot);
		frame.setVisible(true);
		
		activeListener.waitUntilStopped();
		
		frame.setVisible(false);
		
		return timeAliveListener.getTime();
	}
}
