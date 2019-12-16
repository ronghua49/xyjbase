package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.model.SysPostUser;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.vo.SysPostUserVo;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 岗位员工表 (SysPostUserService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
public interface SysPostUserService extends CurdService<SysPostUser>,Manager<String,SysPostUser>  {

	void saveList(SysPostUser[] record);

	List<SysPostUser> findByPostIds(List<SysPost> sysPosts);

	List<SysUser> findUserById(Long id);

	List<SysPostUserVo> findPostByUserId(Long id);
	
	void deleteByUserId(Long id);

}
