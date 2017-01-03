import java.util.ArrayList;

public class ConstBasalPowerNode extends PowerNode
{
    static
    {

    }

    public ConstBasalPowerNode()
    {
        super();
    }
    protected ConstBasalPowerNode(ConstValueNode constBase, EquationNode exponent)
    {
        super(constBase,exponent);
    }
    protected ConstBasalPowerNode(double constBaseValue, EquationNode exponent) {
        this(new ConstValueNode(constBaseValue),exponent);
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
