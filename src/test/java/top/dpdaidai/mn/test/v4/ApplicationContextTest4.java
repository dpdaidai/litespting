package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.context.ApplicationContext;
import top.dpdaidai.mn.context.suport.ClassPathXmlApplicationContext;
import top.dpdaidai.mn.service.daoV4.AccountDao;
import top.dpdaidai.mn.service.v4.PetStoreService;

public class ApplicationContextTest4 {

	@Test
	public void testGetBeanProperty() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
		PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

		Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);
		Assert.assertNotNull(petStore.getItemDao());

	}
}
