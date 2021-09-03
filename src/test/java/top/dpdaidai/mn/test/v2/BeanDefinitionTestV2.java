package top.dpdaidai.mn.test.v2;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.PropertyValue;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;

import java.util.List;

/**
 * @Author chenpantao
 * @Date 9/3/21 2:44 PM
 * @Version 1.0
 */
public class BeanDefinitionTestV2 {

    @Test
    public void testGenericBeanDefinition() {

        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        xmlBeanDefinitionReader.loadBeanDefinition(new ClassPathResource("petstore-v2.xml"));

        BeanDefinition petstore = defaultBeanFactory.getBeanDefinition("petStore");

        List<PropertyValue> propertyValues = petstore.getPropertyValues();
        Assert.assertTrue(propertyValues.size() == 3);

        {
            PropertyValue propertyValue = this.getPropertyValue("accountDao", propertyValues);

            Assert.assertNotNull(propertyValue);

            Assert.assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }

        {
            PropertyValue propertyValue = this.getPropertyValue("itemDao", propertyValues);

            Assert.assertNotNull(propertyValue);

            Assert.assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }

        {
            PropertyValue propertyValue = this.getPropertyValue("maxNumber", propertyValues);

            Assert.assertNotNull(propertyValue);

            Assert.assertTrue(propertyValue.getValue() instanceof TypedStringValue);
        }


    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> propertyValues) {
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.getName().equals(name)) {
                return propertyValue;
            }
        }
        return null;
    }
}
