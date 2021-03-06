package top.dpdaidai.mn.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.dpdaidai.mn.aop.config.ConfigBeanDefinitionParser;
import top.dpdaidai.mn.beans.ConstructorArgument;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;
import top.dpdaidai.mn.beans.factory.PropertyValue;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.config.TypedStringValue;
import top.dpdaidai.mn.context.annotation.ClassPathBeanDefinitionScanner;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.util.StringUtils;

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

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    public static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/aop";

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    BeanDefinitionRegistry registerBeanDefinition;

    protected final Log logger = LogFactory.getLog(getClass());

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.registerBeanDefinition = beanDefinitionRegistry;
    }

    public void loadBeanDefinitions(Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator();
            while (iterator.hasNext()) {
                Element childElement = iterator.next();
                String namespaceUri = childElement.getNamespaceURI();

                //?????????????????????????????? , ??????????????????????????????
                if (this.isDefaultNamespace(namespaceUri)) {
                    //?????????bean
                    //<bean id="transactionManager" class="top.dpdaidai.mn.service.tx.TransactionManager"/>
                    parseDefaultElement(childElement);
                } else if (this.isContextNamespace(namespaceUri)) {
                    //???????????????????????????????????????bean , ???????????????
                    //<context:component-scan base-package="top.dpdaidai.mn.service.v5,top.dpdaidai.mn.service.daoV5" />
                    parseComponentElement(childElement); //??????<context:component-scan>
                } else if (this.isAOPNamespace(namespaceUri)) {
                    // ??????aop??????????????? , ???????????????????????? , ???????????????????????????????????????
                    // <aop:config>
                    //        <aop:aspect ref="transactionManager">
                    //            <aop:pointcut id="placeOrder" expression="execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))"/>
                    //            <aop:before pointcut-ref="placeOrder" method="start"/>
                    //            <aop:after-returning pointcut-ref="placeOrder" method="commit"/>
                    //            <aop:after-throwing pointcut-ref="placeOrder" method="rollback"/>
                    //        </aop:aspect>
                    // </aop:config>
                    parseAOPElement(childElement);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
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

    /**
     * ??????xml?????????bean
     *
     * @param element <bean id="itemDao" class="top.dpdaidai.mn.service.v2.ItemDao"/>
     */
    private void parseDefaultElement(Element beanElement) {
        String id = beanElement.attributeValue(ID_ATTRIBUTE);
        String className = beanElement.attributeValue(CLASS_ATTRIBUTE);
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition(id, className);
        String scope = beanElement.attributeValue(BeanDefinition.SCOPE);
        if (scope != null) {
            genericBeanDefinition.setScope(scope);
        }
        parseConstructorArgElements(beanElement, genericBeanDefinition);
        parseBeanElement(beanElement, genericBeanDefinition);
        this.registerBeanDefinition.registerBeanDefinition(id, genericBeanDefinition);

    }

    /**
     * ??????context:component-scan???????????????????????????bean
     *
     * @param componentElement
     */
    private void parseComponentElement(Element componentElement) {
        String basePackage = componentElement.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner =
                new ClassPathBeanDefinitionScanner(registerBeanDefinition);
        classPathBeanDefinitionScanner.doScan(basePackage);
    }

    private void parseAOPElement(Element aopElement) {
        ConfigBeanDefinitionParser configBeanDefinitionParser = new ConfigBeanDefinitionParser();
        configBeanDefinitionParser.parse(aopElement, this.registerBeanDefinition);
    }

    //??????element?????????
    //BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans" ????????????bean??????
    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    //CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context" ???????????????
    public boolean isContextNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isAOPNamespace(String namespaceUri){
        return (!StringUtils.hasLength(namespaceUri) || AOP_NAMESPACE_URI.equals(namespaceUri));
    }


    /**
     * ??????xml ?????????????????????
     *
     * @param beanElement     <bean id="petStore" class="top.dpdaidai.mn.service.v3.PetStoreService">
     *                              <constructor-arg  ref="accountDao"/>
     *                              <constructor-arg  ref="itemDao"/>
     *                              <constructor-arg  value="1"/>
     *                        </bean>
     * @param beanDefinition
     */
    public void parseConstructorArgElements(Element beanElement, BeanDefinition beanDefinition) {
        Iterator iterator = beanElement.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iterator.hasNext()) {
            Element argElement = (Element) iterator.next();
            parseConstructorArgElement(argElement, beanDefinition);
        }

    }

    /**
     * ????????????????????????
     *
     * @param argElement   <constructor-arg  ref="accountDao"/>
     * @param beanDefinition
     */
    public void parseConstructorArgElement(Element argElement, BeanDefinition beanDefinition) {
        //?????????????????????
        String type = argElement.attributeValue(TYPE_ATTRIBUTE);
        String name = argElement.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyElement(argElement, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value, type, name);
        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder);
    }

    /**
     * ??????beanElement????????????property??????
     *
     * @param beanElement
     * @param beanDefinition
     */
    public void parseBeanElement(Element beanElement, BeanDefinition beanDefinition) {
        Iterator iterator = beanElement.elementIterator(PROPERTY_ELEMENT);
        while (iterator.hasNext()) {
            Element propertyElement = (Element) iterator.next();
            String propertyName = propertyElement.attributeValue(NAME_ATTRIBUTE);

            if (!StringUtils.hasLength(propertyName)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                continue;
            }

            Object propertyValue = parsePropertyElement(propertyElement, propertyName);

            PropertyValue property = new PropertyValue(propertyName, propertyValue);

            beanDefinition.getPropertyValues().add(property);
        }
    }

    /**
     *
     * ?????????????????????
     *
     * @param propertyElement  <constructor-arg  ref="accountDao"/>
     *                         <constructor-arg  ref="itemDao"/>
     *                         <constructor-arg  value="1"/>
     * @param propertyName
     * @return
     */
    public Object parsePropertyElement(Element propertyElement, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        //??????property???value??????
        boolean hasRefAttribute = propertyElement.attribute(REF_ATTRIBUTE) != null;
        boolean hasValueAttribute = propertyElement.attribute(VALUE_ATTRIBUTE) != null;

        if (hasRefAttribute) {
            //?????????????????????RuntimeBeanReference , ???????????????????????????bean??? , ????????????refName???beanFactory?????????
            String refName = propertyElement.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                logger.error(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        } else if (hasValueAttribute) {

            //????????????????????????????????? ????????? TypedStringValue
            //???????????????????????????bean??? , ??????????????????????????????????????? , ???????????????????????????
            TypedStringValue valueHolder = new TypedStringValue(propertyElement.attributeValue(VALUE_ATTRIBUTE));

            return valueHolder;
        } else {

            throw new RuntimeException(elementName + " must specify a ref or value");
        }


    }

}
