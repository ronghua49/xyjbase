package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysKeyword;

/**
 * ---------------------------
 * 关键词 (SysKeywordMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-25 15:13:46
 * ---------------------------
 */
public interface SysKeywordMapper  extends MyBatisDao<String, SysKeyword>{

	/**
	 * 添加关键词
	 * @param record
	 * @return
	 */
    int add(SysKeyword record);

    /**
     * 删除关键词
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改关键词
     * @param record
     * @return
     */
    int update(SysKeyword record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysKeyword findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysKeyword> findPage();

	List<SysKeyword> getByCode(@Param("code")String code);

	List<SysKeyword> getByCodeAndId(@Param("code")String code, @Param("id")Long id);
    
}