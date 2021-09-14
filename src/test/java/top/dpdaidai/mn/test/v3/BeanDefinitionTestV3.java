package top.dpdaidai.mn.test.v3;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.ConstructorArgument;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;

import java.util.List;

/**
 *
 * 本测试类验证 XmlBeanDefinitionReader 能否按照预期解析 constructor-arg 标签
 *
 * @Author chenpantao
 * @Date 9/14/21 2:22 PM
 * @Version 1.0
 */
public class BeanDefinitionTestV3 {

    @Test
    public void testConstructorArgument() {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition petStoreBeanDefinition = defaultBeanFactory.getBeanDefinition("petStore");
        Assert.assertEquals("top.dpdaidai.mn.service.v3.PetStoreService", petStoreBeanDefinition.getBeanClassName());

        ConstructorArgument constructorArgument = petStoreBeanDefinition.getConstructorArgument();
        Assert.assertEquals(constructorArgument.getArgumentCount(), 3);

        List<ConstructorArgument.ValueHolder> valueHolders = constructorArgument.getArgumentValues();

        RuntimeBeanReference ref1 = (RuntimeBeanReference) valueHolders.get(0).getValue();
        Assert.assertEquals("accountDao", ref1.getBeanID());
        RuntimeBeanReference ref2 = (RuntimeBeanReference) valueHolders.get(1).getValue();
        Assert.assertEquals("itemDao", ref2.getBeanID());

        TypedStringValue strValue = (TypedStringValue) valueHolders.get(2).getValue();
        Assert.assertEquals("1", strValue.getValue());


    }

}
