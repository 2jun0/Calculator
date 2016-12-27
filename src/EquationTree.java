import java.util.ArrayList;

public class EquationTree
{
	private ArrayList<EquationNode> nodeArray;
	private EquationNode rootNode;
	private boolean connectableToLowNode;
	
	public EquationTree()
	{
		nodeArray = new ArrayList<EquationNode>();
		connectableToLowNode = true;
	}

	public EquationTree(EquationNode rootNode)
	{
		this();
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
	
	public void connectLowNode(EquationNode... nodes)
	{
		if(nodes.length > 1)
		{
			connectableToLowNode = false;
		}

		for(EquationNode node : nodes)
		{
			nodeArray.add(node);
		}
	}

	public void connectLowNode(EquationTree... trees)
	{
		if(trees.length > 1)
		{
			connectableToLowNode = false;
		}

		for(EquationTree tree : trees)
		{
			nodeArray.add(tree.getRootNode());
		}
	}

	public boolean isConnectableToLowNode()
	{
		return connectableToLowNode;
	}

	public EquationNode getRootNode()
	{
		return rootNode;
	}

	protected double calculate(UnknownValue... unknownValues)
	{
		return rootNode.calculate(unknownValues);
	}

	protected EquationTree differentiate(UnknownValue unknownValue)
	{
		MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
		rootNode.differentiate(unknownValue, multiplyBundleNode);
		EquationTree tree = new EquationTree(multiplyBundleNode);

		return tree;
	}
}
