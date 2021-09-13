package top.dpdaidai.mn.beans;

import top.dpdaidai.mn.beans.exception.TypeMismatchException;

/**
 * @Author chenpantao
 * @Date 9/13/21 5:17 PM
 * @Version 1.0
 */
public interface TypeConverter {

    <T>T convertIfNecessary(Object value, Class<T> requireType) throws TypeMismatchException;

}
