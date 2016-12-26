/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
import java.util.ArrayList;

public class EquationTree
{
	private ArrayList<EquationNode> nodeArray;
	private EquationNode rootNode;
	
	public EquationTree()
	{
		nodeArray = new ArrayList<EquationNode>();
	}

	public EquationTree(EquationNode rootNode)
	{
		nodeArray = new ArrayList<EquationNode>();
		setRootNode(rootNode);
	}

	public void setRootNode(EquationNode rootNode)
	{
		clearNode();
		this.rootNode = rootNode;
		nodeArray.add(rootNode);
	}

	public void clearNode()
	{
		nodeArray.clear();
		rootNode = null;
	}
	
	public void connectUpperNode(EquationNode node)
	{
		rootNode.connectUpperNode(node);
		rootNode = node;
	}

	public double calculate(UnknownValue... unknownValues)
	{
		return rootNode.calculate(unknownValues);
	}

	public EquationTree differentiate(UnknownValue unknownValue)
	{
		MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
		rootNode.differentiate(unknownValue, multiplyBundleNode);
		EquationTree tree = new EquationTree(multiplyBundleNode);

		return tree;
	}
}
