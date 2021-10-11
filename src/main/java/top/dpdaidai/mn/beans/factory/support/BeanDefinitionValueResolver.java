package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.BeansException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.FactoryBean;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;

/**
 * @Author chenpantao
 * @Date 9/3/21 4:31 PM
 * @Version 1.0
 */
public class BeanDefinitionValueResolver {

    public final AbstractBeanFactory beanFactory;

    public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {

        if (value instanceof RuntimeBeanReference) {

            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;

            return beanFactory.getBean(runtimeBeanReference.getBeanID());

        } else if (value instanceof TypedStringValue) {

            TypedStringValue typedStringValue = (TypedStringValue) value;

            return typedStringValue.getValue();

        } else if (value instanceof BeanDefinition){
            BeanDefinition beanDefinition = (BeanDefinition) value;

            //生成内部beanName
            String innerBeanName = "(inner bean)" + beanDefinition.getBeanClassName() + "#" +
                    Integer.toHexString(System.identityHashCode(beanDefinition));

            return resolveInnerBean(innerBeanName, beanDefinition);

        } else {
            return value;
        }

    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBeanDefinition) {
        try {

            Object innerBean = this.beanFactory.createBean(innerBeanDefinition);

            if (innerBean instanceof FactoryBean) {
                try {
                    return ((FactoryBean<?>)innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName, "FactoryBean threw exception on object creation", e);
                }
            }
            else {
                return innerBean;
            }
        }
        catch (BeansException ex) {
            throw new BeanCreationException(
                    innerBeanName,
                    "Cannot create inner bean '" + innerBeanName + "' " +
                            (innerBeanDefinition != null && innerBeanDefinition.getBeanClassName() != null ? "of type [" + innerBeanDefinition.getBeanClassName() + "] " : "")
                    , ex);
        }
    }

}
