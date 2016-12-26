public class PlusBundleNode extends EquationNode
{
	public PlusBundleNode()
	{
		super();
	}
	
	@Override
	public double calculate(UnknownValue[] value)
	{
		double sum = 0;
		
		for(EquationNode node : lowNodes)
		{
			sum+=node.calculate(value);
		}
		
		return sum;
	}

	@Override
	public void differentiate(UnknownValue value, MultiplyBundleNode bundle)
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
}
