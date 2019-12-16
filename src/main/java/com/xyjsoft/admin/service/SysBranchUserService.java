package com.xyjsoft.admin.service;
import com.xyjsoft.core.base.Manager;

import java.util.Set;

import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司用户表 (SysBranchUserService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchUserService extends CurdService<SysBranchUser>,Manager<String,SysBranchUser>  {

	SysBranchUser saveNewUser(SysBranch record, Long deptId);

	SysBranchUser findByName(String username);
	/**
	 * 查找用户的菜单权限标识集合
	 * @param userName
	 * @return
	 */
	Set<String> findPermissions(String userName);

	void changeBind(Long userId, Long imId);

}
