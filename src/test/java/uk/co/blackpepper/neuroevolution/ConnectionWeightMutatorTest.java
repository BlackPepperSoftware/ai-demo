package uk.co.blackpepper.neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionWeightMutatorTest {
	
	private GeneFactory geneFactory;
	
	private Random random;
	
	private ConnectionWeightMutator mutator;
	
	private NodeGene input1;
	
	private NodeGene input2;
	
	private NodeGene input3;
	
	private NodeGene output;
	
	@Before
	public void setUp() {
		geneFactory = new GeneFactory();
		random = mock(Random.class);
		mutator = new ConnectionWeightMutator(random);
		
		input1 = geneFactory.newInputGene();
		input2 = geneFactory.newInputGene();
		input3 = geneFactory.newInputGene();
		output = geneFactory.newOutputGene();
	}
	
	@Test
	public void canMutateConnectionWeights() {
		when(random.nextDouble()).thenReturn(0.4, 0.5, 0.6);
		Genome genome = new Genome(input1, input2, input3, output)
			.addGene(new ConnectionGene(input1, output, 0.1, true, 1))
			.addGene(new ConnectionGene(input2, output, 0.2, true, 2))
			.addGene(new ConnectionGene(input3, output, 0.3, true, 3));
		
		Genome result = mutator.mutateConnectionWeights(genome);
		
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input1, output, 0.08, true, 1),
			new ConnectionGene(input2, output, 0.2, true, 2),
			new ConnectionGene(input3, output, 0.32, true, 3)
		));
	}
	
	@Test
	public void canMutateConnectionWeight() {
		when(random.nextDouble()).thenReturn(0.6);
		ConnectionGene connection = new ConnectionGene(geneFactory.newInputGene(), geneFactory.newOutputGene(), 1.0,
			true, 1);
		
		ConnectionGene result = mutator.mutateConnectionWeight(connection);
		
		// 1.0 + (2 * 0.6 - 1) * 0.1
		assertThat(result.getWeight(), is(1.02));
	}
}
