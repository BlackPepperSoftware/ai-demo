package uk.co.blackpepper.neuroevolution;

public class GeneFactory {
	
	private int innovation;
	
	public GeneFactory() {
		innovation = 1;
	}
	
	public ConnectionGene newConnectionGene(NodeGene input, NodeGene output, double weight) {
		return new ConnectionGene(input, output, weight, true, nextInnovation());
	}
	
	private int nextInnovation() {
		return innovation++;
	}
}
