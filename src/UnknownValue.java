/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
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
}
