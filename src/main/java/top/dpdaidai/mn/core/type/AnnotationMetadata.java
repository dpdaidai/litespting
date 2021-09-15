package top.dpdaidai.mn.core.type;

import top.dpdaidai.mn.core.annotation.AnnotationAttributes;

import java.util.Set;

/**
 * @Author chenpantao
 * @Date 9/15/21 5:38 PM
 * @Version 1.0
 */
public interface AnnotationMetadata extends ClassMetadata {

    Set<String> getAnnotationTypes();

    boolean hasAnnotation(String annotationType);

    AnnotationAttributes getAnnotationAttributes(String annotationType);

}
