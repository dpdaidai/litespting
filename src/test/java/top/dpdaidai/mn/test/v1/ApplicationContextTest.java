package top.dpdaidai.mn.test.v1;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.beans.factory.PetStoreService;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.context.ClassPathXmlApplicationContext;

/**
 * @Author chenpantao
 * @Date 9/1/21 9:04 PM
 * @Version 1.0
 */
public class ApplicationContextTest {

    @Test
    public void testGetBean() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)applicationContext.getBean("petStore");
        Assert.assertNotNull(petStore);
    }

}
