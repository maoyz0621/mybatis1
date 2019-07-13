/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.util;

import com.myz.constant.DynamicDataSourceGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源路由, 动态数据源实现读写分离
 *
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Object writeDataSource;

    private Object readDataSource;

    /**
     * 初始化方法
     */
    @Override
    public void afterPropertiesSet() {
        logger.debug("============== afterPropertiesSet() ==============");

        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

        if (this.writeDataSource != null) {
            targetDataSources.put(DynamicDataSourceGlobal.WRITE.name(), writeDataSource);
        }

        if (this.readDataSource != null) {
            targetDataSources.put(DynamicDataSourceGlobal.READ.name(), readDataSource);
        }

        // 设置目标数据源
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * determine（确定）
     */
    @Override
    protected Object determineCurrentLookupKey() {
        logger.debug("============== determineCurrentLookupKey() ==============");

        DynamicDataSourceGlobal dataSource = HandleDataSource.getDataSource();
        logger.info("******************determineCurrentLookupKey now is [{}]*******************", dataSource);
        if (dataSource == DynamicDataSourceGlobal.WRITE || dataSource == null) {
            return DynamicDataSourceGlobal.WRITE.name();
        }

        return DynamicDataSourceGlobal.READ.name();
    }

    public Object getWriteDataSource() {
        return writeDataSource;
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public Object getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(Object readDataSource) {
        this.readDataSource = readDataSource;
    }
}
