package top.dpdaidai.mn.test.v2;

import org.junit.Test;
import top.dpdaidai.mn.service.v2.PetStoreService;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * 实例化bean时用到了Introspector , BeanInfo 两个类 , 现在测试下它们常用的功能
 *
 * @Author chenpantao
 * @Date 9/13/21 11:51 AM
 * @Version 1.0
 */
public class IntrospectorTest {


    /**
     * 为petStoreService设置owner属性
     */
    @Test
    public void testPropertyDescriptor() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        PetStoreService petStoreService = new PetStoreService();
        //获取owner属性的描述
        PropertyDescriptor propDesc = new PropertyDescriptor("owner", PetStoreService.class);
        //获取设置owner的方法setOwner
        Method methodSetUserName = propDesc.getWriteMethod();
        //通过反射设置owner属性
        methodSetUserName.invoke(petStoreService, "cpt");
        System.out.println("set owner : " + petStoreService.getOwner());

        Method readMethod = propDesc.getReadMethod();

        String invoke = (String) readMethod.invoke(petStoreService);

        System.out.println("read owner : " + invoke);
    }

    @Test
    public void testsetPropertyByIntrospector() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        PetStoreService petStoreService = new PetStoreService();

        BeanInfo beanInfo = Introspector.getBeanInfo(PetStoreService.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals("owner")) {

                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(petStoreService, "cpt");
                System.out.println("set owner : " + petStoreService.getOwner());

                Method readMethod = propertyDescriptor.getReadMethod();

                String invoke = (String) readMethod.invoke(petStoreService);

                System.out.println("read owner : " + invoke);


            }
        }

    }

}
