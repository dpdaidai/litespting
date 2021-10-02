package top.dpdaidai.mn.beans.factory;

/**
 * @Author chenpantao
 * @Date 10/2/21 2:51 PM
 * @Version 1.0
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

}
