package uk.co.blackpepper.neuroevolution;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Population {
	
	private final List<Genome> genomes;
	
	private final GeneFactory geneFactory;
	
	private final Selector selector;
	
	private final Crossover crossover;
	
	private final Mutator mutator;
	
	public Population(int size, int inputNodeCount, int outputNodeCount, GeneFactory geneFactory) {
		this(newGenomes(size, inputNodeCount, outputNodeCount, geneFactory), geneFactory);
	}
	
	public Population(Stream<Genome> genomes, GeneFactory geneFactory) {
		this.genomes = genomes.collect(toList());
		this.geneFactory = geneFactory;
		
		Random random = new Random();
		selector = new RouletteWheelSelector(random);
		crossover = new InnovationCrossover(random);
		mutator = new GenomeMutator(geneFactory, random);
	}
	
	public Stream<Genome> getGenomes() {
		return genomes.stream();
	}
	
	public int getSize() {
		return genomes.size();
	}
	
	public Population evolve(ToIntFunction<Genome> fitness) {
		Map<Genome, Integer> fitnesses = getGenomes()
			.collect(toMap(Function.identity(), fitness::applyAsInt));
		
		return new Population(Stream.generate(() -> reproduce(fitnesses))
			.limit(getSize()), geneFactory
		);
	}
	
	public void print(PrintStream out) {
		out.println("Population:");
		
		genomes.forEach(genome -> genome.print(out));
	}
	
	private static Stream<Genome> newGenomes(int size, int inputNodeCount, int outputNodeCount,
		GeneFactory geneFactory) {
		Genome genome = new Genome()
			.addInputNodes(inputNodeCount, geneFactory)
			.addOutputNodes(outputNodeCount, geneFactory);
		
		return Stream.generate(genome::copy)
			.limit(size);
	}
	
	private Genome reproduce(Map<Genome, Integer> fitnesses) {
		Genome parent1 = selector.select(getGenomes(), fitnesses);
		Genome parent2 = selector.select(getGenomes(), fitnesses);
		Genome child = crossover.crossover(parent1, parent2, fitnesses);
		
		return mutator.mutate(child);
	}
}
