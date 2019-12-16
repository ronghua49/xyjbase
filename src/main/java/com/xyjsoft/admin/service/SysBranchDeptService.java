package com.xyjsoft.admin.service;
import java.util.List;

import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchDept;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司机构表 (SysBranchDeptService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchDeptService extends CurdService<SysBranchDept>,Manager<String,SysBranchDept>  {

	SysBranchDept saveNewDept(SysBranch record);
	
	List<SysBranchDept> findTree(String branchCode);

}
