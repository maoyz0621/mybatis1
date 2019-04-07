package com.myz.dao;

import com.myz.entity.Role;

import java.util.List;

/**
 * 动态SQL查询
 */
public interface RoleMapper {

    //<if>
    Role getRoleDynamic(Role role);

    //<choose>
    Role getRoleDynamic1(Role role);

    //<set>
    void updateDynamic(Role role);

    //<foreach>
    void insertDynamic(List<Role> roles);

    //<foreach>查询指定id内数据
    List<Role> selectDynamicByIds(List<Integer> ids);
}
