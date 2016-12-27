public class PowerNode extends EquationNode
{
    protected final int BASE_POSITION = 0;
    protected final int EXPONENT_POSITION = 1;

    public PowerNode()
    {
        super();
    }

    public PowerNode(EquationNode base,EquationNode exponent)
    {
        this();
        lowNodes.set(BASE_POSITION,base);
        lowNodes.set(EXPONENT_POSITION,exponent);
    }

    @Override
    protected double calculate(UnknownValue... value) {
        return Math.pow(getBaseNode().calculate(value),getExponentNode().calculate(value));
    }

    protected EquationNode getBaseNode()
    {
        return lowNodes.get(BASE_POSITION);
    }

    protected  EquationNode getExponentNode()
    {
        return lowNodes.get(EXPONENT_POSITION);
    }

    @Override
    protected void differentiate(UnknownValue value, MultiplyBundleNode rootBundle)
    {
        rootBundle.connectLowNode(clone());
        PlusBundleNode plusBundleNode = new PlusBundleNode();

        MultiplyBundleNode bundle1 = new MultiplyBundleNode();
        getExponentNode().differentiate(value,bundle1);
        bundle1.connectLowNode(new NaturalLogarithmNode(getBaseNode().clone()));

        MultiplyBundleNode bundle2 = new MultiplyBundleNode();
        getBaseNode().differentiate(value,bundle2);
        bundle2.connectLowNode(getExponentNode());
        bundle2.connectLowNode(new ConstExponentialPowerNode(getBaseNode(),-1));

        plusBundleNode.connectLowNode(bundle1);
        plusBundleNode.connectLowNode(bundle2);

        rootBundle.connectLowNode(plusBundleNode);
    }

    @Override
    protected EquationNode clone() {
        return new PowerNode(getBaseNode().clone(),getExponentNode().clone());
    }
}
