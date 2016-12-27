public class ConstValueNode extends EquationNode
{
	private double value;

	public ConstValueNode(double value)
	{
		super();
		this.value = value;
	}

	@Override
	protected double calculate(UnknownValue[] unknownValue)
	{
		return value;
	}

	@Override
	protected void differentiate(UnknownValue unknownValue, MultiplyBundleNode rootNode)
	{
		rootNode.connectLowNode(new ConstValueNode(0));
	}

	public double getValue()
	{
		return value;
	}

	@Override
	protected EquationNode clone() {
		return new ConstValueNode(value);
	}
}
