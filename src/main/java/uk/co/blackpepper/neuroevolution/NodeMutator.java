package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class NodeMutator implements Mutator {
	
	private static final double RATE = 0.4;
	
	private final GeneFactory geneFactory;
	
	private final Random random;
	
	public NodeMutator(GeneFactory geneFactory, Random random) {
		this.geneFactory = geneFactory;
		this.random = random;
	}
	
	@Override
	public Genome mutate(Genome genome) {
		Genome result = genome.copy();
		
		if (random.nextDouble() < RATE) {
			result = mutateNodes(result);
		}
		
		return result;
	}
	
	Genome mutateNodes(Genome genome) {
		List<ConnectionGene> connections = genome.getEnabledConnections()
			.collect(toList());
		
		if (connections.isEmpty()) {
			return genome;
		}
		
		ConnectionGene connection = connections.get(random.nextInt(connections.size()));
		NodeGene newNode = geneFactory.newHiddenGene(connection.getInput().getId(), connection.getOutput().getId());

		//a previously disabled connection has become enabled through cross over. This is legal but will mean that the mutation for split already exists
		boolean mutationAlreadyExists = genome.getNodes()
                .anyMatch(node -> node.getId() == newNode.getId());

		return mutationAlreadyExists ?
                genome.copy() :
                genome.disableGene(connection)
                        .addGene(newNode)
                        .addGene(geneFactory.newConnectionGene(connection.getInput(), newNode, 1))
                        .addGene(geneFactory.newConnectionGene(newNode, connection.getOutput(), connection.getWeight()));
	}
}
