import java.util.ArrayList;

public class PlusBundleNode extends EquationNode
{
	public PlusBundleNode()
	{
		super();
	}
	public PlusBundleNode(EquationNode... nodes){
		this();
		connectLowNodes(nodes);
	}
	public PlusBundleNode(ArrayList<EquationNode> nodes)
	{
		this();
		connectLowNodes(nodes);
	}

	@Override
	protected double calculate(UnknownValue[] value)
	{
		double sum = 0;
		
		for(EquationNode node : lowNodes)
		{
			sum+=node.calculate(value);
		}
		
		return sum;
	}

	@Override
	protected void differentiate(UnknownValue value, MultiplyBundleNode bundle)
	{
		PlusBundleNode plusBundleNode = new PlusBundleNode();
		
		for(EquationNode node : lowNodes)
		{
			MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
			node.differentiate(value, multiplyBundleNode);
			plusBundleNode.connectLowNode(multiplyBundleNode);
		}
		
		bundle.connectLowNode(plusBundleNode);
	}

	@Override
	protected EquationNode clone()
	{
		MultiplyBundleNode clone = new MultiplyBundleNode();

		for(EquationNode node : lowNodes)
		{
			clone.connectLowNode(node.clone());
		}

		return clone;
	}

	@Override
	protected EquationNode simplify()
	{
		ArrayList<EquationNode> simpleLowNodes = new ArrayList<EquationNode>();

		double constValue = 0;
		EquationNode simpleLowNode = null;
		for(EquationNode node : lowNodes)
		{
			simpleLowNode = node.simplify();
			if(simpleLowNode.getClass().equals(ConstValueNode.class))
			{
				constValue *= ((ConstValueNode)simpleLowNode).getValue();
			}else if(simpleLowNode.getClass().equals(PowerNode.class))
			{

			}else {
				simpleLowNodes.add(simpleLowNode);
			}
		}

		if(constValue == 0) {
			return new ConstValueNode(0);
		}else if(constValue != 1)
		{
			simpleLowNodes.add(new ConstValueNode(constValue));
		}

		return new MultiplyBundleNode(simpleLowNodes.toArray(new EquationNode[simpleLowNodes.size()]));
	}

	@Override
	protected boolean equalAllContent(EquationNode node) {
		if(node.getClass().equals(this.getClass()))
		{
			for(EquationNode lowNode1 : lowNodes)
			{
				boolean isEqaulAllContent = false;

				for(EquationNode lowNode2 : node.getLowNodes())
				{
					isEqaulAllContent = (isEqaulAllContent|lowNode1.equalAllContent(lowNode2));
				}

				if(!isEqaulAllContent)
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
}
