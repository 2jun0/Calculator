public class UnknownValueNode extends EquationNode
{
	private int id;

	public UnknownValueNode(int id)
	{
		super();
		this.id = id;
	}

	@Override
	protected double calculate(UnknownValue[] unknownValue)
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
	protected void differentiate(UnknownValue value, MultiplyBundleNode bundle)
	{
		if(value.getId() == id)
		{
			bundle.connectLowNode(new ConstValueNode(1));
		}else
		{
			bundle.connectLowNode(new ConstValueNode(0));
		}
	}

	public int getId() {
		return id;
	}

	@Override
	protected EquationNode clone() {
		return new UnknownValueNode(id);
	}

	@Override
	protected boolean equalAllContent(EquationNode node) {
		if(super.equalAllContent(node))
		{
			if(((UnknownValueNode)node).getId() == id)
			{
				return true;
			}else
			{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	protected EquationNode simplify() {
		return clone();
	}
}
