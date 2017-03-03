package neuroevolution;

import java.io.PrintStream;
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
	private final List<Gene> genes;
	
	public Genome(int inputNodeCount, int outputNodeCount)
	{
		this(newNodeGenes(inputNodeCount, outputNodeCount));
	}
	
	public Genome(Gene... genes)
	{
		this(Stream.of(genes));
	}
	
	public Genome(Stream<Gene> genes)
	{
		List<Gene> genesList = genes.collect(toList());
		checkConnectionGenesNodes(genesList);
		checkConnectionGenesUnique(genesList);
		
		this.genes = genesList;
	}
	
	private Genome(Genome that)
	{
		this(that.getGenes().map(Gene::copy));
	}
	
	public Genome addGene(Gene gene)
	{
		return new Genome(Stream.concat(getGenes(), Stream.of(gene)));
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
	
	public boolean connects(NodeGene input, NodeGene output)
	{
		return getConnectionGenes()
			.map(gene -> new HashSet<>(asList(gene.getInput(), gene.getOutput())))
			.anyMatch(gene -> gene.equals(new HashSet<>(asList(input, output))));
	}
	
	public Genome mutate(GeneFactory geneFactory, Random random)
	{
		return new GenomeMutator(geneFactory, random).mutate(this);
	}
	
	public Genome copy()
	{
		return new Genome(this);
	}
	
	public void print(PrintStream out)
	{
		out.println("  " + genes);
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
