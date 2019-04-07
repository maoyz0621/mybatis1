/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.constant;

/**
 * 定义动态数据源类别
 *
 * @author maoyz on 2018/6/26
 * @version: v1.0
 */
public enum DynamicDataSourceGlobal {
    /**
     * 写数据库
     */
    WRITE("write"),
    /**
     * 读数据库
     */
    READ("read");
    private final String arg;

    DynamicDataSourceGlobal(String arg) {
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

}

