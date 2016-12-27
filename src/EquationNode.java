import java.util.ArrayList;

public abstract class EquationNode
{
	protected ArrayList<EquationNode> lowNodes;
	
	protected EquationNode()
	{
		lowNodes = new ArrayList<EquationNode>();
	}
	
	protected abstract void differentiate(UnknownValue value, MultiplyBundleNode bundle);
	protected abstract double calculate(UnknownValue... value);
	protected void connectUpperNode(EquationNode node)
	{
		node.connectLowNode(this);
	}
	protected void connectLowNode(EquationNode node)
	{
		lowNodes.add(node);
	}
	protected abstract EquationNode clone();
}
