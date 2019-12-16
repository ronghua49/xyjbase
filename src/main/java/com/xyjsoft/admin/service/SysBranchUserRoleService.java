package com.xyjsoft.admin.service;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysBranchUserRole;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司用户角色表 (SysBranchUserRoleService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchUserRoleService extends CurdService<SysBranchUserRole>,Manager<String,SysBranchUserRole>  {

	void saveNewRole(SysBranchUser saveNewUser);

}
