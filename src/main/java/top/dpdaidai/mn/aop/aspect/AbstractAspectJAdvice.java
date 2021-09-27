package top.dpdaidai.mn.aop.aspect;

import top.dpdaidai.mn.aop.Advice;

import java.lang.reflect.Method;

/**
 *
 * 抽象切面advice , 子类继承它后
 * 重写Object invoke(MethodInvocation methodInvocation) 方法
 * 可是实现在 切点 之前或者之后执行
 *
 * @Author chenpantao
 * @Date 9/27/21 9:39 PM
 * @Version 1.0
 */
public abstract class AbstractAspectJAdvice implements Advice {

    protected Method adviceMethod;

    protected Object adviceObject;

    public AbstractAspectJAdvice(Method adviceMethod, Object adviceObject) {
        this.adviceMethod = adviceMethod;
        this.adviceObject = adviceObject;
    }

    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(adviceObject);
    }

}
