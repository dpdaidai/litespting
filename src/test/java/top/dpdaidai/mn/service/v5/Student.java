package top.dpdaidai.mn.service.v5;

/**
 * @Author chenpantao
 * @Date 9/28/21 10:15 PM
 * @Version 1.0
 */
public class Student implements People {

    public void sayHello(String msg) {
        System.out.println("Hello " + msg);
    }

    public void sayBye(String msg) {
        System.out.println("ByeBye " + msg);
    }

    public void sayNo(String msg) {
        System.out.println("say No : " + msg);
    }
}
