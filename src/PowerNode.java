public class PowerNode extends EquationNode
{
    public final int BASE_POSITION = 0;
    public final int EXPONENT_POSITION = 1;
	
    protected PowerNode()
    {
        super();
    }

    PowerNode(EquationNode base, EquationNode exponent)
    {
        this();
        lowNodes.add(BASE_POSITION,base);
        lowNodes.add(EXPONENT_POSITION,exponent);
    }

    @Override
    protected double calculate(UnknownValue... value) {
        return Math.pow(getBaseNode().calculate(value),getExponentNode().calculate(value));
    }

    protected EquationNode getBaseNode()
    {
        return lowNodes.get(BASE_POSITION);
    }

    protected EquationNode getExponentNode()
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

    @Override
    protected EquationNode simplify() {
        EquationNode simpleBaseNode = getBaseNode().simplify();
        EquationNode simpleExponentNode = getExponentNode().simplify();
        boolean isBaseConst = simpleBaseNode.getClass().equals(ConstValueNode.class);
        boolean isExponentConst = simpleExponentNode.getClass().equals(ConstValueNode.class);

        if(isBaseConst&&isExponentConst)
        {
            return new ConstValueNode((new PowerNode(simpleBaseNode,simpleExponentNode)).calculate(null));//create calculable power node and create const value node with calculated value
        }else if(isBaseConst)//also !isExponentConst
        {
            double baseValue = ((ConstValueNode)simpleBaseNode).getValue();
            return new ConstBasalPowerNode(baseValue,simpleExponentNode);
        }else if(isExponentConst)//also !isBaseConst
        {
            double exponentValue = ((ConstValueNode)simpleExponentNode).getValue();
            return new ConstExponentialPowerNode(simpleBaseNode,exponentValue);
        }

        PowerNode simpleClone = null;
        try {
            simpleClone = this.getClass().newInstance();
            simpleClone.connectLowNode(this.BASE_POSITION,simpleBaseNode);
            simpleClone.connectLowNode(this.EXPONENT_POSITION,simpleExponentNode);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return simpleClone;
        }
    }
}
