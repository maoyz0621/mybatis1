/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.aop;

import com.myz.annotation.TargetDataSource;
import com.myz.constant.DynamicDataSourceGlobal;
import com.myz.util.HandleDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 切面设置
 * <p>
 * 一定要记得:<aop:aspectj-autoproxy proxy-target-class="true"/>
 *
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
@Aspect
@Order(0) // 优先执行
@Component
public class DataSourceAspect {

    private final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    // 定义在service包和所有子包里的任意类的任意方法的执行：
    @Pointcut(value = "execution(* com.myz.service..*.*(..))")
    public void joinPointMethod() {
    }

    @Before("joinPointMethod()")
    public void before(JoinPoint joinPoint) {
        logger.debug("============= before() ===========");
        // 目标对象
        Object target = joinPoint.getTarget();
        // 目标方法名(签名)
        String methodName = joinPoint.getSignature().getName();
        // 目标对象的接口
        Class<?>[] interfaces = target.getClass().getInterfaces();
        // 强转MethodSignature，获取method
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取方法参数
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (Class<?> anInterface : interfaces) {
            try {
                // 接口中的方法
                Method interfaceMethod = anInterface.getMethod(methodName, parameterTypes);
                if (interfaceMethod != null) {
                    // 方法有对应的注解
                    if (interfaceMethod.isAnnotationPresent(TargetDataSource.class)) {
                        // 获取该注解
                        TargetDataSource targetDataSource = interfaceMethod.getAnnotation(TargetDataSource.class);
                        // 放入ThreadLocal中
                        HandleDataSource.putDataSource(targetDataSource.value());
                    } else {
                        // 默认
                        HandleDataSource.putDataSource(DynamicDataSourceGlobal.READ);
                    }
                } else {
                    continue;
                }
            } catch (NoSuchMethodException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @After("joinPointMethod()")
    public void after(JoinPoint joinPoint) {
        logger.debug("============= after() ===========");
        // 移除缓存
        HandleDataSource.clearDataSource();
    }
}
