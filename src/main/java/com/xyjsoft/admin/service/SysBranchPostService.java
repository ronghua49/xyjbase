package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchPost;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司岗位表
 (SysBranchPostService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
public interface SysBranchPostService extends CurdService<SysBranchPost>,Manager<String,SysBranchPost>  {

	List<SysBranchPost> findTree(String branchCode);

	void saveNewPost(SysBranch record, Long userId);
}
