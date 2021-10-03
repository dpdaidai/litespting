package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;

/**
 * @Author chenpantao
 * @Date 10/3/21 10:48 AM
 * @Version 1.0
 */
public class BeanDefinitionReaderUtils {

    //如果一个类名不唯一 , 那么就用#0 , #1 ... 做区分
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    /**
     * 为待注册的BeanDefinition 生成 beanID
     * 默认格式为 beanClassName#0
     *
     * @param beanDefinition
     * @param registry
     * @param isInnerBean  暂不知道有啥用
     * @return
     */
    public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry, boolean isInnerBean) {
        String generatedBeanName = beanDefinition.getBeanClassName();

        String id = generatedBeanName;
        if (isInnerBean) {
            id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + Integer.toHexString(System.identityHashCode(beanDefinition));
        }else {
            int counter = -1;
            while (counter == -1 || registry.getBeanDefinition(id) != null) {
                counter++;
                id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
            }
        }

        return id;

    }

    public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry){
        return generateBeanName(beanDefinition, registry, false);
    }

    /**
     * 注册合成的beanDefinition
     * @param beanDefinition
     * @param beanDefinitionRegistry
     * @return
     */
    public static String registerWithGeneratedName(GenericBeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry){
        String generatedName = generateBeanName(beanDefinition, beanDefinitionRegistry);
        beanDefinitionRegistry.registerBeanDefinition(generatedName, beanDefinition);
        return generatedName;
    }


}
