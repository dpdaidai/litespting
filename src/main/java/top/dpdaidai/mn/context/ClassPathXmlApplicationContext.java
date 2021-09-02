package top.dpdaidai.mn.context;

import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;

/**
 * @Author chenpantao
 * @Date 9/1/21 9:06 PM
 * @Version 1.0
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {

    private DefaultBeanFactory defaultBeanFactory;

    public ClassPathXmlApplicationContext(String configFile) {
        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        ClassPathResource classPathResource = new ClassPathResource(configFile);
        xmlBeanDefinitionReader.loadBeanDefinition(classPathResource);
    }

    public Object getBean(String beanID) {
        return defaultBeanFactory.getBean(beanID);
    }
}
