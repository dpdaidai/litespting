package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.factory.BeanDefinition;

public interface BeanDefinitionRegistry {

    BeanDefinition getBeanDefinition(String beanID);

    void registerBeanDefinition(String beanID, BeanDefinition beanDefinition);

}
