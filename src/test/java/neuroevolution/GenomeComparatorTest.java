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
		Genome genome1 = new Genome(connectionGene(1), connectionGene(2), connectionGene(3));
		Genome genome2 = new Genome(connectionGene(1), connectionGene(2), connectionGene(3));
		
		assertThat(comparator.compare(genome1, genome2), is(0));
	}
	
	@Test
	public void canCountExcessGenes()
	{
		// 1 2 . 4 5 6 . .
		// 1 2 3 4 . 6 7 8
		Genome genome1 = new Genome(connectionGene(1), connectionGene(2), connectionGene(4), connectionGene(5),
			connectionGene(6));
		Genome genome2 = new Genome(connectionGene(1), connectionGene(2), connectionGene(3), connectionGene(4),
			connectionGene(6), connectionGene(7), connectionGene(8));
		
		assertThat(comparator.excessGeneCount(genome1, genome2), is(2));
	}
	
	@Test
	public void canCountDisjointGenes()
	{
		// 1 2 . 4 5 6 . .
		// 1 2 3 4 . 6 7 8
		Genome genome1 = new Genome(connectionGene(1), connectionGene(2), connectionGene(4), connectionGene(5),
			connectionGene(6));
		Genome genome2 = new Genome(connectionGene(1), connectionGene(2), connectionGene(3), connectionGene(4),
			connectionGene(6), connectionGene(7), connectionGene(8));
		
		assertThat(comparator.disjointGeneCount(genome1, genome2), is(2));
	}
	
	@Test
	public void canAverageWeightDifferences()
	{
		// 1 2 . 4 5 6 . .
		// 1 2 3 4 . 6 7 8
		Genome genome1 = new Genome(connectionGene(0.1, 1), connectionGene(0.2, 2), connectionGene(0.3, 4),
			connectionGene(0.5, 5), connectionGene(0.4, 6));
		Genome genome2 = new Genome(connectionGene(0.2, 1), connectionGene(0.4, 2), connectionGene(0.5, 3),
			connectionGene(0.6, 4), connectionGene(0.8, 6), connectionGene(0.5, 7), connectionGene(0.5, 8));
		
		// ( |0.1-0.2| + |0.2-0.4| + |0.3-0.6| + |0.4-0.8| ) / 4
		assertThat(comparator.averageWeightDifferences(genome1, genome2), is(0.25));
	}
	
	private static ConnectionGene connectionGene(int innovation)
	{
		return new ConnectionGene(0, innovation);
	}
	
	private static ConnectionGene connectionGene(double weight, int innovation)
	{
		return new ConnectionGene(weight, innovation);
	}
}
