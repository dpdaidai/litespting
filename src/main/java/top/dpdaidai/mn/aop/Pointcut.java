package top.dpdaidai.mn.aop;

public interface Pointcut {

    MethodMatcher getMethodMatcher();

    String getExpression();

}
