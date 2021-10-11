package top.dpdaidai.mn.aop.config;

import top.dpdaidai.mn.beans.BeanUtils;
import top.dpdaidai.mn.beans.factory.BeanFactory;
import top.dpdaidai.mn.beans.factory.BeanFactoryAware;
import top.dpdaidai.mn.beans.factory.FactoryBean;
import top.dpdaidai.mn.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 方法解析工厂
 * 根据 beanID 和 bean的方法名 , 解析得到目标方法method
 *
 * 当
 * targetBeanName = "transactionManager"
 * methodName = "start"
 * 此时 : method = getObject()
 * 效果等价于 : method = TransactionManager.class.getMethod("start")
 *
 * @Author chenpantao
 * @Date 9/27/21 5:03 PM
 * @Version 1.0
 */
public class MethodLocatingFactory implements FactoryBean<Method>, BeanFactoryAware {

    private String targetBeanName;

    private String methodName;

    private Method method;

    public void setTargetBeanName(String targetBeanName) {
        if (!StringUtils.hasText(targetBeanName))
            throw new IllegalArgumentException("Property 'targetBeanName' can not be empty");
        this.targetBeanName = targetBeanName;
    }

    public void setMethodName(String methodName) {
        if (!StringUtils.hasText(methodName))
            throw new IllegalArgumentException("Property 'methodName' can not be empty");
        this.methodName = methodName;
    }


    public void setBeanFactory(BeanFactory beanFactory) {
        Class<?> targetClass = beanFactory.getType(this.targetBeanName);
        if (targetClass == null)
            throw new IllegalArgumentException("Can't determine type of bean with name [ " + this.targetBeanName + "]");

        method = BeanUtils.resolveSignature(this.methodName, targetClass);
        if (method == null)
            throw new IllegalArgumentException("Unable to locate method [ " + this.targetBeanName + "]" + "on bean :" + this.targetBeanName);

    }

    public Method getObject() throws Exception {
        return method;
    }

    public Class<?> getObjectType() {
        return method.getClass();
    }
}

