package com.myz.mybatis.pagehelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author maoyz
 * @version V1.0
 * @date 2022/2/21 19:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class UserServicePageTest {

    @Autowired
    UserServicePage userServicePage;

    @Test
    public void testPageNoCount() {
        userServicePage.pageNoCount(1, 10);
        userServicePage.pageNoCount(2, 10);
    }

    @Test
    public void testPage() {
        userServicePage.page(1, 10);
        userServicePage.page(2, 10);
    }

    @Test
    public void testPageInfo() {
        userServicePage.pageInfo(2, 5);
    }

    @Test
    public void testPageInfoCopy() {
        userServicePage.pageInfoCopy(2, 5);
    }
}