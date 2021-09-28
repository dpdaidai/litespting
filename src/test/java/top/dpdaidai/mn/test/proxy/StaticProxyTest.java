package top.dpdaidai.mn.test.proxy;

import org.junit.Test;
import top.dpdaidai.mn.service.v5.People;
import top.dpdaidai.mn.service.v5.Student;

/**
 * @Author chenpantao
 * @Date 9/28/21 10:18 PM
 * @Version 1.0
 */
public class StaticProxyTest {

    @Test
    public void test() {
        People people = new Student();
        People peopleProxy = new StaticProxy(people);
        peopleProxy.sayHello("cpt");
        peopleProxy.sayBye("bye");
    }

}
