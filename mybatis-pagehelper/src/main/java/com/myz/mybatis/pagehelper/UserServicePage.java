package com.myz.mybatis.pagehelper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myz.dao.UserVOMapper;
import com.myz.entity.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author maoyz
 * @version V1.0
 * @date 2022/2/21 19:43
 */
@Service
public class UserServicePage {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserVOMapper userVOMapper;

    public List<UserVO> pageNoCount(int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).count(false).doSelectPageInfo(() -> {
            userVOMapper.getAll();
        });
        logger.info("{}", pageInfo);
        return null;
    }

    public List<UserVO> page(int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            userVOMapper.getAll();
        });
        System.out.println(pageInfo);
        logger.info("{}", pageInfo);
        return null;
    }

    public List<UserVO> pageInfo(int pageNum, int pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        logger.info("page = {}", page);
        List<UserVO> all = userVOMapper.getAll();
        PageInfo<UserVO> pageInfo = new PageInfo<>(all);
        logger.info("pageInfo = {}", pageInfo);
        return null;
    }
}
