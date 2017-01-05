import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

			simpleLowNodeMap.get(simpleLowNode.getClass()).add(simpleLowNode);
		}

		//ConstValueNode
		if(simpleLowNodeMap.containsKey(ConstValueNode.class))
			simplifyConstValueNodes(simpleLowNodeMap.get(ConstValueNode.class),simpleLowNodes);

		//NaturalLogarithmNode
		if(simpleLowNodeMap.containsKey(NaturalLogarithmNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(NaturalLogarithmNode.class),simpleLowNodes);

		//Others
		if(simpleLowNodeMap.containsKey(UnknownValueNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(UnknownValueNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(LogarithmNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(LogarithmNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(PowerNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(PlusBundleNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(MultiplyBundleNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(MultiplyBundleNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(PowerNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(PowerNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(ConstExponentialPowerNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(ConstExponentialPowerNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(NaturalBasalPowerNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(PowerNode.class),simpleLowNodes);

		if(simpleLowNodeMap.containsKey(ConstBasalPowerNode.class))
			simplifyOtherNodes(simpleLowNodeMap.get(ConstBasalPowerNode.class),simpleLowNodes);

		return new PlusBundleNode(simpleLowNodes);
	}

	private void simplifyConstValueNodes(ArrayList<EquationNode> nodes,ArrayList<EquationNode> simpleLowNodes)
	{
		double constValue = (new PlusBundleNode(nodes)).calculate(null);
		if(constValue == 0)
		{
			return;
		}else if(constValue != 1)
		{
			simpleLowNodes.add(new ConstValueNode(constValue));
		}
	}

	private void simplifyNaturalLogarithmNodes(ArrayList<EquationNode> nodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//NaturalLogarithmNode
		simpleLowNodes.add((new NaturalLogarithmNode(new MultiplyBundleNode(nodes))).simplify());
	}

	private void simplifyLogarithmNode(ArrayList<EquationNode> nodes,ArrayList<EquationNode> simpleLowNodes)
	{
		//LogarithmNode
		HashMap<EquationNode,ArrayList<EquationNode>> logarithmLowMap = new HashMap<EquationNode,ArrayList<EquationNode>>();
		for(EquationNode node : nodes)
		{
			LogarithmNode logarithmNode = (LogarithmNode)node;
			EquationNode[] keyNodes = logarithmLowMap.keySet().toArray(new EquationNode[logarithmLowMap.keySet().size()]);
			boolean hasKey = false;
			for(EquationNode keyNode : keyNodes)
			{
				if(logarithmNode.getBaseNode().equalAllContent(keyNode))
				{
					hasKey = true;
					logarithmLowMap.get(keyNode).add(logarithmNode.getAntilogarithmNode());
				}
			}

			if(!hasKey)
			{
				ArrayList<EquationNode> antilogarithmNodes = new ArrayList<EquationNode>();
				antilogarithmNodes.add(logarithmNode.getAntilogarithmNode());
				logarithmLowMap.put(logarithmNode.getBaseNode(),antilogarithmNodes);
			}
		}

		EquationNode[] logarithmKeyNodes = logarithmLowMap.keySet().toArray(new EquationNode[logarithmLowMap.keySet().size()]);
		for(EquationNode powerKeyNode : logarithmKeyNodes)
		{
			MultiplyBundleNode antilogarithmBundle = new MultiplyBundleNode(logarithmLowMap.get(powerKeyNode));
			simpleLowNodes.add((new LogarithmNode(powerKeyNode,antilogarithmBundle)).simplify());
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
