package neuroevolution;

public class Gene
{
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
}
