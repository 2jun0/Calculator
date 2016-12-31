public class ConstBasalPowerNode extends PowerNode
{
    protected ConstBasalPowerNode()
    {
        super();
    }
    public ConstBasalPowerNode(ConstValueNode constBase, EquationNode exponent)
    {
        super(constBase,exponent);
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
