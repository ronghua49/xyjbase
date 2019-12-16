package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysBranchMenu;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysMenu;

/**
 * ---------------------------
 * 子公司用户表 (SysBranchUserMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
public interface SysBranchUserMapper  extends MyBatisDao<String, SysBranchUser>{

	/**
	 * 添加子公司用户表
	 * @param record
	 * @return
	 */
    int add(SysBranchUser record);

    /**
     * 删除子公司用户表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改子公司用户表
     * @param record
     * @return
     */
    int update(SysBranchUser record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysBranchUser findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysBranchUser> findPage();

	SysBranchUser findByName(String username);

	List<SysBranchUser> findUserByPostId(Long id);

}