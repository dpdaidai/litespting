package top.dpdaidai.mn.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @Author chenpantao
 * @Date 9/23/21 4:53 PM
 * @Version 1.0
 */
public class AnnotationUtils {

    /**
     * 查看元素是否包含指定的注解类型 .
     * 如果不包含 , 则查看它的注解的注解中是否包含指定类型
     *
     * @param annotatedElement
     * @param annotationType
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annotationType) {
        T annotation = annotatedElement.getAnnotation(annotationType);
        if (annotation == null) {
            for (Annotation elementAnnotation : annotatedElement.getAnnotations()) {
                annotation = elementAnnotation.annotationType().getAnnotation(annotationType);
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }

}
