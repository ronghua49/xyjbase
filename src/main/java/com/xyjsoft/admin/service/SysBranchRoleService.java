package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysBranchMenu;
import com.xyjsoft.admin.model.SysBranchRole;
import com.xyjsoft.admin.model.SysBranchRoleMenu;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司角色表 (SysBranchRoleService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchRoleService extends CurdService<SysBranchRole>,Manager<String,SysBranchRole>  {

	/**
	 * 查询角色菜单集合
	 * @return
	 */
	List<SysBranchMenu> findRoleMenus(Long roleId);

	int saveRoleMenus(List<SysBranchRoleMenu> records);

}
