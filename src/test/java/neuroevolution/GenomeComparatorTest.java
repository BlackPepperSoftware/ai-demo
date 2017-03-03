package neuroevolution;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenomeComparatorTest
{
	private GenomeComparator comparator;
	
	@Before
	public void setUp()
	{
		comparator = new GenomeComparator();
	}
	
	@Test
	public void canMatchSpeciesWhenNoGenomes()
	{
		Genome genome1 = new Genome();
		Genome genome2 = new Genome();
		
		assertThat(comparator.compare(genome1, genome2), is(0));
	}
	
	@Test
	public void canMatchSpeciesWhenIdentical()
	{
		Genome genome1 = new Genome(gene(1), gene(2), gene(3));
		Genome genome2 = new Genome(gene(1), gene(2), gene(3));
		
		assertThat(comparator.compare(genome1, genome2), is(0));
	}
	
	@Test
	public void canCountExcessGenes()
	{
		// 1 2 . 4 5 6 . .
		// 1 2 3 4 . 6 7 8
		Genome genome1 = new Genome(gene(1), gene(2), gene(4), gene(5), gene(6));
		Genome genome2 = new Genome(gene(1), gene(2), gene(3), gene(4), gene(6), gene(7), gene(8));
		
		assertThat(comparator.excessGeneCount(genome1, genome2), is(2));
	}
	
	@Test
	public void canCountDisjointGenes()
	{
		// 1 2 . 4 5 6 . .
		// 1 2 3 4 . 6 7 8
		Genome genome1 = new Genome(gene(1), gene(2), gene(4), gene(5), gene(6));
		Genome genome2 = new Genome(gene(1), gene(2), gene(3), gene(4), gene(6), gene(7), gene(8));
		
		assertThat(comparator.disjointGeneCount(genome1, genome2), is(2));
	}
	
	@Test
	public void canAverageWeightDifferences()
	{
		// 1 2 . 4 5 6 . .
		// 1 2 3 4 . 6 7 8
		Genome genome1 = new Genome(gene(0.1, 1), gene(0.2, 2), gene(0.3, 4), gene(0.5, 5), gene(0.4, 6));
		Genome genome2 = new Genome(gene(0.2, 1), gene(0.4, 2), gene(0.5, 3), gene(0.6, 4), gene(0.8, 6), gene(0.5, 7),
			gene(0.5, 8));
		
		// ( |0.1-0.2| + |0.2-0.4| + |0.3-0.6| + |0.4-0.8| ) / 4
		assertThat(comparator.averageWeightDifferences(genome1, genome2), is(0.25));
	}
	
	private static Gene gene(int innovation)
	{
		return new Gene(0, innovation);
	}
	
	private static Gene gene(double weight, int innovation)
	{
		return new Gene(weight, innovation);
	}
}
