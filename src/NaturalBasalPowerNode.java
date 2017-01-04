public class NaturalBasalPowerNode extends ConstBasalPowerNode
{
    public final int EXPONENT_POSITION = 0;

    public NaturalBasalPowerNode()
    {
        super();
    }

    NaturalBasalPowerNode(EquationNode antilogarithm)
    {
        this();
        lowNodes.add(EXPONENT_POSITION,antilogarithm);
    }

    @Override
    protected double calculate(UnknownValue... value) {
        return Math.exp(lowNodes.get(0).calculate(value));
    }

    @Override
    protected void differentiate(UnknownValue value, MultiplyBundleNode multiplyBundleNode)
    {
        multiplyBundleNode.connectLowNode(clone());
        getExponentNode().differentiate(value, multiplyBundleNode);
    }

    protected EquationNode getBaseNode()
    {
        return null;
    }

    protected EquationNode getExponentNode()
    {
        return lowNodes.get(EXPONENT_POSITION);
    }

    @Override
    protected EquationNode clone()
    {
        NaturalBasalPowerNode naturalExponentialPowerNode = new NaturalBasalPowerNode();
        naturalExponentialPowerNode.connectLowNode(getExponentNode().clone());

        return naturalExponentialPowerNode;
    }

    @Override
    protected EquationNode simplify() {
        EquationNode simpleExponent = getExponentNode().simplify();
        if(simpleExponent.getClass().equals(ConstValueNode.class))
        {
            return new ConstValueNode((new NaturalBasalPowerNode(simpleExponent)).calculate(null));
        }

        return new NaturalBasalPowerNode(simpleExponent);
    }
}
