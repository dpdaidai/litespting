package top.dpdaidai.mn.service.v2;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:33 PM
 * @Version 1.0
 */
public class PetStoreService {

    private AccountDao accountDao;
    private ItemDao itemDao;

    private Integer maxNumber;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

}