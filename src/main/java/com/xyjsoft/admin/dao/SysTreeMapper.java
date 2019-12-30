package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysTree;
import com.xyjsoft.core.base.MyBatisDao;

/**
 * ---------------------------
 * 分类管理表 (SysTreeMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-26 11:08:47
 * ---------------------------
 */
public interface SysTreeMapper  extends MyBatisDao<String, SysTree>{

	/**
	 * 添加分类管理表
	 * @param record
	 * @return
	 */
    int add(SysTree record);

    /**
     * 删除分类管理表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改分类管理表
     * @param record
     * @return
     */
    int update(SysTree record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysTree findById(Long id);

    /**
     * 基础分页查询
     * @param
     * @return
     */    
    List<SysTree> findPage();
    /**
     * @Author wangzhengwen
     * @Description 根据code值进行查询
     * @Date 14:48 2019/9/2 0002
     * @Param [code]
     * @return com.xyjsoft.admin.model.SysTree
     **/
    List<SysTree> fiandByCode(String code);

	List<SysTree> fiandByCodeAndNotId(@Param(value= "code")String code,@Param(value= "id") Long id);
    
}