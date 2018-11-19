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
	
	private final Selector selector;
	
	public Population(int size, int inputNodeCount, int outputNodeCount) {
		this(Stream.generate(() -> new Genome().addInputNodes(inputNodeCount).addOutputNodes(outputNodeCount))
			.limit(size)
		);
	}
	
	public Population(Stream<Genome> genomes) {
		this.genomes = genomes.collect(toList());
		
		Random random = new Random();
		selector = new RouletteWheelSelector(random);
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
			.limit(getSize())
		);
	}
	
	public void print(PrintStream out) {
		out.println("Population:");
		
		genomes.forEach(genome -> genome.print(out));
	}
	
	private Genome reproduce(Map<Genome, Integer> fitnesses) {
		Genome mum = selector.select(getGenomes(), fitnesses);
		Genome dad = selector.select(getGenomes(), fitnesses);
		
		return mutate(crossover(mum, dad));
	}
	
	private Genome crossover(Genome mum, Genome dad) {
		// TODO: crossover
		return mum;
	}
	
	private Genome mutate(Genome genome) {
		// TODO: mutate
		return genome;
	}
}
