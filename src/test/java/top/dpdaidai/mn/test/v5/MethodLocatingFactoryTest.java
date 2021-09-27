package top.dpdaidai.mn.test.v5;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.aop.config.MethodLocatingFactory;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.service.tx.TransactionManager;

import java.lang.reflect.Method;

/**
 * @Author chenpantao
 * @Date 9/27/21 5:15 PM
 * @Version 1.0
 */
public class MethodLocatingFactoryTest {

    @Test
    public void testGetMethod() throws Exception {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v5.xml");
        reader.loadBeanDefinitions(resource);

        MethodLocatingFactory methodLocatingFactory = new MethodLocatingFactory();
        methodLocatingFactory.setTargetBeanName("transactionManager");
        methodLocatingFactory.setMethodName("start");
        methodLocatingFactory.setBeanFactory(defaultBeanFactory);

        Method method = methodLocatingFactory.getMethod();

        Assert.assertTrue(TransactionManager.class.equals(method.getDeclaringClass()));

        Assert.assertTrue(method.equals(TransactionManager.class.getMethod("start")));
    }

}
