/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.mybatis.config;

import javax.sql.DataSource;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author maoyz on 2018/7/2
 * @version: v1.0
 */
public class DynamicDataSourceProxy extends AbstractDynamicDataSourceProxy {
    private AtomicLong counter = new AtomicLong(0);

    private static final Long MAX_POOL = Long.MAX_VALUE;

    private final Lock lock = new ReentrantLock();


    @Override
    protected DataSource loadReadDataSource() {
        int index = 1;

        // 轮询
        if (getReadDataSourcePollPattern() == 1) {
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
            index = (int) (currentVal % getReadDataSourceSize());
        } else {
            // ThreadLocalRandom随机数
            index = ThreadLocalRandom.current().nextInt(0, getReadDataSourceSize());
        }
        // todo
        return getResolvedReadDataSources().get(index);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }
}
