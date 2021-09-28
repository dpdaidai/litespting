package top.dpdaidai.mn.test.proxy.javaDynamicProxy;

import top.dpdaidai.mn.service.v5.People;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * 调用处理器   实现动态代理必不可少的组件
 *
 * @Author chenpantao
 * @Date 9/28/21 10:53 PM
 * @Version 1.0
 */
public class DynamicProxyHandler implements InvocationHandler {

    private People target;

    public DynamicProxyHandler(People people) {
        this.target = people;
    }

    /**
     *
     * @param proxy 1.可以使用反射获取代理对象的信息（也就是proxy.getClass().getName()）。
                    2.可以将代理对象返回以进行连续调用，这就是proxy存在的目的。因为this并不是代理对象，
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("sayHello")){
            System.out.println("开启事务");
        }

        Object invoke = method.invoke(target, args);
        System.out.println("记录访问日志");

        if (method.getName().equals("sayBye")){
            System.out.println("提交事务");
        }

        return invoke;
    }

}
