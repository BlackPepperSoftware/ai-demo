package uk.co.blackpepper.neuroevolution.demo.pong;

import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import uk.co.blackpepper.neuroevolution.ConnectionGene;
import uk.co.blackpepper.neuroevolution.GeneFactory;
import uk.co.blackpepper.neuroevolution.Genome;

import static java.util.stream.Collectors.toList;

public class Bot implements PongListener {
	
	private final Genome genome;
	
	public Bot() {
		genome = createGenome();
	}
	
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
				game.moveBat(1, -1);
				break;
			
			case 1:
				game.moveBat(1, 1);
				break;
			
			case 2:
				break;
		}
	}
	
	private static Genome createGenome() {
		Random random = new Random();
		GeneFactory geneFactory = new GeneFactory();
		
		Genome bot1 = new Genome()
			.addInputNodes(6)
			.addOutputNodes(3);
		Genome bot2 = null;
		
		boolean okay = false;
		while (!okay) {
			try {
				bot2 = bot1.addGenes(Stream.generate(() -> newConnectionGene(bot1, geneFactory, random))
					.limit(5)
				);
				okay = true;
			}
			catch (IllegalArgumentException exception) {
				okay = false;
			}
		}
		
		return bot2;
	}
	
	private static ConnectionGene newConnectionGene(Genome genome, GeneFactory geneFactory, Random random) {
		return geneFactory.newConnectionGene(
			randomElement(genome.getInputGenes().collect(toList()), random),
			randomElement(genome.getOutputGenes().collect(toList()), random),
			random.nextDouble()
		);
	}
	
	private static <T> T randomElement(List<T> list, Random random) {
		return list.get(random.nextInt(list.size()));
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
