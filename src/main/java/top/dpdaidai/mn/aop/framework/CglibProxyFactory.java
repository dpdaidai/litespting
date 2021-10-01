package top.dpdaidai.mn.aop.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.proxy.*;
import top.dpdaidai.mn.aop.Advice;
import top.dpdaidai.mn.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 代理对象工厂 : 它接受AdvisedSupport , 并生成代理对象proxy
 * 生成过程getProxy() :
 *  1   使用cglib , 配置了需要继承的类targetClass
 *  2   设置了回调拦截器 数组Callback[] , 这里只生成了一个DynamicAdvisedInterceptor
 *  3   设置回调过滤器 , 这里只有一个ProxyCallbackFilter
 *
 * @Author chenpantao
 * @Date 9/29/21 8:07 PM
 * @Version 1.0
 */
public class CglibProxyFactory implements AopProxyFactory {

    // Constants for CGLIB callback array indices
    private static final int AOP_PROXY = 0;
    private static final int INVOKE_TARGET = 1;
    private static final int NO_OVERRIDE = 2;
    private static final int DISPATCH_TARGET = 3;
    private static final int DISPATCH_ADVISED = 4;
    private static final int INVOKE_EQUALS = 5;
    private static final int INVOKE_HASHCODE = 6;

    protected static final Log logger = LogFactory.getLog(CglibProxyFactory.class);

    protected final Advised advised;

    public CglibProxyFactory(Advised advised) throws AdvisedException {
        Assert.notNull(advised, "AdvisedSupport must not be null");
        if (advised.getAdvices().size() == 0) {
            throw new AdvisedException("No advisors and no TargetSource specified");
        }
        this.advised = advised;
    }

    public Object getProxy() {
        return getProxy(null);
    }

    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetClass());
        }
        try {
            Enhancer enhancer = new Enhancer();
            if (classLoader != null) {
                enhancer.setClassLoader(classLoader);
            }

            Class<?> targetClass = this.advised.getTargetClass();

            enhancer.setSuperclass(targetClass);

            Callback[] callbacks = this.getCallbacks(targetClass);

            enhancer.setCallbacks(callbacks);
            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised));

            return enhancer.create();

        } catch (CodeGenerationException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                    this.advised.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        } catch (IllegalArgumentException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                    this.advised.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        } catch (Exception ex) {
            // TargetSource.getTarget() failed
            throw new AopConfigException("Unexpected AOP exception", ex);
        }

    }

    /**
     * 简化行为 , 只返回了一个拦截器
     * @param targetClass
     * @return
     */
    private Callback[] getCallbacks(Class<?> targetClass) {

        DynamicAdvisedInterceptor aopInterceptor = new DynamicAdvisedInterceptor(this.advised);

        //Callback targetInterceptor = new StaticUnadvisedExposedInterceptor(this.advised.getTargetObject());

        //Callback targetDispatcher = new StaticDispatcher(this.advised.getTargetObject());

        Callback[] callbacks = new Callback[] {
                aopInterceptor,  // AOP_PROXY for normal advice
                /*targetInterceptor,  // INVOKE_TARGET invoke target without considering advice, if optimized
                new SerializableNoOp(),  // NO_OVERRIDE  no override for methods mapped to this
                targetDispatcher,        //DISPATCH_TARGET
                this.advisedDispatcher,  //DISPATCH_ADVISED
                new EqualsInterceptor(this.advised),
                new HashCodeInterceptor(this.advised)*/
        };

        return callbacks;

    }


    /**
     * 动态拦截器
     *
     *
     */
    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

        private final Advised advised;

        public DynamicAdvisedInterceptor(Advised advised) {
            this.advised = advised;
        }

        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object targetObject = this.advised.getTargetObject();
            List<Advice> adviceChain = this.advised.getAdvices(method);

            Object returnValue;
            // Check whether we only have one InvokerInterceptor: that is,
            // no real advice, but just reflective invocation of the target.
            if (adviceChain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                // We can skip creating a MethodInvocation: just invoke the target directly.
                // Note that the final invoker must be an InvokerInterceptor, so we know
                // it does nothing but a reflective operation on the target, and no hot
                // swapping or fancy proxying.
                returnValue = methodProxy.invoke(targetObject, args);
            } else {
                ArrayList<org.aopalliance.intercept.MethodInterceptor> methodInterceptors =
                        new ArrayList<org.aopalliance.intercept.MethodInterceptor>();

                methodInterceptors.addAll(adviceChain);

                ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(targetObject, method, args, methodInterceptors);
                returnValue = reflectiveMethodInvocation.proceed();
            }

            return returnValue;
        }

    }

    /**
     * CallbackFilter : 根据method 返回index , 决定使用CallbackList中的哪个
     * 这里做了简化 , 没有深入 . 永远调用决定使用CallbackList中的第0个 .
     *
     */
    private static class ProxyCallbackFilter implements CallbackFilter {

        private final Advised advised;

        public ProxyCallbackFilter(Advised advised) {
            this.advised = advised;
        }

        /**
         * 该方法在生成代理proxy.class之前调用 , 生成.class之后 , 就一定定好了哪个方法回调哪个MethodInterceptor
         * @param method
         * @return
         */
        public int accept(Method method) {
            return AOP_PROXY;
        }
    }

}

