package com.xyjsoft.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.SysMenuRoute;

/**
 * ---------------------------
 * 菜单路由表 (SysMenuRouteMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-10-28 18:45:36
 * ---------------------------
 */
public interface SysMenuRouteMapper  extends MyBatisDao<String, SysMenuRoute>{

	/**
	 * 添加菜单路由表
	 * @param record
	 * @return
	 */
    int add(SysMenuRoute record);

    /**
     * 删除菜单路由表
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改菜单路由表
     * @param record
     * @return
     */
    int update(SysMenuRoute record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysMenuRoute findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysMenuRoute> findPage();

	List<SysMenuRoute> findByRoute(@Param("menuRoute")String menuRoute);

	List<SysMenuRoute> findByRouteAndNotId(@Param("menuRoute")String menuRoute, @Param("id")Long id);
    
}