package neuroevolution;

import java.util.Objects;
import java.util.Random;

public class Gene
{
	private static final double WEIGHT_MUTATION_STEP = 0.1;
	
	private static final double DOUBLE_EQUALS_DELTA = 0.000001;
	
	private final double weight;
	
	private final int innovation;
	
	public Gene(double weight, int innovation)
	{
		this.weight = weight;
		this.innovation = innovation;
	}
	
	public Gene(Gene that)
	{
		weight = that.weight;
		innovation = that.innovation;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public int getInnovation()
	{
		return innovation;
	}
	
	public Gene mutateConnectionWeight(Random random)
	{
		// TODO: introduce low probability of randomising rather than perturbing
		
		double resultWeight = weight + (2 * random.nextDouble() - 1) * WEIGHT_MUTATION_STEP;
		
		return new Gene(resultWeight, innovation);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(weight, innovation);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Gene))
		{
			return false;
		}
		
		Gene that = (Gene) object;
		
		return Math.abs(weight - that.weight) < DOUBLE_EQUALS_DELTA
			&& innovation == that.innovation;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[weight=%f, innovation=%d]", getClass().getName(), weight, innovation);
	}
}
