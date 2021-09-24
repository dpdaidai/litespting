package top.dpdaidai.mn.test.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.context.suport.ClassPathXmlApplicationContext;
import top.dpdaidai.mn.service.daoV5.AccountDao;
import top.dpdaidai.mn.service.util.MessageTracker;
import top.dpdaidai.mn.service.v5.PetStoreService;

import java.util.List;

/**
 *
 * 测试aop功能
 *
 * @Author chenpantao
 * @Date 9/24/21 3:06 PM
 * @Version 1.0
 */
public class ApplicationContextTest5 {

    @Before
    public void setUp() {
        MessageTracker.clearMsgs();
    }

    @Test
    public void testPlaceOrder() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v5.xml");
        PetStoreService petStoreService = (PetStoreService) applicationContext.getBean("petStore");

        Assert.assertNotNull(petStoreService.getItemDao());
        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);

        petStoreService.placeOrder();

        List<String> msgs = MessageTracker.getMsgs();

        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }


}
