package top.dpdaidai.mn.beans.factory.config;

import top.dpdaidai.mn.beans.factory.BeanFactory;

public interface ConfigurableBeanFactory extends BeanFactory {

    void setBeanClassloader(ClassLoader classloader);

    ClassLoader getBeanClassloader();


}
