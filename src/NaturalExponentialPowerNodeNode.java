public class NaturalExponentialPowerNodeNode extends ConstBasalPowerNode
{
    protected final int EXPONENT_POSITION = 0;

    public NaturalExponentialPowerNodeNode()
    {
        super();
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

    protected  EquationNode getExponentNode()
    {
        return lowNodes.get(EXPONENT_POSITION);
    }

    @Override
    protected EquationNode clone()
    {
        NaturalExponentialPowerNodeNode naturalExponentialPowerNode = new NaturalExponentialPowerNodeNode();
        naturalExponentialPowerNode.connectLowNode(getExponentNode().clone());

        return naturalExponentialPowerNode;
    }
}
