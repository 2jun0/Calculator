/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
public class ConstValueNode extends EquationNode
{
	private double value;

	public ConstValueNode(double value)
	{
		super();
		this.value = value;
	}

	@Override
	public double calculate(UnknownValue[] unknownValue)
	{
		return value;
	}

	@Override
	public void differentiate(UnknownValue unknownValue, MultiplyBundleNode rootNode)
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
