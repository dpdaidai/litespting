package top.dpdaidai.mn.beans.factory;

public interface BeanFactory {

    BeanDefinition getBeanDefinition(String beanID);

    Object getBean(String beanID);
}
