package com.myz.dao;

import com.myz.entity.Role;
import com.myz.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author maoyz
 */
public interface RoleDao {

    Role selectByName(String roleName);

    Role selectById(Integer roleId);

    /*　左外链接查询　*/
    Role selectAllUserById(Integer roleId);

    /*　分步查询　*/
    Role selectAllUserByIdStep(Integer roleId);
}
