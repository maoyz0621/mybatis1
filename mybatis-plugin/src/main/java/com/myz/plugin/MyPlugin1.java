package com.myz.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * 编写插件
 * 1 实现Interceptor接口
 * 2 使用@Intercepts完成插件签名(四大组件)
 *     哪个插件　哪个方法　哪个参数
 * 3 注册到全局配置文件中
 */
@Intercepts(
    {@Signature(type = StatementHandler.class ,
            method = "parameterize",
            args = Statement.class )})
public class MyPlugin1 implements Interceptor {

    /**
     * 拦截目标对象的目标方法执行
     * invocation.proceed() 放行
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MyPlugin1拦截的方法："+invocation.getMethod());
        // 执行目标方法
        Object proceed = invocation.proceed();
        // 返回执行后的返回值
        return proceed;
    }

    /**
     * 插件
     * 包装目标对象，为其创建一个代理对象
     *     Plugin.wrap(Object target, Interceptor interceptor)
     */
    @Override
    public Object plugin(Object target) {
        System.out.println("MyPlugin1将要包装的对象：" + target);
        // 包装目标对象,使用当前Interceptor
        Object wrap = Plugin.wrap(target, this);
        // 返回当前target创建的动态代理
        return wrap;
    }

    /**
     * 将插件注册时的property的设置进来
     */
    @Override
    public void setProperties(Properties properties) {
        // 将插件注册时的property属性设置进来
        System.out.println("MyPlugin1插件配置的信息" +properties);
    }
}
