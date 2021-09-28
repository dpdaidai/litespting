package top.dpdaidai.mn.test.v5;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import top.dpdaidai.mn.test.proxy.javaDynamicProxy.DynamicProxyHandlerTest;
import top.dpdaidai.mn.test.proxy.javaDynamicProxy.JavaDynamicProxyTest;

@RunWith(Suite.class)
@SuiteClasses({
        PointcutTest.class,
        MethodLocatingFactoryTest.class,
        ReflectiveMethodInvocationTest.class,
        JavaDynamicProxyTest.class,
        DynamicProxyHandlerTest.class
})
public class V5AllTests {

}
