package top.dpdaidai.mn.beans.factory.config;

import top.dpdaidai.mn.util.Assert;

import java.lang.reflect.Field;

/**
 *
 * 保存被框架管理的类的属性 .
 *
 * @Author chenpantao
 * @Date 9/22/21 8:00 PM
 * @Version 1.0
 */
public class DependencyDescriptor {

    private Field field;
    private boolean required;


    public DependencyDescriptor(Field field, boolean required) {
        Assert.notNull(field, "Field must not be null");
        this.field = field;
        this.required = required;
    }

    public Class<?> getDependencyType() {
        if (this.field != null) {
            return field.getType();
        }
        throw new RuntimeException("only support filed dependency");
    }

    public boolean isRequired() {
        return this.required;
    }


}
