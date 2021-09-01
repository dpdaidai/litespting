package top.dpdaidai.mn.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.dpdaidai.mn.beans.exception.BeanCreationException;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.BeanFactory;
import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;
import top.dpdaidai.mn.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chenpantao
 * @Date 9/1/21 2:40 PM
 * @Version 1.0
 */
public class DefaultBeanFactory implements BeanFactory {

    public static final String ID_ATTRIBUTE = "id";
    public static final String CLASS_ATTRIBUTE = "class";

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    public DefaultBeanFactory(String configFile) {
        loadBeanDefinition(configFile);
    }

    private void loadBeanDefinition(String configFile) {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        InputStream is = defaultClassLoader.getResourceAsStream(configFile);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(is);
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while (iterator.hasNext()) {
                Element next = (Element) iterator.next();
                String id = next.attributeValue(ID_ATTRIBUTE);
                String className = next.attributeValue(CLASS_ATTRIBUTE);
                GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition(id, className);
                this.beanDefinitionMap.put(id, genericBeanDefinition);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + configFile, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    public Object getBean(String beanID) {

        BeanDefinition beanDefinition = this.getBeanDefinition(beanID);
        if (beanDefinition == null) {
            //TODO
        }
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> aClass = defaultClassLoader.loadClass(beanClassName);
            return aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
        }

    }


}
