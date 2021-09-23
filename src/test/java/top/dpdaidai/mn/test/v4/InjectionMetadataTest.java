package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.annotation.AutowiredFieldElement;
import top.dpdaidai.mn.beans.factory.annotation.InjectionElement;
import top.dpdaidai.mn.beans.factory.annotation.InjectionMetadata;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.service.daoV4.AccountDao;
import top.dpdaidai.mn.service.daoV4.ItemDao;
import top.dpdaidai.mn.service.v4.PetStoreService;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * 测试 InjectionMetadata . 它将注入的细节包装 . 我们仅需准备好需要注入的字段 , 对象 . 通过它即可完成属性的注入 .
 * 它本来还应该支持方法注入 , 构造器注入 . 不过没有实现它
 *
 * @Author chenpantao
 * @Date 9/23/21 2:49 PM
 * @Version 1.0
 */
public class InjectionMetadataTest {

    @Test
    public void testInjection() throws NoSuchFieldException {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Class<PetStoreService> petStoreServiceClass = PetStoreService.class;
        LinkedList<InjectionElement> injectionElementLinkedList = new LinkedList<InjectionElement>();

        {
            Field accountDao = PetStoreService.class.getDeclaredField("accountDao");
            InjectionElement fieldElement = new AutowiredFieldElement(accountDao, true, defaultBeanFactory);
            injectionElementLinkedList.add(fieldElement);
        }
        {
            Field itemDao = PetStoreService.class.getDeclaredField("itemDao");
            InjectionElement fieldElement = new AutowiredFieldElement(itemDao, true, defaultBeanFactory);
            injectionElementLinkedList.add(fieldElement);
        }

        InjectionMetadata metadata = new InjectionMetadata(petStoreServiceClass, injectionElementLinkedList);
        PetStoreService petStoreService = new PetStoreService();

        metadata.inject(petStoreService);

        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);

    }


}
