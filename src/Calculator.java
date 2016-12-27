import java.util.ArrayList;

public class Calculator
{
	private static Calculator myCalculator = null;
	private int targetTreeIndex;
	private ArrayList<EquationTree> treeArrayList = null;

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
	    treeArrayList = new ArrayList<EquationTree>();
		targetTreeIndex = 0;
	}

	public void setTargetTree(int index)
	{
		targetTreeIndex = index;
	}

	public void addEquationTree(EquationTree tree)
	{
		treeArrayList.add(tree);
	}

	public EquationTree[] getEquationTree()
    {
	    return treeArrayList.toArray(new EquationTree[treeArrayList.size()]);
    }

    public EquationTree getTargetTree()
    {
	    return treeArrayList.get(targetTreeIndex);
    }

	public double calculate(UnknownValue... unknownValues)
	{
		return getTargetTree().calculate(unknownValues);
	}

	public EquationTree differentiate(UnknownValue unknownValue)
	{
		return  getTargetTree().differentiate(unknownValue);
	}
}
