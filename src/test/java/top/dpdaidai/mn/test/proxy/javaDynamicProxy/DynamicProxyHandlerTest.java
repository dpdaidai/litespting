package top.dpdaidai.mn.test.proxy.javaDynamicProxy;

import org.junit.Test;
import sun.misc.ProxyGenerator;
import top.dpdaidai.mn.service.v5.People;
import top.dpdaidai.mn.service.v5.Student;
import top.dpdaidai.mn.util.ObjectAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * https://blog.csdn.net/tanggao1314/article/details/78131834
 *
 * https://linling1.github.io/programming_blog/2019/11/03/%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90-Java-%E5%AD%97%E8%8A%82%E7%A0%81-%E4%B9%8B-%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86%E7%9A%84%E5%AE%9E%E7%8E%B0/
 *
 * java 动态代理 .
 * 动态代理是在java运行时生成了代理类的class
 *   1  Proxy接受 目标接口和InvocationHandler 作为参数
 *   2  使用ProxyGenerator.generateClassFile() 方法 , 将需要的方法拼成了一个class文件 . 这个方法包含大量操作字节码的操作 . 普通用户使用不了
 *   3  包含了 接口的所有方法
 *   4  包含了 Object的toString() , equals() , hashCode()
 *   5  包含了 代理类的构造器方法 , 该方法需要一个参数InvocationHandler
 *
 * 当使用代理类的调用接口方法时 , 实际上是通过代理类调用了 InvocationHandler的invoke()方法
 * 所以在invoke中 , 调用this , 并不会返回代理类 , 而是返回了InvocationHandler这个类的对象
 *
 * @Author chenpantao
 * @Date 9/28/21 10:58 PM
 * @Version 1.0
 */
public class DynamicProxyHandlerTest {

    public static void main(String[] args) {

        // 是否将动态代理生成的class文件保存起来 , 该配置在main时才有效
        // 代理类在 com/sun/proxy/$Proxy0.class
        //「super.h.invoke(…)」本质上都会调用到我们自己实现的InvocationHandler的实现类 —— 「DynamicProxyHandler#invoke」方法，「DynamicProxyHandler#invoke」中会去调用真实对象的相应的方法。
        // 也就是说，本例子中「equals」、「toString」、「hashCode」、「request」方法最终都会通过动态代理调用到真实的 Student 的对应的方法。
//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        People people = new Student();

        DynamicProxyHandler proxyHandler = new DynamicProxyHandler(people);

        ClassLoader classLoader = people.getClass().getClassLoader();
        Class<?>[] interfaces = people.getClass().getInterfaces();

        //源码 Class<?> cl =getProxyClass(loader, interfaces); 生成代理类
        //① 查找或构建指定的代理类的Class（该过程就会动态生成指定代理类的字节码，然后通过加载动态生成的字节码实现这个动态代理类的加载）。
        //② 获取指定的代理类Class的特定Constructor，该构造方法要求具有唯一一个类型为InvocationHandler的参数。
        //③ 通过反射创建指定的代理类，这里会传入我们自己实现的 DynamicSubject 类（InvocationHandler 接口的实现类）作为构造方法的参数。
        Object instance = Proxy.newProxyInstance(classLoader, interfaces, proxyHandler);
        People proxyPeople = (People) instance;

        proxyPeople.sayHello("cpt");

        proxyPeople.sayBye("cpt");

        System.out.println(proxyPeople.toString());

        //动态代理是在运行时动态生成的，即编译完成后没有实际的class文件，而是在运行时动态生成类字节码，并加载到JVM中
        System.out.println(proxyPeople.getClass());  //class com.sun.proxy.$Proxy4
        System.out.println(people.getClass());    //class top.dpdaidai.mn.service.v5.Student

        System.out.println("========= 分析 代理类生成的对象 proxyPeople 的全部属性 =====");
        System.out.println(new ObjectAnalyzer().toString(proxyPeople));
        //com.sun.proxy.$Proxy0[],  java.lang.reflect.Proxy[h=top.dpdaidai.mn.test.proxy.DynamicProxyHandler[target=top.dpdaidai.mn.service.v5.Student[],  java.lang.Object[],  ],  java.lang.Object[],  ],  java.lang.Object[],

    }

    @Test
    /**
     * 本测试可以生成 将生成的代理类字节码输出 , 反编译查看效果
     */
    public void generatorProxyClass() {
        String name = "PeopleProxy";
        byte[] data = ProxyGenerator.generateProxyClass(name, new Class[]{People.class});
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(name + ".class");
            System.out.println((new File("hello")).getAbsolutePath());
            out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

}
