package neural_network;

public class NeuralNetworkDemo
{
	public static void main(String[] args)
	{
		NeuralNetwork network = new NeuralNetwork(1, 3, 1);
		
		Matrix input = new Matrix(3, 1)
			.row(0, 0.25)
			.row(1, 0.5)
			.row(2, 0.75);
		
		Matrix output = network.forward(input);
		
		System.out.println(output);
	}
}
