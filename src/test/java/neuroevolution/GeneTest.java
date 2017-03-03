package neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeneTest
{
	private Random random;
	
	@Before
	public void setUp()
	{
		random = mock(Random.class);
	}
	
	@Test
	public void canMutateConnectionWeight()
	{
		when(random.nextDouble()).thenReturn(0.6);
		Gene gene = new Gene(1.0, 1);
		
		Gene result = gene.mutateConnectionWeight(random);
		
		// 1.0 + (2 * 0.6 - 1) * 0.1
		assertThat(result.getWeight(), is(1.02));
	}
}
