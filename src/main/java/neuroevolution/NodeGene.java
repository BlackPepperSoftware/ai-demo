package neuroevolution;

import static neuroevolution.NodeGene.NodeType.INPUT;
import static neuroevolution.NodeGene.NodeType.OUTPUT;

public class NodeGene implements Gene
{
	private final NodeType type;
	
	public enum NodeType
	{
		INPUT,
		OUTPUT,
		HIDDEN;
	}
	
	private NodeGene(NodeType type)
	{
		this.type = type;
	}
	
	private NodeGene(NodeGene that)
	{
		this(that.type);
	}
	
	public static NodeGene newInput()
	{
		return new NodeGene(INPUT);
	}
	
	public static NodeGene newOutput()
	{
		return new NodeGene(OUTPUT);
	}
	
	@Override
	public NodeGene copy()
	{
		return new NodeGene(this);
	}
	
	@Override
	public String toString()
	{
		return type.toString();
	}
}
