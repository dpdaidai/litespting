package top.dpdaidai.mn.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.PetStoreService;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        xmlBeanDefinitionReader.loadBeanDefinition("petstore-v1.xml");

        BeanDefinition bd = beanFactory.getBeanDefinition("petStore");

        assertEquals("top.dpdaidai.mn.beans.factory.PetStoreService", bd.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) beanFactory.getBean("petStore");

        assertNotNull(petStoreService);

    }

    /**
     * 测试能否捕捉实例化DefaultBeanFactory时的异常
     */
    @Test
    public void testInvalidXML() {

        try {
            xmlBeanDefinitionReader.loadBeanDefinition("nonexistent.xml");

        } catch (BeanDefinitionStoreException exception) {
            return;
        }

        Assert.fail("expect BeanDefinitionStoreException ");

    }


    @Test
    public void testInvalidBean() {
        xmlBeanDefinitionReader.loadBeanDefinition("petstore-v1.xml");

        try {
            beanFactory.getBean("nonexistentBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expect BeanCreationException ");

    }

}
