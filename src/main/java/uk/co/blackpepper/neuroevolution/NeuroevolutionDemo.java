package uk.co.blackpepper.neuroevolution;

public class NeuroevolutionDemo {
	
	public static void main(String[] args) {
		NeatNetwork neat = new NeatNetwork(2, 1);
		
		neat.print(System.out);
	}
}
