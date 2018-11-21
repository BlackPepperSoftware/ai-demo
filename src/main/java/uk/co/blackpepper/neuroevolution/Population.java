package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Population {
	
	private final List<Genome> genomes;
	
	public Population(Stream<Genome> genomes) {
		this.genomes = genomes.collect(toList());
	}
	
	public Stream<Genome> getGenomes() {
		return genomes.stream();
	}
	
	public int getSize() {
		return genomes.size();
	}
}
