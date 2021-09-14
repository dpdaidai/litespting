package top.dpdaidai.mn.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.service.v1.PetStoreService;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;

import static org.junit.Assert.*;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:36 PM
 * @Version 1.0
 */
public class BeanFactoryTest {

    DefaultBeanFactory beanFactory = null;
    XmlBeanDefinitionReader xmlBeanDefinitionReader = null;

    @Before
    public void setUp() {
        beanFactory = new DefaultBeanFactory();
        xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    }

    /**
     * 创建BeanFactory , 并实例化petSore
     */
    @Test
    public void testGetBean() {
        xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition bd = beanFactory.getBeanDefinition("petStore");

        //判断是否是单例bean
        assertTrue(bd.isSingleton());

        assertFalse(bd.isPrototype());

        assertEquals(BeanDefinition.SCOPE_DEFAULT, bd.getScope());

        assertEquals("top.dpdaidai.mn.service.v1.PetStoreService", bd.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) beanFactory.getBean("petStore");

        assertNotNull(petStoreService);

        PetStoreService petStoreService2 = (PetStoreService) beanFactory.getBean("petStore");

        //判断是否成功实现单例模式
        assertTrue(petStoreService.equals(petStoreService2));


    }

    /**
     * 测试能否捕捉实例化DefaultBeanFactory时的异常
     */
    @Test
    public void testInvalidXML() {

        try {
            xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("nonexistent.xml"));

        } catch (BeanDefinitionStoreException exception) {
            return;
        }

        Assert.fail("expect BeanDefinitionStoreException ");

    }


    @Test
    public void testInvalidBean() {
        xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        try {
            beanFactory.getBean("nonexistentBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expect BeanCreationException ");

    }

}
