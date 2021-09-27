package top.dpdaidai.mn.aop.aspect;

import org.aspectj.weaver.tools.*;
import top.dpdaidai.mn.aop.MethodMatcher;
import top.dpdaidai.mn.aop.Pointcut;
import top.dpdaidai.mn.util.ClassUtils;
import top.dpdaidai.mn.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * 切点表达式
 * 实现了  根据字符串expression返回MethodMatcher的功能
 * 另外 也实现了 MethodMatcher 接口 , 可以将 字符串表达式 和 method 进行匹配
 *
 * @Author chenpantao
 * @Date 9/27/21 3:24 PM
 * @Version 1.0
 */
public class AspectExpressionPointcut implements MethodMatcher , Pointcut {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    //保存字符表达式
    private String expression;

    //将字符表达式解析后 , 保存在这里
    private PointcutExpression pointcutExpression;

    private ClassLoader pointcutClassLoader;

    public AspectExpressionPointcut() {

    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * 将字符串表达式 和 method 进行匹配 , 满足则返回true
     * @param method
     * @return
     */
    public boolean matches(Method method) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = getShadowMatch(method);
        if (shadowMatch.alwaysMatches()) {
            return true;
        }
        return false;
    }

    /**
     * 返回 方法匹配器 , 其实就是自己
     * @return
     */
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    public String getExpression() {
        return this.expression;
    }

    /**
     * 将参数方法 method 和 表达式 pointcutExpression 进行匹配 ,
     * 将匹配结果包装在返回值 ShadowMatch 中
     * @param method
     * @return
     */
    private ShadowMatch getShadowMatch(Method method) {
        ShadowMatch shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        return shadowMatch;
    }

    /**
     * 匹配前 , 检查是否缺少 字符串表达式 ,
     * 并初始化PointcutExpression
     */
    private void checkReadyToMatch() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (this.pointcutExpression == null) {
            this.pointcutClassLoader = ClassUtils.getDefaultClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
        }
    }

    /**
     * 根据字符串表达式 expression 解析得到  PointcutExpression
     * 返回值可以用于和 method 进行匹配
     *
     * @param classLoader
     * @return
     */
    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {

        //字符串表达式 解析器 , 将字符串解析为 pointcutExpression
        PointcutParser pointcutParser = PointcutParser
                        .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, classLoader);

        /*PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
		for (int i = 0; i < pointcutParameters.length; i++) {
			pointcutParameters[i] = parser.createPointcutParameter(
					this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
		}*/

        PointcutExpression pointcutExpression =
                pointcutParser.parsePointcutExpression(
                        replaceBooleanOperators(getExpression()), null, new PointcutParameter[0]);
        return pointcutExpression;

    }

    private String replaceBooleanOperators(String pcExpr) {
        String result = StringUtils.replace(pcExpr, " and ", " && ");
        result = StringUtils.replace(result, " or ", " || ");
        result = StringUtils.replace(result, " not ", " ! ");
        return result;
    }

}
