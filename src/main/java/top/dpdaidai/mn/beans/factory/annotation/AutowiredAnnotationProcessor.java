package top.dpdaidai.mn.beans.factory.annotation;

import top.dpdaidai.mn.beans.exception.BeansException;
import top.dpdaidai.mn.beans.factory.config.AutowireCapableBeanFactory;
import top.dpdaidai.mn.beans.factory.config.InstantiationAwareBeanPostProcessor;
import top.dpdaidai.mn.core.annotation.AnnotationUtils;
import top.dpdaidai.mn.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * 构建clazz这个类型的 依赖元数据InjectionMetadata .
 * 解析目标clazz中所有的需要依赖注入的属性 , 方法 等
 *
 * @Author chenpantao
 * @Date 9/23/21 4:57 PM
 * @Version 1.0
 */
public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor {

    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    //标记该装配是否是必须的
    private final String requiredParameterName = "required";
    private final boolean requiredParameterValue = true;

    //该集合保存所有注解 , 被这些注解标记的位置就需要自动装配
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>();

    public AutowiredAnnotationProcessor() {
        this.autowiredAnnotationTypes.add(Autowired.class);
    }

    public void setBeanFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }

    /**
     * 查看目标对象accessibleObject是否包含autowiredAnnotationTypes中的注解类型
     * 如果包含则返回该注解
     *
     * @param accessibleObject
     * @return
     */
    private Annotation findAutowiredAnnotation(AccessibleObject accessibleObject) {
        for (Class<? extends Annotation> autowiredAnnotationType : autowiredAnnotationTypes) {
            Annotation annotation = AnnotationUtils.getAnnotation(accessibleObject, autowiredAnnotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 判断注解annotation是否包含required方法 , 如果有的话 , 返回该方法的值
     * @param annotation
     * @return
     */
    protected boolean determineRequiredStatus(Annotation annotation) {
        Method method = ReflectionUtils.findMethod(annotation.annotationType(), this.requiredParameterName);
        if (method == null) {
            return true;
        }
        //使用反射执行目标方法 , 查看它的值 . 为什么要和requiredParameterValue比较 , 暂时不清楚
        boolean b = (Boolean) ReflectionUtils.invokeMethod(method, annotation) == this.requiredParameterValue;
        return b;
    }

    /**
     * 构建clazz这个类型的 依赖元数据
     * @param clazz
     * @return
     */
    public InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        LinkedList<InjectionElement> elementList = new LinkedList<InjectionElement>();

        Class<?> targetClass = clazz;
        while (targetClass != null && targetClass != Object.class) {
            LinkedList<InjectionElement> currentElement = new LinkedList<InjectionElement>();
            for (Field field : targetClass.getDeclaredFields()) {

                //如果属性是静态的, 则直接跳过
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                //是否有自动注入的注解
                Annotation autowiredAnnotation = findAutowiredAnnotation(field);
                if (autowiredAnnotation != null) {
                    //判断是否是必须注入
                    boolean required = determineRequiredStatus(autowiredAnnotation);
                    currentElement.add(new AutowiredFieldElement(field, required, autowireCapableBeanFactory));
                }

            }

            for (Method declaredMethod : targetClass.getDeclaredMethods()) {
                //TODO 处理方法的注入
            }

            elementList.addAll(currentElement);
            targetClass = targetClass.getSuperclass();

        }

        return new InjectionMetadata(clazz, elementList);

    }

    public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata injectionMetadata = buildAutowiringMetadata(bean.getClass());
        injectionMetadata.inject(bean);
    }

    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
