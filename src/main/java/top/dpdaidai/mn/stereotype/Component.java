package top.dpdaidai.mn.stereotype;


import java.lang.annotation.*;

@Target(ElementType.TYPE)  //可用于 类、接口、枚举类
@Retention(RetentionPolicy.RUNTIME)  // 保留到运行时有效
@Documented  // 使用 javadoc 工具为类生成帮助文档时是否要保留其注解信息
public @interface Component {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String value() default "";

}
