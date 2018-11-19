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
		Genome fit1 = fitness1 >= fitness2 ? parent1 : parent2;
		Genome fit2 = fitness1 >= fitness2 ? parent2 : parent1;
		
		// Genes are randomly chosen from either parent at matching genes
		// all excess or disjoint genes are always included from the more fit parent

		Map<Integer, ConnectionGene> connections2 = fit2.getConnectionGenes()
			.collect(toMap(ConnectionGene::getInnovation, Function.identity()));
		
		List<Gene> childGenes = fit1.getNodeGenes().collect(toList());
		
		for (ConnectionGene connection1 : fit1.getConnectionGenes().collect(toList())) {
			ConnectionGene connection2 = connections2.get(connection1.getInnovation());
			
			childGenes.add(crossoverGene(connection1, connection2));
		}
		
		return new Genome(childGenes.stream());
	}
	
	private ConnectionGene crossoverGene(ConnectionGene connection1, ConnectionGene connection2) {
		if (connection2 == null) {
			return connection1;
		}
		
		return random.nextBoolean() ? connection1 : connection2;
	}
}
