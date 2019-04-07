package com.myz.test;

import com.myz.entity.UserVO;
import com.myz.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author maoyz on 2018/6/21
 * @version: v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class UserServiceImplTest {

  @Autowired
  private IUserService userService;

  @Test
  public void getUser() throws Exception {
    for (int i = 0; i < 5; i++) {
      List<UserVO> user = userService.getUser(1L);
      System.out.println("查询结果:" + user);
    }
  }

  @Test
  public void insertuser() throws Exception {
    UserVO userVO = new UserVO();
    userVO.setId(1L);
    userVO.setUsername("maoyz");
    userVO.setPassword("123");
    userVO.setSex('1');
    userService.insertUser(userVO);
  }

}