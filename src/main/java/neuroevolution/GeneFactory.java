package neuroevolution;

public class GeneFactory
{
	private int innovation;
	
	public GeneFactory()
	{
		innovation = 1;
	}
	
	public Gene newGene(double weight)
	{
		return new Gene(weight, nextInnovation());
	}
	
	private int nextInnovation()
	{
		return innovation++;
	}
}
