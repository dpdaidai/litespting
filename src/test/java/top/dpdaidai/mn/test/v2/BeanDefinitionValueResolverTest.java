package top.dpdaidai.mn.test.v2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;
import top.dpdaidai.mn.beans.factory.support.BeanDefinitionValueResolver;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.service.v2.PetStoreService;

/**
 *
 * 测试 BeanDefinitionValueResolver 能否根据参数的类型来实例化对象
 *
 * @Author chenpantao
 * @Date 9/3/21 4:39 PM
 * @Version 1.0
 */
public class BeanDefinitionValueResolverTest {

    BeanDefinitionValueResolver beanDefinitionValueResolver;

    @Before
    public void setUp() {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        xmlBeanDefinitionReader.loadBeanDefinition(new ClassPathResource("petstore-v2.xml"));

        beanDefinitionValueResolver = new BeanDefinitionValueResolver(defaultBeanFactory);
    }

    @Test
    public void testResolveRuntimeBeanReference() {

        RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference("petStore");
        Object o = beanDefinitionValueResolver.resolveValueIfNecessary(runtimeBeanReference);

        Assert.assertNotNull(o);

        Assert.assertTrue(o instanceof PetStoreService);

    }

    @Test
    public void testResolveTypedStringValue() {

        TypedStringValue typedStringValue = new TypedStringValue("5");
        String o = (String)beanDefinitionValueResolver.resolveValueIfNecessary(typedStringValue);

        Assert.assertNotNull(o);

    }
}
