package com.myz.dao;

import com.myz.entity.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    List<Role> selectDynamicByIdsCollection(Collection<Integer> ids);

    List<Role> selectDynamicByIds(List<Integer> ids);

    List<Role> selectDynamicByIdsSet(Set<Integer> ids);

    List<Role> selectDynamicByIdsArray(Integer[] ids);
}
