package top.dpdaidai.mn.test.proxy.cglibProxy;

import org.springframework.cglib.proxy.FixedValue;

/**
 *
 * 表示锁定方法返回值，无论被代理类的方法返回什么值，回调方法都返回固定值。
 *
 * @Author chenpantao
 * @Date 9/29/21 2:52 PM
 * @Version 1.0
 */
public class TargetResultFixed implements FixedValue {


    /**
     * 该类实现FixedValue接口，同时锁定回调值为 "NO"
     * (整型，CallbackFilter中定义的使用FixedValue型回调的方法为getConcreteMethodFixedValue，该方法返回值为整型)。
     */
    public Object loadObject() throws Exception {
        System.out.println("TargetResultFixed: 锁定结果");
        return "NO";
    }
}
