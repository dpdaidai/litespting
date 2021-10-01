package top.dpdaidai.mn.aop.aspect;

import org.aopalliance.intercept.MethodInvocation;
import top.dpdaidai.mn.aop.Pointcut;

import java.lang.reflect.Method;

/**
 *
 * 根据 切入方法和ReflectiveMethodInvocation.proceed() 的先后顺序 , 来决定切面方法谁先执行
 *
 * @Author chenpantao
 * @Date 9/27/21 10:06 PM
 * @Version 1.0
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(Method adviceMethod, Object adviceObject, Pointcut pointcut) {
        super(adviceMethod, adviceObject, pointcut);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //先调用切面方法
        this.invokeAdviceMethod();
        Object proceed = methodInvocation.proceed();
        return proceed;
    }

}
