/**
 * Calculator
 * Created by Notelessness on 2016-12-24.
 */
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
    public double calculate(UnknownValue... value) {
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
    public void differentiate(UnknownValue value, MultiplyBundleNode bundle)
    {

    }

    @Override
    protected EquationNode clone() {
        return new PowerNode(getBaseNode().clone(),getExponentNode().clone());
    }
}
