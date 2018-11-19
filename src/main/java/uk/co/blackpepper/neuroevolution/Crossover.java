package uk.co.blackpepper.neuroevolution;

import java.util.Map;

public interface Crossover {
	
	Genome crossover(Genome parent1, Genome parent2, Map<Genome, Integer> fitnesses);
}
