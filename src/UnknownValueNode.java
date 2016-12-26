/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
public class UnknownValueNode extends EquationNode
{
	private int id;

	public UnknownValueNode(int id)
	{
		super();
		this.id = id;
	}

	@Override
	public double calculate(UnknownValue[] unknownValue)
	{
		for(UnknownValue value : unknownValue)
		{
			if(value.getId() == id)
			{
				return value.getValue();
			}
		}
		
		return Double.NaN;
	}

	@Override
	public void differentiate(UnknownValue value,MultiplyBundleNode bundle)
	{
		if(value.getId() == id)
		{
			bundle.connectLowNode(new ConstValueNode(1));
		}else
		{
			bundle.connectLowNode(new ConstValueNode(0));
		}
	}

	@Override
	protected EquationNode clone() {
		return new UnknownValueNode(id);
	}
}
