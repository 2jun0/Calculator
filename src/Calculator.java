public class Calculator
{
	private static Calculator myCalculator = null;

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
		//empty
	}

	public double calculate(EquationTree tree,UnknownValue... unknownValues)
	{
		return tree.calculate(unknownValues);
	}

	public EquationTree differentiate(EquationTree tree,UnknownValue unknownValue)
	{
		return tree.differentiate(unknownValue);
	}
}
