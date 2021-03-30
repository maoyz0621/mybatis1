/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.mybatis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maoyz on 2018/7/2
 * @version: v1.0
 */
public abstract class AbstractDynamicDataSourceProxy extends AbstractRoutingDataSource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     *
     */
    private List<Object> readDataSources;

    /**
     *
     */
    private List<DataSource> resolvedReadDataSources;

    private Object writeDataSource;

    private DataSource resolvesWriteDataSource;

    /**
     * 获取读数据源方式，0：随机，1：轮询
     */
    private int readDataSourcePollPattern = 0;

    /**
     * 读数据源个数
     */
    private int readDataSourceSize;

    /**
     * 默然事务隔离级别
     */
    private int defaultTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
    private boolean defaultAutoCommit = true;

    private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();


    public static final String READ = "read";
    public static final String WRITE = "write";

    /**
     * 前置处理器
     */
    @Override
    public void afterPropertiesSet() {
        logger.debug("============== afterPropertiesSet() ==============");
        if (writeDataSource == null) {
            throw new IllegalArgumentException("writeDataSource不能为null");
        }

        this.resolvesWriteDataSource = resolveSpecifiedDataSource(writeDataSource);

        resolvedReadDataSources = new ArrayList<DataSource>(readDataSources.size());

        for (Object readDataSource : readDataSources) {
            resolvedReadDataSources.add(resolveSpecifiedDataSource(readDataSource));
        }
        readDataSourceSize = readDataSources.size();
    }

    /**
     * 使用动态代理
     */
    @Override
    public Connection getConnection() throws SQLException {
        logger.debug("============== getConnection() ==============");
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(),
                new Class[]{ConnectionProxy.class},
                new WriteAndReadConnectionInvocationHandler());
    }

    /**
     * @param username
     * @param password
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(),
                new Class[]{ConnectionProxy.class},
                new WriteAndReadConnectionInvocationHandler(username, password));
    }

    /**
     * 根据key选择数据源
     */
    protected DataSource determineTargetDataSource(String key) {
        logger.debug("============== determineTargetDataSource() ==============");
        Assert.notNull(readDataSources, "DataSource router not initialized");
        if (WRITE.equals(key)) {
            return resolvesWriteDataSource;
        } else {
            return loadReadDataSource();
        }
    }

    /**
     * 获取真实的data source
     */
    @Override
    protected DataSource resolveSpecifiedDataSource(Object dataSource) {
        logger.debug("============== resolveSpecifiedDataSource() ==============");
        if (dataSource instanceof DataSource) {
            return (DataSource) dataSource;
        } else if (dataSource instanceof String) {
            return dataSourceLookup.getDataSource((String) dataSource);
        } else {
            throw new IllegalArgumentException("datasource error");
        }
    }

    /**
     * 获取读数据源
     *
     * @return
     */
    protected abstract DataSource loadReadDataSource();

    public List<Object> getReadDataSources() {
        return readDataSources;
    }

    public void setReadDataSources(List<Object> readDataSources) {
        this.readDataSources = readDataSources;
    }

    public List<DataSource> getResolvedReadDataSources() {
        return resolvedReadDataSources;
    }

    public void setResolvedReadDataSources(List<DataSource> resolvedReadDataSources) {
        this.resolvedReadDataSources = resolvedReadDataSources;
    }

    public Object getWriteDataSource() {
        return writeDataSource;
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public DataSource getResolvesWriteDataSource() {
        return resolvesWriteDataSource;
    }

    public void setResolvesWriteDataSource(DataSource resolvesWriteDataSource) {
        this.resolvesWriteDataSource = resolvesWriteDataSource;
    }

    public int getReadDataSourceSize() {
        return readDataSourceSize;
    }

    public void setReadDataSourceSize(int readDataSourceSize) {
        this.readDataSourceSize = readDataSourceSize;
    }

    public int getReadDataSourcePollPattern() {
        return readDataSourcePollPattern;
    }

    public void setReadDataSourcePollPattern(int readDataSourcePollPattern) {
        this.readDataSourcePollPattern = readDataSourcePollPattern;
    }


    /**
     * 定义一个代理对象内部类
     */
    private class WriteAndReadConnectionInvocationHandler implements InvocationHandler {

        private String username;
        private String password;
        private Connection target;
        /**
         * 只读
         */
        private Boolean readOnly = Boolean.FALSE;
        /**
         * 事务隔离级别
         */
        private Integer transactionIsolation;
        /**
         * 是否关闭
         */
        private Boolean closed = Boolean.FALSE;
        /**
         * 自动提交
         */
        private Boolean autoCommit;


        public WriteAndReadConnectionInvocationHandler() {
        }

        public WriteAndReadConnectionInvocationHandler(String username, String password) {
            this();
            this.username = username;
            this.password = password;
        }

        /**
         * 代理方法
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.debug("============== invoke() ==============");
            // Invocation on ConnectionProxy interface coming in...

            if (method.getName().equals("equals")) {
                return proxy == args[0] ? Boolean.TRUE : Boolean.FALSE;
            } else if (method.getName().equals("hashCode")) {
                // We must avoid fetching a target Connection for "hashCode",and we must return the same hash code even when the target Connection has been fetched: use hashCode of Connection proxy
                return new Integer(System.identityHashCode(proxy));
            } else if (method.getName().equals("getTargetConnection")) {
                return getTargetConnection(method, args);
            }

            // connection为null
            if (!hasTargetConnection()) {
                switch (method.getName()) {
                    case "toString":
                        return "";
                    case "isReadOnly":
                        return this.readOnly;
                    case "setReadOnly":
                        this.readOnly = (Boolean) args[0];
                        return null;
                    case "getTransactionIsolation":
                        return (this.transactionIsolation != null) ? this.transactionIsolation : defaultTransactionIsolation;
                    case "setTransactionIsolation":
                        this.transactionIsolation = (Integer) args[0];
                        return null;
                    case "getAutoCommit":
                        return (autoCommit != null) ? this.autoCommit : defaultAutoCommit;
                    case "setAutoCommit":
                        this.autoCommit = (Boolean) args[0];
                        return null;
                    case "commit":
                        return null;
                    case "rollBack":
                        return null;
                    case "getWarnings":
                        return null;
                    case "clearWarnings":
                        return null;
                    case "isClosed":
                        return (this.closed) ? Boolean.TRUE : Boolean.FALSE;
                    case "close":
                        this.closed = Boolean.TRUE;
                        return null;
                    default:
                        if (this.closed) {
                            throw new IllegalAccessException("Illegal operation: connection is closed");
                        }
                        return null;
                }
            }

            try {
                return method.invoke(proxy, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }

        }

        /**
         * Return whether the proxy currently holds a target Connection.
         */
        private boolean hasTargetConnection() {
            return (target != null);
        }

        /**
         * 获取目标连接对象
         */
        private Connection getTargetConnection(Method method, Object[] args) throws SQLException {
            if (this.target == null) {
                String key = (String) args[0];

                this.target = (this.username == null) ?
                        determineTargetDataSource(key).getConnection() :
                        determineTargetDataSource(key).getConnection(username, password);
            }

            // 是否只读
            if (this.readOnly) {
                target.setReadOnly(this.readOnly);
            }

            // 事务隔离级别
            if (this.transactionIsolation != null) {
                target.setTransactionIsolation(transactionIsolation.intValue());
            }

            // 是否自动提交
            if (this.autoCommit != null && this.autoCommit != target.getAutoCommit()) {
                target.setAutoCommit(autoCommit);
            }
            return this.target;
        }

    }
}
