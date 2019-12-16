package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchPost;

/**
 * ---------------------------
 * 子公司岗位表
 (SysBranchPostMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
public interface SysBranchPostMapper  extends MyBatisDao<String, SysBranchPost>{

	/**
	 * 添加子公司岗位表

	 * @param record
	 * @return
	 */
    int add(SysBranchPost record);

    /**
     * 删除子公司岗位表

     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司岗位表

     * @param record
     * @return
     */
    int update(SysBranchPost record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchPost findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchPost> findPage();

	List<SysBranchPost> findAll(String branchCode);

	List<SysBranchPost> findPostByUserId(Long id);
    
}