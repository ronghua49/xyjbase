package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysPostUser;
import com.xyjsoft.core.base.MyBatisDao;

/**
 * ---------------------------
 * 岗位员工表 (SysPostUserMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
public interface SysPostUserMapper  extends MyBatisDao<String, SysPostUser>{

	/**
	 * 添加岗位员工表
	 * @param record
	 * @return
	 */
    int add(SysPostUser record);

    /**
     * 删除岗位员工表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改岗位员工表
     * @param record
     * @return
     */
    int update(SysPostUser record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysPostUser findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysPostUser> findPage();

    /**
     * 根据岗位id数组查询用户
     * @param postIds
     * @return
     */
	List<SysPostUser> findByPostIds(@Param("list")List<Long> postIds);

	void deleteByUserId(Long id);

	void deleteByPostId(Long id);
    
}