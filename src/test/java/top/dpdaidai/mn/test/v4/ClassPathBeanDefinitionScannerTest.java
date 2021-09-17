package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.context.annotation.ClassPathBeanDefinitionScanner;
import top.dpdaidai.mn.context.annotation.ScannedGenericBeanDefinition;
import top.dpdaidai.mn.core.annotation.AnnotationAttributes;
import top.dpdaidai.mn.core.type.AnnotationMetadata;
import top.dpdaidai.mn.stereotype.Component;

/**
 *
 * 测试类ClassPathBeanDefinitionScanner
 * 该类用于根据包名 , 扫描包下含有Component注解的类 , 根据class文件 , 解析得到的 beanDefinition 相关信息并进行注册
 *
 * @Author chenpantao
 * @Date 9/17/21 3:46 PM
 * @Version 1.0
 */
public class ClassPathBeanDefinitionScannerTest {

    @Test
    public void testBeanDefinitionScanner() {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();

        String basePackage = "top.dpdaidai.mn.service.v4,top.dpdaidai.mn.service.daoV4";

        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(defaultBeanFactory);

        classPathBeanDefinitionScanner.doScan(basePackage);

        String componentAnnotation = Component.class.getName();

        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("petStore");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();


            Assert.assertTrue(amd.hasAnnotation(componentAnnotation));
            AnnotationAttributes attributes = amd.getAnnotationAttributes(componentAnnotation);
            Assert.assertEquals("petStore", attributes.get("value"));
        }
        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("accountDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(componentAnnotation));
        }
        {
            BeanDefinition bd = defaultBeanFactory.getBeanDefinition("itemDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(componentAnnotation));
        }


    }

}
