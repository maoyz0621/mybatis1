/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.plugin;

import com.myz.constant.DynamicDataSourceGlobal;

import com.myz.util.HandleDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拦截器
 *
 * @author maoyz on 2018/7/2
 * @version: v1.0
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicPlugin implements Interceptor {
    protected static final Logger logger = LoggerFactory.getLogger(DynamicPlugin.class);
    /**
     * 空格\u0020
     */
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";
    /**
     * 存放动态资源
     */
    private static final Map<String, DynamicDataSourceGlobal> cacheMap = new ConcurrentHashMap<>();

    /**
     * 最后执行
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.debug("=========mybatis intercept() methodName: { " + invocation.getMethod() + " }==========");

        // 当前操作是否有事务
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        // 获取执行参数
        Object[] args = invocation.getArgs();
        for (Object arg : args) {
            logger.debug("=========== 参数 args: =======" + arg);
        }
        // 对应配置文件中的sql节点,select,update...
        MappedStatement mappedStatement = (MappedStatement) args[0];
        DynamicDataSourceGlobal dataSource = null;

        // 当前操作没有事务
        if (synchronizationActive) {
            // 节点ID如果不存在
            if ((dataSource = cacheMap.get(mappedStatement.getId())) == null) {
                // 1 select方法
                if (Objects.equals(mappedStatement.getSqlCommandType(), SqlCommandType.SELECT)) {
                    // todo selectKey为自增主键（SELECT LAST_INSERT_ID()）方法,使用主库
                    if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dataSource = DynamicDataSourceGlobal.WRITE;
                    } else {
                        // todo 执行的SQL语句
                        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replace("[\\t\\r\\n]", " ");
                        //如果是insert、delete、update操作 使用主库
                        if (sql.matches(REGEX)) {
                            dataSource = DynamicDataSourceGlobal.WRITE;
                        } else {
                            dataSource = DynamicDataSourceGlobal.READ;
                        }
                    }
                } else {
                    //todo
                    dataSource = DynamicDataSourceGlobal.WRITE;
                }
                // 放入map中
                cacheMap.put(mappedStatement.getId(), dataSource);
            }
            HandleDataSource.putDataSource(dataSource);
        }
        // 放行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        logger.debug("===========mybatis intercept plugin()=============: " + target);

        if (target instanceof Executor) {
            // 如果是Executor（执行增删改查操作），则拦截下来
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 优先执行
     */
    @Override
    public void setProperties(Properties properties) {
        logger.debug("===========mybatis intercept setProperties()=============");
    }
}
