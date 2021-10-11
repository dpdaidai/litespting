package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.config.ConfigurableBeanFactory;

/**
 * @Author chenpantao
 * @Date 10/11/21 4:13 PM
 * @Version 1.0
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    protected abstract Object createBean(BeanDefinition beanDefinition) throws BeanCreationException;

}
