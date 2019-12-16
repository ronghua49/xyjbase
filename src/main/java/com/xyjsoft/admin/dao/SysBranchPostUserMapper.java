package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchPostUser;
import com.xyjsoft.admin.model.SysBranchUser;

/**
 * ---------------------------
 * 子公司用户岗位表
 (SysBranchPostUserMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
public interface SysBranchPostUserMapper  extends MyBatisDao<String, SysBranchPostUser>{

	/**
	 * 添加子公司用户岗位表

	 * @param record
	 * @return
	 */
    int add(SysBranchPostUser record);

    /**
     * 删除子公司用户岗位表

     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司用户岗位表

     * @param record
     * @return
     */
    int update(SysBranchPostUser record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchPostUser findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchPostUser> findPage();

}