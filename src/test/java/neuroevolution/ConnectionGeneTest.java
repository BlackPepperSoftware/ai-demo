package neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionGeneTest
{
	private Random random;
	
	@Before
	public void setUp()
	{
		random = mock(Random.class);
	}
	
	@Test
	public void canMutateWeight()
	{
		when(random.nextDouble()).thenReturn(0.6);
		ConnectionGene gene = new ConnectionGene(1.0, 1);
		
		ConnectionGene result = gene.mutateWeight(random);
		
		// 1.0 + (2 * 0.6 - 1) * 0.1
		assertThat(result.getWeight(), is(1.02));
	}
}
