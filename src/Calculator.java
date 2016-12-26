/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
public class Calculator
{
	private Calculator myCalculator = null;
	private EquationTree tree = null;
//er
	public Calculator getInstance()
	{
		if(myCalculator == null)
		{
			myCalculator = new Calculator();
		}
		
		return myCalculator;
	}
	
	private Calculator()
	{
		
	}
	
	public double calculate(UnknownValue... unknownValues)
	{
		return tree.calculate(unknownValues);
	}

	public EquationTree differentiate(UnknownValue unknownValue)
	{
		return  tree.differentiate(unknownValue);
	}

	public void addEquationTree(EquationTree tree)
	{
		this.tree = tree;
	}
}
