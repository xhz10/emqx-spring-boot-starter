package com.xhz.emqxspringbootstarter.config.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/09
 */
public class EmqxCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //1、能获取到ioc使用的beanfactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //2、获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        //3、获取当前环境信息
        Environment environment = context.getEnvironment();
        String isOpen = environment.getProperty("emqx.basic.isOpen");
        return Boolean.valueOf(isOpen);
    }
}

