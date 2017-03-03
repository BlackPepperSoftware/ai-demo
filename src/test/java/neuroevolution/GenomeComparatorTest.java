package neuroevolution;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static neuroevolution.NodeGene.newHidden;
import static neuroevolution.NodeGene.newInput;
import static neuroevolution.NodeGene.newOutput;

public class GenomeComparatorTest
{
	private GenomeComparator comparator;
	
	private NodeGene input1;
	
	private NodeGene input2;
	
	private NodeGene input3;
	
	private NodeGene hidden1;
	
	private NodeGene hidden2;
	
	private NodeGene output;
	
	@Before
	public void setUp()
	{
		comparator = new GenomeComparator();
		
		input1 = newInput();
		input2 = newInput();
		input3 = newInput();
		output = newOutput();
		hidden1 = newHidden();
		hidden2 = newHidden();
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
		Genome genome1 = new Genome(input1, input2, input3, output,
			new ConnectionGene(input1, output, 0, 1),
			new ConnectionGene(input2, output, 0, 2),
			new ConnectionGene(input3, output, 0, 3)
		);
		Genome genome2 = new Genome(input1, input2, input3, output,
			new ConnectionGene(input1, output, 0, 1),
			new ConnectionGene(input2, output, 0, 2),
			new ConnectionGene(input3, output, 0, 3)
		);
		
		assertThat(comparator.compare(genome1, genome2), is(0));
	}
	
	@Test
	public void canCountExcessGenes()
	{
		assertThat(comparator.excessGeneCount(genome1(), genome2()), is(2));
	}
	
	@Test
	public void canCountDisjointGenes()
	{
		assertThat(comparator.disjointGeneCount(genome1(), genome2()), is(2));
	}
	
	@Test
	public void canAverageWeightDifferences()
	{
		// ( |0.1-0.2| + |0.2-0.4| + |0.3-0.6| + |0.4-0.8| ) / 4
		assertThat(comparator.averageWeightDifferences(genome1(), genome2()), is(0.25));
	}
	
	/**
	 * Example genome from original paper.
	 */
	private Genome genome1()
	{
		return new Genome(input1, input2, input3, output, hidden1,
			new ConnectionGene(input1, hidden1, 0.1, 1),
			new ConnectionGene(input2, hidden1, 0.2, 2),
			// disjoint
			new ConnectionGene(input2, output, 0.3, 4),
			new ConnectionGene(input3, output, 0.5, 5),
			new ConnectionGene(hidden1, output, 0.4, 6)
			// excess
			// excess
		);
	}
	
	/**
	 * Example genome from original paper.
	 */
	private Genome genome2()
	{
		return new Genome(input1, input2, input3, output, hidden1, hidden2,
			new ConnectionGene(input1, hidden1, 0.2, 1),
			new ConnectionGene(input2, hidden1, 0.4, 2),
			new ConnectionGene(input3, hidden1, 0.5, 3),
			new ConnectionGene(input2, output, 0.6, 4),
			// disjoint
			new ConnectionGene(hidden1, output, 0.8, 6),
			new ConnectionGene(input1, hidden2, 0.5, 7),
			new ConnectionGene(hidden2, hidden1, 0.5, 8)
		);
	}
}
