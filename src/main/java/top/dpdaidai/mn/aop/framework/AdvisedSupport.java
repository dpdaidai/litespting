package top.dpdaidai.mn.aop.framework;

import top.dpdaidai.mn.aop.Advice;
import top.dpdaidai.mn.aop.MethodMatcher;
import top.dpdaidai.mn.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * AOP的支持类
 * 1   它持有所有切面对象advice
 * 2   持有 targetObject 对象和它的class
 * 3   可以根据method 找出匹配的切面对象adviceList
 *
 * @Author chenpantao
 * @Date 9/29/21 5:40 PM
 * @Version 1.0
 */
public class AdvisedSupport implements Advised {

    private Object targetObject = null;

    private List<Advice> adviceList = new ArrayList<Advice>();

    public AdvisedSupport() {

    }

    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object getTargetObject() {
        return this.targetObject;
    }

    public void addAdvice(Advice advice) {
        this.adviceList.add(advice);
    }

    public List<Advice> getAdvices() {
        return this.adviceList;
    }

    /**
     * 获取和method匹配的advice
     * @param method
     * @return
     */
    public List<Advice> getAdvices(Method method) {
        ArrayList<Advice> resultAdvice = new ArrayList<Advice>();
        for (Advice advice : this.getAdvices()) {
            Pointcut pointcut = advice.getPointcut();
            MethodMatcher methodMatcher = pointcut.getMethodMatcher();
            if (methodMatcher.matches(method)) {
                resultAdvice.add(advice);
            }
        }
        return resultAdvice;
    }
}
