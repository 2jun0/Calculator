import java.util.ArrayList;

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
	@Override
	protected abstract EquationNode clone();
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
}
