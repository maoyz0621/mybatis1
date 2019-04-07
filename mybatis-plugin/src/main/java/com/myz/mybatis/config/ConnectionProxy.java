/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.mybatis.config;


import java.sql.Connection;

/**
 * 创建Connection代理接口
 *
 * @author maoyz on 2018/7/2
 * @version: v1.0
 */
public interface ConnectionProxy extends Connection {

    /**
     * 根据传入的读写分离需要的key路由到正确的connection
     *
     * @param key
     * @return
     */
    Connection getTargetConnection(String key);
}
