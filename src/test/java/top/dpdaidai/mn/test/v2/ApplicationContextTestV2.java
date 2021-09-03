package top.dpdaidai.mn.test.v2;

import org.junit.Test;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.context.suport.ClassPathXmlApplicationContext;
import top.dpdaidai.mn.service.v2.AccountDao;
import top.dpdaidai.mn.service.v2.ItemDao;
import top.dpdaidai.mn.service.v2.PetStoreService;

import static org.junit.Assert.*;

/**
 * @Author chenpantao
 * @Date 9/3/21 6:25 PM
 * @Version 1.0
 */
public class ApplicationContextTestV2 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStore = (PetStoreService) applicationContext.getBean("petStore");

        assertNotNull(petStore.getAccountDao());
        assertNotNull(petStore.getItemDao());
        assertTrue(petStore.getAccountDao() instanceof AccountDao);
        assertTrue(petStore.getItemDao() instanceof ItemDao);

        assertEquals("cpt", petStore.getOwner());


    }


}
