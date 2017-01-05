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

		//ConstExponentialNode
		if(simpleLowNodeMap.containsKey(ConstExponentialPowerNode.class))
			simplifyConstExponentalPowerNodes(simpleLowNodeMap.get(ConstExponentialPowerNode.class),simpleLowNodes);

		//MultiplyBundleNode
		if(simpleLowNodeMap.containsKey(MultiplyBundleNode.class))
			simplifyMultiplyBundleNodes(simpleLowNodeMap.get(MultiplyBundleNode.class),simpleLowNodes);

		//Others
		if(simpleLowNodeMap.containsKey(UnknownValueNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(UnknownValueNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(NaturalLogarithmNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(NaturalLogarithmNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(LogarithmNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(LogarithmNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(PowerNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(PlusBundleNode.class),simpleLowNodes);

		return new MultiplyBundleNode(simpleLowNodes);
	}

	private void simplifyMultiplyBundleNodes(ArrayList<EquationNode> multiplyBundleNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//MultiplyBundleNode
		MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
		for(EquationNode node : multiplyBundleNodes)
		{
			multiplyBundleNode.connectLowNodes(((MultiplyBundleNode)node).getLowNodes());
		}

		simpleLowNodes.add(multiplyBundleNode.simplify());
	}

	private void simplifyConstBasalPowerNodes(ArrayList<EquationNode> constBasalPowerNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//ConstBasalPowerNode
		HashMap<Double,ArrayList<EquationNode>> powerLowNodeMap = new HashMap<Double,ArrayList<EquationNode>>();
		for(EquationNode node : constBasalPowerNodes)
		{
			ConstBasalPowerNode constBasalPowerNode = (ConstBasalPowerNode)node;

			double baseValue = constBasalPowerNode.getBaseNode().getValue();
			if(!powerLowNodeMap.containsKey(baseValue))
			{
				powerLowNodeMap.put(baseValue,new ArrayList<EquationNode>());
			}

			powerLowNodeMap.get(baseValue).add(constBasalPowerNode.getExponentNode());
		}

		Double[] keys = powerLowNodeMap.keySet().toArray(new Double[powerLowNodeMap.keySet().size()]);
		for(double key : keys)
		{
			PlusBundleNode exponentBundle = new PlusBundleNode(powerLowNodeMap.get(key));
			simpleLowNodes.add((new ConstBasalPowerNode(key,exponentBundle)).simplify());
		}
	}

	private void simplifyConstExponentalPowerNodes(ArrayList<EquationNode> constExponentialPowerNodes, ArrayList<EquationNode> simpleLowNodes)
	{
		//ConstExponentialNode
		HashMap<EquationNode,Double> constExponentialPowerLowNodeMap = new HashMap<EquationNode,Double>();
		for(EquationNode node : constExponentialPowerNodes)
		{
			ConstExponentialPowerNode constExponentialPowerNode = (ConstExponentialPowerNode)node;
			EquationNode[] keyNodes = constExponentialPowerLowNodeMap.keySet().toArray(new EquationNode[constExponentialPowerLowNodeMap.keySet().size()]);

			boolean hasKey = false;
			for(EquationNode keyNode : keyNodes)
			{
				if(constExponentialPowerNode.getBaseNode().equalAllContent(keyNode))
				{
					hasKey = true;
					constExponentialPowerLowNodeMap.put(keyNode,constExponentialPowerLowNodeMap.get(keyNode)+constExponentialPowerNode.getExponentNode().getValue());
				}
			}

			if(!hasKey)
			{
				constExponentialPowerLowNodeMap.put(constExponentialPowerNode.getBaseNode(),constExponentialPowerNode.getExponentNode().getValue());
			}
		}

		EquationNode[] powerKeyNodes = constExponentialPowerLowNodeMap.keySet().toArray(new EquationNode[constExponentialPowerLowNodeMap.keySet().size()]);
		for(EquationNode powerKeyNode : powerKeyNodes)
		{
			simpleLowNodes.add((new ConstExponentialPowerNode(powerKeyNode,constExponentialPowerLowNodeMap.get(powerKeyNode))).simplify());
		}
	}

	private void simplifyNaturalBasalPowerNodes(ArrayList<EquationNode> naturalBasalPowerNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//NaturalBasalPowerNode
		PlusBundleNode naturalBasalExponentBundle = new PlusBundleNode(naturalBasalPowerNodes);
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
			PlusBundleNode exponentBundle = new PlusBundleNode(powerLowNodeMap.get(powerKeyNode));
			simpleLowNodes.add((new PowerNode(powerKeyNode,exponentBundle)).simplify());
		}
	}

	private void simplifyOtherNodes(ArrayList<EquationNode> otherNodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//Others
		Collections.addAll(simpleLowNodes,otherNodes.toArray(new EquationNode[otherNodes.size()]));
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
