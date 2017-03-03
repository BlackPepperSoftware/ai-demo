package neuroevolution;

import java.util.Random;

public class NeatNetwork
{
	private Population population;
	
	public NeatNetwork()
	{
		population = new Population(10, new Random());
	}
}
