/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.mybatis.plugin;

import com.myz.mybatis.config.AbstractDynamicDataSourceProxy;
import com.myz.mybatis.config.ConnectionProxy;
import com.myz.mybatis.util.ReflectionUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * 拦截器
 *
 * @author maoyz on 2018/7/2
 * @version: v1.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = Connection.class)})
public class DynamicPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Connection connection = (Connection) invocation.getArgs()[0];

        if (connection instanceof ConnectionProxy) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

            MappedStatement mappedStatement = null;
            if (statementHandler instanceof RoutingStatementHandler) {
                StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
                mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, "mappedStatement");
            } else {
                mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(statementHandler, "mappedStatement");
            }

            String key = AbstractDynamicDataSourceProxy.WRITE;

            if (mappedStatement.getSqlCommandType() == SqlCommandType.SELECT) {
                key = AbstractDynamicDataSourceProxy.READ;
            } else {
                key = AbstractDynamicDataSourceProxy.WRITE;
            }

            ConnectionProxy connectionProxy = (ConnectionProxy) connection;
            connectionProxy.getTargetConnection(key);
        }
        // 放行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
