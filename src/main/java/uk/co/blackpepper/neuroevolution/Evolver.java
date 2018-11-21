package uk.co.blackpepper.neuroevolution;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Evolver {
	
	private final ToIntFunction<Genome> fitness;
	
	private final GeneFactory geneFactory;
	
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
	
	public Evolver(ToIntFunction<Genome> fitness, Random random) {
		this.fitness = fitness;
		geneFactory = new GeneFactory();
		
		selector = new RouletteWheelSelector(random);
		crossover = new InnovationCrossover(random);
		mutator = new GenomeMutator(geneFactory, random);
	}
	
	public GeneFactory getGeneFactory() {
		return geneFactory;
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
