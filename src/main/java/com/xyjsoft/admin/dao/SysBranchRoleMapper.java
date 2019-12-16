package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchRole;

/**
 * ---------------------------
 * 子公司角色表 (SysBranchRoleMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchRoleMapper  extends MyBatisDao<String, SysBranchRole>{

	/**
	 * 添加子公司角色表
	 * @param record
	 * @return
	 */
    int add(SysBranchRole record);

    /**
     * 删除子公司角色表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司角色表
     * @param record
     * @return
     */
    int update(SysBranchRole record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchRole findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchRole> findPage();

	SysBranchRole selectByPrimaryKey(Long roleId);
    
}