package neuroevolution;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
	public void canCopyGenome()
	{
		Genome genome = new Genome(input, output)
			.addGene(new ConnectionGene(input, output, 0.5, true, 1));
		
		Genome result = genome.copy();
		
		List<Gene> resultGenes = result.getGenes().collect(toList());
		NodeGene resultNode1 = (NodeGene) resultGenes.get(0);
		NodeGene resultNode2 = (NodeGene) resultGenes.get(1);
		assertThat(resultNode1.isInput(), is(true));
		assertThat(resultNode2.isOutput(), is(true));
		assertThat(resultGenes.get(2), is(new ConnectionGene(resultNode1, resultNode2, 0.5, true, 1)));
	}
	
	@Test
	public void cannotConnectUnknownNodeGenes()
	{
		Genome genome = new Genome(input);
		ConnectionGene connection = new ConnectionGene(input, output, 1.0, true, 1);
		
		thrown.expect(IllegalArgumentException.class);
		
		genome.addGene(connection);
	}
	
	@Test
	public void cannotDuplicateConnectionGenes()
	{
		Genome genome = new Genome(input, output)
			.addGene(new ConnectionGene(input, output, 0.1, true, 1));
		ConnectionGene connection = new ConnectionGene(input, output, 0.2, true, 2);
		
		thrown.expect(IllegalArgumentException.class);
		
		genome.addGene(connection);
	}
	
	@Test
	public void cannotReverseConnectionGenes()
	{
		Genome genome = new Genome(input, output)
			.addGene(new ConnectionGene(input, output, 0.1, true, 1));
		ConnectionGene connection = new ConnectionGene(output, input, 0.2, true, 2);
		
		thrown.expect(IllegalArgumentException.class);
		
		genome.addGene(connection);
	}
}
