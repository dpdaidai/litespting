package top.dpdaidai.mn.test.v5;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.aop.MethodMatcher;
import top.dpdaidai.mn.aop.aspect.AspectExpressionPointcut;
import top.dpdaidai.mn.service.v5.PetStoreService;

import java.lang.reflect.Method;

/**
 * @Author chenpantao
 * @Date 9/27/21 3:46 PM
 * @Version 1.0
 */
public class PointcutTest {

    @Test
    public void testPointcut() throws NoSuchMethodException {
        //表达式 : 包含(返回值 方法名(参数) )
        String expression = "execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))";

        AspectExpressionPointcut pointcut = new AspectExpressionPointcut();
        pointcut.setExpression(expression);

        MethodMatcher methodMatcher = pointcut.getMethodMatcher();

        {
            Class<?> targetClass = PetStoreService.class;

            Method placeOrder = targetClass.getMethod("placeOrder");
            Assert.assertTrue(methodMatcher.matches(placeOrder));

            Method getAccountDao = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(methodMatcher.matches(getAccountDao));

        }

    }


}
