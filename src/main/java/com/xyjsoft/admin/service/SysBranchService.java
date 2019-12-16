package com.xyjsoft.admin.service;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 子公司表 (SysBranchService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchService extends CurdService<SysBranch>,Manager<String,SysBranch>  {

	String StartOrStop(Long id);

}
