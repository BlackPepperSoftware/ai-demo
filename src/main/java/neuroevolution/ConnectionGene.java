package neuroevolution;

import java.util.Objects;

public class ConnectionGene implements Gene
{
	private final NodeGene input;
	
	private final NodeGene output;
	
	private final double weight;
	
	private final boolean enabled;
	
	private final int innovation;
	
	ConnectionGene(NodeGene input, NodeGene output, double weight, boolean enabled, int innovation)
	{
		if (input.equals(output))
		{
			throw new IllegalArgumentException("Cannot connect node gene to itself");
		}
		
		this.input = input;
		this.output = output;
		this.weight = weight;
		this.enabled = enabled;
		this.innovation = innovation;
	}
	
	private ConnectionGene(ConnectionGene that)
	{
		this(that.input, that.output, that.weight, that.enabled, that.innovation);
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
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public ConnectionGene disable()
	{
		if (!enabled)
		{
			throw new IllegalStateException("Connection gene already disabled");
		}
		
		return new ConnectionGene(input, output, weight, false, innovation);
	}
	
	public int getInnovation()
	{
		return innovation;
	}
	
	@Override
	public ConnectionGene copy()
	{
		return new ConnectionGene(this);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(input, output, weight, enabled, innovation);
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
			&& enabled == that.enabled
			&& innovation == that.innovation;
	}
	
	@Override
	public String toString()
	{
		return String.format("[in=%s out=%s w=%f %d i=%d]", input, output, weight, enabled ? 1 : 0, innovation);
	}
}
