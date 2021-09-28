package top.dpdaidai.mn.test.proxy;

import top.dpdaidai.mn.service.v5.People;

/**
 * 静态代理
 *
 * @Author chenpantao
 * @Date 9/28/21 10:16 PM
 * @Version 1.0
 */
public class StaticProxy implements People {

    //真实对象，客户端不能直接访问
    private People people;

    public StaticProxy(People people) {
        this.people = people;
    }

    public void sayHello(String msg) {
        people.sayHello(msg);
        System.out.println("记录访问日志");
    }

    public void sayBye(String msg) {
        System.out.println("开启事务");
        try {
            people.sayBye(msg);
            System.out.println("提交事务");
        } catch (Exception e) {
            System.out.println("回滚事务");
        }
    }
}
