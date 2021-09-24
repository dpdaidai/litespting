package top.dpdaidai.mn.beans.factory.config;

import top.dpdaidai.mn.beans.exception.BeansException;

/**
 * @Author chenpantao
 * @Date 9/23/21 8:46 PM
 * @Version 1.0
 */
public interface BeanPostProcessor {

    /**
     * bean初始化之前的操作
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object beforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * bean初始化后的操作
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object afterInitialization(Object bean, String beanName) throws BeansException;


}
