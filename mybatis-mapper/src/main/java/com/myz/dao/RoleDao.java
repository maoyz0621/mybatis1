package com.myz.dao;

import com.myz.entity.Role;

import java.util.Map;

/**
 * @author maoyz
 */
public interface RoleDao {

    Role selectByName(String roleName);

    Map selectByNameMap(String roleName);

    Role selectById(Integer roleId);

    /**
     * 使用左外链接连表查询
     */
    Role selectAllUserById(Integer roleId);

    /**
     * 分步查询
     * 先根据条件查出本表结果，在根据查出结果作为查询条件去查询其他表，非连表查询，多个单表查询，在集合
     */
    Role selectAllUserByIdStep(Integer roleId);
}
