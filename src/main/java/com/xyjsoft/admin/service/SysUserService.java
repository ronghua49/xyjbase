package com.xyjsoft.admin.service;

import java.util.List;
import java.util.Set;

import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.model.SysUserRole;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.service.CurdService;

/**
 * 用户管理
 * @author Louis
 * @date Oct 29, 2018
 */
public interface SysUserService extends CurdService<SysUser>,Manager<String,SysUser> {

	SysUser findByName(String username);

	/**
	 * 查找用户的菜单权限标识集合
	 * @param userName
	 * @return
	 */
	Set<String> findPermissions(String userName);

	/**
	 * 查找用户的角色集合
	 * @param userName
	 * @return
	 */
	List<SysUserRole> findUserRoles(Long userId);

	String updatePwd(String name, String password, String newPWD);

	PageList<SysUser> listUserByDeptIds(QueryFilter queryFilter) throws Exception;

	void changeBind(Long userId, Long imId);

}
