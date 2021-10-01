package top.dpdaidai.mn.aop.aspect;

import org.aopalliance.intercept.MethodInvocation;
import top.dpdaidai.mn.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * @Author chenpantao
 * @Date 9/27/21 10:25 PM
 * @Version 1.0
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice {

    public AspectJAfterThrowingAdvice(Method adviceMethod, Object adviceObject, Pointcut pointcut) {
        super(adviceMethod, adviceObject, pointcut);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Throwable throwable) {

            //切点方法抛出异常后 ,先捕获 ,  然后执行切面方法的异常处理程序 , 再接着抛异常
            invokeAdviceMethod();
            throw throwable;
        }
    }
}
