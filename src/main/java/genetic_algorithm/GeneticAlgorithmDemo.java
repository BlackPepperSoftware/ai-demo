package genetic_algorithm;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class GeneticAlgorithmDemo
{
	private static final String GENE_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
	
	private static final String TARGET_INDIVIDUAL = "HELLO WORLD";
	
	private static final int CHROMOSOME_SIZE = TARGET_INDIVIDUAL.length();
	
	private static final double MUTATION_PROBABILITY = 0.1;
	
	private static final int POPULATION_SIZE = 200;
	
	private static final int MAX_GENERATIONS = 1000;
	
	private static final Random random = new Random();
	
	public static void main(String[] args)
	{
		List<String> population = randomPopulation();
		ToIntFunction<String> fitness = individual -> getFitness(individual, TARGET_INDIVIDUAL);
		
		boolean found = false;
		int generation = 0;
		
		while (!found && generation < MAX_GENERATIONS)
		{
			System.out.format("#%4d %s%n", generation + 1, getFittest(population, fitness).orElse(""));
			
			found = population.contains(TARGET_INDIVIDUAL);
			population = evolve(population, fitness);
			generation++;
		}
		
		String message = found ? "%nFound %s in %d generations!%n" : "%n%s not found in %d generations :(%n";
		System.out.format(message, TARGET_INDIVIDUAL, generation);
	}
	
	private static List<String> randomPopulation()
	{
		return Stream.generate(GeneticAlgorithmDemo::randomChromosome)
			.limit(POPULATION_SIZE)
			.collect(toList());
	}
	
	private static String randomChromosome()
	{
		StringBuilder builder = new StringBuilder(CHROMOSOME_SIZE);
		
		for (int index = 0; index < builder.capacity(); index++)
		{
			builder.append(randomGene());
		}
		
		return builder.toString();
	}
	
	private static char randomGene()
	{
		int index = random.nextInt(GENE_POOL.length());
		return GENE_POOL.charAt(index);
	}

	private static List<String> evolve(List<String> population, ToIntFunction<String> fitness)
	{
		return Stream.generate(() -> reproduce(population, fitness))
			.limit(population.size())
			.collect(toList());
	}
	
	private static String reproduce(List<String> population, ToIntFunction<String> fitness)
	{
		String mum = select(population, fitness);
		String dad = select(population, fitness);
		
		return mutate(crossover(mum, dad));
	}
	
	private static String select(List<String> population, ToIntFunction<String> fitness)
	{
		// Roulette wheel selection

		int totalFitness = population
			.stream()
			.mapToInt(fitness)
			.sum();
		
		int thresholdFitness = random.nextInt(totalFitness);
		int cumulativeFitness = 0;
		
		for (String individual : population)
		{
			cumulativeFitness += fitness.applyAsInt(individual);
			
			if (cumulativeFitness > thresholdFitness)
			{
				return individual;
			}
		}
		
		throw new IllegalStateException("No parents found");
	}
	
	private static String crossover(String mum, String dad)
	{
		// Single point crossover
		
		int crossoverPoint = random.nextInt(mum.length());
		
		String mumGenes = mum.substring(0, crossoverPoint);
		String dadGenes = dad.substring(crossoverPoint);
		
		return mumGenes + dadGenes;
	}
	
	private static String mutate(String chromosome)
	{
		StringBuilder builder = new StringBuilder(chromosome);
		
		if (random.nextDouble() < MUTATION_PROBABILITY)
		{
			int geneIndex = random.nextInt(chromosome.length());
			builder.setCharAt(geneIndex, randomGene());
		}
		
		return builder.toString();
	}
	
	private static Optional<String> getFittest(List<String> population, ToIntFunction<String> fitness)
	{
		return population.stream()
			.max(comparingInt(fitness));
	}
	
	private static int getFitness(String current, String target)
	{
		int fitness = 0;
		
		for (int index = 0; index < current.length(); index++)
		{
			char currentGene = current.charAt(index);
			char targetGene = target.charAt(index);
			
			if (currentGene == targetGene)
			{
				fitness++;
			}
		}
		
		return fitness;
	}
}
