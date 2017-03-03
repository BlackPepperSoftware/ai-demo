package neuroevolution;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static neuroevolution.NodeGene.newInput;
import static neuroevolution.NodeGene.newOutput;

public class GenomeTest
{
	private NodeGene input;
	
	private NodeGene output;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp()
	{
		input = newInput();
		output = newOutput();
	}
	
	@Test
	public void cannotConnectUnknownNodeGenes()
	{
		Genome genome = new Genome(input);
		ConnectionGene connectionGene = new ConnectionGene(input, output, 1.0, 1);
		
		thrown.expect(IllegalArgumentException.class);
		
		genome.addGene(connectionGene);
	}
	
	@Test
	public void cannotDuplicateConnectionGenes()
	{
		Genome genome = new Genome(input, output)
			.addGene(new ConnectionGene(input, output, 0.1, 1));
		ConnectionGene connectionGene = new ConnectionGene(input, output, 0.2, 2);
		
		thrown.expect(IllegalArgumentException.class);
		
		genome.addGene(connectionGene);
	}
	
	@Test
	public void cannotReverseConnectionGenes()
	{
		Genome genome = new Genome(input, output)
			.addGene(new ConnectionGene(input, output, 0.1, 1));
		ConnectionGene connectionGene = new ConnectionGene(output, input, 0.2, 2);
		
		thrown.expect(IllegalArgumentException.class);
		
		genome.addGene(connectionGene);
	}
}
