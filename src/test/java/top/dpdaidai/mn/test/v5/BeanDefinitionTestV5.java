package top.dpdaidai.mn.test.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.aop.aspect.AspectExpressionPointcut;
import top.dpdaidai.mn.aop.aspect.AspectJBeforeAdvice;
import top.dpdaidai.mn.aop.config.AspectInstanceFactory;
import top.dpdaidai.mn.aop.config.MethodLocatingFactory;
import top.dpdaidai.mn.beans.ConstructorArgument;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.PropertyValue;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.service.tx.TransactionManager;

import java.util.List;

/**
 * @Author chenpantao
 * @Date 10/4/21 1:06 AM
 * @Version 1.0
 */
public class BeanDefinitionTestV5 {

    public static DefaultBeanFactory defaultBeanFactory;

    @Before
    public void before() {

        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v5.xml");
        reader.loadBeanDefinitions(resource);
    }

    @Test
    public void testAOPBean() {

        //检查transactionManager是否生成
        {
            BeanDefinition transactionManagerBeanDefinition = defaultBeanFactory.getBeanDefinition("transactionManager");
            Assert.assertTrue(transactionManagerBeanDefinition.getBeanClassName().equals(TransactionManager.class.getName()));

        }

        //检查placeOrder是否正确生成
        {
            BeanDefinition placeOrderBeanDefinition = defaultBeanFactory.getBeanDefinition("placeOrder");
            Assert.assertTrue(placeOrderBeanDefinition.isSynthetic());
            Assert.assertTrue(placeOrderBeanDefinition.getBeanClass().equals(AspectExpressionPointcut.class));

            PropertyValue propertyValue = placeOrderBeanDefinition.getPropertyValues().get(0);
            Assert.assertEquals("expression", propertyValue.getName());
            Assert.assertEquals("execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))", propertyValue.getValue());

        }
    }

    @Test
    public void testAspectAdvice() {
        String beanId = AspectJBeforeAdvice.class.getName() + "#0";
        BeanDefinition beforeAdviceBeanDefinition = defaultBeanFactory.getBeanDefinition(beanId);

        Assert.assertEquals(beforeAdviceBeanDefinition.getBeanClass(), AspectJBeforeAdvice.class);
        Assert.assertTrue(beforeAdviceBeanDefinition.isSynthetic());

        //校验构造器
        ConstructorArgument constructorArgument = beforeAdviceBeanDefinition.getConstructorArgument();
        List<ConstructorArgument.ValueHolder> argumentValues = constructorArgument.getArgumentValues();
        Assert.assertEquals(argumentValues.size(), 3);

        //构造函数第一参数
        {
            ConstructorArgument.ValueHolder valueHolder = argumentValues.get(0);
            BeanDefinition methodBeanDefinition = (BeanDefinition) valueHolder.getValue();

            Assert.assertTrue(methodBeanDefinition.isSynthetic());
            Assert.assertTrue(methodBeanDefinition.getBeanClass().equals(MethodLocatingFactory.class));

            List<PropertyValue> propertyValues = methodBeanDefinition.getPropertyValues();
            Assert.assertEquals("targetBeanName", propertyValues.get(0).getName());
            Assert.assertEquals("transactionManager", propertyValues.get(0).getValue());
            Assert.assertEquals("methodName", propertyValues.get(1).getName());
            Assert.assertEquals("start", propertyValues.get(1).getValue());
        }

        //构造函数第二个参数
        {
            BeanDefinition aspectFactoryBeanDefinition = (BeanDefinition)argumentValues.get(1).getValue();
            Assert.assertTrue(aspectFactoryBeanDefinition.isSynthetic());
            Assert.assertTrue(aspectFactoryBeanDefinition.getBeanClass().equals(AspectInstanceFactory.class));

            List<PropertyValue> propertyValues = aspectFactoryBeanDefinition.getPropertyValues();
            Assert.assertEquals("aspectBeanName", propertyValues.get(0).getName());
            Assert.assertEquals("transactionManager", propertyValues.get(0).getValue());

        }

        //构造函数第三个参数
        {
            RuntimeBeanReference reference = (RuntimeBeanReference)argumentValues.get(2).getValue();
            Assert.assertEquals("placeOrder", reference.getBeanID());

        }
    }

}
