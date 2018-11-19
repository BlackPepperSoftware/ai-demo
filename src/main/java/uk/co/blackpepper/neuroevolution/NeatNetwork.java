package uk.co.blackpepper.neuroevolution;

import java.io.PrintStream;

public class NeatNetwork {
	
	private Population population;
	
	public NeatNetwork(int inputNodeCount, int outputNodeCount) {
		population = new Population(10, inputNodeCount, outputNodeCount);
	}
	
	public void print(PrintStream out) {
		population.print(out);
	}
}
