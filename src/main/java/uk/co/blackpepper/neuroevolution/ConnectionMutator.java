package uk.co.blackpepper.neuroevolution;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ConnectionMutator implements Mutator {
	
	private static final double CONNECTION_MUTATION_RATE = 0.5;
	
	private final GeneFactory geneFactory;
	
	private final Random random;
	
	public ConnectionMutator(GeneFactory geneFactory, Random random) {
		this.geneFactory = geneFactory;
		this.random = random;
	}
	
	@Override
	public Genome mutate(Genome genome) {
		Genome result = genome.copy();
		
		if (random.nextDouble() < CONNECTION_MUTATION_RATE) {
			result = mutateConnections(result);
		}
		
		return result;
	}
	
	Genome mutateConnections(Genome genome) {
		List<NodeGene> nodes = genome.getNodeGenes()
			.collect(toList());
		
		if (nodes.isEmpty()) {
			return genome;
		}
		
		NodeGene input = nodes.get(random.nextInt(nodes.size()));
		NodeGene output = nodes.get(random.nextInt(nodes.size()));
		
		if (input.equals(output) || input.isOutput() || output.isInput() || genome.connects(input, output)
			|| isCyclic(genome, input, output)
		) {
			return genome;
		}
		
		double newWeight = random.nextDouble();
		
		return genome.addGene(geneFactory.newConnectionGene(input, output, newWeight));
	}
	
	private boolean isCyclic(Genome genome, NodeGene input, NodeGene output) {
		Set<NodeGene> visitedNodes = new HashSet<>();
		visitedNodes.add(output);
		
		return isCyclic(genome, input, visitedNodes);
	}
	
	private boolean isCyclic(Genome genome, NodeGene node, Set<NodeGene> visitedNodes) {
		if (visitedNodes.contains(node)) {
			return true;
		}
		
		Set<NodeGene> nextVisitedNodes = new HashSet<>(visitedNodes);
		nextVisitedNodes.add(node);
		
		return genome.getConnectionsTo(node)
			.anyMatch(connection -> isCyclic(genome, connection.getInput(), nextVisitedNodes));
	}
}
