package top.dpdaidai.mn.context.suport;

import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.core.io.Resource;

/**
 * @Author chenpantao
 * @Date 9/2/21 3:14 PM
 * @Version 1.0
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory defaultBeanFactory;

    public AbstractApplicationContext(String configFile) {
        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = this.getResourceByPath(configFile);

        //将xml文件里的内容 , 解析为一个个BeanDefinition , 它是bean的定义类
        //getBean时会根据 BeanDefinition 实例化bean
        xmlBeanDefinitionReader.loadBeanDefinition(resource);
    }

    protected abstract Resource getResourceByPath(String path);

    public void setBeanClassloader(ClassLoader classloader) {
        defaultBeanFactory.setBeanClassloader(classloader);
    }

    public ClassLoader getBeanClassloader() {
        return defaultBeanFactory.getBeanClassloader();
    }

    public Object getBean(String beanID) {
        return defaultBeanFactory.getBean(beanID);
    }
}
