package com.myz.dao;

import com.myz.entity.User;
import com.myz.entity.UserPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/*
 * Created by maoyz on 17-9-15.
 */
public interface UserMapper {

    /*
     * 查询所有
     */
    List<User> selectAll() throws SQLException;

    /*
     * 查询所有
     */
    List<User> selectAllByName(String lastName) throws SQLException;

    /**
     * 查询结果返回一条map
     */
    Map<String, User> selectReturnMap(Integer id) throws SQLException;

    /**
     * 查询结果返回多条map
     * /@MapKey()　map中的key值
     */
    @MapKey("lastName")
    Map<String, User> selectReturnMaps(String lastName) throws SQLException;


    /**
     * 　根据id查询
     */
    User selectById(@Param("id") Integer id) throws SQLException;

    /**
     * 重载
     */
    User selectById(@Param("lastName") String lastName) throws SQLException;

    /**
     * 重载
     */
    User selectById(@Param("id") Integer id, @Param("lastName") String lastName) throws SQLException;

    /**
     * 　根据id和gender查询
     * 多参数查询@Param("")
     */
    User selectByIdAndGender(@Param("id") Integer id, @Param("gender") String gender) throws SQLException;

    /**
     * 根据pojo属性查询
     * #{属性名}
     */
    User selectByPOJO(User user);

    /**
     * 根据Map属性查询
     * #{key}
     */
    User selectByMap(Map<String, Object> map);

    /*
     *　添加
     */
    void insertUser(User user) throws SQLException;

    int batchInsert(@Param("users") List<UserPO> user) throws SQLException;

    /*
     * 更新
     */
    void updateUser(User user) throws SQLException;

    int batchUpdate(List<UserPO> user) throws SQLException;

    /*
     * 删除
     */
    void deleteUser(Integer id) throws SQLException;
}
