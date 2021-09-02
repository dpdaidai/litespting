package top.dpdaidai.mn.beans.factory.config;

public interface SingletonBeanRegistry {

    void registrySingletonBean(String beanName, Object singletonObject);

    Object getSingletonBean(String beanName);

}
