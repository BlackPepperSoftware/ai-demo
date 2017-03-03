package neuroevolution;

import java.util.Random;

public class NeatNetwork
{
	private Population population;
	
	public NeatNetwork(int inputNodeCount, int outputNodeCount)
	{
		population = new Population(10, inputNodeCount, outputNodeCount, new GeneFactory(), new Random());
	}
}
