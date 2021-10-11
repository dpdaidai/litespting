package top.dpdaidai.mn.test.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.aop.Advice;
import top.dpdaidai.mn.aop.Pointcut;
import top.dpdaidai.mn.aop.aspect.AspectExpressionPointcut;
import top.dpdaidai.mn.aop.aspect.AspectJAfterReturningAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJAfterThrowingAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJBeforeAdvice;
import top.dpdaidai.mn.beans.factory.support.DefaultBeanFactory;
import top.dpdaidai.mn.beans.factory.support.XmlBeanDefinitionReader;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.service.tx.TransactionManager;
import top.dpdaidai.mn.service.v5.PetStoreService;

import java.util.List;

/**
 * @Author chenpantao
 * @Date 10/11/21 4:30 PM
 * @Version 1.0
 */
public class BeanFactoryTestV5 {

    String expression = "execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))";
    DefaultBeanFactory defaultBeanFactory;

    @Before
    public void setUp() {
        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource("petstore-v5.xml");
        reader.loadBeanDefinitions(resource);
    }

    @Test
    public void testGetBeanByType() throws Exception {

        List<Object> advices = defaultBeanFactory.getBeansByType(Advice.class);

        Assert.assertEquals(advices.size(), 3);

        {
            AspectJBeforeAdvice beforeAdvice = (AspectJBeforeAdvice) this.findObject(AspectJBeforeAdvice.class, advices);
            Assert.assertEquals(TransactionManager.class.getMethod("start"), beforeAdvice.getAdviceMethod());
            Assert.assertEquals(expression, beforeAdvice.getPointcut().getExpression());
            Assert.assertEquals(TransactionManager.class, beforeAdvice.getAdviceInstance().getClass());
        }

        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice) this.findObject(AspectJAfterReturningAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("commit"), advice.getAdviceMethod());

            Assert.assertEquals(expression, advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());

        }

        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice) this.findObject(AspectJAfterThrowingAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("rollback"), advice.getAdviceMethod());

            Assert.assertEquals(expression, advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());

        }

    }

    @Test
    public void testPointcut() throws Exception {
        List<Object> pointcutList = defaultBeanFactory.getBeansByType(Pointcut.class);

        Assert.assertEquals(pointcutList.size(), 1);

        {
            AspectExpressionPointcut pointcut = (AspectExpressionPointcut) this.findObject(AspectExpressionPointcut.class, pointcutList);
            Assert.assertEquals(expression, pointcut.getExpression());
            Assert.assertTrue(pointcut.matches(PetStoreService.class.getMethod("placeOrder")));
        }
    }

    public Object findObject(Class<?> type, List<Object> advices) {
        for (Object o : advices) {
            if (o.getClass().equals(type)) {
                return o;
            }
        }
        return null;
    }

}
