package top.dpdaidai.mn.beans.factory.support;

import top.dpdaidai.mn.beans.factory.config.SingletonBeanRegistry;
import top.dpdaidai.mn.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 该类的map维护单例实例
 *
 * @Author chenpantao
 * @Date 9/2/21 5:19 PM
 * @Version 1.0
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);

    public void registrySingletonBean(String beanID, Object singletonObject) {
        Assert.notNull(beanID, "'beanID' must not be null");

        Object oldObject = this.singletonObjects.get(beanID);
        if (oldObject != null) {
            throw new IllegalStateException("Could not register object [" + singletonObject +
                    "] under bean name '" + beanID + "': there is already object [" + oldObject + "] bound");
        }
        singletonObjects.put(beanID, singletonObject);
    }

    public Object getSingletonBean(String beanID) {
        return singletonObjects.get(beanID);
    }
}
