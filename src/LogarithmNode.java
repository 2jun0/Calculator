public class LogarithmNode extends EquationNode
{
    protected final int BASE_POSITION = 0;
    protected final int ANTILOGARITHM_POSITION = 1;

    public LogarithmNode()
    {
        super();
    }

    public LogarithmNode(EquationNode base,EquationNode antilogarithm)
    {
        this();
        lowNodes.set(BASE_POSITION,base);
        lowNodes.set(ANTILOGARITHM_POSITION,antilogarithm);
    }

    @Override
    protected double calculate(UnknownValue... value) {
        return log(getBaseNode().calculate(value),getAntilogarithmNode().calculate(value));
    }

    @Override
    protected void differentiate(UnknownValue value, MultiplyBundleNode bundle)
    {
        MultiplyBundleNode multiplyBundleNode = new MultiplyBundleNode();
        multiplyBundleNode.connectLowNode(new NaturalLogarithmNode(getAntilogarithmNode()));
        multiplyBundleNode.connectLowNode(new ConstExponentialPowerNode(new NaturalLogarithmNode(getBaseNode()),-1));

        multiplyBundleNode.differentiate(value,bundle);
    }

    @Override
    protected EquationNode clone() {
        return new LogarithmNode(getBaseNode().clone(),getAntilogarithmNode().clone());
    }

    protected EquationNode getBaseNode()
    {
        return lowNodes.get(BASE_POSITION);
    }

    protected  EquationNode getAntilogarithmNode()
    {
        return lowNodes.get(ANTILOGARITHM_POSITION);
    }

    protected double log(double base, double antilogarithm)
    {
        return Math.log(antilogarithm)/Math.log(base);
    }
}
