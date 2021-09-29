package top.dpdaidai.mn.test.proxy.cglibProxy;

import org.junit.Test;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import top.dpdaidai.mn.service.v5.Student;
import top.dpdaidai.mn.util.ObjectAnalyzer;

/**
 *
 * https://www.runoob.com/w3cnote/cglibcode-generation-library-intro.html
 *
 * @Author chenpantao
 * @Date 9/29/21 11:07 AM
 * @Version 1.0
 */
public class CglibTest {

    @Test
    public void test() {

        //配置保存cglib运行时动态生成的字节码文件
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "cglib");

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();

        //Enhancer类是CGLib中的一个字节码增强器
        Enhancer en = new Enhancer();

        //1 将被代理类TargetObject设置成父类
        en.setSuperclass(Student.class);
        //2 设置拦截器TargetInterceptor
        en.setCallback(transactionInterceptor);
        //3 执行enhancer.create()动态生成一个代理类 , 并从Object强制转型成父类型TargetObject
        Student studentProxy = (Student) en.create();


        studentProxy.sayBye("cpt");
//        studentProxy.sayHello("cpt");
//        studentProxy.sayNo("no");

        //调用toString也会调用MethodInterceptor
        System.out.println(studentProxy.toString());

        System.out.println(new ObjectAnalyzer().toString(studentProxy));
        //top.dpdaidai.mn.service.v5.Student$$EnhancerByCGLIB$$9567e498[CGLIB$BOUND=true,CGLIB$CALLBACK_0=top.dpdaidai.mn.test.proxy.cglibProxy.TransactionInterceptor[],  java.lang.Object[],  ],  top.dpdaidai.mn.service.v5.Student[],  java.lang.Object[],

    }

    @Test
    /**
     *
     * 4、回调过滤器CallbackFilter
     *
     * 在CGLib回调时可以设置对不同方法执行不同的回调逻辑，或者根本不执行回调。
     *
     * 在JDK动态代理中并没有类似的功能，对InvocationHandler接口方法的调用对代理类内的所以方法都有效。
     *
     */
    public void testFilter() {

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(Student.class);

        //
        CallbackFilter targetMethodCallbackFilter = new TargetMethodCallbackFilter();

        /*
        (1)callback1：方法拦截器
        (2)NoOp.INSTANCE：这个NoOp表示no operator，即什么操作也不做，代理类直接调用被代理的方法不进行拦截。
        (3)FixedValue：表示锁定方法返回值，无论被代理类的方法返回什么值，回调方法都返回固定值。
         */
        Callback noCallback = NoOp.INSTANCE;
        //自定义的回掉函数
        Callback myCallback = new TransactionInterceptor();

        Callback fixedValue = new TargetResultFixed();

        Callback[] callbackList = new Callback[]{myCallback, noCallback, fixedValue};

        /*
            TargetMethodCallbackFilter的返回值 , 就是要调用的 callbackList 的index
         */
        enhancer.setCallbacks(callbackList);
        enhancer.setCallbackFilter(targetMethodCallbackFilter);

        Student studentProxy = (Student)enhancer.create();

        //callback : 被自定义拦截
        studentProxy.sayHello("cpt");
        //callback : 不被拦截
        studentProxy.sayBye("bb");
        //callback : 返回固定值
        studentProxy.sayNo("??");

    }



}
