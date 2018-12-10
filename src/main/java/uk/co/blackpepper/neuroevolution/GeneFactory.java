package uk.co.blackpepper.neuroevolution;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.co.blackpepper.neuroevolution.NodeGene.NodeType.HIDDEN;
import static uk.co.blackpepper.neuroevolution.NodeGene.NodeType.INPUT;
import static uk.co.blackpepper.neuroevolution.NodeGene.NodeType.OUTPUT;

public class GeneFactory {
	
    private Innovator innovator;

	public GeneFactory() {
		innovator = new Innovator();
	}
	
	public NodeGene newInputGene() {
		return new NodeGene(INPUT, nextId());
	}
	
	public Stream<NodeGene> newInputGenes() {
		return Stream.generate(this::newInputGene);
	}
	
	public NodeGene newOutputGene() {
		return new NodeGene(OUTPUT, nextId());
	}
	
	public Stream<NodeGene> newOutputGenes() {
		return Stream.generate(this::newOutputGene);
	}
	
	public NodeGene newHiddenGene(int input, int output) {
		return new NodeGene(HIDDEN, innovator.retrieveOrGenerateNodeIdBetween(input, output));
	}
	
	public ConnectionGene newConnectionGene(NodeGene input, NodeGene output, double weight) {
		return new ConnectionGene(input, output, weight, true, innovator.retrieveOrGenerateInnovationBetween(input.getId(), output.getId()));
	}

	public Stream<? extends Gene> fullyConnected(Stream<NodeGene> inputs, Stream<NodeGene> outputs, Random random) {
        List<NodeGene> inputNodes = inputs.collect(Collectors.toList());
        List<NodeGene> outputNodes = outputs.collect(Collectors.toList());

        Stream<ConnectionGene> connections = inputNodes.stream()
                .flatMap(in -> outputNodes.stream()
                        .map(out -> {
                            double weight = 2 * random.nextDouble() - 1;
                            return newConnectionGene(in, out, weight);
        }));

        return Stream.concat(Stream.concat(inputNodes.stream(), outputNodes.stream()), connections);
    }
	
	private int nextId() {
		return innovator.nextNodeId();
	}
}
