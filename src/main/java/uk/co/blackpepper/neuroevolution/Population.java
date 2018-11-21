package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Population {
	
	private final List<Genome> genomes;
	
	public Population(int size, int inputNodeCount, int outputNodeCount, GeneFactory geneFactory) {
		this(newGenomes(size, inputNodeCount, outputNodeCount, geneFactory));
	}
	
	public Population(Stream<Genome> genomes) {
		this.genomes = genomes.collect(toList());
	}
	
	public Stream<Genome> getGenomes() {
		return genomes.stream();
	}
	
	public int getSize() {
		return genomes.size();
	}
	
	private static Stream<Genome> newGenomes(int size, int inputNodeCount, int outputNodeCount,
		GeneFactory geneFactory) {
		Genome genome = new Genome()
			.addInputNodes(inputNodeCount, geneFactory)
			.addOutputNodes(outputNodeCount, geneFactory);
		
		return Stream.generate(genome::copy)
			.limit(size);
	}
}
