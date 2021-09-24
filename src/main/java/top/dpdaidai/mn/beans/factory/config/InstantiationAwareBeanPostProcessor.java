package top.dpdaidai.mn.beans.factory.config;

import top.dpdaidai.mn.beans.exception.BeansException;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * bean实例化之前
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    /**
     * bean实例化之后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    boolean afterInstantiation(Object bean, String beanName) throws BeansException;

    /**
     * @param bean
     * @param beanName
     * @throws BeansException
     */
    void postProcessPropertyValues(Object bean, String beanName) throws BeansException;


}
