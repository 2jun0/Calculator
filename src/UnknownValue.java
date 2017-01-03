public class UnknownValue
{
	private int id;
	private double value;
	
	public UnknownValue(int id)
	{
		this.id = id;
	}
	
	public UnknownValue(int id,double value)
	{
		this.id = id;
		this.value = value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public int getId()
	{
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass().equals(UnknownValue.class))
		{
			UnknownValue unknownValue = (UnknownValue)obj;
			if(unknownValue.getId() == id&&unknownValue.getValue() == value)
			{
				return true;
			}else{
				return false;
			}
		}else
		{
			return false;
		}
	}
}
