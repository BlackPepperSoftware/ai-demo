package neuroevolution;

public class GeneFactory
{
	private int innovation;
	
	public GeneFactory()
	{
		innovation = 1;
	}
	
	public ConnectionGene newConnectionGene(double weight)
	{
		return new ConnectionGene(weight, nextInnovation());
	}
	
	private int nextInnovation()
	{
		return innovation++;
	}
}
