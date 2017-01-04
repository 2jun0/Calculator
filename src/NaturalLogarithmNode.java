public class NaturalLogarithmNode extends LogarithmNode
{
    public final int ANTILOGARITHM_POSITION = 0;

    public NaturalLogarithmNode()
    {
        super();
    }

    NaturalLogarithmNode(EquationNode antilogarithm)
    {
        this();
        lowNodes.add(ANTILOGARITHM_POSITION,antilogarithm);
    }

    @Override
    protected EquationNode getBaseNode()
    {
        return null;
    }

    @Override
    protected EquationNode getAntilogarithmNode()
    {
        return lowNodes.get(ANTILOGARITHM_POSITION);
    }

    @Override
    protected double calculate(UnknownValue... value)
    {
        return Math.log(getAntilogarithmNode().calculate(value));
    }

    @Override
    protected void differentiate(UnknownValue value, MultiplyBundleNode bundle)
    {
        bundle.connectLowNode(new ConstExponentialPowerNode(getAntilogarithmNode().clone(),-1));
        getAntilogarithmNode().differentiate(value,bundle);
    }

    @Override
    protected EquationNode clone() {
        return new NaturalLogarithmNode(getAntilogarithmNode());
    }

    @Override
    protected EquationNode simplify() {
        EquationNode simpleAntilogarithm = getAntilogarithmNode().simplify();
        if(simpleAntilogarithm.getClass().equals(ConstValueNode.class))
        {
            return new ConstValueNode((new NaturalLogarithmNode(simpleAntilogarithm)).calculate(null));
        }

        return new NaturalBasalPowerNode(simpleAntilogarithm);
    }
}
