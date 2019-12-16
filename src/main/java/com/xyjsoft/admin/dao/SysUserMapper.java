package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.core.base.MyBatisDao;

public interface SysUserMapper extends MyBatisDao<String,SysUser>{
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    
    List<SysUser> findPage();
    
    SysUser findByName(@Param(value="name") String name);
    
	List<SysUser> findPageByName(@Param(value="name") String name);
	
	List<SysUser> findPageByNameAndEmail(@Param(value="name") String name, @Param(value="email") String email);

	List<SysUser> findUserByPostId(Long id);
}