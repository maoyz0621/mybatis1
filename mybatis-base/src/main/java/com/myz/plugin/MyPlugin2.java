package com.myz.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * 多个插件，创建动态代理对象按照<plugins>配置顺序执行
 * 执行目标方法之后，按照逆序执行
 */
@Intercepts(
    {@Signature(type = StatementHandler.class ,
            method = "parameterize",
            args = Statement.class )})
public class MyPlugin2 implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MyPlugin2拦截的方法："+invocation.getMethod());

        // 编写插件

        // 执行目标方法
        Object proceed = invocation.proceed();
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        System.out.println("MyPlugin2将要包装的对象：" + target);
        // 包装目标对象,使用当前Interceptor
        Object wrap = Plugin.wrap(target, this);
        // 返回当前target创建的动态代理
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        // 将插件注册时的property属性设置进来
        System.out.println("MyPlugin2插件配置的信息" +properties );
    }
}
