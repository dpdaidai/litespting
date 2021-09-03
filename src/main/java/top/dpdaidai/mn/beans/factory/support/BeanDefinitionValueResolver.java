package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.factory.BeanFactory;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;

/**
 * @Author chenpantao
 * @Date 9/3/21 4:31 PM
 * @Version 1.0
 */
public class BeanDefinitionValueResolver {

    public final BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {

        if (value instanceof RuntimeBeanReference) {

            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;

            return beanFactory.getBean(runtimeBeanReference.getBeanID());

        } else if (value instanceof TypedStringValue) {

            TypedStringValue typedStringValue = (TypedStringValue) value;

            return typedStringValue.getValue();

        } else {
            throw new RuntimeException("the value " + value + " has not implemented");
        }
        
    }

}
