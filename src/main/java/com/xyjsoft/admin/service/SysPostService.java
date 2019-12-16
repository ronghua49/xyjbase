package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.bean.LoginInfo;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 岗位表 (SysPostService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
public interface SysPostService extends CurdService<SysPost>,Manager<String,SysPost>  {

	List<SysPost> findTree();

	SysPost findByUser(LoginInfo loginUserInfo);

	SysPost findLowerPost(String postid);

}
