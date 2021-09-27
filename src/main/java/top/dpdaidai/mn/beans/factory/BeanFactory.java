package top.dpdaidai.mn.beans.factory;

import top.dpdaidai.mn.beans.exception.NoSuchBeanDefinitionException;

public interface BeanFactory {

    Object getBean(String beanID);

    /**
     * 根据 beanID 获取类对象
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    Class<?> getType(String beanID) throws NoSuchBeanDefinitionException;
}
