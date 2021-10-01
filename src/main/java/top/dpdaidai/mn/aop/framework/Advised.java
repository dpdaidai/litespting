package top.dpdaidai.mn.aop.framework;

import top.dpdaidai.mn.aop.Advice;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author chenpantao
 * @Date 9/29/21 5:36 PM
 * @Version 1.0
 */
public interface Advised {

    Class<?> getTargetClass();

    void setTargetObject(Object targetObject);

    Object getTargetObject();

    void addAdvice(Advice advice);

    List<Advice> getAdvices();

    List<Advice> getAdvices(Method method);

}
