package uk.co.blackpepper.neuroevolution;

import java.util.List;

import static java.util.Arrays.asList;

class CompositeMutator implements Mutator {
	
	private final List<Mutator> mutators;
	
	public CompositeMutator(Mutator... mutators) {
		this(asList(mutators));
	}
	
	public CompositeMutator(List<Mutator> mutators) {
		this.mutators = mutators;
	}
	
	@Override
	public Genome mutate(Genome genome) {
		for (Mutator mutator : mutators) {
			genome = mutator.mutate(genome);
		}
		
		return genome;
	}
}
