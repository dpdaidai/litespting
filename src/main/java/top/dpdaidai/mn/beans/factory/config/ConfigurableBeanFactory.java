package top.dpdaidai.mn.beans.factory.config;

import java.util.List;

public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setBeanClassloader(ClassLoader classloader);

    ClassLoader getBeanClassloader();

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    List<BeanPostProcessor> getBeanPostProcessors();


}
