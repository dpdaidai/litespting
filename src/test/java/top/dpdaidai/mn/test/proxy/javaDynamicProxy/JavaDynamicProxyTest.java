package top.dpdaidai.mn.test.proxy.javaDynamicProxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1264804593397984
 *
 * Java标准库提供了一种动态代理（Dynamic Proxy）的机制：可以在运行期动态创建某个interface的实例。
 *
 * 先定义了接口Hello，但是我们并不去编写实现类，而是直接通过JDK提供的一个Proxy.newProxyInstance()创建了一个Hello接口对象。
 * 这种没有实现类但是在运行期动态创建了一个接口对象的方式，我们称为动态代理。
 *
 * JDK提供的动态创建接口对象的方式，就叫动态代理。
 *
 * 在运行期动态创建一个interface实例的方法如下：
 * 1  定义一个InvocationHandler实例，它负责实现接口的方法调用；
 * 2  通过Proxy.newProxyInstance()创建interface实例，它需要3个参数：
 *      1  使用的ClassLoader，通常就是接口类的ClassLoader；
 *      2  需要实现的接口数组，至少需要传入一个接口进去；
 *      3  用来处理接口方法调用的InvocationHandler实例。
 * 3  将返回的Object强制转型为接口。
 *
 * 动态代理实际上是JVM在运行期动态创建class字节码并加载的过程，它并没有什么黑魔法
 *
 * @Author chenpantao
 * @Date 9/28/21 5:53 PM
 * @Version 1.0
 */
public class JavaDynamicProxyTest {

    @Test
    public void testHello() throws Throwable {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("======handler invoke() ========");
                System.out.println(method);
                if (method.getName().equals("morning")) {
                    System.out.println("Good morning , " + args[0]);
                } else if (method.getName().equals("toString")){
                    System.out.println(this.getClass());
                    //class top.dpdaidai.mn.test.proxy.javaDynamicProxy.JavaDynamicProxyTest$1
                    //这是匿名类
                    return method.invoke(this, args);
                }
                return null;
            }
        };

        //根据Hello接口 和 InvocationHandler , 在运行时动态生成一个Hello的子类字节码
        Hello hello = (Hello) Proxy.newProxyInstance(
                Hello.class.getClassLoader(), new Class[]{Hello.class}, handler);
        hello.morning("cpt");

        System.out.println(hello.toString());  //top.dpdaidai.mn.test.proxy.javaDynamicProxy.JavaDynamicProxyTest$1@4dcbadb4

        System.out.println(hello.getClass());
        //class top.dpdaidai.mn.test.proxy.javaDynamicProxy.$Proxy4
        //这是动态代理生成的类
    }

}

interface Hello {
    void morning(String name) throws Throwable;
}

/**
 *
 * 动态代理生成的Hello的实现的class , 反编译后 , 实现类大概长这样
 */
class HelloDynamicProxy implements Hello {
    InvocationHandler handler;

    public HelloDynamicProxy(InvocationHandler handler) {
        this.handler = handler;
    }

    public void morning(String name) throws Throwable {
        handler.invoke(
                this,
                Hello.class.getMethod("morning", String.class),
                new Object[]{name});
    }
}
