package neural_network;

import java.util.function.DoubleUnaryOperator;

public class NeuralNetworkDemo
{
	public static void main(String[] args)
	{
		DoubleUnaryOperator targetFunction = x -> (Math.sin(x * 2 * Math.PI) + 1) / 2;
		
		NeuralNetwork network = new NeuralNetwork(1, 3, 1);
		Matrix input = Matrix.ofColumn(0.25, 0.5, 0.75);
		Matrix target = input.map(targetFunction);
		
		// learn
		
		System.out.println("Learning...\n");
		System.out.println("Iteration | Cost");
		
		for (int iteration = 0; iteration <= 100000; iteration++)
		{
			if (iteration > 0)
			{
				network.learn(input, target);
			}
			
			if (iteration % 10000 == 0)
			{
				System.out.format("#%8d | %f%n", iteration, network.cost(input, target));
			}
		}
		
		// output graph
		
		Matrix graphInput = Matrix.ofColumn(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
		Matrix graphOutput = network.forward(graphInput);
		Matrix graphTarget = graphInput.map(targetFunction);
		
		Matrix graph = new Matrix(graphInput.rowCount(), 3)
			.column(0, graphInput.column(0).toArray())
			.column(1, graphOutput.column(0).toArray())
			.column(2, graphTarget.column(0).toArray());
		
		System.out.println("\n------------ Paste into spreadsheet ------------");
		System.out.println("Input\tOutput\tTarget");
		System.out.print(graph.toTsv());
		System.out.println("------------------------------------------------");
	}
}
