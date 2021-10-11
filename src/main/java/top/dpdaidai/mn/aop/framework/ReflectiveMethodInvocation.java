package top.dpdaidai.mn.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * 切点方法唤醒类 , 将切入的方法保存在拦截器 interceptor 中 .
 * 唤醒切点方法时 , 先执行所有的拦截器的 invoke 方法
 * ReflectiveMethodInvocation 在 proceed() 方法中调用拦截器的 invoke 方法 ,
 * 而 拦截器也在 invoke 方法中调用了 ReflectiveMethodInvocation 的proceed() 方法 .
 * 直到拦截器全都被调用.
 *
 * 该类依赖 :
 *    1  切点对象 targetObject
 *    2  切点方法 targetMethod
 *    3  切点方法参数 arguments
 *    4  关联该切点的拦截器 interceptors
 *
 *
 *
 * @Author chenpantao
 * @Date 9/27/21 9:47 PM
 * @Version 1.0
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    protected final Object targetObject; // petStoreService
    protected final Method targetMethod; // placeOrder()方法
    protected Object[] arguments; // placeOrder()方法的参数

    //拦截器的集合
    protected final List<MethodInterceptor> interceptorList;

    //拦截器被调用的次数的计数
    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object targetObject, Method targetMethod,
                                      Object[] arguments, List<MethodInterceptor> interceptorList) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.arguments = arguments;
        this.interceptorList = interceptorList;
    }

    public Method getMethod() {
        return targetMethod;
    }

    public Object[] getArguments() {
        return this.arguments != null ? this.arguments : new Object[0];
    }

    /**
     * 拦截器调用链的起点 , 它的行为 :
     * 1  一定会执行 targetMethod.invoke(targetObject, arguments)
     * 2  在执行目标方法 targetMethod 时 , 会按加入顺序执行 链中的所有拦截器 interceptorList 的切面方法
     * 3  根据以下代码可知 执行顺序 :
     *          1    AspectJBeforeAdvice >  targetMethod() >  AspectJAfterReturningAdvice / AspectJAfterThrowingAdvice
     *          2    AspectJBeforeAdvice : 先加入interceptorList的拦截器先执行
     *          3    AspectJAfterReturningAdvice / AspectJAfterThrowingAdvice : 先加入interceptorList的拦截器后执行
     *
     * @return
     * @throws Throwable
     */
    public Object proceed() throws Throwable {
        //所有拦截器都被调用过了 , 那么调用 切点的方法
        if (this.currentInterceptorIndex + 1 == this.interceptorList.size()) {
            return invokeJoinpoint();
        }
        this.currentInterceptorIndex++;

        MethodInterceptor methodInterceptor = this.interceptorList.get(currentInterceptorIndex);
        return methodInterceptor.invoke(this);
    }

    /**
     * 调用切点的方法
     * @return
     * @throws Throwable
     */
    protected Object invokeJoinpoint() throws Throwable {
        return this.targetMethod.invoke(this.targetObject, this.arguments);
    }

    public Object getThis() {
        return this.targetObject;
    }

    public AccessibleObject getStaticPart() {
        return null;
    }
}
