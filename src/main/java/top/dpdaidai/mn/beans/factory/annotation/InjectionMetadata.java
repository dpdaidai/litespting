package top.dpdaidai.mn.beans.factory.annotation;

import java.util.List;

/**
 *
 * 单个对象的待自动装备的属性 , 由该类保存 .
 *
 * @Author chenpantao
 * @Date 9/23/21 2:46 PM
 * @Version 1.0
 */
public class InjectionMetadata {

    private final Class<?> targetClass;

    private List<InjectionElement> injectionElementList;

    public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElementList) {
        this.targetClass = targetClass;
        this.injectionElementList = injectionElementList;
    }

    public List<InjectionElement> getInjectionElementList() {
        return this.injectionElementList;
    }

    public void inject(Object target) {
        if (injectionElementList == null || injectionElementList.isEmpty()) {
            return;
        }
        for (InjectionElement injectionElement : injectionElementList) {
            injectionElement.inject(target);
        }
    }

}
