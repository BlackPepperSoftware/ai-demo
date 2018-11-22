package uk.co.blackpepper.neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionMutatorTest {
	
	private GeneFactory geneFactory;
	
	private Random random;
	
	private ConnectionMutator mutator;
	
	private NodeGene input1;
	
	private NodeGene input2;
	
	private NodeGene input3;
	
	private NodeGene output;
	
	@Before
	public void setUp() {
		geneFactory = new GeneFactory();
		random = mock(Random.class);
		mutator = new ConnectionMutator(geneFactory, random);
		
		input1 = geneFactory.newInputGene();
		input2 = geneFactory.newInputGene();
		input3 = geneFactory.newInputGene();
		output = geneFactory.newOutputGene();
	}
	
	@Test
	public void canMutateConnections() {
		when(random.nextInt(anyInt())).thenReturn(2, 3);
		when(random.nextDouble()).thenReturn(0.3);
		Genome genome = new Genome(input1, input2, input3, output)
			.addGene(geneFactory.newConnectionGene(input1, output, 0.1))
			.addGene(geneFactory.newConnectionGene(input2, output, 0.2));
		
		Genome result = mutator.mutateConnections(genome);
		
		assertThat(result.getConnections().collect(toList()), contains(
			new ConnectionGene(input1, output, 0.1, true, 1),
			new ConnectionGene(input2, output, 0.2, true, 2),
			new ConnectionGene(input3, output, 0.3, true, 3)
		));
	}
}
