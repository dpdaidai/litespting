package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.BeansException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.FactoryBean;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;

/**
 *
 * 该类 接受 RuntimeBeanReference , TypedStringValue , BeanDefinition 三种类型的值, 并将它们解析为实例
 *
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

            //RuntimeBeanReference 是通过 id 引用 bean
            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;

            return beanFactory.getBean(runtimeBeanReference.getBeanID());

        } else if (value instanceof TypedStringValue) {

            //TypedStringValue 保存字符串类型和基本数据类型的值 , 在使用时还需要根据需要的类型进行转换
            TypedStringValue typedStringValue = (TypedStringValue) value;

            return typedStringValue.getValue();

        } else if (value instanceof BeanDefinition){

            //BeanDefinition 当需要使用内部bean时 , 由beanFactory根据保存的beanDefinition生成实例 .
            //它和RuntimeBeanReference的区别主要在于 它不是生成全局共享的bean , 属于内部bean
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

            // FactoryBean类型的bean生成后不能直接使用 , 有用的是它持有的object , 所以需要个额外判断
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
