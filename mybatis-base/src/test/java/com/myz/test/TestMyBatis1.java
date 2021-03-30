package com.myz.test;

import com.myz.dao.UserMapper;
import com.myz.entity.User;
import com.myz.entity.UserPO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by maoyz on 17-9-12.
 */
public class TestMyBatis1 {

    private SqlSessionFactory sqlSessionFactory;

    /**
     * 创建sqlSessionFactory
     */
    @Before
    public void test() throws IOException {
        // 从xml文件中创建sqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    /*
     * 插入测试
     */
    @Test
    public void test1() throws SQLException {
        // 创建session
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setLastName("115");
        user.setGender("1");
        user.setEmail("111.000");
        userMapper.insertUser(user);
        sqlSession.close();
    }

    @Test
    public void testBatchInsert() throws SQLException {
        // 创建session
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        UserPO user = new UserPO();
        user.setLastName("115");
        user.setGender("1");
        user.setEmail("111.000");
        user.setRoleId(1L);

        UserPO user1 = new UserPO();
        user1.setLastName("11511");
        user1.setGender("1111");
        user1.setEmail("11111111.000");
        user1.setRoleId(2L);

        List<UserPO> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        userMapper.batchInsert(list);
        sqlSession.close();
    }

    /*
     * 删除测试
     */
    @Test
    public void test2() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.deleteUser(3);
        sqlSession.close();
    }

    /*
     *　测试更新
     */
    @Test
    public void test3() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(10L);
        user.setLastName("222");
        user.setGender("1");
        user.setEmail("222.cn");
        userMapper.updateUser(user);
        sqlSession.close();
    }

    @Test
    public void testBatchUpdate() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        UserPO user = new UserPO();
        user.setId(5L);
        user.setLastName("5");
        user.setEmail("5.cn");

        UserPO user1 = new UserPO();
        user1.setId(10L);
        user1.setLastName("10");
        user1.setEmail("10.cn");

        List<UserPO> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        userMapper.batchUpdate(list);
        sqlSession.close();
    }

    /**
     * 测试多参数查询
     */
    @Test
    public void test4() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.selectByIdAndGender(1, "1");
        sqlSession.close();
    }

    /**
     * 测试多参数查询
     * 根据pojo
     */
    @Test
    public void test5() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(3L);
        user.setLastName("ccc");
        User user1 = userMapper.selectByPOJO(user);
        System.out.println(user1);
        sqlSession.close();
    }

    /**
     * 测试多参数查询
     * 根据map
     */
    @Test
    public void test6() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap();
        map.put("id", 11);
        map.put("lastName", "112");
        User user = userMapper.selectByMap(map);
        System.out.println(user);
        sqlSession.close();
    }

    /**
     * 测试全部查询
     */
    @Test
    public void test7() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> lists = userMapper.selectAll();
        for (User list : lists) {
            System.out.println(list);
        }
        sqlSession.close();
    }

    /**
     * 测试模糊查询
     */
    @Test
    public void test71() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> lists = userMapper.selectAllByName("1");
        for (User list : lists) {
            System.out.println(list);
        }
        sqlSession.close();
    }


    /**
     * 测试查询返回一条map结果
     */
    @Test
    public void test8() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Map<String, User> map = userMapper.selectReturnMap(1);
        System.out.println(map);
        sqlSession.close();
    }

    /**
     * 测试查询返回多条结果封装子啊map中
     */
    @Test
    public void test9() throws SQLException {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Map<String, User> map = userMapper.selectReturnMaps("11");
        System.out.println(map);

        sqlSession.close();
    }

}
