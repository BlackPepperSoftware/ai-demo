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

import static uk.co.blackpepper.neuroevolution.NodeGene.newInput;
import static uk.co.blackpepper.neuroevolution.NodeGene.newOutput;

public class GenomeMutatorTest {
	
	private GeneFactory geneFactory;
	
	private Random random;
	
	private GenomeMutator mutator;
	
	private NodeGene input1;
	
	private NodeGene input2;
	
	private NodeGene input3;
	
	private NodeGene output;
	
	@Before
	public void setUp() {
		geneFactory = new GeneFactory();
		random = mock(Random.class);
		mutator = new GenomeMutator(geneFactory, random);
		
		input1 = newInput();
		input2 = newInput();
		input3 = newInput();
		output = newOutput();
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
		ConnectionGene connection = new ConnectionGene(newInput(), newOutput(), 1.0, true, 1);
		
		ConnectionGene result = mutator.mutateConnectionWeight(connection);
		
		// 1.0 + (2 * 0.6 - 1) * 0.1
		assertThat(result.getWeight(), is(1.02));
	}
	
	@Test
	public void canMutateConnections() {
		when(random.nextInt(anyInt())).thenReturn(2, 3);
		when(random.nextDouble()).thenReturn(0.3);
		Genome genome = new Genome(input1, input2, input3, output)
			.addGene(geneFactory.newConnectionGene(input1, output, 0.1))
			.addGene(geneFactory.newConnectionGene(input2, output, 0.2));
		
		Genome result = mutator.mutateConnections(genome);
		
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input1, output, 0.1, true, 1),
			new ConnectionGene(input2, output, 0.2, true, 2),
			new ConnectionGene(input3, output, 0.3, true, 3)
		));
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
