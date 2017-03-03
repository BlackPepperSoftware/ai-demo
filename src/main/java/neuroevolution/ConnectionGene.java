package neuroevolution;

import java.util.Objects;
import java.util.Random;

public class ConnectionGene implements Gene
{
	private static final double WEIGHT_MUTATION_STEP = 0.1;
	
	private final double weight;
	
	private final int innovation;
	
	ConnectionGene(double weight, int innovation)
	{
		this.weight = weight;
		this.innovation = innovation;
	}
	
	private ConnectionGene(ConnectionGene that)
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
	
	public ConnectionGene mutateWeight(Random random)
	{
		// TODO: introduce low probability of randomising rather than perturbing
		
		double resultWeight = weight + (2 * random.nextDouble() - 1) * WEIGHT_MUTATION_STEP;
		
		return new ConnectionGene(resultWeight, innovation);
	}
	
	@Override
	public ConnectionGene copy()
	{
		return new ConnectionGene(this);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(weight, innovation);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof ConnectionGene))
		{
			return false;
		}
		
		ConnectionGene that = (ConnectionGene) object;
		
		return Maths.equals(weight, that.weight)
			&& innovation == that.innovation;
	}
	
	@Override
	public String toString()
	{
		return String.format("[w=%f i=%d]", weight, innovation);
	}
}
