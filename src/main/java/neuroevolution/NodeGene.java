package neuroevolution;

public enum NodeGene implements Gene
{
	INPUT,
	OUTPUT,
	HIDDEN;
	
	@Override
	public NodeGene copy()
	{
		return this;
	}
}
