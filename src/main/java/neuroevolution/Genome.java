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
	
	private static final double ADD_CONNECTION_MUTATION_RATE = 0.5;

	private final List<Gene> genes;
	
	public Genome(int inputNodeCount, int outputNodeCount)
	{
		this(newNodeGenes(inputNodeCount, outputNodeCount).collect(toList()));
	}
	
	public Genome(Gene... genes)
	{
		this(asList(genes));
	}
	
	public Genome(Collection<Gene> genes)
	{
		this.genes = new ArrayList<>(genes);
	}
	
	private Genome(Genome that)
	{
		this(that.getGenes()
			.map(Gene::copy)
			.collect(toList())
		);
	}
	
	public Stream<Gene> getGenes()
	{
		return genes.stream();
	}
	
	public Stream<NodeGene> getNodeGenes()
	{
		return genes.stream()
			.filter(gene -> gene instanceof NodeGene)
			.map(NodeGene.class::cast);
	}
	
	public Stream<ConnectionGene> getConnectionGenes()
	{
		return genes.stream()
			.filter(gene -> gene instanceof ConnectionGene)
			.map(ConnectionGene.class::cast);
	}
	
	public Genome mutate(GeneFactory geneFactory, Random random)
	{
		Genome result = copy();
		
		// TODO: mutate mutation rates?
		
		if (random.nextDouble() < CONNECTION_WEIGHT_MUTATION_RATE)
		{
			result = result.mutateConnectionWeights(random);
		}
		
		if (random.nextDouble() < ADD_CONNECTION_MUTATION_RATE)
		{
			result = result.addConnection(geneFactory, random);
		}
		
		// TODO: mutate add node; split existing connection, disable old connection,
		//       add new connection into new node with weight 1, add new connection out of new node with old weight
		
		return result;
	}
	
	public Genome copy()
	{
		return new Genome(this);
	}
	
	Genome mutateConnectionWeights(Random random)
	{
		Stream<ConnectionGene> resultConnectionGenes = getConnectionGenes()
			.map(gene -> gene.mutateWeight(random));
		
		return new Genome(Stream.concat(getNodeGenes(), resultConnectionGenes)
			.collect(toList())
		);
	}
	
	Genome addConnection(GeneFactory geneFactory, Random random)
	{
		// TODO: choose random input and output nodes for connection
		double weight = random.nextDouble();
		
		ConnectionGene gene = geneFactory.newConnectionGene(weight);
		
		return new Genome(Stream.concat(getGenes(), Stream.of(gene))
			.collect(toList())
		);
	}
	
	private static Stream<Gene> newNodeGenes(int inputNodeCount, int outputNodeCount)
	{
		return Stream.concat(
			Stream.generate(() -> NodeGene.INPUT).limit(inputNodeCount),
			Stream.generate(() -> NodeGene.OUTPUT).limit(outputNodeCount)
		);
	}
}
