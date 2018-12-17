package uk.co.blackpepper.neuroevolution;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
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

	private final GenomeComparator genomeComparator;
	
	private class PopulationSupplier implements Supplier<Population> {
		
		private Population population;
		
		public PopulationSupplier(Population population) {
			this.population = population;
		}
		
		@Override
		public Population get() {
			Map<Genome, Integer> fitnesses = population.getGenomes()
				.collect(toMap(Function.identity(), fitness::applyAsInt));

			return new Population(reproduce(population, fitnesses));
		}
	}
	
	private Evolver(ToIntFunction<Genome> fitness, GeneFactory geneFactory, Random random) {
		this.fitness = fitness;

		selector = new RouletteWheelSelector(random);
		crossover = new InnovationCrossover(random);
		mutator = new CompositeMutator(
			new ConnectionWeightMutator(random),
			new ConnectionEnabledMutator(random),
			new ConnectionMutator(geneFactory, random),
			new NodeMutator(geneFactory, random)
		);
		genomeComparator = new GenomeComparator();
	}

	public Stream<Population> evolve(Population population) {
		return Stream.generate(new PopulationSupplier(population));
	}

	public Collector<Population, AtomicReference<Genome>, Genome> toFittest() {
		return Collector.of(
			AtomicReference::new,
			(ref, population) -> {
				Genome fittest = getFittest(population);
				if (fitness.applyAsInt(fittest) > getFitness(ref.get())) {
					ref.set(fittest);
				}
			},
			(ref1, ref2) -> (getFitness(ref1.get()) > getFitness(ref2.get())) ? ref1 : ref2,
			AtomicReference::get
		);
	}

	private Genome getFittest(Population population) {
		return population.getGenomes()
			.max(comparingInt(fitness))
			.orElseThrow(IllegalStateException::new);
	}

	private int getFitness(Genome genome) {
		return Optional.ofNullable(genome)
			.map(fitness::applyAsInt)
			.orElse(0);
	}

    private Stream<Species> reproduce(Population population, Map<Genome, Integer> fitnesses) {

        population.getSpecies().forEach(species -> species.setFittestScore(species.getGenomes().mapToDouble(genome -> fitnesses.get(genome)).max().orElse(0d)));

        double averageFittestScore = population.getSpecies().mapToDouble(species -> species.getFittestScore()).average()
                .orElseThrow(() -> new IllegalStateException("no average score"));

        List<Species> newSpecies = population.getSpecies()
                .map(species -> new Species(Stream.generate(() -> reproduce(species, fitnesses)).limit(species.getSize()), species.getFittestScore()))
                .collect(Collectors.toList());

        List<Species> speciesBelonging = newSpecies.stream()
                .map(species -> new Species(species.getGenomes().filter(genome -> genomeComparator.compare(genome, species.getRepresentative()) == 0), species.getFittestScore()))
                .collect(Collectors.toList());

        List<Species> speciesThatMadeThreshold = speciesBelonging.stream()
                .filter(species -> species.getFittestScore() >= averageFittestScore)
                .collect(Collectors.toList());

        long diff = speciesBelonging.stream().flatMap(species -> species.getGenomes()).count() -
                speciesThatMadeThreshold.stream().flatMap(species -> species.getGenomes()).count();

        List<Genome> genomesThatDontBelong = newSpecies.stream()
                .flatMap(species -> species.getGenomes().filter(genome -> genomeComparator.compare(genome, species.getRepresentative()) != 0))
                .collect(Collectors.toList());

        if(speciesThatMadeThreshold.size() > 0) {
            Random random = new Random();

            List<Species> hat = speciesThatMadeThreshold.stream().sorted(Comparator.comparingDouble(Species::getFittestScore)).limit(3)
                    .collect(Collectors.toList());

            genomesThatDontBelong.addAll(
                    Stream.generate(() -> reproduce(
                            hat.get(random.nextInt(hat.size())), fitnesses))
                            .limit(diff)
                            .collect(Collectors.toList())
            );
        }

        genomesThatDontBelong.forEach(genome ->
                speciesThatMadeThreshold.stream()
                    .filter(species -> genomeComparator.compare(genome, species.getRepresentative()) == 0).findFirst()
                        .orElseGet(() -> {
                            Species species = new Species();
                            speciesThatMadeThreshold.add(species);
                            return species;
                        }).add(genome)
        );

        return speciesThatMadeThreshold.stream();
    }

    private Genome reproduce(Species species, Map<Genome, Integer> fitnesses) {

        Map<Genome, Integer> speciesFitnesses = new HashMap<>();
        species.getGenomes().forEach(genome -> speciesFitnesses.put(genome, fitnesses.get(genome)));

        Genome parent1 = selector.select(species.getGenomes(), speciesFitnesses);
        Genome parent2 = selector.select(species.getGenomes(), speciesFitnesses);
        Genome child = crossover.crossover(parent1, parent2, speciesFitnesses);

        return mutator.mutate(child);
    }
}
