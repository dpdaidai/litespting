package top.dpdaidai.mn.service.v4;


import top.dpdaidai.mn.beans.factory.annotation.Autowired;
import top.dpdaidai.mn.service.daoV4.AccountDao;
import top.dpdaidai.mn.service.daoV4.ItemDao;
import top.dpdaidai.mn.stereotype.Component;

@Component(value = "petStore")
public class PetStoreService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }


}
