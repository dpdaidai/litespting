package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.BeanFactory;
import top.dpdaidai.mn.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:40 PM
 * @Version 1.0
 */
public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    public DefaultBeanFactory() {

    }

    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    public void registerBeanDefinition(String beanID, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanID, beanDefinition);
    }

    public Object getBean(String beanID) {

        BeanDefinition beanDefinition = this.getBeanDefinition(beanID);
        if (beanDefinition == null) {
            return null;
        }
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> aClass = defaultClassLoader.loadClass(beanClassName);
            return aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
        }

    }


}
