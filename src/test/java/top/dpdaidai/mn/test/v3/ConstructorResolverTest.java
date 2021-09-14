package top.dpdaidai.mn.test.v3;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.support.ConstructorResolver;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.service.v3.PetStoreService;

/**
 *
 * 本测试类用于验证能否通过构造器的实例化bean
 *
 * @Author chenpantao
 * @Date 9/14/21 3:04 PM
 * @Version 1.0
 */
public class ConstructorResolverTest {

    @Test
    public void testAutowireConstructor() {

        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource config = new ClassPathResource("petstore-v3.xml");
        xmlBeanDefinitionReader.loadBeanDefinitions(config);

        BeanDefinition petStoreBeanDefinition = defaultBeanFactory.getBeanDefinition("petStore");

        ConstructorResolver constructorResolver = new ConstructorResolver(defaultBeanFactory);
        PetStoreService petStoreService = (PetStoreService)constructorResolver.autowireConstructor(petStoreBeanDefinition);

        // 验证参数version 正确地通过此构造函数做了初始化
        // PetStoreService(AccountDao accountDao, ItemDao itemDao,int version)
        Assert.assertEquals(1, petStoreService.getVersion());

        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());


    }

}
