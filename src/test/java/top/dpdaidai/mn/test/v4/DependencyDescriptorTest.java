package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.config.DependencyDescriptor;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.service.daoV4.AccountDao;
import top.dpdaidai.mn.service.v4.PetStoreService;

import java.lang.reflect.Field;

/**
 * @Author chenpantao
 * @Date 9/23/21 11:29 AM
 * @Version 1.0
 */
public class DependencyDescriptorTest {

    @Test
    public void testResolveDependency() throws  Exception{
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);


        Field accountDao = PetStoreService.class.getDeclaredField("accountDao");

        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(accountDao, true);

        Object o = factory.resolveDependency(dependencyDescriptor);
        Assert.assertTrue(o instanceof AccountDao);


    }

}
