import java.util.ArrayList;

public class EquationTree
{
	private ArrayList<EquationNode> nodeArray;
	private EquationNode rootNode;
	private EquationNode lowestNode;
	private boolean connectableToLowNode;
	
	public EquationTree()
	{
		nodeArray = new ArrayList<EquationNode>();
		connectableToLowNode = false;
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
		this.lowestNode = rootNode.getLowestNode();
		nodeArray.add(rootNode);
		rootNode.getAllOfLowNode(nodeArray);
		connectableToLowNode = true;
	}

	public void clearNode()
	{
		nodeArray.clear();
		rootNode = null;
		lowestNode = null;
		connectableToLowNode = false;
	}
	
	public void connectLowNode(EquationNode... nodes)
	{
		connectableToLowNode = false;

		for(EquationNode node : nodes) {
			nodeArray.add(node);
			lowestNode.connectLowNode(node);
			node.getAllOfLowNode(nodeArray);
		}

		lowestNode = null;
	}

	public void connectLowNode(EquationNode node)
	{
		lowestNode.connectLowNode(node);
		nodeArray.add(node);
		node.getAllOfLowNode(nodeArray);
		lowestNode = node.getLowestNode();
	}

	public void connectLowTree(EquationTree... trees)
	{
		connectableToLowNode = false;

		for(EquationTree tree : trees)
		{
			EquationNode node = tree.getRootNode();
			nodeArray.add(node);
			lowestNode.connectLowNode(node);
		}

		lowestNode = null;
	}

	public void connectLowTree(EquationTree tree)
	{
		EquationNode node = tree.getRootNode();
		lowestNode.connectLowNode(node);
		lowestNode = tree.getLowestNode();
		nodeArray.add(node);
	}

	public boolean isConnectableToLowNode()
	{
		return connectableToLowNode;
	}

	public EquationNode getRootNode()
	{
		return rootNode;
	}

	public EquationNode getLowestNode()
	{
		return lowestNode;
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
