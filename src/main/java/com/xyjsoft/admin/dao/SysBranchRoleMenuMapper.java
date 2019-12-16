package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchRoleMenu;

/**
 * ---------------------------
 * 子公司角色菜单表 (SysBranchRoleMenuMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchRoleMenuMapper  extends MyBatisDao<String, SysBranchRoleMenu>{

	/**
	 * 添加子公司角色菜单表
	 * @param record
	 * @return
	 */
    int add(SysBranchRoleMenu record);

    /**
     * 删除子公司角色菜单表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司角色菜单表
     * @param record
     * @return
     */
    int update(SysBranchRoleMenu record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchRoleMenu findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchRoleMenu> findPage();

	void deleteByRoleId(Long roleId);

	void insertSelective(SysBranchRoleMenu record);
    
}