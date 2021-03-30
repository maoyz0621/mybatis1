/**
 * Copyright 2020 Inc.
 **/
package com.myz.base.annotation;

import java.lang.annotation.*;

/**
 * 加解密类
 *
 * @author maoyz0621 on 2020/11/2
 * @version v1.0
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptDecryptClass {
}