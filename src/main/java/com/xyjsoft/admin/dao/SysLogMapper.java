package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysTree;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.model.SysLog;


public interface SysLogMapper extends MyBatisDao<String, SysLog>{
    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
    
    List<SysLog> findPage();
    
    List<SysLog> findPageByUserName(@Param(value="userName") String userName);
}