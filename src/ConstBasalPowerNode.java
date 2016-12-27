public class ConstBasalPowerNode extends PowerNode
{
    public ConstBasalPowerNode()
    {
        super();
    }

    public ConstBasalPowerNode(ConstValueNode constBase, EquationNode exponent)
    {
        this();
        lowNodes.set(BASE_POSITION,constBase);
        lowNodes.set(EXPONENT_POSITION,exponent);
    }

    @Override
    protected double calculate(UnknownValue... value) {
        return super.calculate(value);
    }

    @Override
    protected void differentiate(UnknownValue value, MultiplyBundleNode bundle) {
        bundle.connectLowNode(clone());
        bundle.connectLowNode(new ConstValueNode(Math.log(getBaseNode().calculate(null))));
        getExponentNode().differentiate(value,bundle);
    }
}
