package top.dpdaidai.mn.beans.factory;

import top.dpdaidai.mn.beans.ConstructorArgument;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {

    private String id;
    private String beanClassName;
    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;
    private Class<?> beanClass;

    //表名该BeanDefinition是否是合成的
    private boolean isSynthetic;

    private ConstructorArgument constructorArgument = new ConstructorArgument();

    private List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>();

    public GenericBeanDefinition() {

    }

    public GenericBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanClassName = beanClass.getName();
    }

    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_DEFAULT.equals(scope) || SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public String getBeanClassName() {
        return this.beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public List<PropertyValue> getPropertyValues() {
        return this.propertyValueList;
    }

    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }

    /**
     * 解析BeanDefinition的class对象
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     */
    public void resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) return;
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        if (this.beanClass == null) {
            throw new IllegalStateException("Bean class name [ " + this.getBeanClassName() +
                    "] has not been init");
        }
        return this.beanClass;
    }

    public boolean hasBeanClass() {
        return this.beanClass != null;
    }

    public boolean isSynthetic() {
        return this.isSynthetic;
    }

    public void setSynthetic(boolean isSynthetic) {
        this.isSynthetic = isSynthetic;
    }

}
