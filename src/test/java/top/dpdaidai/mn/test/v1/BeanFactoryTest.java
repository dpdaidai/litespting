package top.dpdaidai.mn.test.v1;

import org.junit.Test;
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

    @Test
    public void testGetBean(){
        BeanFactory beanFactory = new DefaultBeanFactory("petstore-v1.xml");

        BeanDefinition bd = beanFactory.getBeanDefinition("petStore");

        assertEquals("top.dpdaidai.mn.beans.factory.PetStoreService", bd.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) beanFactory.getBean("petStore");

        assertNotNull(petStoreService);


    }
}
