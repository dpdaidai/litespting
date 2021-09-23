package top.dpdaidai.mn.beans.factory;

import top.dpdaidai.mn.beans.ConstructorArgument;

import java.util.List;

/**
 *
 * bean的定义 , 主要包含beanName , 是否单例
 *
 * @Author chenpantao
 * @Date 9/1/21 2:44 PM
 * @Version 1.0
 */
public interface BeanDefinition {

    public static final String SCOPE = "scope";
    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";
    public static final String SCOPE_DEFAULT = "";

    public boolean isSingleton();

    public boolean isPrototype();

    String getScope();

    void setScope(String scope);

    public String getBeanClassName();

    public List<PropertyValue> getPropertyValues();

    public ConstructorArgument getConstructorArgument();

    public String getID();

    public boolean hasConstructorArgumentValues();

    /**
     * 根据bean 的 className解析bean 的class对象
     * @param classLoader
     * @throws ClassNotFoundException
     */
    public void resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;

    public Class<?> getBeanClass() throws IllegalStateException;

    public boolean hasBeanClass();

}
