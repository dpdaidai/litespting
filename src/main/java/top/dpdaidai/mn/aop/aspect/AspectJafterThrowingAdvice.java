package top.dpdaidai.mn.aop.aspect;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author chenpantao
 * @Date 9/27/21 10:25 PM
 * @Version 1.0
 */
public class AspectJafterThrowingAdvice extends AbstractAspectJAdvice {

    public AspectJafterThrowingAdvice(Method adviceMethod, Object adviceObject) {
        super(adviceMethod, adviceObject);
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
