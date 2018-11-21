package uk.co.blackpepper.neuroevolution;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Evolver {
	
	public static class Builder {
		
		private ToIntFunction<Genome> fitness;
		
		private GeneFactory geneFactory;
		
		private Random random;
		
		public Builder fitness(ToIntFunction<Genome> fitness) {
			this.fitness = fitness;
			return this;
		}
		
		public Builder geneFactory(GeneFactory geneFactory) {
			this.geneFactory = geneFactory;
			return this;
		}
		
		public Builder random(Random random) {
			this.random = random;
			return this;
		}
		
		public Evolver build() {
			return new Evolver(fitness, geneFactory, random);
		}
	}
	
	private final ToIntFunction<Genome> fitness;
	
	private final Selector selector;
	
	private final Crossover crossover;
	
	private final Mutator mutator;
	
	private class PopulationSupplier implements Supplier<Population> {
		
		private Population population;
		
		public PopulationSupplier(Population population) {
			this.population = population;
		}
		
		@Override
		public Population get() {
			Map<Genome, Integer> fitnesses = population.getGenomes()
				.collect(toMap(Function.identity(), fitness::applyAsInt));
			
			population = new Population(Stream.generate(() -> reproduce(population, fitnesses))
				.limit(population.getSize())
			);
			
			return population;
		}
	}
	
	private Evolver(ToIntFunction<Genome> fitness, GeneFactory geneFactory, Random random) {
		this.fitness = fitness;
		
		selector = new RouletteWheelSelector(random);
		crossover = new InnovationCrossover(random);
		mutator = new CompositeMutator(
			new ConnectionWeightMutator(random),
			new ConnectionMutator(geneFactory, random),
			new NodeMutator(geneFactory, random)
		);
	}
	
	public Stream<Population> evolve(Population population) {
		return Stream.generate(new PopulationSupplier(population));
	}
	
	private Genome reproduce(Population population, Map<Genome, Integer> fitnesses) {
		Genome parent1 = selector.select(population.getGenomes(), fitnesses);
		Genome parent2 = selector.select(population.getGenomes(), fitnesses);
		Genome child = crossover.crossover(parent1, parent2, fitnesses);
		
		return mutator.mutate(child);
	}
}
