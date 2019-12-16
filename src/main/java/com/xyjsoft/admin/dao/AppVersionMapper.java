package com.xyjsoft.admin.dao;

import java.util.List;

import com.xyjsoft.admin.model.AppVersion;
import com.xyjsoft.core.base.MyBatisDao;

/**
 * ---------------------------
 * app版本 (AppVersionMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-03-11 17:38:22
 * 说明：  我是由代码生成器生生成的
 * ---------------------------
 */
public interface AppVersionMapper  extends MyBatisDao<String, AppVersion>{

	/**
	 * 添加app版本
	 * @param record
	 * @return
	 */
    int add(AppVersion record);

    /**
     * 删除app版本
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改app版本
     * @param record
     * @return
     */
    int update(AppVersion record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    AppVersion findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<AppVersion> findPage();
    
}