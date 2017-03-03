package neuroevolution;

public class NodeGene implements Gene
{
	private NodeGene(NodeGene that)
	{
	}
	
	@Override
	public NodeGene copy()
	{
		return new NodeGene(this);
	}
}
