package neuroevolution;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class GenomeMutator
{
	private static final double CONNECTION_WEIGHT_MUTATION_RATE = 0.25;
	
	private static final double CONNECTION_MUTATION_RATE = 0.5;
	
	private static final double NODE_MUTATION_RATE = 0.5;
	
	private final GeneFactory geneFactory;
	
	private final Random random;
	
	public GenomeMutator(GeneFactory geneFactory, Random random)
	{
		this.geneFactory = geneFactory;
		this.random = random;
	}
	
	public Genome mutate(Genome genome)
	{
		Genome result = genome.copy();
		
		// TODO: mutate mutation rates?
		
		if (random.nextDouble() < CONNECTION_WEIGHT_MUTATION_RATE)
		{
			result = mutateConnectionWeights(result);
		}
		
		if (random.nextDouble() < CONNECTION_MUTATION_RATE)
		{
			result = mutateConnections(result);
		}
		
		if (random.nextDouble() < NODE_MUTATION_RATE)
		{
			result = mutateNodes(result);
		}
		
		return result;
	}
	
	Genome mutateConnectionWeights(Genome genome)
	{
		Stream<ConnectionGene> resultConnectionGenes = genome.getConnectionGenes()
			.map(gene -> gene.mutateWeight(random));
		
		return new Genome(Stream.concat(genome.getNodeGenes(), resultConnectionGenes));
	}
	
	Genome mutateConnections(Genome genome)
	{
		List<NodeGene> nodeGenes = genome.getNodeGenes().collect(toList());
		if (nodeGenes.isEmpty())
		{
			return genome;
		}
		
		NodeGene input = nodeGenes.get(random.nextInt(nodeGenes.size()));
		NodeGene output = nodeGenes.get(random.nextInt(nodeGenes.size()));
		if (input.equals(output) || output.isInput() || genome.connects(input, output))
		{
			return genome;
		}
		
		double weight = random.nextDouble();
		
		ConnectionGene gene = geneFactory.newConnectionGene(input, output, weight);
		
		return genome.addGene(gene);
	}
	
	Genome mutateNodes(Genome genome)
	{
		// TODO: mutate add node; split existing connection, disable old connection,
		//       add new connection into new node with weight 1, add new connection out of new node with old weight
		
		return genome;
	}
}
