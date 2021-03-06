/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.base.service;


import com.myz.entity.UserVO;

import java.io.Serializable;
import java.util.List;

/**
 * @author maoyz on 2018/6/21
 * @version v1.0
 */
public interface IUserService {

    /**
     * 执行读数据库
     */
    List<UserVO> getUser(Serializable id);


    /**
     * 执行写数据库
     */
    void insertUser(UserVO userVO);


}
