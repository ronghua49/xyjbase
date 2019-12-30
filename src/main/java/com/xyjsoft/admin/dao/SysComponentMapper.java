package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysComponent;

/**
 * ---------------------------
 * VUE组件源代码存放表 (SysComponentMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-12-27 12:27:43
 * ---------------------------
 */
public interface SysComponentMapper  extends MyBatisDao<String, SysComponent>{

	/**
	 * 添加VUE组件源代码存放表
	 * @param record
	 * @return
	 */
    int add(SysComponent record);

    /**
     * 删除VUE组件源代码存放表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改VUE组件源代码存放表
     * @param record
     * @return
     */
    int update(SysComponent record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysComponent findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysComponent> findPage();
    
}