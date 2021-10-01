package top.dpdaidai.mn.aop.aspect;

import top.dpdaidai.mn.aop.Advice;
import top.dpdaidai.mn.aop.Pointcut;

import java.lang.reflect.Method;

/**
 *
 * 抽象切面advice
 * 它的三个成员变量的关系 : 在满足 切面条件 pointcut 时 , 调用 切面对象adviceObject 的 切面方法adviceMethod
 *
 * 什么时候调用 , 取决于子类在 invoke() 中的实现
 *
 * @Author chenpantao
 * @Date 9/27/21 9:39 PM
 * @Version 1.0
 */
public abstract class AbstractAspectJAdvice implements Advice {

    /**
     * 切面方法
     */
    protected Method adviceMethod;

    /**
     * 执行切面方法的对象
     */
    protected Object adviceObject;

    /**
     * 切点 :
     *    1 含有切点表达式
     *    2 查看切点表达式和目标Method是否匹配
     *    3 如果匹配 , 那么在子类 advice 的invoke()方法中 , 会根据情况调用切面方法
     */
    protected Pointcut pointcut;

    public AbstractAspectJAdvice(Method adviceMethod, Object adviceObject, Pointcut pointcut) {
        this.adviceMethod = adviceMethod;
        this.adviceObject = adviceObject;
        this.pointcut = pointcut;
    }


    /**
     * 切面方法 , 这里实现的是没有参数的切面方法
     *
     * @throws Throwable
     */
    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(adviceObject);
    }

    public Pointcut getPointcut() {
        return pointcut;
    }
}
