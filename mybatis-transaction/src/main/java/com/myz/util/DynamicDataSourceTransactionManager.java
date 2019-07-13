/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.util;

import com.myz.constant.DynamicDataSourceGlobal;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 自定义动态事务处理
 *
 * @author maoyz on 2018/7/4
 * @version: v1.0
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    /**
     * 只读事务到读库，读写事务到写库
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        logger.info("============= transaction doBegin() ==========" + definition);

        // 设置数据源
        boolean readOnly = definition.isReadOnly();

        if (readOnly) {
            HandleDataSource.putDataSource(DynamicDataSourceGlobal.READ);
        } else {
            HandleDataSource.putDataSource(DynamicDataSourceGlobal.WRITE);
        }
        // 放行
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        logger.info("========== transaction doCleanupAfterCompletion() ===========");

        super.doCleanupAfterCompletion(transaction);
        HandleDataSource.clearDataSource();
    }

}
