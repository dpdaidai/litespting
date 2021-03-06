package top.dpdaidai.mn.beans.propertyeditors;

import top.dpdaidai.mn.util.NumberUtils;
import top.dpdaidai.mn.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

/**
 * 自定义number编辑器
 *
 * @Author chenpantao
 * @Date 9/13/21 5:02 PM
 * @Version 1.0
 */
public class CustomNumberEditor extends PropertyEditorSupport {

    private final Class<? extends Number> numberClass;

    private final NumberFormat numberFormat;

    private final boolean allowEmpty;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass,
                              NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {

        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        }
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        }
        else if (this.numberFormat != null) {
            // Use given NumberFormat for parsing text.
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        }
        else {
            // Use default valueOf methods for parsing text.
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }


    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            super.setValue(NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
        }
        else {
            super.setValue(value);
        }
    }


    @Override
    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        }
        if (this.numberFormat != null) {
            // Use NumberFormat for rendering value.
            return this.numberFormat.format(value);
        }
        else {
            // Use toString method for rendering value.
            return value.toString();
        }
    }

}
