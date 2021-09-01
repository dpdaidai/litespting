package top.dpdaidai.mn.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;
import top.dpdaidai.mn.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @Author chenpantao
 * @Date 9/1/21 8:32 PM
 * @Version 1.0
 */
public class XmlBeanDefinitionReader {

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    BeanDefinitionRegistry registerBeanDefinition;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.registerBeanDefinition = beanDefinitionRegistry;
    }

    public void loadBeanDefinition(String configFile) {
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
                this.registerBeanDefinition.registerBeanDefinition(id, genericBeanDefinition);
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

}
