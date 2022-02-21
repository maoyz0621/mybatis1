/**
 * Copyright 2018 asiainfo Inc.
 **/
package com.myz.base.service.impl;

import com.myz.base.service.IUserService;
import com.myz.dao.UserVOMapper;
import com.myz.entity.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author maoyz on 2018/6/21
 * @version v1.0
 */
@Service
public class UserServiceImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserVOMapper userVOMapper;

    @Transactional
    @Override
    public List<UserVO> getUser(Serializable id) {
        logger.debug("=============== service getUser() ===============");

        List<UserVO> userList = userVOMapper.getUser(id);
        System.out.println("userList=" + userList.hashCode());

        logger.info("判断是否启用Mybatis一级缓存");
        UserVO updateUser = new UserVO();
        updateUser.setId((Long) id);
        updateUser.setPassword("asdas1111");
        userVOMapper.updateUser(updateUser);

        List<UserVO> userList1 = userVOMapper.getUser(id);
        System.out.println("userList1=" + userList1.hashCode());
        return userList;
    }

    @Transactional
    @Override
    public void insertUser(UserVO userVO) {
        logger.debug("=============== service insertUser() ===============");
        int num = userVOMapper.insertUser(userVO);
        if (1 == num) {
            System.out.println("insert success");
        }
    }
}
