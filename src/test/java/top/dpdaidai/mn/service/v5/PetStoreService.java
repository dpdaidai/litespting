package top.dpdaidai.mn.service.v5;


import top.dpdaidai.mn.beans.factory.annotation.Autowired;
import top.dpdaidai.mn.service.daoV5.AccountDao;
import top.dpdaidai.mn.service.daoV5.ItemDao;
import top.dpdaidai.mn.service.util.MessageTracker;
import top.dpdaidai.mn.stereotype.Component;

@Component(value = "petStore")
public class PetStoreService {
    @Autowired
    AccountDao accountDao;
    @Autowired
    ItemDao itemDao;

    public PetStoreService() {

    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void placeOrder() {
        System.out.println("place order");
        MessageTracker.addMsg("place order");

    }
}
