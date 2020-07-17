package com.myz.sql.generate.mapper;

import com.myz.sql.generate.po.TRole;

public interface TRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TRole record);

    int insertSelective(TRole record);

    TRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);
}