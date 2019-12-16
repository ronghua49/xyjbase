package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysBranchPost;
import com.xyjsoft.admin.model.SysBranchPostUser;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司用户岗位表
 (SysBranchPostUserService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
public interface SysBranchPostUserService extends CurdService<SysBranchPostUser>,Manager<String,SysBranchPostUser>  {

	List<SysBranchUser> findUserById(Long id);

	List<SysBranchPost> findPostByUserId(Long id);

}
