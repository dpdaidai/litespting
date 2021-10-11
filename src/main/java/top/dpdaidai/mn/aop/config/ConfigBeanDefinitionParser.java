package top.dpdaidai.mn.aop.config;

import org.dom4j.Element;
import top.dpdaidai.mn.aop.aspect.AspectExpressionPointcut;
import top.dpdaidai.mn.aop.aspect.AspectJAfterReturningAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJAfterThrowingAdvice;
import top.dpdaidai.mn.aop.aspect.AspectJBeforeAdvice;
import top.dpdaidai.mn.beans.ConstructorArgument;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.GenericBeanDefinition;
import top.dpdaidai.mn.beans.factory.PropertyValue;
import top.dpdaidai.mn.beans.factory.config.RuntimeBeanReference;
import top.dpdaidai.mn.beans.factory.support.BeanDefinitionReaderUtils;
import top.dpdaidai.mn.beans.factory.support.BeanDefinitionRegistry;
import top.dpdaidai.mn.util.StringUtils;

import java.util.List;

/**
 *
 * 主要解析<aop:config>下的xml元素
 *
 * @Author chenpantao
 * @Date 10/2/21 11:29 PM
 * @Version 1.0
 */
public class ConfigBeanDefinitionParser {

    private static final String ASPECT = "aspect";
    private static final String EXPRESSION = "expression";
    private static final String ID = "id";
    private static final String POINTCUT = "pointcut";
    private static final String POINTCUT_REF = "pointcut-ref";
    private static final String REF = "ref";
    private static final String BEFORE = "before";
    private static final String AFTER = "after";
    private static final String AFTER_RETURNING_ELEMENT = "after-returning";
    private static final String AFTER_THROWING_ELEMENT = "after-throwing";
    private static final String AROUND = "around";
    private static final String ASPECT_NAME_PROPERTY = "aspectName";

    /**
     *
     * 解析 aop:config element
     * @param element
     *    <aop:config>
     *         <aop:aspect ref="transactionManager">
     *             <aop:pointcut id="placeOrder" expression="execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))"/>
     *             <aop:before pointcut-ref="placeOrder" method="start"/>
     *             <aop:after-returning pointcut-ref="placeOrder" method="commit"/>
     *             <aop:after-throwing pointcut-ref="placeOrder" method="rollback"/>
     *         </aop:aspect>
     *     </aop:config>
     * @param registry
     * @return
     */
    public BeanDefinition parse(Element element, BeanDefinitionRegistry registry) {
        List<Element> childElements = element.elements();
        for (Element childElement : childElements) {
            String localName = childElement.getName();
            /*if (POINTCUT.equals(localName)) {
				parsePointcut(elt, registry);
			}*/
			/*else if (ADVISOR.equals(localName)) {
				parseAdvisor(elt, registry);
			}*/
            if (ASPECT.equals(localName)) {
                parseAspect(childElement, registry);
            }
        }

        return null;
    }

    /**
     *  解析 aop:aspect element
     * @param aspectElement
     *         <aop:aspect ref="transactionManager">
     *             <aop:pointcut id="placeOrder" expression="execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))"/>
     *             <aop:before pointcut-ref="placeOrder" method="start"/>
     *             <aop:after-returning pointcut-ref="placeOrder" method="commit"/>
     *             <aop:after-throwing pointcut-ref="placeOrder" method="rollback"/>
     *         </aop:aspect>
     * @param registry
     */
    private void parseAspect(Element aspectElement, BeanDefinitionRegistry registry) {
        String aspectId = aspectElement.attributeValue(ID);

        //transactionManager
        String aspectRefName = aspectElement.attributeValue(REF);

        List<Element> elements = aspectElement.elements();

        //是否找到了advice , 只会出现一次
        boolean adviceFoundReady = false;

        //解析 before , after-returning , after-throwing
        for (Element adviceElement : elements) {

            //判断是否是advice
            if (isAdviceNode(adviceElement)) {
                parseAdvice(aspectRefName, adviceElement, registry);
            }
        }

        //解析 pointcut
        List<Element> pointcutElementList = aspectElement.elements(POINTCUT);
        for (Element element : pointcutElementList) {
            parsePointcut(element, registry);
        }

    }

