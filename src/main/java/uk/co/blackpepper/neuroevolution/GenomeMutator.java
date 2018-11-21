package uk.co.blackpepper.neuroevolution;

import java.util.Random;

class GenomeMutator implements Mutator {
	
	private final GeneFactory geneFactory;
	
	private final Random random;
	
	public GenomeMutator(GeneFactory geneFactory, Random random) {
		this.geneFactory = geneFactory;
		this.random = random;
	}
	
	@Override
	public Genome mutate(Genome genome) {
		Genome result = genome.copy();
		
		// TODO: mutate mutation rates?
		
		result = new ConnectionWeightMutator(random).mutate(result);
		
		result = new ConnectionMutator(geneFactory, random).mutate(result);
		
		result = new NodeMutator(geneFactory, random).mutate(result);
		
		// TODO: mutate enabled/disabled genes
		
		return result;
	}
}
