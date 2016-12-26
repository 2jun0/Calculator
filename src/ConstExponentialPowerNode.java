public class ConstExponentialPowerNode extends PowerNode
{
    public ConstExponentialPowerNode()
    {
        super();
    }

    public ConstExponentialPowerNode(EquationNode base,ConstValueNode constExponent)
    {
        this();
        lowNodes.set(BASE_POSITION,base);
        lowNodes.set(EXPONENT_POSITION,constExponent);
    }

    public ConstExponentialPowerNode(EquationNode base,double exponentValue)
    {
        this(base,new ConstValueNode(exponentValue));
    }

    @Override
    public void differentiate(UnknownValue value, MultiplyBundleNode bundle)
    {
        double exponentValue = getExponentNode().calculate(null);
        bundle.connectLowNode(new ConstValueNode(exponentValue));
        MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
        getBaseNode().differentiate(value, multiplyBundleNode);
        bundle.connectLowNode(multiplyBundleNode);
        bundle.connectLowNode(new ConstExponentialPowerNode(getBaseNode().clone(),new ConstValueNode(exponentValue-1)));
    }

    @Override
    protected EquationNode clone() {
        return new ConstExponentialPowerNode(getBaseNode(),(ConstValueNode) getExponentNode());
    }
}
