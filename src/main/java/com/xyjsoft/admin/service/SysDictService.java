package com.xyjsoft.admin.service;

import java.util.List;
import java.util.Map;

import com.xyjsoft.admin.model.SysDict;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * 字典管理
 * @author Louis
 * @date Oct 29, 2018
 */
public interface SysDictService extends CurdService<SysDict>,Manager<String,SysDict> {

	/**
	 * 根据名称查询
	 * @param lable
	 * @return
	 */
	List<SysDict> findByLable(String lable);
	
	/**
	 * 根据类型查询
	 * @param type
	 * @return
	 */	
	List<SysDict> findByType(String type);
	
	/**
	 * 查询字典中是否删除状态为0（正常）的所有类型信息
	 * @param delFlag
	 * @return
	 */	
	List<Map<String, Object>> findByGroupType(Integer delFlag);

}
