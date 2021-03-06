package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.SimpleTypeConverter;
import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.NoSuchBeanDefinitionException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.BeanFactoryAware;
import top.dpdaidai.mn.beans.factory.PropertyValue;
import top.dpdaidai.mn.beans.factory.config.BeanPostProcessor;
import top.dpdaidai.mn.beans.factory.config.DependencyDescriptor;
import top.dpdaidai.mn.beans.factory.config.InstantiationAwareBeanPostProcessor;
import top.dpdaidai.mn.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:40 PM
 * @Version 1.0
 */
public class DefaultBeanFactory extends AbstractBeanFactory
        implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    private ClassLoader classLoader;

    //BeanPostProcessor为什么会有多个
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

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
        Object bean;
        if (beanDefinition.isSingleton()) {
            bean = this.getSingletonBean(beanID);
            if (bean == null) {
                bean = createBean(beanDefinition);
                this.registrySingletonBean(beanID, bean);
            }
        } else {
            bean = createBean(beanDefinition);
        }
        return bean;
    }

    public Class<?> getType(String beanID) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanID);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(beanID);
        }
        resolveBeanClass(beanDefinition);
        return beanDefinition.getBeanClass();
    }

    public List<Object> getBeansByType(Class<?> classType) {
        List<Object> result = new ArrayList<Object>();
        List<String> beanIDs = this.getBeanIDsByType(classType);
        for (String beanID : beanIDs) {
            result.add(this.getBean(beanID));
        }
        return result;
    }

    private List<String> getBeanIDsByType(Class<?> classType) {
        List<String> result = new ArrayList<String>();
        for (String beanName : beanDefinitionMap.keySet()) {
            if (classType.isAssignableFrom(this.getType(beanName))) {
                result.add(beanName);
            }
        }
        return result;
    }

    /**
     * 根据bean定义创建bean实例
     * @param beanDefinition
     * @return
     */
    protected Object createBean(BeanDefinition beanDefinition) {

        //初始化bean
        //如果有构造器参数 , 那么使用构造器参数初始化bean
        //没有则new一个bean , 等待populateBean() 和 initializeBean()填充属性
        Object bean = instantiateBean(beanDefinition);
        //填充属性
        populateBean(beanDefinition, bean);

        bean = initializeBean(beanDefinition, bean);

        return bean;
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {

        if (beanDefinition.hasConstructorArgumentValues()) {
            ConstructorResolver resolver = new ConstructorResolver(this);
            Object o = resolver.autowireConstructor(beanDefinition);
            return o;
        }

        ClassLoader defaultClassLoader = this.getBeanClassloader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            //类加载器获取类 , 然后返回一个实例
            Class<?> aClass = defaultClassLoader.loadClass(beanClassName);
            return aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
        }
    }

    protected void populateBean(BeanDefinition beanDefinition, Object bean) {

        //在bean的实例化时做的操作 , 为注解生成的bean内的@autowired的属性赋值
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(bean, beanDefinition.getID());
            }
        }

        //根据xml解析的propertyValues填充bean
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();

        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver beanDefinitionValueResolver = new BeanDefinitionValueResolver(this);

        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        try {
            //获取bean的类相关属性
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyValue propertyValue : propertyValues) {

                //property名
                String propertyValueName = propertyValue.getName();
                //property值
                Object originalValue = propertyValue.getValue();

                //根据property的类型 , 来解析它的值.
                //如果是RuntimeBeanReference则给property注入关联的类
                //如果是TypedStringValue , 则给property填充字符串的值
                Object resolveValue = beanDefinitionValueResolver.resolveValueIfNecessary(originalValue);

                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getName().equals(propertyValueName)) {

                        //主要是转换字符串类型为别的主数据类型或者包装类型 , 根据property获取property的数据类型 , 然后将字符串转换为对应的数据类型
                        Object convertValue = simpleTypeConverter.convertIfNecessary(resolveValue, propertyDescriptor.getPropertyType());

                        propertyDescriptor.getWriteMethod().invoke(bean, convertValue);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]", e);
        }

    }

    protected Object initializeBean(BeanDefinition beanDefinition, Object bean) {
        //如果bean是BeanFactoryAware类型 , 我也不知道要干啥 ..
        invokeAwareMethod(bean);
        //TODO 对bean做初始化 , 暂不实现

        //如果bean不是合成的 , 那么在initializeBean() 中 ,
        //还要回调beanPostProcessor.afterInitialization(existingBean, beanName)方法
        //这是在实例化bean后要做的动作 , 作为回调函数
        if (!beanDefinition.isSynthetic()) {
            return applyBeanPostProcessorsAfterInitialization(bean, beanDefinition.getID());
        }

        return bean;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            existingBean = beanPostProcessor.afterInitialization(existingBean, beanName);
        }

        return existingBean;
    }

    private void invokeAwareMethod(final Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
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

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public Object resolveDependency(DependencyDescriptor dependencyDescriptor) {
        Class<?> typeToMatch = dependencyDescriptor.getDependencyType();
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            resolveBeanClass(beanDefinition);
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (typeToMatch.isAssignableFrom(beanClass)) {
                return this.getBean(beanDefinition.getID());
            }
        }
        return null;
    }

    public void resolveBeanClass(BeanDefinition beanDefinition) {
        if (beanDefinition.hasBeanClass()) {
            return;
        } else {
            try {
                beanDefinition.resolveBeanClass(this.getBeanClassloader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class :" + beanDefinition.getBeanClassName());
            }
        }
    }
}
