package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class ConnectionEnabledMutator implements Mutator {
	
	private static final double RATE = 0.1;
	
	private final Random random;
	
	public ConnectionEnabledMutator(Random random) {
		this.random = random;
	}
	
	@Override
	public Genome mutate(Genome genome) {
		Genome result = genome.copy();
		
		if (random.nextDouble() < RATE) {
			result = doMutate(result);
		}
		
		return result;
	}
	
	private Genome doMutate(Genome genome) {
		List<ConnectionGene> connections = genome.getConnectionGenes().collect(toList());
		
		if (connections.isEmpty()) {
			return genome;
		}
		
		ConnectionGene connection = connections.get(random.nextInt(connections.size()));
		
		return new Genome(genome.getGenes()
			.map(gene -> gene.equals(connection) ? flip((ConnectionGene) gene) : gene)
		);
	}
	
	private static ConnectionGene flip(ConnectionGene connection) {
		return connection.isEnabled() ? connection.disable() : connection.enable();
	}
}
