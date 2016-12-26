/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
import java.util.ArrayList;

public abstract class EquationNode
{
	protected ArrayList<EquationNode> lowNodes;
	
	protected EquationNode()
	{
		lowNodes = new ArrayList<EquationNode>();
	}
	
	public abstract void differentiate(UnknownValue value, MultiplyBundleNode bundle);
	public abstract double calculate(UnknownValue... value);
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
