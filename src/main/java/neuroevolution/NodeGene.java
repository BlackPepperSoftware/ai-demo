package neuroevolution;

public class NodeGene implements Gene
{
	private final NodeType type;
	
	private enum NodeType
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
		return new NodeGene(NodeType.INPUT);
	}
	
	public static NodeGene newOutput()
	{
		return new NodeGene(NodeType.OUTPUT);
	}
	
	public static NodeGene newHidden()
	{
		return new NodeGene(NodeType.HIDDEN);
	}
	
	public boolean isInput()
	{
		return type == NodeType.INPUT;
	}
	
	public boolean isOutput()
	{
		return type == NodeType.OUTPUT;
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
