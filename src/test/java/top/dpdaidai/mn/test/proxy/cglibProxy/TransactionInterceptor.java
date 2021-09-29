package top.dpdaidai.mn.test.proxy.cglibProxy;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * 定义一个拦截器。在调用目标方法时，
 * CGLib会回调MethodInterceptor接口方法拦截，来实现你自己的代理逻辑，类似于JDK中的InvocationHandler接口。
 *
 * 代理对象所有的方法 , 都会调用intercept()
 *
 * @Author chenpantao
 * @Date 9/29/21 11:02 AM
 * @Version 1.0
 */
public class TransactionInterceptor implements MethodInterceptor {

    public TransactionInterceptor() {
    }

    /**
     *
     * @param objectProxy  由CGLib动态生成的代理类实例
     * @param method     原方法 : 实体类所调用的被代理的方法引用
     * @param params    方法参数
     * @param methodProxy  为生成的代理类对方法的代理引用
     * @return  从代理实例的方法调用返回的值
     * @throws Throwable
     */
    public Object intercept(Object objectProxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {

        System.out.println("开启事务");
        // 执行目标对象的方法
        // 可以选择执行 代理类的父类 或者
        // 或者原方法 method.invoke() , 那么需要提交将原对象传递进来
//        Object returnValue = method.invoke(target, params);
        methodProxy.invokeSuper(objectProxy, params);

        System.out.println("关闭事务");

        return null;
    }
}
