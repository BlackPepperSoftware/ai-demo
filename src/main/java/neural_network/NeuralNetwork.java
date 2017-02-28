package neural_network;

import java.util.Random;

public class NeuralNetwork
{
	private Matrix weights1;
	
	private Matrix weights2;
	
	public NeuralNetwork(int inputCount, int hiddenCount, int outputCount)
	{
		Random random = new Random();
		weights1 = new Matrix(inputCount, hiddenCount).fillGaussian(random);
		weights2 = new Matrix(hiddenCount, outputCount).fillGaussian(random);
	}
	
	public Matrix forward(Matrix input)
	{
		return input.multiply(weights1)
			.scaleSigmoid()
			.multiply(weights2)
			.scaleSigmoid();
	}
}
