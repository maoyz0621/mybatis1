package com.myz.test;

import com.myz.dao.UserMapper;
import com.myz.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by maoyz on 17-9-12.
 */
public class TestMyBatis {

    private SqlSessionFactory sqlSessionFactory;
    /**
     *创建sqlSessionFactory
     */
    @Before
    public void before() throws IOException {
        // 从xml文件中创建sqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }


    /*
     * 之前用法，使用selectOne()
     */
    @Test
    public void test1(){
        // 创建session
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        /*
         * 1 唯一标示
         * 2 查询参数
         */
        User user = sqlSession.selectOne("com.myz.dao.Mapper.selectById1",1);
        System.out.println(user.getLastName());
        sqlSession.close();
    }


    /*
     * 接口式编程，推荐使用
     */
    @Test
    public void test2() throws SQLException {
        // 创建session,是非线程安全的
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        /*
         * 1 getMapper()获取接口的实现类，即代理对象com.sun.proxy.$Proxy5
         * 2 调用接口的方法
         */
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//        System.out.println(userMapper.getClass());  //　com.sun.proxy.$Proxy5 动态绑定
        User user = userMapper.selectById(3);
        System.out.println(user.getEmail());
        sqlSession.close();
    }

    /*
     * 测试本地缓存localCacheScope
     */
    @Test
    public void test3() throws SQLException{
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        long int1 = System.currentTimeMillis();
        User user1 = userMapper.selectById(1);
        long int2 = System.currentTimeMillis();
        System.out.println(int2 - int1);    //第一次查询时间

        long int3 = System.currentTimeMillis();
        User user2 = userMapper.selectById(1);
        long int4 = System.currentTimeMillis();
        System.out.println(int4 - int3);    //第二次查询时间
    }
}
