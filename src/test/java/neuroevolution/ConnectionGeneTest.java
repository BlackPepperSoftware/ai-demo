package neuroevolution;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static neuroevolution.NodeGene.newInput;

public class ConnectionGeneTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void cannotConnectSameNodeGene()
	{
		NodeGene node = newInput();
		
		thrown.expect(IllegalArgumentException.class);
		
		new ConnectionGene(node, node, 1.0, true, 1);
	}
}
