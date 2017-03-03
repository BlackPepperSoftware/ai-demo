package neuroevolution;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

import static neuroevolution.NodeGene.newHidden;

class GenomeMutator
{
	private static final double CONNECTION_WEIGHT_MUTATION_RATE = 0.25;
	
	private static final double CONNECTION_WEIGHT_MUTATION_STEP = 0.1;
	
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
		
		// TODO: mutate enabled/disabled genes
		
		return result;
	}
	
	Genome mutateConnectionWeights(Genome genome)
	{
		Stream<ConnectionGene> connections = genome.getConnectionGenes()
			.map(this::mutateConnectionWeight);
		
		return new Genome(Stream.concat(genome.getNodeGenes(), connections));
	}
	
	ConnectionGene mutateConnectionWeight(ConnectionGene connection)
	{
		// TODO: introduce low probability of randomising rather than perturbing
		
		double newWeight = connection.getWeight() + (2 * random.nextDouble() - 1) * CONNECTION_WEIGHT_MUTATION_STEP;
		
		return new ConnectionGene(connection.getInput(), connection.getOutput(), newWeight, connection.isEnabled(),
			connection.getInnovation());
	}
	
	Genome mutateConnections(Genome genome)
	{
		List<NodeGene> nodes = genome.getNodeGenes()
			.collect(toList());
		
		if (nodes.isEmpty())
		{
			return genome;
		}
		
		NodeGene input = nodes.get(random.nextInt(nodes.size()));
		NodeGene output = nodes.get(random.nextInt(nodes.size()));
		
		if (input.equals(output) || output.isInput() || genome.connects(input, output))
		{
			return genome;
		}
		
		double newWeight = random.nextDouble();
		
		return genome.addGene(geneFactory.newConnectionGene(input, output, newWeight));
	}
	
	Genome mutateNodes(Genome genome)
	{
		List<ConnectionGene> connections = genome.getConnectionGenes()
			.collect(toList());
		
		if (connections.isEmpty())
		{
			return genome;
		}
		
		ConnectionGene connection = connections.get(random.nextInt(connections.size()));
		NodeGene newNode = newHidden();
		
		return genome.disableGene(connection)
			.addGene(newNode)
			.addGene(geneFactory.newConnectionGene(connection.getInput(), newNode, 1))
			.addGene(geneFactory.newConnectionGene(newNode, connection.getOutput(), connection.getWeight()));
	}
}
