import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MultiplyBundleNode extends EquationNode
{
	public MultiplyBundleNode()
	{
		super();
	}
	protected MultiplyBundleNode(EquationNode... nodes)
	{
		this();
		connectLowNodes(nodes);
	}
	protected MultiplyBundleNode(ArrayList<EquationNode> nodes)
	{
		this();
		connectLowNodes(nodes);
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
		ArrayList<EquationNode> simpleLowNodes = new ArrayList<EquationNode>();//return
		HashMap<Class<? extends EquationNode>,ArrayList<EquationNode>> simpleLowNodeMap = new HashMap<Class<? extends EquationNode>,ArrayList<EquationNode>>();

		EquationNode simpleLowNode = null;
		for(EquationNode node : lowNodes)
		{
			simpleLowNode = node.simplify();
			if(!simpleLowNodeMap.containsKey(simpleLowNode.getClass()))
			{
				simpleLowNodeMap.put(simpleLowNode.getClass(),new ArrayList<EquationNode>());
			}

			//exception


			simpleLowNodeMap.get(simpleLowNode.getClass()).add(simpleLowNode);
		}

		//ConstValueNode
		if(simpleLowNodeMap.containsKey(ConstValueNode.class))
			if(!simplifyConstValueNodes(simpleLowNodeMap.get(ConstValueNode.class),simpleLowNodes))
			{
				return new ConstValueNode(0);
			}

		//PowerNode
		if(simpleLowNodeMap.containsKey(PowerNode.class))
			simplifyPowerNodes(simpleLowNodeMap.get(PowerNode.class),simpleLowNodes);

		//NaturalBasalPowerNode
		if(simpleLowNodeMap.containsKey(NaturalBasalPowerNode.class))
			simplifyNaturalBasalPowerNodes(simpleLowNodeMap.get(PowerNode.class),simpleLowNodes);

		//ConstBasalPowerNode
		if(simpleLowNodeMap.containsKey(ConstBasalPowerNode.class))
			simplifyConstBasalPowerNodes(simpleLowNodeMap.get(ConstBasalPowerNode.class),simpleLowNodes);

		//PlusBundleNode
		if(simpleLowNodeMap.containsKey(PowerNode.class))
			simplifyPlusBundleNode(simpleLowNodeMap.get(PlusBundleNode.class),simpleLowNodes);

		return new MultiplyBundleNode(simpleLowNodes);
	}

	private void simplifyConstBasalPowerNodes(ArrayList<EquationNode> constBasalPowerNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//ConstBasalPowerNode

	}

	private void simplifyPlusBundleNode(ArrayList<EquationNode> plusBundleNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//PlusBundleNode
		ArrayList<EquationNode> simpleBundleLowNode = new ArrayList<EquationNode>();
		for(EquationNode node : plusBundleNodes)
		{
			PlusBundleNode plusBundleNode = (PlusBundleNode)node;
			Collections.addAll(simpleBundleLowNode,plusBundleNode.getLowNodes().toArray(new EquationNode[plusBundleNode.getLowNodeSize()]));
		}

		simpleBundleLowNode.add((new PlusBundleNode(simpleBundleLowNode)).simplify());
	}

	private void simplifyNaturalBasalPowerNodes(ArrayList<EquationNode> naturalBasalPowerNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//NaturalBasalPowerNode
		MultiplyBundleNode naturalBasalExponentBundle = new MultiplyBundleNode(naturalBasalPowerNodes);
		simpleLowNodes.add((new NaturalBasalPowerNode(naturalBasalExponentBundle)).simplify());
	}

	private boolean simplifyConstValueNodes(ArrayList<EquationNode> constValueNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		double constValue = (new MultiplyBundleNode(constValueNodes)).calculate(null);
		if(constValue == 0)
		{
			return false;
		}else if(constValue != 1)
		{
			simpleLowNodes.add(new ConstValueNode(constValue));
		}

		return true;
	}

	private void simplifyPowerNodes(ArrayList<EquationNode> powerNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//PowerNode
		HashMap<EquationNode,ArrayList<EquationNode>> powerLowNodeMap = new HashMap<EquationNode,ArrayList<EquationNode>>();
		for(EquationNode node : powerNodes)
		{
			PowerNode powerNode = (PowerNode)node;
			EquationNode[] keyNodes = powerLowNodeMap.keySet().toArray(new EquationNode[powerLowNodeMap.keySet().size()]);

			boolean hasKey = false;
			for(EquationNode keyNode : keyNodes)
			{
				if(powerNode.getBaseNode().equalAllContent(keyNode))
				{
					hasKey = true;
					powerLowNodeMap.get(keyNode).add(powerNode.getExponentNode());
				}
			}

			if(!hasKey)
			{
				ArrayList<EquationNode> exponentNodes = new ArrayList<EquationNode>();
				exponentNodes.add(powerNode.getExponentNode());
				powerLowNodeMap.put(powerNode.getBaseNode(),exponentNodes);
			}
		}

		EquationNode[] powerKeyNodes = powerLowNodeMap.keySet().toArray(new EquationNode[powerLowNodeMap.keySet().size()]);
		for(EquationNode powerKeyNode : powerKeyNodes)
		{
			MultiplyBundleNode exponentBundle = new MultiplyBundleNode(powerLowNodeMap.get(powerKeyNode));
			simpleLowNodes.add((new PowerNode(powerKeyNode,exponentBundle.simplify())).simplify());
		}
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
