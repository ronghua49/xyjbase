package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysBranchMenu;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司菜单表 (SysBranchMenuService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchMenuService extends CurdService<SysBranchMenu>,Manager<String,SysBranchMenu>  {

	/**
	 * 查询菜单树,用户ID和用户名为空则查询全部
	 * @param menuType 获取菜单类型，0：获取所有菜单，包含按钮，1：获取所有菜单，不包含按钮
	 * @param userId 
	 * @return
	 */
	List<SysBranchMenu> findTree(String userName, int menuType);

	List<SysBranchMenu> findByUser(String userName);
}
