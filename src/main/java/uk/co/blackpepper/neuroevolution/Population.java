package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Population {
	
	private final List<Species> species;

	public Population(Stream<Species> species) {
	    this.species = species.collect(toList());
    }
	
	public Stream<Genome> getGenomes() {
		return species.stream().flatMap(Species::getGenomes);
	}

	public Stream<Species> getSpecies() {
	    return species.stream();
    }
	
	public long getSize() {
		return species.stream()
                .mapToLong(Species::getSize)
                .sum();
	}
}
