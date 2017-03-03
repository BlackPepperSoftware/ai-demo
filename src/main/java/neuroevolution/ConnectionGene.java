package neuroevolution;

import java.util.Objects;
import java.util.Random;

public class ConnectionGene implements Gene
{
	private static final double WEIGHT_MUTATION_STEP = 0.1;
	
	private final NodeGene input;
	
	private final NodeGene output;
	
	private final double weight;
	
	private final int innovation;
	
	ConnectionGene(NodeGene input, NodeGene output, double weight, int innovation)
	{
		if (input.equals(output))
		{
			throw new IllegalArgumentException("Cannot connect node gene to itself");
		}
		
		this.input = input;
		this.output = output;
		this.weight = weight;
		this.innovation = innovation;
	}
	
	private ConnectionGene(ConnectionGene that)
	{
		this(that.input, that.output, that.weight, that.innovation);
	}
	
	public NodeGene getInput()
	{
		return input;
	}
	
	public NodeGene getOutput()
	{
		return output;
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
		
		return new ConnectionGene(input, output, resultWeight, innovation);
	}
	
	@Override
	public ConnectionGene copy()
	{
		return new ConnectionGene(this);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(input, output, weight, innovation);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof ConnectionGene))
		{
			return false;
		}
		
		ConnectionGene that = (ConnectionGene) object;
		
		return input.equals(that.input)
			&& output.equals(that.output)
			&& Maths.equals(weight, that.weight)
			&& innovation == that.innovation;
	}
	
	@Override
	public String toString()
	{
		return String.format("[in=%s out=%s w=%f i=%d]", input, output, weight, innovation);
	}
}
