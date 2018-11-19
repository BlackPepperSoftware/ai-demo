package uk.co.blackpepper.neuroevolution;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Population {
	
	private final List<Genome> genomes;
	
	public Population(int size, int inputNodeCount, int outputNodeCount) {
		genomes = Stream.generate(() -> new Genome().addInputNodes(inputNodeCount).addOutputNodes(outputNodeCount))
			.limit(size)
			.collect(toList());
	}
	
	public Stream<Genome> getGenomes() {
		return genomes.stream();
	}
	
	public void print(PrintStream out) {
		out.println("Population:");
		
		genomes.forEach(genome -> genome.print(out));
	}
}
