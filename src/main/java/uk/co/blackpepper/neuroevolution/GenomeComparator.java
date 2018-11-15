package uk.co.blackpepper.neuroevolution;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * Compares genomes by species.
 */
class GenomeComparator implements Comparator<Genome> {
	
	private static final double EXCESS_COEFFICIENT = 2.0;
	
	private static final double DISJOINT_COEFFICIENT = 2.0;
	
	private static final double WEIGHT_COEFFICIENT = 0.4;
	
	private static final double COMPATIBILITY_THRESHOLD = 1.0;
	
	@Override
	public int compare(Genome genome1, Genome genome2) {
		boolean compatible = compatibilityDistance(genome1, genome2) < COMPATIBILITY_THRESHOLD;
		
		return compatible ? 0 : 1;
	}
	
	private double compatibilityDistance(Genome genome1, Genome genome2) {
		long maxGeneCount = Math.max(genome1.getGenes().count(), genome2.getGenes().count());
		
		if (maxGeneCount == 0) {
			return 0;
		}
		
		double excessDistance = EXCESS_COEFFICIENT * excessGeneCount(genome1, genome2) / maxGeneCount;
		double disjointDistance = DISJOINT_COEFFICIENT * disjointGeneCount(genome1, genome2) / maxGeneCount;
		double weightDistance = WEIGHT_COEFFICIENT * averageWeightDifferences(genome1, genome2);
		
		return excessDistance + disjointDistance + weightDistance;
	}
	
	int excessGeneCount(Genome genome1, Genome genome2) {
		int maxInnovation = Math.min(getMaxInnovation(genome1), getMaxInnovation(genome2));
		
		return (int) Stream.concat(genome1.getConnectionGenes(), genome2.getConnectionGenes())
			.map(ConnectionGene::getInnovation)
			.filter(innovation -> innovation > maxInnovation)
			.count();
	}
	
	int disjointGeneCount(Genome genome1, Genome genome2) {
		int maxInnovation = Math.min(getMaxInnovation(genome1), getMaxInnovation(genome2));
		
		Set<Integer> innovations1 = genome1.getConnectionGenes()
			.map(ConnectionGene::getInnovation)
			.filter(innovation -> innovation <= maxInnovation)
			.collect(toSet());
		
		Set<Integer> innovations2 = genome2.getConnectionGenes()
			.map(ConnectionGene::getInnovation)
			.filter(innovation -> innovation <= maxInnovation)
			.collect(toSet());
		
		Set<Integer> disjointInnovations1 = new HashSet<>(innovations1);
		disjointInnovations1.removeAll(innovations2);
		
		Set<Integer> disjointInnovations2 = new HashSet<>(innovations2);
		disjointInnovations2.removeAll(innovations1);
		
		return disjointInnovations1.size() + disjointInnovations2.size();
	}
	
	double averageWeightDifferences(Genome genome1, Genome genome2) {
		Map<Integer, List<ConnectionGene>> genesByInnovation =
			Stream.concat(genome1.getConnectionGenes(), genome2.getConnectionGenes())
				.collect(groupingBy(ConnectionGene::getInnovation));
		
		return genesByInnovation.values()
			.stream()
			.filter(genes -> genes.size() == 2)
			.mapToDouble(genes -> Math.abs(genes.get(0).getWeight() - genes.get(1).getWeight()))
			.average()
			.orElse(0);
	}
	
	private static int getMaxInnovation(Genome genome) {
		return genome.getConnectionGenes()
			.mapToInt(ConnectionGene::getInnovation)
			.max()
			.orElse(0);
	}
}
