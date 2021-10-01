package top.dpdaidai.mn.test.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.aop.aspect.AspectExpressionPointcut;
import top.dpdaidai.mn.aop.aspect.AspectJAfterReturningAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJBeforeAdvice;
import top.dpdaidai.mn.aop.framework.Advised;
import top.dpdaidai.mn.aop.framework.AdvisedSupport;
import top.dpdaidai.mn.aop.framework.CglibProxyFactory;
import top.dpdaidai.mn.service.tx.TransactionManager;
import top.dpdaidai.mn.service.util.MessageTracker;
import top.dpdaidai.mn.service.v5.PetStoreService;

import java.util.List;

/**
 * @Author chenpantao
 * @Date 10/1/21 10:12 PM
 * @Version 1.0
 */
public class CglibAopProxyTest {

    private static AspectExpressionPointcut pointcut = null;
    private static AspectJBeforeAdvice beforeAdvice = null;
    private static AspectJAfterReturningAdvice aspectJAfterReturningAdvice = null;
    private static TransactionManager transactionManager = null;


    @Before
    public void setUp() throws NoSuchMethodException {
        String expression = "execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))";
        pointcut = new AspectExpressionPointcut();
        pointcut.setExpression(expression);

        transactionManager = new TransactionManager();

        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                transactionManager,
                pointcut);

        aspectJAfterReturningAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                transactionManager,
                pointcut);

    }

    @Test
    public void testGetProxy() {

        Advised advisedSupport = new AdvisedSupport();
        advisedSupport.addAdvice(beforeAdvice);
        advisedSupport.addAdvice(aspectJAfterReturningAdvice);
        advisedSupport.setTargetObject(new PetStoreService());

        CglibProxyFactory cglibProxyFactory = new CglibProxyFactory(advisedSupport);
        PetStoreService petStoreServiceProxy = (PetStoreService)cglibProxyFactory.getProxy();

        petStoreServiceProxy.placeOrder();
        petStoreServiceProxy.toString();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));



    }

}
