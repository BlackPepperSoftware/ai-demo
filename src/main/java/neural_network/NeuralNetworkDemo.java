package neural_network;

public class NeuralNetworkDemo
{
	private static final int MAX_ITERATIONS = 100000;
	
	public static void main(String[] args)
	{
		NeuralNetwork network = new NeuralNetwork(1, 3, 1);
		
		Matrix input = new Matrix(3, 1)
			.row(0, 0.25)
			.row(1, 0.5)
			.row(2, 0.75);
		
		// normalised sine function
		Matrix target = input.map(x -> (Math.sin(x * 2 * Math.PI) + 1) / 2);
		
		System.out.format("Target output:%n%s%n", target);
		System.out.format("Initial output:%n%s%n", network.forward(input));
		
		System.out.format("Learning...%n%nIteration | Cost%n");
		
		for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++)
		{
			if (iteration % 10000 == 0)
			{
				System.out.format("#%8d | %f%n", iteration, network.cost(input, target));
			}
			
			network.learn(input, target);
		}
		
		System.out.format("#%8d | %f%n", MAX_ITERATIONS, network.cost(input, target));
		
		System.out.format("%nFinal output:%n%s%n", network.forward(input));
	}
}
