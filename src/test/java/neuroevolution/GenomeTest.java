package neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static neuroevolution.NodeGene.newInput;
import static neuroevolution.NodeGene.newOutput;

public class GenomeTest
{
	private Random random;
	
	private NodeGene input;
	
	private NodeGene output;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp()
	{
		random = mock(Random.class);
		input = newInput();
		output = newOutput();
	}
	
	@Test
	public void cannotConnectUnknownNodeGenes()
	{
		ConnectionGene connectionGene = new ConnectionGene(input, output, 1.0, 1);
		
		thrown.expect(IllegalArgumentException.class);
		
		new Genome(input, connectionGene);
	}
	
	@Test
	public void canMutateConnectionWeights()
	{
		when(random.nextDouble()).thenReturn(0.4, 0.5, 0.6);
		Genome genome = new Genome(input, output,
			new ConnectionGene(input, output, 0.1, 1),
			new ConnectionGene(input, output, 0.2, 2),
			new ConnectionGene(input, output, 0.3, 3)
		);
		
		Genome result = genome.mutateConnectionWeights(random);
		
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input, output, 0.08, 1),
			new ConnectionGene(input, output, 0.2, 2),
			new ConnectionGene(input, output, 0.32, 3)
		));
	}
	
	@Test
	public void canMutateConnections()
	{
		when(random.nextInt(anyInt())).thenReturn(0, 1);
		when(random.nextDouble()).thenReturn(0.3);
		GeneFactory geneFactory = new GeneFactory();
		Genome genome = new Genome(input, output,
			geneFactory.newConnectionGene(input, output, 0.1),
			geneFactory.newConnectionGene(input, output, 0.2)
		);
		
		Genome result = genome.mutateConnections(geneFactory, random);
		
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input, output, 0.1, 1),
			new ConnectionGene(input, output, 0.2, 2),
			new ConnectionGene(input, output, 0.3, 3)
		));
	}
}
