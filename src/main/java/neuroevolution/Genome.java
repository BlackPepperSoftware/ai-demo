package neuroevolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Genome
{
	private static final double CONNECTION_WEIGHT_MUTATION_RATE = 0.25;
	
	private final List<Gene> genes;
	
	public Genome(Gene... genes)
	{
		this(asList(genes));
	}
	
	public Genome(Collection<Gene> genes)
	{
		this.genes = new ArrayList<>(genes);
	}
	
	public Genome(Genome that)
	{
		this(that.getGenes()
			.map(Gene::new)
			.collect(toList())
		);
	}
	
	public Stream<Gene> getGenes()
	{
		return genes.stream();
	}
	
	public Genome mutate(Random random)
	{
		Genome result = new Genome(this);
		
		// TODO: mutate mutation rates?
		
		if (random.nextDouble() < CONNECTION_WEIGHT_MUTATION_RATE)
		{
			result = result.mutateConnectionWeights(random);
		}
		
		// TODO: mutate add connection; random weight, connect two existing nodes
		// TODO: mutate add node; split existing connection, disable old connection,
		//       add new connection into new node with weight 1, add new connection out of new node with old weight
		
		return result;
	}
	
	Genome mutateConnectionWeights(Random random)
	{
		return new Genome(getGenes()
			.map(gene -> gene.mutateConnectionWeight(random))
			.collect(toList())
		);
	}
}
