package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class NeatCrossover implements Crossover {
	
	private final Random random;
	
	public NeatCrossover(Random random) {
		this.random = random;
	}
	
	@Override
	public Genome crossover(Genome parent1, Genome parent2, Map<Genome, Integer> fitnesses) {
		int fitness1 = fitnesses.get(parent1);
		int fitness2 = fitnesses.get(parent2);

		// TODO: if equal fitness then disjoint and excess genes are also inherited randomly
		return fitness1 >= fitness2
			? crossoverFittest(parent1, parent2)
			: crossoverFittest(parent2, parent1);
	}
	
	// Genes are randomly chosen from either parent at matching genes
	// all excess or disjoint genes are always included from the more fit parent
	private Genome crossoverFittest(Genome alpha, Genome beta) {
		Map<Integer, ConnectionGene> betaConnections = beta.getConnectionGenes()
			.collect(toMap(ConnectionGene::getInnovation, Function.identity()));
		
		List<Gene> childGenes = alpha.getNodeGenes().collect(toList());
		
		for (ConnectionGene alphaConnection : alpha.getConnectionGenes().collect(toList())) {
			ConnectionGene betaConnection = betaConnections.get(alphaConnection.getInnovation());
			
			childGenes.add(crossoverGene(alphaConnection, betaConnection));
		}
		
		return new Genome(childGenes.stream());
	}
	
	private ConnectionGene crossoverGene(ConnectionGene alphaConnection, ConnectionGene betaConnection) {
		if (betaConnection == null) {
			return alphaConnection;
		}
		
		return random.nextBoolean() ? alphaConnection : betaConnection;
	}
}
