package top.dpdaidai.mn.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.dpdaidai.mn.beans.ConstructorArgument;
import top.dpdaidai.mn.beans.SimpleTypeConverter;
import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 *
 * 根据BeanDefinition中的构造函数参数类型和顺序 , 寻找与Bean匹配的构造函数 , 并实例化bean
 *
 * @Author chenpantao
 * @Date 9/14/21 3:37 PM
 * @Version 1.0
 */
public class ConstructorResolver {

    protected final Log logger = LogFactory.getLog(getClass());

    private final ConfigurableBeanFactory beanFactory;


    public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 根据BeanDefinition中的构造函数参数类型和顺序 , 寻找与Bean匹配的构造函数 , 并实例化bean
     *
     * @param beanDefinition
     * @return
     */
    public Object autowireConstructor(final BeanDefinition beanDefinition) {
        //被选中的构造器
        Constructor<?> constructorToUse = null;
        //该数组用来保存转化后的参数
        Object[] argsToUse = null;

        Class<?> beanClass;

        try {
            beanClass = this.beanFactory.getBeanClassloader().loadClass(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new BeanCreationException(beanDefinition.getID(), "Instantiation of bean failed, can't resolve class", e);
        }

        //bean所有的构造器数组
        Constructor<?>[] candidates = beanClass.getConstructors();

        ConstructorArgument constructorArgument = beanDefinition.getConstructorArgument();
        List<ConstructorArgument.ValueHolder> argumentValues = constructorArgument.getArgumentValues();

        for (Constructor<?> candidate : candidates) {
            Class<?>[] parameterTypes = candidate.getParameterTypes();

            //参数数量不匹配直接跳过
            if (parameterTypes.length != argumentValues.size()) {
                continue;
            }

            argsToUse = new Object[parameterTypes.length];

            //返回值为参数是否匹配
            boolean match = valuesMatchTypes(parameterTypes, argumentValues, argsToUse);

            if (match) {
                constructorToUse = candidate;
                break;
            }

        }

        //找不到一个合适的构造函数
        if (constructorToUse == null) {
            throw new BeanCreationException(beanDefinition.getID(), "can't find a apporiate constructor");
        }

        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException(beanDefinition.getID(), "can't find a create instance using " + constructorToUse);
        }
    }

    /**
     * 检查参数类型数组和BeadDefinition中的参数数组是否匹配
     * 匹配则返回true
     *
     * @param parameterTypes
     * @param valueHolders
     * @param argsToUse
     * @return
     */
    private boolean valuesMatchTypes(Class<?>[] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse) {
        BeanDefinitionValueResolver valueResolver =
                new BeanDefinitionValueResolver(this.beanFactory);

        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        for (int i = 0; i < parameterTypes.length; i++) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            //获取参数的值，可能是TypedStringValue, 也可能是RuntimeBeanReference
            Object originalValue = valueHolder.getValue();
            //获得真正的值
            //RuntimeBeanReference解析为对象
            //TypedStringValue还需要再转换
            Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);

            try {

                //转过过程中 , 如果抛出异常 , 则说明类型不匹配 , 返回false
                Object convertIfNecessary = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);

                argsToUse[i] = convertIfNecessary;

            } catch (Exception e) {
                logger.error(e);
                return false;
            }

        }

        return true;
    }

}
