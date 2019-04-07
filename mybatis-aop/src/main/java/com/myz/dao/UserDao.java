package com.myz.dao;

import com.myz.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author maoyz on 17-9-15.
 */
public interface UserDao {

    /**
     * 查询所有
     */
    List<User> selectAll() throws SQLException;

    /**
    * 查询所有
    */
    List<User> selectAllByRoleId(Integer roleId) throws SQLException;

    /**
     *　根据id查询
     */
    User selectById(Integer id) throws SQLException;

    /**
    *　根据id查询,使用级联分布查询
    */
    User selectByIdAndRole(Integer id) throws SQLException;


}
