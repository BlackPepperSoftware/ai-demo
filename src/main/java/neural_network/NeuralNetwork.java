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
	
	public double cost(Matrix input, Matrix target)
	{
		Matrix output = forward(input);
		Matrix costs = target.subtract(output).square().scale(0.5);
		return costs.column(0).average().orElse(0);
	}
	
	public void learn(Matrix input, Matrix target)
	{
		// feed forward
		
		Matrix layerInput1 = input.multiply(weights1);
		Matrix layerOutput1 = layerInput1.scaleSigmoid();
		
		Matrix layerInput2 = layerOutput1.multiply(weights2);
		Matrix output = layerInput2.scaleSigmoid();
		
		// back propagation
		
		Matrix delta2 = output.subtract(target).times(layerInput2.scaleSigmoidPrime());
		Matrix deltaCost2 = layerOutput1.transpose().multiply(delta2);
		
		Matrix delta1 = delta2.multiply(weights2.transpose()).times(layerInput1.scaleSigmoidPrime());
		Matrix deltaCost1 = input.transpose().multiply(delta1);
		
		weights2 = weights2.subtract(deltaCost2);
		weights1 = weights1.subtract(deltaCost1);
	}
}
