/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
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
    public double calculate(UnknownValue... value) {
        return super.calculate(value);
    }

    @Override
    public void differentiate(UnknownValue value, MultiplyBundleNode bundle) {
        bundle.connectLowNode(clone());
        bundle.connectLowNode(new ConstValueNode(Math.log(getBaseNode().calculate(null))));
        getExponentNode().differentiate(value,bundle);
    }
}
