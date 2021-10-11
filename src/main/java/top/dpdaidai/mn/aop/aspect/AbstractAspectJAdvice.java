package top.dpdaidai.mn.aop.aspect;

import top.dpdaidai.mn.aop.Advice;
import top.dpdaidai.mn.aop.Pointcut;
import top.dpdaidai.mn.aop.config.AspectInstanceFactory;

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
     * 切面方法 , 例如 示例中 TransactionManager 的"start" , "commit" , "rollback"方法
     */
    protected Method adviceMethod;

    /**
     * 执行切面方法的对象实例 ,
     * 现在由AspectInstanceFactory替换 , 该类为切面实例生成工厂 , AbstractAspectJAdvice 不再直接持有切面对象实例
     * 其实也只是从beanFactory中根据beanId获取bean
     */
//    protected Object adviceObject;
    protected AspectInstanceFactory aspectInstanceFactory;


    /**
     * 切点 :
     *    1 含有切点表达式
     *    2 查看切点表达式和目标Method是否匹配
     *    3 如果匹配 , 那么在子类 advice 的invoke()方法中 , 会根据情况调用切面方法
     */
    protected Pointcut pointcut;

    public AbstractAspectJAdvice(Method adviceMethod, AspectInstanceFactory aspectInstanceFactory, Pointcut pointcut) {
        this.adviceMethod = adviceMethod;
        this.aspectInstanceFactory = aspectInstanceFactory;
        this.pointcut = pointcut;
    }


    /**
     * 切面方法 , 当前只实现了没有参数的切面方法
     *
     * @throws Throwable
     */
    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(aspectInstanceFactory.getAspectInstance());
    }

    public Pointcut getPointcut() {
        return pointcut;
    }
}
