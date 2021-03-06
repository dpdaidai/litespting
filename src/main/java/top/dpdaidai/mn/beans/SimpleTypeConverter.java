package top.dpdaidai.mn.beans;

import top.dpdaidai.mn.beans.exception.TypeMismatchException;
import top.dpdaidai.mn.beans.propertyeditors.CustomBooleanEditor;
import top.dpdaidai.mn.beans.propertyeditors.CustomNumberEditor;
import top.dpdaidai.mn.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 简单类型转换器
 *
 * @Author chenpantao
 * @Date 9/13/21 5:19 PM
 * @Version 1.0
 */
public class SimpleTypeConverter implements TypeConverter {

    private Map<Class<?>, PropertyEditor> defaultEditors;

    public SimpleTypeConverter() {

    }

    /**
     * 判断值和需要的类型是否匹配
     * 1  如果是引用类型 , 那么它们 value.class 应该 等于 requiredType , 或者是它的子类/实现
     * 2  如果不匹配 , 并且value是String类型 , 那么多半是要把String转换为主数据类型 , 检查下value是否可以转换为requiredType
     *
     * @param value  值
     * @param requiredType  需要的class类型
     * @param <T>
     * @return
     * @throws TypeMismatchException
     */
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {

        if (ClassUtils.isAssignableValue(requiredType, value)) {
            return (T) value;
        } else {
            if (value instanceof String) {
                PropertyEditor editor = findDefaultEditor(requiredType);
                try {
                    editor.setAsText((String) value);
                } catch (IllegalArgumentException e) {
                    throw new TypeMismatchException(value, requiredType);
                }
                return (T) editor.getValue();
            } else {
                throw new RuntimeException("Todo : can't convert value for " + value + " class:" + requiredType);
            }
        }
    }

    private PropertyEditor findDefaultEditor(Class<?> requiredType) {
        PropertyEditor editor = this.getDefaultEditor(requiredType);
        if (editor == null) {
            throw new RuntimeException("Editor for " + requiredType + " has not been implemented");
        }
        return editor;
    }

    public PropertyEditor getDefaultEditor(Class<?> requiredType) {

        if (this.defaultEditors == null) {
            createDefaultEditors();
        }
        return this.defaultEditors.get(requiredType);
    }

    private void createDefaultEditors() {
        this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);

        // Spring's CustomBooleanEditor accepts more flag values than the JDK's default editor.
        this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
        this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));

        // The JDK does not contain default editors for number wrapper types!
        // Override JDK primitive number editors with our own CustomNumberEditor.
		this.defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, false));
		this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
		this.defaultEditors.put(short.class, new CustomNumberEditor(Short.class, false));
		this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
        this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
		this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
		this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
		this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
		this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
		this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
		this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
//		this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
//		this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));


    }


}
