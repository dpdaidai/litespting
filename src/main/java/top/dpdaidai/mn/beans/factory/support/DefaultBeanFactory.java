package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.PropertyValue;
import top.dpdaidai.mn.beans.factory.config.ConfigurableBeanFactory;
import top.dpdaidai.mn.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:40 PM
 * @Version 1.0
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    private ClassLoader classLoader;

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

        //判断是否是bean的定义是否是单例
        //如果是单例 , 则直接取 , 如果该bean还为实例化 , 则新生成一个并注册
        Object bean = null;
        if (beanDefinition.isSingleton()) {
            bean = this.getSingletonBean(beanID);
            if (bean == null) {
                bean = createBean(beanDefinition);
                this.registrySingletonBean(beanID, bean);
            }
        }
        return bean;
    }

    /**
     * 根据bean定义创建bean实例
     * @param beanDefinition
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) {

        //初始化bean
        Object bean = instantiateBean(beanDefinition);
        //填充属性
        populateBean(beanDefinition, bean);
        return bean;
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        ClassLoader defaultClassLoader = this.getBeanClassloader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> aClass = defaultClassLoader.loadClass(beanClassName);
            return aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
        }
    }

    protected void populateBean(BeanDefinition beanDefinition, Object bean) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();

        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver beanDefinitionValueResolver = new BeanDefinitionValueResolver(this);


        for (PropertyValue propertyValue : propertyValues) {

            String propertyValueName = propertyValue.getName();
            Object originalValue = propertyValue.getValue();

            Object resolveValue = beanDefinitionValueResolver.resolveValueIfNecessary(originalValue);

            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getName().equals(propertyValueName)) {
                        propertyDescriptor.getWriteMethod().invoke(bean, resolveValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]", e);
            }

        }

    }


    public void setBeanClassloader(ClassLoader classloader) {
        this.classLoader = classloader;
    }

    public ClassLoader getBeanClassloader() {
        if (this.classLoader == null) {
            this.classLoader = ClassUtils.getDefaultClassLoader();
        }
        return this.classLoader;
    }
}
