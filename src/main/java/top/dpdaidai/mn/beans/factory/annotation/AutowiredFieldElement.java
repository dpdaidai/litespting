package top.dpdaidai.mn.beans.factory.annotation;

import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.factory.config.AutowireCapableBeanFactory;
import top.dpdaidai.mn.beans.factory.config.DependencyDescriptor;
import top.dpdaidai.mn.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 *
 * 它将属性自动注入
 *
 * @Author chenpantao
 * @Date 9/23/21 2:22 PM
 * @Version 1.0
 */
public class AutowiredFieldElement extends InjectionElement {

    boolean required;

    public AutowiredFieldElement(Field field, boolean required, AutowireCapableBeanFactory autowireCapableBeanFactory) {
        super(field, autowireCapableBeanFactory);
        this.required = required;
    }

    public Field getField() {
        return (Field) this.member;
    }

    public void inject(Object target) {
        Field field = this.getField();
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(field, required);
        Object value = autowireCapableBeanFactory.resolveDependency(dependencyDescriptor);
        ReflectionUtils.makeAccessible(field);
        if (value != null) {
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new BeanCreationException("Could not autowire field: " + field, e);
            }
        }

    }
}
