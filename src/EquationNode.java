import java.util.ArrayList;
import java.util.Collections;

public abstract class EquationNode
{
	protected ArrayList<EquationNode> lowNodes;
	
	protected EquationNode()
	{
		this.lowNodes = new ArrayList<EquationNode>();
	}
	protected abstract void differentiate(UnknownValue value, MultiplyBundleNode bundle);
	protected abstract double calculate(UnknownValue... value);
	protected void connectUpperNode(EquationNode node)
	{
		node.connectLowNode(this);
	}
	protected void connectLowNode(EquationNode node)
	{
		this.lowNodes.add(node);
	}
	protected void connectLowNode(int position, EquationNode node){
		this.lowNodes.add(position,node);
	}
	protected void connectLowNodes(EquationNode... nodes){
		Collections.addAll(this.lowNodes,nodes);
	}
	protected void connectLowNodes(ArrayList<EquationNode> nodes){
		connectLowNodes(nodes.toArray(new EquationNode[nodes.size()]));
	}
	@Override
	protected abstract EquationNode clone();
	public ArrayList<EquationNode> getLowNodes() {
		return this.lowNodes;
	}
	public int getLowNodeSize(){
		return this.lowNodes.size();
	}
	public EquationNode getLowNode(int positino)
	{
		return this.lowNodes.get(positino);
	}
	protected void getAllOfLowNode(ArrayList<EquationNode> allOfLowNode)
	{
		for(EquationNode lowNode : this.lowNodes)
		{
			lowNode.getAllOfLowNode(allOfLowNode);
		}
	}
	protected EquationNode getLowestNode()
	{
		if(this.lowNodes.size() == 1)
		{
			return lowNodes.get(0).getLowestNode();
		}if(this.lowNodes.size() == 0)
		{
			return this;
		}else
		{
			return null;
		}
	}

	protected boolean equalAllContent(EquationNode node) {
		if(node.getClass().equals(this.getClass()))
		{
			for(int i = 0; i < this.lowNodes.size(); i++)
			{
				if(!this.lowNodes.get(i).equalAllContent(node.getLowNode(i)))
				{
					return false;
				}
			}

			return true;
		}else
		{
			return false;
		}
	}

	protected abstract EquationNode simplify();
}
