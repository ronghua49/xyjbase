package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchMenu;

/**
 * ---------------------------
 * 子公司菜单表 (SysBranchMenuMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchMenuMapper  extends MyBatisDao<String, SysBranchMenu>{

	/**
	 * 添加子公司菜单表
	 * @param record
	 * @return
	 */
    int add(SysBranchMenu record);

    /**
     * 删除子公司菜单表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司菜单表
     * @param record
     * @return
     */
    int update(SysBranchMenu record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchMenu findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchMenu> findPage();

	List<SysBranchMenu> findAll();

	List<SysBranchMenu> findByUserName(String userName);

	List<SysBranchMenu> findRoleMenus(Long roleId);

}