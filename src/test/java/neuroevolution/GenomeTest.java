package neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenomeTest
{
	private Random random;
	
	@Before
	public void setUp()
	{
		random = mock(Random.class);
	}
	
	@Test
	public void canMutateConnectionWeights()
	{
		when(random.nextDouble()).thenReturn(0.4, 0.5, 0.6);
		Genome genome = new Genome(new ConnectionGene(0.1, 1), new ConnectionGene(0.2, 2), new ConnectionGene(0.3, 3));
		
		Genome result = genome.mutateConnectionWeights(random);
		
		assertThat(result.getGenes().collect(toList()), contains(
			new ConnectionGene(0.08, 1), new ConnectionGene(0.2, 2), new ConnectionGene(0.32, 3)
		));
	}
	
	@Test
	public void canAddConnection()
	{
		when(random.nextDouble()).thenReturn(0.3);
		GeneFactory geneFactory = new GeneFactory();
		Genome genome = new Genome(geneFactory.newConnectionGene(0.1), geneFactory.newConnectionGene(0.2));
		
		Genome result = genome.addConnection(geneFactory, random);
		
		assertThat(result.getGenes().collect(toList()), contains(
			new ConnectionGene(0.1, 1), new ConnectionGene(0.2, 2), new ConnectionGene(0.3, 3)
		));
	}
}
