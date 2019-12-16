package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.vo.SysPostUserVo;
import com.xyjsoft.core.base.MyBatisDao;

/**
 * ---------------------------
 * 岗位表 (SysPostMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
public interface SysPostMapper  extends MyBatisDao<String, SysPost>{

	/**
	 * 添加岗位表
	 * @param record
	 * @return
	 */
    int add(SysPost record);

    /**
     * 删除岗位表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改岗位表
     * @param record
     * @return
     */
    int update(SysPost record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysPost findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysPost> findPage();
    
	/**
	 * 获取所有岗位
	 * @param userid
	 * @return
	 */
	List<SysPost> findAll();

	/**
	 * 根据用户ID获取主岗位
	 * @param userid
	 * @return
	 */
	SysPost findByUser(Long userid);

	/**
	 * 根据上级ID获取下级列表
	 * @param userid
	 * @return
	 */
	List<SysPost> findByCid(Long id);

	/**
	 * 根据员工ID获取岗位列表
	 * @param userid
	 * @return
	 */
	List<SysPostUserVo> findPostByUserId(Long id);

	List<SysPost> getPostByCode(String code);

	List<SysPost> getPostByCodeAndNotId(@Param("code")String code, @Param("id")Long id);
    
}