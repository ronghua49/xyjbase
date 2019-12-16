package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchDept;

/**
 * ---------------------------
 * 子公司机构表 (SysBranchDeptMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchDeptMapper  extends MyBatisDao<String, SysBranchDept>{

	/**
	 * 添加子公司机构表
	 * @param record
	 * @return
	 */
    int add(SysBranchDept record);

    /**
     * 删除子公司机构表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司机构表
     * @param record
     * @return
     */
    int update(SysBranchDept record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchDept findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchDept> findPage();

	List<SysBranchDept> findAllByBranchCode(String branchCode);
    
}