    /**
     * 解析 advice 元素 ,   不包括pointcut元素
     *
     * @param aspectName  切面名称 , ref的值,   <aop:aspect ref="transactionManager">
     * @param adviceElement
     *             <aop:before pointcut-ref="placeOrder" method="start"/>
     *             <aop:after-returning pointcut-ref="placeOrder" method="commit"/>
     *             <aop:after-throwing pointcut-ref="placeOrder" method="rollback"/>
     * @param registry
     * @return
     */
    private GenericBeanDefinition parseAdvice(String aspectName, Element adviceElement, BeanDefinitionRegistry registry) {

        // 第一个参数 : 用来获取根据aspectName和methodName 反射获取 aspectMethod (例如start()方法)
        GenericBeanDefinition methodBeanDefinition = new GenericBeanDefinition(MethodLocatingFactory.class);

        //合成beanDefinition的标志
        methodBeanDefinition.setSynthetic(true);

        List<PropertyValue> propertyValues = methodBeanDefinition.getPropertyValues();
        propertyValues.add(new PropertyValue("targetBeanName", aspectName));
        propertyValues.add(new PropertyValue("methodName", adviceElement.attributeValue("method")));

        //第二个参数 : AspectInstanceFactory 用来从beanFactory中 , 根据aspectName 获取 aspect实例transactionManager
        GenericBeanDefinition aspectFactoryDef = new GenericBeanDefinition(AspectInstanceFactory.class);
        aspectFactoryDef.getPropertyValues().add(new PropertyValue("aspectBeanName", aspectName));
        aspectFactoryDef.setSynthetic(true);

        //创建adviceDefinition
        GenericBeanDefinition adviceDefinition = createAdviceDefinition(adviceElement, aspectName, methodBeanDefinition, aspectFactoryDef);
        adviceDefinition.setSynthetic(true);

        //注册adviceDefinition
        String generateBeanName = BeanDefinitionReaderUtils.generateBeanName(adviceDefinition, registry);
        registry.registerBeanDefinition(generateBeanName, adviceDefinition);

        return adviceDefinition;

    }

    /**
     * 生成adviceBeanDefinition
     *      1  设置PropertyValue的aspectName
     *      2  设置构造器
     *          1  切面方法的定义 methodDefBeanDefinition
     *          2  切面表达式 pointcutProperty
     *          3  切面实例生成工厂的定义 aspectFactoryDefBeanDefinition
     *
     * @param adviceElement
     * @param aspectName
     * @param methodDefBeanDefinition
     * @param aspectFactoryDefBeanDefinition
     * @return
     */
    private GenericBeanDefinition createAdviceDefinition(Element adviceElement, String aspectName,
                                                         GenericBeanDefinition methodDefBeanDefinition,
                                                         GenericBeanDefinition aspectFactoryDefBeanDefinition) {
        //根据标签判断创建哪个 advice 的 beanDefinition
        Class<?> adviceClass = getAdviceClass(adviceElement);
        GenericBeanDefinition adviceBeanDefinition = new GenericBeanDefinition(adviceClass);

        //设置adviceBeanDefinition的propertyValue , 这个似乎没用 , 暂时注释掉
//        List<PropertyValue> propertyValues = adviceBeanDefinition.getPropertyValues();
//        propertyValues.add(new PropertyValue(ASPECT_NAME_PROPERTY, aspectName));

        //获取adviceBeanDefinition的构造器
        ConstructorArgument constructorArgument = adviceBeanDefinition.getConstructorArgument();

        //第一个参数 : advice 的切面方法
        constructorArgument.addArgumentValue(methodDefBeanDefinition);

        //第二个参数 : AspectInstanceFactory , 通过 beanFactory , 根据beanDefinition取出 aspect 实例
        constructorArgument.addArgumentValue(aspectFactoryDefBeanDefinition);

        //第三个参数 : 切面表达式
        Object pointcutProperty = parsePointcutProperty(adviceElement);
        constructorArgument.addArgumentValue(pointcutProperty);

        return adviceBeanDefinition;
    }

