package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranch;

/**
 * ---------------------------
 * 子公司表 (SysBranchMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchMapper  extends MyBatisDao<String, SysBranch>{

	/**
	 * 添加子公司表
	 * @param record
	 * @return
	 */
    int add(SysBranch record);

    /**
     * 删除子公司表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司表
     * @param record
     * @return
     */
    int update(SysBranch record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranch findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranch> findPage();
    
}