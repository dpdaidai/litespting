package top.dpdaidai.mn.beans.factory.config;

public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setBeanClassloader(ClassLoader classloader);

    ClassLoader getBeanClassloader();


}
