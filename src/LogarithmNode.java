public class LogarithmNode extends EquationNode
{
    public final int BASE_POSITION = 0;
    public final int ANTILOGARITHM_POSITION = 1;

    protected LogarithmNode()
    {
        super();
    }

    public LogarithmNode(EquationNode base,EquationNode antilogarithm)
    {
        this();
        lowNodes.add(BASE_POSITION,base);
        lowNodes.add(ANTILOGARITHM_POSITION,antilogarithm);
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

    @Override
    protected EquationNode simplify() {
        EquationNode simpleBaseNode = getBaseNode().simplify();
        EquationNode simpleAntilogarithmNode = getAntilogarithmNode().simplify();
        boolean isBaseConst = simpleBaseNode.getClass().equals(ConstValueNode.class);
        boolean isAntilogarithmConst = simpleAntilogarithmNode.getClass().equals(ConstValueNode.class);

        if(isBaseConst&&isAntilogarithmConst)
        {
            return new ConstValueNode((new LogarithmNode(simpleBaseNode,simpleAntilogarithmNode)).calculate(null));//create calculable power node and create const value node with calculated value
        }

        LogarithmNode simpleClone = null;
        try {
            simpleClone = this.getClass().newInstance();
            simpleClone.connectLowNode(this.BASE_POSITION,simpleBaseNode);
            simpleClone.connectLowNode(this.ANTILOGARITHM_POSITION,simpleAntilogarithmNode);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return simpleClone;
        }
    }

    protected double log(double base, double antilogarithm)
    {
        return Math.log(antilogarithm)/Math.log(base);
    }
}
