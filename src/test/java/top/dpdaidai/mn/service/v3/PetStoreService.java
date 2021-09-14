package top.dpdaidai.mn.service.v3;

import top.dpdaidai.mn.service.v2.AccountDao;
import top.dpdaidai.mn.service.v2.ItemDao;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:33 PM
 * @Version 1.0
 */
public class PetStoreService {

    private AccountDao accountDao;

    private ItemDao itemDao;

    private int version;

    public PetStoreService(AccountDao accountDao, ItemDao itemDao){
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.version = -1;
    }
    public PetStoreService(AccountDao accountDao, ItemDao itemDao,int version){
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.version = version;
    }
    public int getVersion() {
        return version;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

}
