package uk.co.blackpepper.neuroevolution;

import static uk.co.blackpepper.neuroevolution.NodeGene.NodeType.HIDDEN;
import static uk.co.blackpepper.neuroevolution.NodeGene.NodeType.INPUT;
import static uk.co.blackpepper.neuroevolution.NodeGene.NodeType.OUTPUT;

public class GeneFactory {
	
	private int id;
	
	private int innovation;
	
	public GeneFactory() {
		id = 1;
		innovation = 1;
	}
	
	public NodeGene newInputGene() {
		return new NodeGene(INPUT, nextId());
	}
	
	public NodeGene newOutputGene() {
		return new NodeGene(OUTPUT, nextId());
	}
	
	public NodeGene newHiddenGene() {
		return new NodeGene(HIDDEN, nextId());
	}
	
	public ConnectionGene newConnectionGene(NodeGene input, NodeGene output, double weight) {
		return new ConnectionGene(input, output, weight, true, nextInnovation());
	}
	
	private int nextId() {
		return id++;
	}
	
	private int nextInnovation() {
		return innovation++;
	}
}
