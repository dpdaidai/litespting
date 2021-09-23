package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.annotation.AutowiredAnnotationProcessor;
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
import java.util.List;

/**
 * @Author chenpantao
 * @Date 9/23/21 5:30 PM
 * @Version 1.0
 */
public class AutowiredAnnotationProcessorTest {

    @Test
    public void testGetInjectionMetadata() {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        AutowiredAnnotationProcessor autowiredAnnotationProcessor = new AutowiredAnnotationProcessor();
        autowiredAnnotationProcessor.setBeanFactory(defaultBeanFactory);

        //获取目标类需要被自动装备的元数据集合
        InjectionMetadata injectionMetadata = autowiredAnnotationProcessor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionElement> injectionElementList = injectionMetadata.getInjectionElementList();

        Assert.assertEquals(2, injectionElementList.size());
        assertFieldExists(injectionElementList, "accountDao");
        assertFieldExists(injectionElementList, "itemDao");

        PetStoreService petStoreService = new PetStoreService();

        injectionMetadata.inject(petStoreService);
        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);
        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);


    }

    private void assertFieldExists(List<InjectionElement> elementList, String fieldName) {
        for (InjectionElement injectionElement : elementList) {
            AutowiredFieldElement autowiredFieldElement = (AutowiredFieldElement) injectionElement;
            Field field = autowiredFieldElement.getField();
            if (field.getName().equals(fieldName)) {
                return;
            }
        }
        Assert.fail(fieldName + "does not exist");
    }

}
