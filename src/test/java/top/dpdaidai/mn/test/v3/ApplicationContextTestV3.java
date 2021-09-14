package top.dpdaidai.mn.test.v3;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.context.suport.ClassPathXmlApplicationContext;
import top.dpdaidai.mn.service.v3.PetStoreService;

/**
 * @Author chenpantao
 * @Date 9/14/21 4:16 PM
 * @Version 1.0
 */
public class ApplicationContextTestV3 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());
        Assert.assertEquals(1, petStore.getVersion());

    }

}
