package top.dpdaidai.mn.aop.aspect;

import top.dpdaidai.mn.aop.Advice;
import top.dpdaidai.mn.aop.MethodMatcher;
import top.dpdaidai.mn.aop.Pointcut;
import top.dpdaidai.mn.aop.framework.AdvisedSupport;
import top.dpdaidai.mn.aop.framework.AopProxyFactory;
import top.dpdaidai.mn.aop.framework.CglibProxyFactory;
import top.dpdaidai.mn.beans.exception.BeansException;
import top.dpdaidai.mn.beans.factory.config.BeanPostProcessor;
import top.dpdaidai.mn.beans.factory.config.ConfigurableBeanFactory;
import top.dpdaidai.mn.util.SpringClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 该类用来生成动态代理子类
 *
 * @Author chenpantao
 * @Date 10/12/21 4:18 PM
 * @Version 1.0
 */
public class AspectJAutoProxyCreator implements BeanPostProcessor {

    ConfigurableBeanFactory beanFactory;

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        //TODO 暂无实现
        return bean;
    }

    /**
     * 在bean生成后 , 回调的函数 .
     * 目的 : 判断该bean是否有关联的拦截器 , 如果有 , 则为该bean生成动态代理类替换原bean
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    public Object afterInitialization(Object bean, String beanName) throws BeansException {

        //如果bean本上就是advice及其子类 , 那么就不在生成动态代理了
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        //寻找所有和bean匹配的advice , 也就是拦截器
        List<Advice> candidateAdvices = getCandidateAdvices(bean);
        if (candidateAdvices.isEmpty()) {
            return bean;
        }

        return createProxy(candidateAdvices, bean);
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean resultValue = Advice.class.isAssignableFrom(beanClass);
        return resultValue;
    }

    private List<Advice> getCandidateAdvices(Object bean) {
        List<Object> adviceList = this.beanFactory.getBeansByType(Advice.class);

        List<Advice> result = new ArrayList<Advice>();
        for (Object o : adviceList) {
            Pointcut pointcut = ((Advice) o).getPointcut();
            if (canApply(pointcut, bean.getClass())) {
                result.add((Advice) o);
            }

        }

        return result;

    }

    public static boolean canApply(Pointcut pointcut, Class<?> targetClass) {
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        Set<Class> allInterfacesForClassAsSet = SpringClassUtils.getAllInterfacesForClassAsSet(targetClass);
        allInterfacesForClassAsSet.add(targetClass);

        for (Class aClass : allInterfacesForClassAsSet) {
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if (methodMatcher.matches(method)) {
                    return true;
                }
            }
        }

        return false;

    }

    protected Object createProxy(List<Advice> adviceList, Object bean) {
        AdvisedSupport advised = new AdvisedSupport();
        for (Advice advice : adviceList) {
            advised.addAdvice(advice);
        }

        advised.setTargetObject(bean);

        AopProxyFactory proxyFactory = null;
        proxyFactory = new CglibProxyFactory(advised);

        return proxyFactory.getProxy();

    }

}
