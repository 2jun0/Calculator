public class ConstExponentialPowerNode extends PowerNode
{
    protected ConstExponentialPowerNode()
    {
        super();
    }

    public ConstExponentialPowerNode(EquationNode base,ConstValueNode constExponent)
    {
        super(base,constExponent);
    }

    ConstExponentialPowerNode(EquationNode base,double exponentValue)
    {
        this(base,new ConstValueNode(exponentValue));
    }

    @Override
    protected void differentiate(UnknownValue value, MultiplyBundleNode bundle)
    {
        double exponentValue = getExponentNode().calculate(null);
        bundle.connectLowNode(new ConstValueNode(exponentValue));
        getBaseNode().differentiate(value,bundle);
        bundle.connectLowNode(new ConstExponentialPowerNode(getBaseNode().clone(),new ConstValueNode(exponentValue-1)));
    }

    @Override
    protected EquationNode clone() {
        return new ConstExponentialPowerNode(getBaseNode(),(ConstValueNode) getExponentNode());
    }
}
