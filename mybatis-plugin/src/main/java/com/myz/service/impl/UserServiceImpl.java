/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.service.impl;

import com.myz.dao.UserVOMapper;
import com.myz.entity.UserVO;
import com.myz.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
@Service
public class UserServiceImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserVOMapper userVOMapper;

    @Transactional
    @Override
    public List<UserVO> getUser(Serializable id) {
        logger.debug("=============== service getUser() ===============");
        List<UserVO> userList = userVOMapper.getuser(id);
        return userList;
    }

    @Override
    public void insertUser(UserVO userVO) {
        logger.debug("=============== service insertUser() ===============");
        int num = userVOMapper.insertuser(userVO);
        if (1 == num) {
            System.out.println("insert success");
        }
    }
}
