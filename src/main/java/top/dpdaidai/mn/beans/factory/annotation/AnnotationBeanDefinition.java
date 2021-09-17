package top.dpdaidai.mn.beans.factory.annotation;

import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.core.type.AnnotationMetadata;

/**
 * @Author chenpantao
 * @Date 9/17/21 2:24 PM
 * @Version 1.0
 */
public interface AnnotationBeanDefinition extends BeanDefinition {

    AnnotationMetadata getMetadata();

}
