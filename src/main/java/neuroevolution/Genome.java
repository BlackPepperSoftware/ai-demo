package neuroevolution;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Genome
{
	private static final double CONNECTION_WEIGHT_MUTATION_RATE = 0.25;
	
	private static final double CONNECTION_MUTATION_RATE = 0.5;

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
		checkConnectionGenesNodes(genes);
		checkConnectionGenesUnique(genes);
		
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
		
		if (random.nextDouble() < CONNECTION_MUTATION_RATE)
		{
			result = result.mutateConnections(geneFactory, random);
		}
		
		// TODO: mutate add node; split existing connection, disable old connection,
		//       add new connection into new node with weight 1, add new connection out of new node with old weight
		
		return result;
	}
	
	public Genome copy()
	{
		return new Genome(this);
	}
	
	public void print(PrintStream out)
	{
		out.println("  " + genes);
	}
	
	Genome mutateConnectionWeights(Random random)
	{
		Stream<ConnectionGene> resultConnectionGenes = getConnectionGenes()
			.map(gene -> gene.mutateWeight(random));
		
		return new Genome(Stream.concat(getNodeGenes(), resultConnectionGenes)
			.collect(toList())
		);
	}
	
	Genome mutateConnections(GeneFactory geneFactory, Random random)
	{
		List<NodeGene> nodeGenes = getNodeGenes().collect(toList());
		if (nodeGenes.isEmpty())
		{
			return this;
		}
		
		NodeGene input = nodeGenes.get(random.nextInt(nodeGenes.size()));
		NodeGene output = nodeGenes.get(random.nextInt(nodeGenes.size()));
		if (input.equals(output) || output.isInput() || connects(input, output))
		{
			return this;
		}
		
		double weight = random.nextDouble();
		
		ConnectionGene gene = geneFactory.newConnectionGene(input, output, weight);
		
		return new Genome(Stream.concat(getGenes(), Stream.of(gene))
			.collect(toList())
		);
	}
	
	private boolean connects(NodeGene input, NodeGene output)
	{
		return getConnectionGenes()
			.map(gene -> new HashSet<>(asList(gene.getInput(), gene.getOutput())))
			.anyMatch(gene -> gene.equals(new HashSet<>(asList(input, output))));
	}
	
	private static Stream<Gene> newNodeGenes(int inputNodeCount, int outputNodeCount)
	{
		return Stream.concat(
			Stream.generate(NodeGene::newInput).limit(inputNodeCount),
			Stream.generate(NodeGene::newOutput).limit(outputNodeCount)
		);
	}
	
	private static void checkConnectionGenesNodes(Collection<Gene> genes)
	{
		List<NodeGene> nodeGenes = genes.stream()
			.filter(gene -> gene instanceof NodeGene)
			.map(NodeGene.class::cast)
			.collect(toList());
		
		boolean valid = genes.stream()
			.filter(gene -> gene instanceof ConnectionGene)
			.map(ConnectionGene.class::cast)
			.flatMap(gene -> Stream.of(gene.getInput(), gene.getOutput()))
			.allMatch(nodeGenes::contains);
		
		if (!valid)
		{
			throw new IllegalArgumentException("Connection gene references unknown node gene");
		}
	}
	
	private static void checkConnectionGenesUnique(Collection<Gene> genes)
	{
		List<Set<NodeGene>> connectionGeneNodes = genes.stream()
			.filter(gene -> gene instanceof ConnectionGene)
			.map(ConnectionGene.class::cast)
			.map(gene -> new HashSet<>(asList(gene.getInput(), gene.getOutput())))
			.collect(toList());
		
		boolean unique = connectionGeneNodes.size() == connectionGeneNodes.stream().distinct().count();
		
		if (!unique)
		{
			throw new IllegalArgumentException("Duplicate connection genes");
		}
	}
}
