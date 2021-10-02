package top.dpdaidai.mn.aop.config;

import top.dpdaidai.mn.beans.exception.BeansException;
import top.dpdaidai.mn.beans.factory.BeanFactory;
import top.dpdaidai.mn.beans.factory.BeanFactoryAware;
import top.dpdaidai.mn.util.StringUtils;

/**
 * @Author chenpantao
 * @Date 10/2/21 2:55 PM
 * @Version 1.0
 */
public class AspectInstanceFactory implements BeanFactoryAware {

    private String aspectBeanName;

    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName) {
        if (!StringUtils.hasText(aspectBeanName)) {
            throw new IllegalArgumentException("'aspectBeanName' is required");
        }
        this.aspectBeanName = aspectBeanName;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public Object getAspectInstance() throws Exception {
        return this.beanFactory.getBean(this.aspectBeanName);
    }
}
