package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchUserRole;

/**
 * ---------------------------
 * 子公司用户角色表 (SysBranchUserRoleMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchUserRoleMapper  extends MyBatisDao<String, SysBranchUserRole>{

	/**
	 * 添加子公司用户角色表
	 * @param record
	 * @return
	 */
    int add(SysBranchUserRole record);

    /**
     * 删除子公司用户角色表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司用户角色表
     * @param record
     * @return
     */
    int update(SysBranchUserRole record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchUserRole findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchUserRole> findPage();
    
}