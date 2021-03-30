package com.myz.test;

import com.myz.dao.RoleMapper;
import com.myz.dao.UserDao;
import com.myz.dao.UserVOMapper;
import com.myz.entity.Role;
import com.myz.entity.UserVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/*
 * Created by maoyz on 17-9-12.
 * 使用resultMap
 */
public class TestMyBatis3 {

    private SqlSessionFactory sqlSessionFactory = null;
    private SqlSession sqlSession = null;
    private RoleMapper roleMapper = null;
    private UserDao userDao = null;
    private UserVOMapper userVOMapper = null;

    /**
     * 创建sqlSessionFactory
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
        this.userVOMapper = this.sqlSession.getMapper(UserVOMapper.class);

    }

    @After
    public void test_() {
        this.sqlSession.close();
    }

    /**
     * 测试if标签语句,满足条件即执行
     */
    @Test
    public void test1() {
        Role role = new Role();
        role.setId(2l);
        role.setRoleName("k");
        Role role1 = this.roleMapper.getRoleDynamic(role);
        System.out.println(role1.getId());
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
        role.setId(1l);
//        role.setRoleName("ka1");
        role.setDescription("h");
        Role role1 = this.roleMapper.getRoleDynamic1(role);
        System.out.println(role1.getId());
        System.out.println(role1.getUsers());
    }

    /**
     * 测试set标签语句
     */
    @Test
    public void test3() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("333");
        role.setDescription("ha33");
        this.roleMapper.updateDynamic(role);
    }

    /**
     * 测试<foreach>标签语句
     */
    @Test
    public void test4() {
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
    public void test5() {
        //定义list
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(3);
        ids.add(5);
        List<Role> roles = this.roleMapper.selectDynamicByIds(ids);
        System.out.println(roles);
    }

    /**
     * 测试<foreach>标签语句
     */
    @Test
    public void testEncrypt(){
        List<UserVO> user = userVOMapper.getUser(1111L);
        System.out.println(user);

        System.out.println("======================");


        UserVO userVO = new UserVO();
        userVO.setId(1111L);
        userVO.setPassword("123");
        userVO.setUsername("maoyz");
        userVOMapper.insertUser(userVO);

        System.out.println("============================");
        Collection<Integer> ids1 = new ArrayList<>();
        ids1.add(11);
        ids1.add(31);
        ids1.add(51);
        List<Role> roles1 = this.roleMapper.selectDynamicByIdsCollection(ids1);
        System.out.println(roles1);

        System.out.println("============================");
        Set<Integer> ids2 = new HashSet<>();
        ids2.add(1);
        ids2.add(3);
        ids2.add(5);
        List<Role> roles2 = this.roleMapper.selectDynamicByIdsSet(ids2);
        System.out.println(roles2);

        System.out.println("============================");
        Integer[] data = {1, 2, 4};
        List<Role> roles3 =roleMapper.selectDynamicByIdsArray(data);
        System.out.println(roles3);

    }
}
