package top.dpdaidai.mn.context.suport;

import top.dpdaidai.mn.aop.aspect.AspectJAutoProxyCreator;
import top.dpdaidai.mn.beans.exception.NoSuchBeanDefinitionException;
import top.dpdaidai.mn.beans.factory.annotation.AutowiredAnnotationProcessor;
import top.dpdaidai.mn.beans.factory.config.ConfigurableBeanFactory;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.core.io.Resource;

import java.util.List;

/**
 * @Author chenpantao
 * @Date 9/2/21 3:14 PM
 * @Version 1.0
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory defaultBeanFactory;

    public AbstractApplicationContext(String configFile) {
        defaultBeanFactory = new DefaultBeanFactory();
        //初始化bean定义解析器 xmlBeanDefinitionReader , 它通过BeanDefinitionRegistry将bean定义注册到beanFactory中
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);

        //加载xml资源文件
        Resource resource = this.getResourceByPath(configFile);

        //将xml文件里的内容 , 解析为一个个BeanDefinition , 它是bean的定义类
        //getBean时会根据 BeanDefinition 实例化bean
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        registerBeanPostProcessors(defaultBeanFactory);
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

    /**
     * 注册post processor
     * 可注册多个
     *
     * @param beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {
        AutowiredAnnotationProcessor autowiredAnnotationProcessor = new AutowiredAnnotationProcessor();
        autowiredAnnotationProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(autowiredAnnotationProcessor);

        AspectJAutoProxyCreator aspectJAutoProxyCreator = new AspectJAutoProxyCreator();
        aspectJAutoProxyCreator.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(aspectJAutoProxyCreator);
    }

    public Class<?> getType(String beanID) throws NoSuchBeanDefinitionException {
        return this.defaultBeanFactory.getType(beanID);
    }

    public List<Object> getBeansByType(Class<?> classType) {
        return this.defaultBeanFactory.getBeansByType(classType);
    }
}