    /**
     * 根据advice解析 这个拦截器作用在什么范围上
     * 1  如果pointcut-ref 有值 , 那么它的作用范围在pointcut-ref关联的表达式对象上 ,
     *      需要从beanFactory中根据beanID取到关联对象AspectExpressionPointcut
     *      <aop:pointcut id="placeOrder" expression="execution(* top.dpdaidai.mn.service.v5.*.placeOrder(..))"/>
     *
     * 2  如果是pointcut有值 , 那么这个拦截器作为范围为自定义的表达式上 ,
     *      需要根据表达式自建AspectExpressionPointcut 的 beanDefinition,
     *      在初始化bean时 , 会根据beanDefinition创建AspectExpressionPointcut
     *
     * @param adviceElement
     *             <aop:before pointcut-ref="placeOrder" method="start"/>
     *             <aop:after-returning pointcut-ref="placeOrder" method="commit"/>
     *             <aop:after-throwing pointcut-ref="placeOrder" method="rollback"/>
     */
    private Object parsePointcutProperty(Element adviceElement) {

        String pointcutExpression = adviceElement.attributeValue(POINTCUT);
        String pointcutRef = adviceElement.attributeValue(POINTCUT_REF);

        //根据pointcutRef 或者 pointcutExpression 选择不同的 pointcutExpression 的创建方式
        if (pointcutExpression == null && pointcutRef == null) {
            return null;
        } else if (pointcutExpression != null) {
            return createPointcutDefinition(pointcutExpression);
        } else if (pointcutRef != null) {
            //判断pointcutRef是不是空
            if (!StringUtils.hasText(pointcutRef)) {
                return null;
            }
            return new RuntimeBeanReference(pointcutRef);
        } else {
            return null;
        }
    }

    /**
     * 判断element元素是不是advice
     *
     * @param element
     * @return
     */
    private boolean isAdviceNode(Element element) {
        String name = element.getName();
        return (BEFORE.equals(name) || AFTER.equals(name) || AFTER_RETURNING_ELEMENT.equals(name) ||
                AFTER_THROWING_ELEMENT.equals(name) || AROUND.equals(name));
    }

    /**
     * 根据element的名字选择adivce
     * @param adviceElement
     * @return
     */
    private Class<?> getAdviceClass(Element adviceElement) {
        String elementName = adviceElement.getName();
        if (BEFORE.equals(elementName)) {
            return AspectJBeforeAdvice.class;
        } else if (AFTER_RETURNING_ELEMENT.equals(elementName)) {
            return AspectJAfterReturningAdvice.class;
        } else if (AFTER_THROWING_ELEMENT.equals(elementName)) {
            return AspectJAfterThrowingAdvice.class;
        } else {
            throw new IllegalArgumentException("Unknown advice kind [" + elementName + "]");
        }
    }

    /**
     *
     * 解析pointcut , 并注册为合成的 beanDefinition
     * @param pointcutElement  <aop:pointcut id="placeOrder" expression="execution(* org.litespring.service.v5.*.placeOrder(..))" />
     * @param registry
     * @return
     */
    private GenericBeanDefinition parsePointcut(Element pointcutElement, BeanDefinitionRegistry registry) {
        String id = pointcutElement.attributeValue(ID);
        String expression = pointcutElement.attributeValue(EXPRESSION);

        //pointcutDefinition 只保存了字符串"execution(* org.litespring.service.v5.*.placeOrder(..))"
        GenericBeanDefinition pointcutDefinition = createPointcutDefinition(expression);

        //判断是否有默认的beanId是否为空
        if (StringUtils.hasText(id)) {
            registry.registerBeanDefinition(id, pointcutDefinition);
        } else {
            //生成beanId 并注册
            String generateBeanName = BeanDefinitionReaderUtils.generateBeanName(pointcutDefinition, registry);
            registry.registerBeanDefinition(generateBeanName, pointcutDefinition);
        }

        return pointcutDefinition;

    }


    protected GenericBeanDefinition createPointcutDefinition(String expression) {
        GenericBeanDefinition pointcutBeanDefinition = new GenericBeanDefinition(AspectExpressionPointcut.class);
        pointcutBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        pointcutBeanDefinition.setSynthetic(true);
        pointcutBeanDefinition.getPropertyValues().add(new PropertyValue(EXPRESSION, expression));
        return pointcutBeanDefinition;
    }

}
