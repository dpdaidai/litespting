package top.dpdaidai.mn.beans.factory.annotation;

import top.dpdaidai.mn.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;

/**
 * 单个元素注入的抽象模版 .
 * 注入属性这个抽象方法 , 由子类各自实现 .
 * 包含构造器注入 , 属性输入 , 方法注入
 *
 * @Author chenpantao
 * @Date 9/23/21 2:20 PM
 * @Version 1.0
 */
public abstract class InjectionElement {

    protected Member member;

    protected AutowireCapableBeanFactory autowireCapableBeanFactory;

    InjectionElement(Member member, AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.member = member;
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }

    public abstract void inject(Object object);

}
