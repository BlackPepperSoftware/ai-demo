package neuroevolution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Genome
{
	private static final double EXCESS_COMPATIBILITY_COEFFICIENT = 2.0;
	
	private static final double DISJOINT_COMPATIBILITY_COEFFICIENT = 2.0;
	
	private static final double WEIGHT_COMPATIBILITY_COEFFICIENT = 0.4;
	
	private static final double COMPATIBILITY_THRESHOLD = 1.0;
	
	private final List<Gene> genes;
	
	public Genome(Gene... genes)
	{
		this.genes = new ArrayList<>(asList(genes));
	}
	
	public Genome(Genome that)
	{
		genes = that.genes.stream()
			.map(Gene::new)
			.collect(toList());
	}
	
	public boolean sameSpecies(Genome that)
	{
		return compatibilityDistance(that) < COMPATIBILITY_THRESHOLD;
	}
	
	public Genome mutate()
	{
		Genome genome = new Genome(this);
		
		// TODO: mutate
		
		return genome;
	}
	
	private double compatibilityDistance(Genome that)
	{
		int maxGeneCount = Math.max(genes.size(), that.genes.size());
		
		if (maxGeneCount == 0)
		{
			return 0;
		}
		
		double excessDistance = EXCESS_COMPATIBILITY_COEFFICIENT * excessGeneCount(that) / maxGeneCount;
		double disjointDistance = DISJOINT_COMPATIBILITY_COEFFICIENT * disjointGeneCount(that) / maxGeneCount;
		double weightDistance = WEIGHT_COMPATIBILITY_COEFFICIENT * averageWeightDifferences(that);
		
		return excessDistance + disjointDistance + weightDistance;
	}
	
	int excessGeneCount(Genome that)
	{
		int maxInnovation = Math.min(getMaxInnovation(), that.getMaxInnovation());
		
		return (int) Stream.concat(genes.stream(), that.genes.stream())
			.map(Gene::getInnovation)
			.filter(innovation -> innovation > maxInnovation)
			.count();
	}
	
	int disjointGeneCount(Genome that)
	{
		int maxInnovation = Math.min(getMaxInnovation(), that.getMaxInnovation());
		
		Set<Integer> thisInnovations = genes.stream()
			.map(Gene::getInnovation)
			.filter(innovation -> innovation <= maxInnovation)
			.collect(toSet());
		
		Set<Integer> thatInnovations = that.genes.stream()
			.map(Gene::getInnovation)
			.filter(innovation -> innovation <= maxInnovation)
			.collect(toSet());
		
		Set<Integer> thisDisjointInnovations = new HashSet<>(thisInnovations);
		thisDisjointInnovations.removeAll(thatInnovations);
		
		Set<Integer> thatDisjointInnovations = new HashSet<>(thatInnovations);
		thatDisjointInnovations.removeAll(thisInnovations);
		
		return thisDisjointInnovations.size() + thatDisjointInnovations.size();
	}
	
	double averageWeightDifferences(Genome that)
	{
		Map<Integer, List<Gene>> genesByInnovation = Stream.concat(genes.stream(), that.genes.stream())
			.collect(groupingBy(Gene::getInnovation));
		
		return genesByInnovation.values()
			.stream()
			.filter(genes -> genes.size() == 2)
			.mapToDouble(genes -> Math.abs(genes.get(0).getWeight() - genes.get(1).getWeight()))
			.average()
			.orElse(0);
	}
	
	private int getMaxInnovation()
	{
		return genes.stream()
			.mapToInt(Gene::getInnovation)
			.max()
			.orElse(0);
	}
}
