package top.dpdaidai.mn.beans.factory.config;

import top.dpdaidai.mn.beans.factory.BeanFactory;

/**
 * @Author chenpantao
 * @Date 9/22/21 8:05 PM
 * @Version 1.0
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 解析依赖
     * @param dependencyDescriptor
     * @return
     */
    public Object resolveDependency(DependencyDescriptor dependencyDescriptor);

}
