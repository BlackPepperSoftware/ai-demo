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

public class NodeMutatorTest {
	
	private GeneFactory geneFactory;
	
	private Random random;
	
	private NodeMutator mutator;
	
	private NodeGene input1;
	
	private NodeGene input2;
	
	private NodeGene output;
	
	@Before
	public void setUp() {
		geneFactory = new GeneFactory();
		random = mock(Random.class);
		mutator = new NodeMutator(geneFactory, random);
		
		input1 = geneFactory.newInputGene();
		input2 = geneFactory.newInputGene();
		output = geneFactory.newOutputGene();
	}
	
	@Test
	public void canMutateNodes() {
		when(random.nextInt(anyInt())).thenReturn(1);
		Genome genome = new Genome(input1, input2, output)
			.addGene(geneFactory.newConnectionGene(input1, output, 0.1))
			.addGene(geneFactory.newConnectionGene(input2, output, 0.2));
		
		Genome result = mutator.mutateNodes(genome);
		
		NodeGene resultNewNode = result.getNodeGenes().collect(toList()).get(3);
		assertThat(result.getNodeGenes().collect(toList()), contains(input1, input2, output, resultNewNode));
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input1, output, 0.1, true, 1),
			new ConnectionGene(input2, output, 0.2, false, 2),
			new ConnectionGene(input2, resultNewNode, 1.0, true, 3),
			new ConnectionGene(resultNewNode, output, 0.2, true, 4)
		));
	}
}
