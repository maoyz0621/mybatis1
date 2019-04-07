package com.myz.test;

import com.myz.dao.RoleDao;
import com.myz.dao.RoleMapper;
import com.myz.dao.UserDao;
import com.myz.entity.Role;
import com.myz.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/*
 * Created by maoyz on 17-9-12.
 * 使用resultMap
 */
public class TestMyBatis3 {

    private SqlSessionFactory sqlSessionFactory = null;
    private SqlSession sqlSession = null;
    private RoleMapper roleMapper = null;
    private UserDao userDao = null;

    /**
     *创建sqlSessionFactory
     */
    @Before
    public void test() throws IOException {
        // 从xml文件中创建sqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSession = this.sqlSessionFactory.openSession(true);
        this.roleMapper = this.sqlSession.getMapper(RoleMapper.class);
        this.userDao = this.sqlSession.getMapper(UserDao.class);

    }

    @After
    public void test_(){
        this.sqlSession.close();
    }

    /**
     * 测试if标签语句,满足条件即执行
     */
    @Test
    public void test1(){
        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("k");
        Role role1 = this.roleMapper.getRoleDynamic(role);
        System.out.println(role1.getRoleId());
        System.out.println(role1.getDescription());
        System.out.println(role1.getRoleName());
        System.out.println(role1.getUsers());
    }

    /**
     * 测试choose条件语句
     */
    @Test
    public void test2() {
        Role role = new Role();
        role.setRoleId(1);
//        role.setRoleName("ka1");
        role.setDescription("h");
        Role role1 = this.roleMapper.getRoleDynamic1(role);
        System.out.println(role1.getRoleId());
        System.out.println(role1.getUsers());
    }

    /**
     * 测试set标签语句
     */
    @Test
    public void test3(){
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("333");
        role.setDescription("ha33");
        this.roleMapper.updateDynamic(role);
    }

    /**
     * 测试<foreach>标签语句
     */
    @Test
    public void test4(){
        //定义list
        List<Role> list = new ArrayList<Role>();
        Role role1 = new Role();
        role1.setRoleName("mmm");
        role1.setDescription("shaui");
        Role role2 = new Role();
        role2.setRoleName("yyy");
        role2.setDescription("hohoho");
        list.add(role1);
        list.add(role2);
        this.roleMapper.insertDynamic(list);
    }

    /**
     * 测试<foreach>标签语句
     */
    @Test
    public void test5(){
        //定义list
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(3);
        ids.add(5);
        List<Role> roles = this.roleMapper.selectDynamicByIds(ids);
        System.out.println(roles);

    }
}
