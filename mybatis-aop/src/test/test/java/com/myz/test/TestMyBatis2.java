package com.myz.test;

import com.myz.dao.RoleDao;
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
import java.util.List;


/*
 * Created by maoyz on 17-9-12.
 * 使用resultMap
 */
public class TestMyBatis2 {

    private SqlSessionFactory sqlSessionFactory = null;
    private SqlSession sqlSession = null;
    private UserDao userDao = null;
    private RoleDao roleDao = null;
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
        this.userDao = this.sqlSession.getMapper(UserDao.class);
        this.roleDao = this.sqlSession.getMapper(RoleDao.class);
    }

    @After
    public void test_(){
        this.sqlSession.close();
    }

    /**
     *　association属性查询
     */
    @Test
    public void test1() throws SQLException{
        User user = this.userDao.selectById(2);
        System.out.println(user);
    }

    /**
     *  association级联查询
     */
    @Test
    public void test2() throws SQLException{
        List<User> users = this.userDao.selectAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * association分布查询
     * 同时也应用了懒加载技术，使用时才调用
     */
    @Test
    public void test3() throws SQLException {
        User user = this.userDao.selectByIdAndRole(2);
        System.out.println(user.getEmail());
        System.out.println(user.getRole().getDescription());
    }

    /**
     * collection关联属性查询，使用左外链接查询
     */
    @Test
    public void test4(){
        Role role = this.roleDao.selectAllUserById(1);
        System.out.println(role);
    }

    /**
     * collection分步查询
     */
    @Test
    public void test5(){
        Role role = this.roleDao.selectAllUserByIdStep(1);
        System.out.println(role.getDescription());
        System.out.println(role.getUsers());
    }

    @Test
    public void test6(){
        Role role = this.roleDao.selectByName("mmm");
        System.out.println(role);
    }
}
