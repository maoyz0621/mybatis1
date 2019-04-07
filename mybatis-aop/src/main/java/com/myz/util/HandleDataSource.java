/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.util;

import com.myz.constant.DynamicDataSourceGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户的请求和操作的数据源绑定
 *
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
public class HandleDataSource {

    private static final Logger logger = LoggerFactory.getLogger(HandleDataSource.class);
    /**
     * 当前线程，即用户请求
     */
    private static final ThreadLocal<DynamicDataSourceGlobal> handle = new ThreadLocal<DynamicDataSourceGlobal>();

    /**
     * 绑定数据源
     * <p>
     * 需在调用服务层之前进行绑定
     */
    public static void putDataSource(DynamicDataSourceGlobal dataSource) {
        logger.debug("========= putDataSource =========");
        handle.set(dataSource);
    }

    /**
     * 获取数据源
     */
    public static DynamicDataSourceGlobal getDataSource() {
        logger.debug("========= getDataSource =========");
        return handle.get();
    }

    /**
     * 清除
     */
    public static void clearDataSource() {
        handle.remove();
    }
}
