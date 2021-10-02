package top.dpdaidai.mn.beans.factory;

import top.dpdaidai.mn.beans.exception.BeansException;

public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
