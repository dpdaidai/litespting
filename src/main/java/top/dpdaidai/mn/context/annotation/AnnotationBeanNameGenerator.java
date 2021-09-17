package top.dpdaidai.mn.context.annotation;

import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.annotation.AnnotationBeanDefinition;
import top.dpdaidai.mn.beans.factory.support.BeanDefinitionRegistry;
import top.dpdaidai.mn.beans.factory.support.BeanNameGenerator;
import top.dpdaidai.mn.core.annotation.AnnotationAttributes;
import top.dpdaidai.mn.core.type.AnnotationMetadata;
import top.dpdaidai.mn.stereotype.Component;
import top.dpdaidai.mn.util.ClassUtils;
import top.dpdaidai.mn.util.StringUtils;

import java.beans.Introspector;

/**
 *
 * 根据beanClassName生成id
 *
 * @Author chenpantao
 * @Date 9/17/21 3:05 PM
 * @Version 1.0
 */
public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    public static final String COMPONENT = Component.class.getName();

    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if (definition instanceof AnnotationBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotationBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                // Explicit bean name found.
                return beanName;
            }
        }
        return buildDefaultBeanName(definition);
    }

    /**
     * 如果在Component注解中有value属性 , 则以该属性值为beanID
     */
    protected String determineBeanNameFromAnnotation(AnnotationBeanDefinition annotationBeanDefinition) {
        AnnotationMetadata annotationMetadata = annotationBeanDefinition.getMetadata();
        String beanName = null;
        AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(COMPONENT);
        if (attributes.get("value") != null) {
            Object value = attributes.get("value");
            if (value instanceof String) {
                String strVal = (String) value;
                if (StringUtils.hasLength(strVal)) {
                    beanName = strVal;
                }
            }
        }
        return beanName;
    }

    /**
     *
     * 根据BeanDefinition生成默认的 beanName
     *
     * Derive a default bean name from the given bean definition.
     * <p>The default implementation simply builds a decapitalized version
     * of the short class name: e.g. "mypackage.MyJdbcDao" -> "myJdbcDao".
     * <p>Note that inner classes will thus have names of the form
     * "outerClassName.InnerClassName", which because of the period in the
     * name may be an issue if you are autowiring by name.
     * @param definition the bean definition to build a bean name for
     * @return the default bean name (never {@code null})
     */
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
        return Introspector.decapitalize(shortClassName);
    }

}
