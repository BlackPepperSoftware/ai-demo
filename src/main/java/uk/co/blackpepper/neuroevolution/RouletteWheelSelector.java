package uk.co.blackpepper.neuroevolution;

import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class RouletteWheelSelector implements Selector {
	
	private final Random random;
	
	public RouletteWheelSelector(Random random) {
		this.random = random;
	}
	
	@Override
	public Genome select(Stream<Genome> genomes, Map<Genome, Integer> fitnesses) {
		int totalFitness = fitnesses.values()
			.stream()
			.mapToInt(Integer::intValue)
			.sum();
		
		int thresholdFitness = random.nextInt(totalFitness);
		int cumulativeFitness = 0;
		
		for (Genome genome : genomes.collect(toList())) {
			cumulativeFitness += fitnesses.get(genome);
			
			if (cumulativeFitness > thresholdFitness) {
				return genome;
			}
		}
		
		throw new IllegalStateException("No parents found");
	}
}
