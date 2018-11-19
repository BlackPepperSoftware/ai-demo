package uk.co.blackpepper.neuroevolution.demo.pong;

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
			normalize(game.getBall().getDeltaX(), -1, 1),
			normalize(game.getBall().getDeltaY(), -1, 1)
		));
		
		switch (getMaxIndex(outputs.toArray())) {
			case 0:
				game.moveBat(player, -1);
				break;
			
			case 1:
				game.moveBat(player, 1);
				break;
			
			case 2:
				break;
		}
	}
	
	private static double normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}
	
	private static int getMaxIndex(double[] values) {
		int maxIndex = -1;
		double maxValue = 0;
		
		for (int index = 0; index < values.length; index++) {
			if (values[index] > maxValue) {
				maxValue = values[index];
				maxIndex = index;
			}
		}
		return maxIndex;
	}
}
