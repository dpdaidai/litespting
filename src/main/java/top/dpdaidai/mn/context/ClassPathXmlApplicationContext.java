package top.dpdaidai.mn.context;

import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;

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
        xmlBeanDefinitionReader.loadBeanDefinition(configFile);
    }

    public Object getBean(String beanID) {
        return defaultBeanFactory.getBean(beanID);
    }
}
