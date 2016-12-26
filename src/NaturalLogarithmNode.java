public class NaturalLogarithmNode extends LogarithmNode
{
    protected final int ANTILOGARITHM_POSITION = 0;

    public NaturalLogarithmNode()
    {
        super();
    }

    public NaturalLogarithmNode(EquationNode antilogarithm)
    {
        this();
        lowNodes.set(ANTILOGARITHM_POSITION,antilogarithm);
    }

    @Override
    protected EquationNode getBaseNode()
    {
        return null;
    }

    @Override
    protected  EquationNode getAntilogarithmNode()
    {
        return lowNodes.get(ANTILOGARITHM_POSITION);
    }

    @Override
    public double calculate(UnknownValue... value)
    {
        return Math.log(getAntilogarithmNode().calculate(value));
    }

    @Override
    public void differentiate(UnknownValue value, MultiplyBundleNode bundle)
    {
        bundle.connectLowNode(new ConstExponentialPowerNode(getAntilogarithmNode().clone(),-1));
        getAntilogarithmNode().differentiate(value,bundle);
    }
}
