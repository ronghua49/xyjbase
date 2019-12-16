package com.xyjsoft.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysDict;
import com.xyjsoft.core.base.MyBatisDao;

public interface SysDictMapper  extends MyBatisDao<String, SysDict>{
    int deleteByPrimaryKey(Long id);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);
    
    List<SysDict> findPage();
    
    List<SysDict> findPageByLabel(@Param(value="label") String label);

    List<SysDict> findByLable(@Param(value="label") String label);
    
    List<SysDict> findByType(@Param(value="type") String type);

	List<Map<String, Object>> findByGroupType(@Param(value="delFlag") Integer delFlag);
    
}