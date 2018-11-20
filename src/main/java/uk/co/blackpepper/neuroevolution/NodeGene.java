package uk.co.blackpepper.neuroevolution;

public class NodeGene implements Gene {
	
	private final NodeType type;
	
	private final int id;
	
	public enum NodeType {
		INPUT,
		OUTPUT,
		HIDDEN;
	}
	
	NodeGene(NodeType type, int id) {
		this.type = type;
		this.id = id;
	}
	
	private NodeGene(NodeGene that) {
		this(that.type, that.id);
	}
	
	public boolean isInput() {
		return type == NodeType.INPUT;
	}
	
	public boolean isOutput() {
		return type == NodeType.OUTPUT;
	}
	
	@Override
	public NodeGene copy() {
		return new NodeGene(this);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof NodeGene)) {
			return false;
		}
		
		NodeGene that = (NodeGene) object;
		
		return id == that.id;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%d]", type, id);
	}
}
