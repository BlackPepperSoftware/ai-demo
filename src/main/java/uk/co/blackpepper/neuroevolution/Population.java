package uk.co.blackpepper.neuroevolution;

import java.io.PrintStream;
import java.util.Random;
import java.util.stream.Stream;

public class Population {
	
	private Species species;
	
	public Population(int size, int inputNodeCount, int outputNodeCount, GeneFactory geneFactory, Random random) {
		species = new Species();
		
		Stream.generate(() -> new Genome().addInputNodes(inputNodeCount).addOutputNodes(outputNodeCount))
			.limit(size)
			.forEach(this::addGenome);
	}
	
	public Stream<Genome> getGenomes() {
		return species.getGenomes();
	}
	
	public void print(PrintStream out) {
		species.print(out);
	}
	
	private void addGenome(Genome genome) {
		species.addGenome(genome);
	}
}
