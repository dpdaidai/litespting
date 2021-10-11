package top.dpdaidai.mn.test.proxy.cglibProxy;

import org.springframework.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 *
 * 4、回调过滤器CallbackFilter
 *
 * 在CGLib回调时可以设置对不同方法执行不同的回调逻辑，或者根本不执行回调。
 *
 * 在JDK动态代理中并没有类似的功能，对InvocationHandler接口方法的调用对代理类内的所以方法都有效。
 *
 * =================
 * 在创建动态代理类时 , 会将代理类每个方法放入accept()中进行比对 , 来确定每个类的回调方法
 * 动态代理类创建好后 , 每个方法调用哪个callback已经写在字节码里面了
 * =================
 *
 */
public class TargetMethodCallbackFilter implements CallbackFilter {

    /**
     * 过滤方法
     * 返回的值为数字，代表了Callback数组中的索引位置，要到用的Callback
     * 例如 , sayHello使用的就是第0个callback
     *
     */
    public int accept(Method method) {
        String methodName = method.getName();
        if (methodName.equals("sayHello")) {
            System.out.println("filter sayHello == 0");
            return 0;
        }else if (methodName.equals("sayBye")){
            System.out.println("filter sayBye == 1");
            return 1;
        } else if (methodName.equals("sayNo")) {
            System.out.println("filter sayNo == 2");
            return 2;
        }
        return 0;
    }
}
