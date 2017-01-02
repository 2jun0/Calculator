import java.util.ArrayList;
import java.util.Collections;

public class MultiplyBundleNode extends EquationNode
{
	public MultiplyBundleNode()
	{
		super();
	}
	protected MultiplyBundleNode(EquationNode... nodes)
	{
		this();
		Collections.addAll(lowNodes,nodes);
	}
	@Override
	protected double calculate(UnknownValue[] value)
	{
		double sum = 1;

		for(EquationNode node : lowNodes)
		{
			sum*=node.calculate(value);
		}

		return sum;
	}

	@Override
	protected void differentiate(UnknownValue value, MultiplyBundleNode bundle)
	{
		PlusBundleNode plusBundleNode = new PlusBundleNode();

		for(EquationNode differentiatedNode : lowNodes)
		{
			MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
			differentiatedNode.differentiate(value, multiplyBundleNode);
			for(EquationNode node : lowNodes)
			{
				if(!node.equals(differentiatedNode))
				{
					multiplyBundleNode.connectLowNode(node.clone());
				}
			}
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

		double constValue = 1;
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


}
