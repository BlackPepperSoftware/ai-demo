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
		
		return result;
	}
	
	Genome mutateConnectionWeights(Genome genome)
	{
		Stream<ConnectionGene> resultConnectionGenes = genome.getConnectionGenes()
			.map(this::mutateConnectionWeight);
		
		return new Genome(Stream.concat(genome.getNodeGenes(), resultConnectionGenes));
	}
	
	ConnectionGene mutateConnectionWeight(ConnectionGene gene)
	{
		// TODO: introduce low probability of randomising rather than perturbing
		
		double resultWeight = gene.getWeight() + (2 * random.nextDouble() - 1) * CONNECTION_WEIGHT_MUTATION_STEP;
		
		return new ConnectionGene(gene.getInput(), gene.getOutput(), resultWeight, gene.isEnabled(),
			gene.getInnovation());
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
		List<ConnectionGene> connectionGenes = genome.getConnectionGenes().collect(toList());
		if (connectionGenes.isEmpty())
		{
			return genome;
		}
		
		ConnectionGene oldConnectionGene = connectionGenes.get(random.nextInt(connectionGenes.size()));
		
		Stream<Gene> newGenes = genome.getGenes()
			.map(gene -> gene.equals(oldConnectionGene) ? oldConnectionGene.disable() : gene);
		
		NodeGene newNodeGene = newHidden();
		ConnectionGene newConnectionGene1 = geneFactory.newConnectionGene(oldConnectionGene.getInput(), newNodeGene, 1);
		ConnectionGene newConnectionGene2 = geneFactory.newConnectionGene(newNodeGene, oldConnectionGene.getOutput(),
			oldConnectionGene.getWeight());
		
		return new Genome(newGenes)
			.addGene(newNodeGene)
			.addGene(newConnectionGene1)
			.addGene(newConnectionGene2);
	}
}
