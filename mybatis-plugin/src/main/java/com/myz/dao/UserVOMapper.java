/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.dao;

import com.myz.entity.UserVO;

import java.io.Serializable;
import java.util.List;

/**
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
public interface UserVOMapper {

    List<UserVO> getuser(Serializable id);

    int insertuser(UserVO userVO);
}
