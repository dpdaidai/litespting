package top.dpdaidai.mn.context.annotation;

import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;
import top.dpdaidai.mn.beans.factory.annotation.AnnotationBeanDefinition;
import top.dpdaidai.mn.core.type.AnnotationMetadata;

/**
 * @Author chenpantao
 * @Date 9/17/21 2:45 PM
 * @Version 1.0
 */
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotationBeanDefinition {

    private AnnotationMetadata annotationMetadata;

    public ScannedGenericBeanDefinition(AnnotationMetadata annotationMetadata) {
        this.annotationMetadata = annotationMetadata;
    }

    public String getBeanClassName() {
        return this.annotationMetadata.getClassName();
    }

    //final修饰方法 , 不允许子类重写
    public final AnnotationMetadata getMetadata() {
        return annotationMetadata;
    }

}
