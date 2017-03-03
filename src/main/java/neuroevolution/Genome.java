package neuroevolution;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
		this(copyGenes(that.getGenes().collect(toList())));
	}
	
	public Genome addGene(Gene gene)
	{
		return new Genome(Stream.concat(getGenes(), Stream.of(gene)));
	}
	
	public Genome disableGene(ConnectionGene gene)
	{
		if (!genes.contains(gene))
		{
			throw new IllegalArgumentException("Unknown gene");
		}
		
		return new Genome(genes.stream()
			.map(g -> g.equals(gene) ? ((ConnectionGene) g).disable() : g)
		);
	}
	
	public Stream<Gene> getGenes()
	{
		return genes.stream();
	}
	
	public Stream<NodeGene> getNodeGenes()
	{
		return getNodeGenes(genes);
	}
	
	public Stream<ConnectionGene> getConnectionGenes()
	{
		return getConnectionGenes(genes);
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
	
	private static Stream<Gene> copyGenes(Collection<Gene> genes)
	{
		Map<NodeGene, NodeGene> resultNodeGenesByOriginal = getNodeGenes(genes)
			.collect(toMap(gene -> gene, NodeGene::copy, throwingMerger(), LinkedHashMap::new));
		
		Stream<ConnectionGene> resultConnectionGenes = getConnectionGenes(genes)
			.map(gene -> new ConnectionGene(
				resultNodeGenesByOriginal.get(gene.getInput()),
				resultNodeGenesByOriginal.get(gene.getOutput()),
				gene.getWeight(),
				gene.isEnabled(),
				gene.getInnovation()
			));
		
		return Stream.concat(resultNodeGenesByOriginal.values().stream(), resultConnectionGenes);
	}
	
	private static void checkConnectionGenesNodes(Collection<Gene> genes)
	{
		List<NodeGene> nodeGenes = getNodeGenes(genes)
			.collect(toList());
		
		boolean valid = getConnectionGenes(genes)
			.flatMap(gene -> Stream.of(gene.getInput(), gene.getOutput()))
			.allMatch(nodeGenes::contains);
		
		if (!valid)
		{
			throw new IllegalArgumentException("Connection gene references unknown node gene");
		}
	}
	
	private static void checkConnectionGenesUnique(Collection<Gene> genes)
	{
		List<Set<NodeGene>> connectionGeneNodes = getConnectionGenes(genes)
			.map(gene -> new HashSet<>(asList(gene.getInput(), gene.getOutput())))
			.collect(toList());
		
		boolean unique = connectionGeneNodes.size() == connectionGeneNodes.stream().distinct().count();
		
		if (!unique)
		{
			throw new IllegalArgumentException("Duplicate connection genes");
		}
	}
	
	private static Stream<NodeGene> getNodeGenes(Collection<Gene> genes)
	{
		return genes.stream()
			.filter(gene -> gene instanceof NodeGene)
			.map(NodeGene.class::cast);
	}
	
	private static Stream<ConnectionGene> getConnectionGenes(Collection<Gene> genes)
	{
		return genes.stream()
			.filter(gene -> gene instanceof ConnectionGene)
			.map(ConnectionGene.class::cast);
	}
	
	/**
	 * @see Collectors#throwingMerger()
	 */
	private static <T> BinaryOperator<T> throwingMerger()
	{
		return (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
	}
}
