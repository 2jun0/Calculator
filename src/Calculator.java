public class Calculator
{
	private static Calculator myCalculator = null;
	private EquationTree tree = null;

	public static Calculator getInstance()
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

	public void setTargetTree(int index)
	{
		//empty
	}

	public void addEquationTree(EquationTree tree)
	{
		//empty
	}

	public double calculate(UnknownValue... unknownValues)
	{
		return tree.calculate(unknownValues);
	}

	public EquationTree differentiate(UnknownValue unknownValue)
	{
		return  tree.differentiate(unknownValue);
	}

	public void setEquationTree(EquationTree tree)
	{
		this.tree = tree;
	}
}
