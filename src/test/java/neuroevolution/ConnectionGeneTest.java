package neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static neuroevolution.NodeGene.newInput;
import static neuroevolution.NodeGene.newOutput;

public class ConnectionGeneTest
{
	private Random random;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp()
	{
		random = mock(Random.class);
	}
	
	@Test
	public void cannotConnectSameNodeGene()
	{
		NodeGene node = newInput();
		
		thrown.expect(IllegalArgumentException.class);
		
		new ConnectionGene(node, node, 1.0, true, 1);
	}
	
	@Test
	public void canMutateWeight()
	{
		when(random.nextDouble()).thenReturn(0.6);
		ConnectionGene gene = new ConnectionGene(newInput(), newOutput(), 1.0, true, 1);
		
		ConnectionGene result = gene.mutateWeight(random);
		
		// 1.0 + (2 * 0.6 - 1) * 0.1
		assertThat(result.getWeight(), is(1.02));
	}
}
