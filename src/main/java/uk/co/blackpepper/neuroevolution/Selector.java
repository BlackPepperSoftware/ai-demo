package uk.co.blackpepper.neuroevolution;

import java.util.Map;
import java.util.stream.Stream;

public interface Selector {
	
	Genome select(Stream<Genome> genomes, Map<Genome, Integer> fitnesses);
}
