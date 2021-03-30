package com.myz.base.plugin;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * 多个插件，创建动态代理对象按照<plugins>配置顺序执行
 * 执行目标方法之后，按照逆序执行
 */
@Intercepts(
        {@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)})
public class MyPlugin3 implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MyPlugin3拦截的方法：" + invocation.getMethod());

        // 编写插件

        // 执行目标方法
        Object proceed = invocation.proceed();
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        System.out.println("MyPlugin3将要包装的对象：" + target);

        if (target instanceof ResultSetHandler) {
            // 包装目标对象,使用当前Interceptor
            return Plugin.wrap(target, this);
        }
        // 返回当前target创建的动态代理
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // 将插件注册时的property属性设置进来
        System.out.println("MyPlugin3插件配置的信息" + properties);
    }
}
