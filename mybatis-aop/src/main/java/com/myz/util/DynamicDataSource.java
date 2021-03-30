/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.util;

import com.myz.constant.DynamicDataSourceGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据源路由
 * 动态数据源实现读写分离
 *
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 写数据源
     */
    private Object writeDataSource;

    /**
     * 多个读数据源
     */
    private List<Object> readDataSources;

    /**
     * 读数据源个数
     */
    private int readDataSourceSize;

    /**
     * 获取读数据源方式，0：随机，1：轮询
     */
    private int readDataSourcePollPattern;

    /**
     * 原子类
     */
    private AtomicLong counter = new AtomicLong(0);

    private static final Long MAX_POOL = Long.MAX_VALUE;

    private final Lock lock = new ReentrantLock();

    /**
     * 初始化方法
     */
    @Override
    public void afterPropertiesSet() {
        logger.debug("============== afterPropertiesSet() ==============");
        // if (writeDataSource == null) {
        //   throw new IllegalArgumentException("writeDataSource不能为null");
        // }
        // 设置默认数据源
        // setDefaultTargetDataSource(writeDataSource);

        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();
        targetDataSources.put(DynamicDataSourceGlobal.WRITE.name(), writeDataSource);

        if (readDataSources == null) {
            readDataSourceSize = 0;
        } else {
            for (int i = 0; i < readDataSources.size(); i++) {
                targetDataSources.put(DynamicDataSourceGlobal.READ.name() + i, readDataSources.get(i));
            }
            readDataSourceSize = readDataSources.size();
        }

        // 设置目标数据源
        setTargetDataSources(targetDataSources);

        super.afterPropertiesSet();
    }

    /**
     * 在Service层，调用DAO层之前动态切换数据源
     * determine（确定）
     */
    @Override
    protected Object determineCurrentLookupKey() {
        logger.debug("============== determineCurrentLookupKey() ==============");

        DynamicDataSourceGlobal dataSource = HandleDataSource.getDataSource();
        if (dataSource == null || dataSource == DynamicDataSourceGlobal.WRITE || readDataSourceSize <= 0) {
            return DynamicDataSourceGlobal.WRITE.name();
        }

        int index = 1;

        // 轮询
        if (this.readDataSourcePollPattern == 1) {
            long currentVal = counter.incrementAndGet();
            if ((currentVal + 1) >= MAX_POOL) {
                try {
                    // 上锁
                    lock.lock();
                    if ((currentVal + 1) >= MAX_POOL) {
                        counter.set(0);
                    }
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            }
            // 取模
            index = (int) (currentVal % readDataSourceSize);
        } else {
            // ThreadLocalRandom随机数
            index = ThreadLocalRandom.current().nextInt(0, readDataSourceSize);
        }
        // todo
        return dataSource.name() + index;
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public void setReadDataSources(List<Object> readDataSources) {
        this.readDataSources = readDataSources;
    }

    public void setReadDataSourcePollPattern(int readDataSourcePollPattern) {
        this.readDataSourcePollPattern = readDataSourcePollPattern;
    }
}
