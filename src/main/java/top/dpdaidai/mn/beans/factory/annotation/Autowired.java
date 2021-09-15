package top.dpdaidai.mn.beans.factory.annotation;

import java.lang.annotation.*;

//可用于构造方法 , 成员变量 , 成员方法 , 注解类
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME) //保留到运行时有效
@Documented
public @interface Autowired {

    /**
     * Declares whether the annotated dependency is required.
     * <p>Defaults to {@code true}.
     */
    boolean required() default true;

}
