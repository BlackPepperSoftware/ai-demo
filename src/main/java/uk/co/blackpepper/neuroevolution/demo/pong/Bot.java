package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.Arrays;
import java.util.stream.DoubleStream;

import uk.co.blackpepper.neuroevolution.Genome;

public class Bot extends PongAdapter {
	
	private final Genome genome;
	
	private final int player;
	
	public Bot(Genome genome, int player) {
		this.genome = genome;
		this.player = player;
	}
	
	@Override
	public void tick(Game game) {
		DoubleStream outputs = genome.evaluate(DoubleStream.of(
			normalize(game.getBat1().getY(), 0, game.getScreenSize().height),
			normalize(game.getBat2().getY(), 0, game.getScreenSize().height),
			normalize(game.getBall().getX(), 0, game.getScreenSize().width),
			normalize(game.getBall().getY(), 0, game.getScreenSize().height),
			normalize(game.getBall().getDeltaX(), -Bat.SPEED, Bat.SPEED),
			normalize(game.getBall().getDeltaY(), -Bat.SPEED, Bat.SPEED)
		));
		
		Bounceable bat = (player == 0) ? game.getBat1() : game.getBat2();
		
		switch (getMaxIndex(outputs.toArray())) {
			case 0:
				// do nothing
				break;
			
			case 1:
				bat.moveUp();
				break;
			
			case 2:
				bat.moveDown();
				break;
		}
	}
	
	private static double normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}
	
	private static int getMaxIndex(double[] values) {
		int maxIndex = -1;
		double maxValue = -Double.MAX_VALUE;
		
		for (int index = 0; index < values.length; index++) {
			if (values[index] > maxValue) {
				maxValue = values[index];
				maxIndex = index;
			}
		}
		
		return maxIndex;
	}
}
