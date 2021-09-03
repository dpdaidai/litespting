package top.dpdaidai.mn.beans.factory;

/**
 *
 * bean中的字段属性
 *
 * @Author chenpantao
 * @Date 9/3/21 11:52 AM
 * @Version 1.0
 */
public class PropertyValue {

    private String name;

    private Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

}
