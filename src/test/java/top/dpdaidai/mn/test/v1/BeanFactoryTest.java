package top.dpdaidai.mn.test.v1;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.BeanFactory;
import top.dpdaidai.mn.beans.factory.PetStoreService;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:36 PM
 * @Version 1.0
 */
public class BeanFactoryTest {

    /**
     * 创建BeanFactory , 并实例化petSore
     */
    @Test
    public void testGetBean() {
        BeanFactory beanFactory = new DefaultBeanFactory("petstore-v1.xml");

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
            BeanFactory beanFactory = new DefaultBeanFactory("nonexistent.xml");
        } catch (BeanDefinitionStoreException exception) {
            return;
        }

        Assert.fail("expect BeanDefinitionStoreException ");

    }


    @Test
    public void testInvalidBean() {
        BeanFactory beanFactory = new DefaultBeanFactory("petstore-v1.xml");

        try {
            beanFactory.getBean("nonexistentBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expect BeanCreationException ");

    }

}
