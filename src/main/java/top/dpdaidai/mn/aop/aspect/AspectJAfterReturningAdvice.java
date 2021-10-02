package top.dpdaidai.mn.aop.aspect;

import org.aopalliance.intercept.MethodInvocation;
import top.dpdaidai.mn.aop.Pointcut;
import top.dpdaidai.mn.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @Author chenpantao
 * @Date 9/27/21 10:12 PM
 * @Version 1.0
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectInstanceFactory aspectInstanceFactory, Pointcut pointcut) {
        super(adviceMethod, aspectInstanceFactory, pointcut);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object proceed = methodInvocation.proceed();

        //后调用切面方法
        this.invokeAdviceMethod();

        return proceed;
    }
}
