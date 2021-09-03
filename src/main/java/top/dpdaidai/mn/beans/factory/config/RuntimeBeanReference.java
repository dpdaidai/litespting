package top.dpdaidai.mn.beans.factory.config;

/**
 * @Author chenpantao
 * @Date 9/3/21 11:43 AM
 * @Version 1.0
 */
public class RuntimeBeanReference {

    public String beanID ;

    public RuntimeBeanReference(String beanID) {
        this.beanID = beanID;
    }

    public String getBeanID() {
        return beanID;
    }

}
