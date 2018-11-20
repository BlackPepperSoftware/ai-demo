package uk.co.blackpepper.neuroevolution;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Genome {
	
	private final List<Gene> genes;
	
	public Genome(Gene... genes) {
		this(Stream.of(genes));
	}
	
	public Genome(Stream<Gene> genes) {
		List<Gene> genesList = genes.collect(toList());
		checkConnectionGenesNodes(genesList);
		checkConnectionGenesUnique(genesList);
		
		this.genes = genesList;
	}
	
	private Genome(Genome that) {
		this(copyGenes(that.getGenes().collect(toList())));
	}
	
	public Genome addGene(Gene gene) {
		return addGenes(Stream.of(gene));
	}
	
	public Genome addGenes(Stream<? extends Gene> genes) {
		return new Genome(Stream.concat(getGenes(), genes));
	}
	
	public Genome addInputNodes(int count) {
		return addGenes(Stream.generate(NodeGene::newInput).limit(count));
	}
	
	public Genome addOutputNodes(int count) {
		return addGenes(Stream.generate(NodeGene::newOutput).limit(count));
	}
	
	public Genome disableGene(ConnectionGene connection) {
		if (!genes.contains(connection)) {
			throw new IllegalArgumentException("Unknown gene");
		}
		
		return new Genome(genes.stream()
			.map(gene -> gene.equals(connection) ? ((ConnectionGene) gene).disable() : gene)
		);
	}
	
	public Stream<Gene> getGenes() {
		return genes.stream();
	}
	
	public Stream<NodeGene> getNodeGenes() {
		return getNodeGenes(genes);
	}
	
	public Stream<NodeGene> getInputGenes() {
		return getNodeGenes(genes)
			.filter(NodeGene::isInput);
	}
	
	public Stream<NodeGene> getOutputGenes() {
		return getNodeGenes(genes)
			.filter(NodeGene::isOutput);
	}
	
	public Stream<ConnectionGene> getConnectionGenes() {
		return getConnectionGenes(genes);
	}
	
	public boolean connects(NodeGene input, NodeGene output) {
		return getConnectionGenes()
			.map(gene -> new HashSet<>(asList(gene.getInput(), gene.getOutput())))
			.anyMatch(gene -> gene.equals(new HashSet<>(asList(input, output))));
	}
	
	public DoubleStream evaluate(DoubleStream inputs) {
		PrimitiveIterator.OfDouble inputsIterator = inputs.iterator();
		Map<NodeGene, Double> inputValues = getInputGenes()
			.collect(toMap(Function.identity(), x -> inputsIterator.nextDouble()));
		
		return getOutputGenes()
			.mapToDouble(output -> evaluateNode(output, inputValues));
	}
	
	public Genome copy() {
		return new Genome(this);
	}
	
	public void print(PrintStream out) {
		out.println("  " + genes);
	}
	
	private double evaluateNode(NodeGene node, Map<NodeGene, Double> inputValues) {
		if (node.isInput()) {
			return inputValues.get(node);
		}
		
		return getGenesConnectedTo(node)
			.mapToDouble(connection -> evaluateNode(connection.getInput(), inputValues) * connection.getWeight())
			.sum();
	}
	
	private Stream<ConnectionGene> getGenesConnectedTo(NodeGene node) {
		return getConnectionGenes()
			.filter(connection -> connection.getOutput() == node);
	}
	
	private static Stream<Gene> copyGenes(Collection<Gene> genes) {
		Map<NodeGene, NodeGene> newNodesByOriginal = getNodeGenes(genes)
			.collect(toMap(gene -> gene, NodeGene::copy, throwingMerger(), LinkedHashMap::new));
		
		Stream<ConnectionGene> newConnections = getConnectionGenes(genes)
			.map(connection -> copyConnectionGene(connection, newNodesByOriginal));
		
		return Stream.concat(newNodesByOriginal.values().stream(), newConnections);
	}
	
	private static ConnectionGene copyConnectionGene(ConnectionGene connection, Map<NodeGene, NodeGene> nodeMap) {
		NodeGene newInput = nodeMap.get(connection.getInput());
		NodeGene newOutput = nodeMap.get(connection.getOutput());
		
		return new ConnectionGene(newInput, newOutput, connection.getWeight(), connection.isEnabled(),
			connection.getInnovation());
	}
	
	private static void checkConnectionGenesNodes(Collection<Gene> genes) {
		List<NodeGene> nodes = getNodeGenes(genes)
			.collect(toList());
		
		boolean valid = getConnectionGenes(genes)
			.flatMap(gene -> Stream.of(gene.getInput(), gene.getOutput()))
			.allMatch(nodes::contains);
		
		if (!valid) {
			throw new IllegalArgumentException("Connection gene references unknown node gene");
		}
	}
	
	private static void checkConnectionGenesUnique(Collection<Gene> genes) {
		List<Set<NodeGene>> connectionNodes = getConnectionGenes(genes)
			.map(gene -> new HashSet<>(asList(gene.getInput(), gene.getOutput())))
			.collect(toList());
		
		boolean unique = connectionNodes.size() == connectionNodes.stream().distinct().count();
		
		if (!unique) {
			throw new IllegalArgumentException("Duplicate connection genes");
		}
	}
	
	private static Stream<NodeGene> getNodeGenes(Collection<Gene> genes) {
		return genes.stream()
			.filter(gene -> gene instanceof NodeGene)
			.map(NodeGene.class::cast);
	}
	
	private static Stream<ConnectionGene> getConnectionGenes(Collection<Gene> genes) {
		return genes.stream()
			.filter(gene -> gene instanceof ConnectionGene)
			.map(ConnectionGene.class::cast);
	}
	
	/**
	 * @see Collectors#throwingMerger()
	 */
	private static <T> BinaryOperator<T> throwingMerger() {
		return (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
	}
}
