/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.annotation;

import com.myz.constant.DynamicDataSourceGlobal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源注解
 *
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TargetDataSource {

  DynamicDataSourceGlobal value() default DynamicDataSourceGlobal.READ;

}
