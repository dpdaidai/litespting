package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.context.annotation.ScannedGenericBeanDefinition;
import top.dpdaidai.mn.core.annotation.AnnotationAttributes;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.core.type.AnnotationMetadata;
import top.dpdaidai.mn.stereotype.Component;

/**
 *
 *
 *
 * @Author chenpantao
 * @Date 9/17/21 5:09 PM
 * @Version 1.0
 */
public class XmlBeanDefinitionReaderTest {

    @Test
    public void testBeanDefinitionScanner() {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);

        String componentAnnotation = Component.class.getName();

        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("petStore");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
            AnnotationMetadata amd = sbd.getMetadata();


            Assert.assertTrue(amd.hasAnnotation(componentAnnotation));
            AnnotationAttributes attributes = amd.getAnnotationAttributes(componentAnnotation);
            Assert.assertEquals("petStore", attributes.get("value"));
        }
        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("accountDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(componentAnnotation));
        }
        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("itemDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(componentAnnotation));
        }

        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("A");
            Assert.assertTrue(bd instanceof GenericBeanDefinition);
            Assert.assertFalse(bd instanceof ScannedGenericBeanDefinition);

            Assert.assertEquals("top.dpdaidai.mn.service.v4.config.A", bd.getBeanClassName());

        }


    }


}
