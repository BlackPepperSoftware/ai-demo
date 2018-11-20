package uk.co.blackpepper.neuroevolution;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConnectionGeneTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void cannotConnectSameNodeGene() {
		NodeGene node = new GeneFactory().newInputGene();
		
		thrown.expect(IllegalArgumentException.class);
		
		new ConnectionGene(node, node, 1.0, true, 1);
	}
}
