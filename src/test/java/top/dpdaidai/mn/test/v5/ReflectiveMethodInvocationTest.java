package top.dpdaidai.mn.test.v5;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.aop.aspect.AspectJAfterReturningAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJBeforeAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJAfterThrowingAdvice;
import top.dpdaidai.mn.aop.framework.ReflectiveMethodInvocation;
import top.dpdaidai.mn.service.tx.TransactionManager;
import top.dpdaidai.mn.service.util.MessageTracker;
import top.dpdaidai.mn.service.v5.PetStoreService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author chenpantao
 * @Date 9/27/21 10:15 PM
 * @Version 1.0
 */
public class ReflectiveMethodInvocationTest {

    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterReturningAdvice = null;
    private AspectJAfterThrowingAdvice throwingAdvice = null;
    private PetStoreService petStoreService = null;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        petStoreService = new PetStoreService();
        transactionManager = new TransactionManager();
        MessageTracker.clearMsgs();

        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"), transactionManager, null);

        afterReturningAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"), transactionManager, null);

        throwingAdvice = new AspectJAfterThrowingAdvice(
                TransactionManager.class.getMethod("rollback"), transactionManager, null);
    }

    @Test
    public void testReflectiveMethodInvocation1() throws Throwable {
        ArrayList<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>();
        //不关注拦截器假如的顺序
        methodInterceptors.add(afterReturningAdvice);
        methodInterceptors.add(beforeAdvice);

        Method placeOrderMethod = PetStoreService.class.getMethod("placeOrder");

        ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(
                petStoreService,
                placeOrderMethod,
                new Object[0],
                methodInterceptors);

        reflectiveMethodInvocation.proceed();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testReflectiveMethodInvocation2() throws Throwable {
        ArrayList<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>();
        methodInterceptors.add(afterReturningAdvice);
        methodInterceptors.add(beforeAdvice);
        methodInterceptors.add(throwingAdvice);

        Method placeOrderWithExceptionMethod = PetStoreService.class.getMethod("placeOrderWithException");

        ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(
                petStoreService,
                placeOrderWithExceptionMethod,
                new Object[0],
                methodInterceptors
        );

        try {
            reflectiveMethodInvocation.proceed();
        } catch (Exception e) {
            //异常抛出前 , 已经做了rollback处理
            List<String> msgs = MessageTracker.getMsgs();
            Assert.assertEquals(2, msgs.size());
            Assert.assertEquals("start tx", msgs.get(0));
            Assert.assertEquals("rollback tx", msgs.get(1));

            e.printStackTrace();

            return;

        }

        Assert.fail("No Exception thrown");

    }

}