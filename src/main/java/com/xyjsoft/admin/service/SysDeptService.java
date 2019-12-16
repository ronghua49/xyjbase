package com.xyjsoft.admin.service;

import java.util.List;

import com.xyjsoft.core.service.CurdService;
import com.xyjsoft.admin.model.SysDept;

/**
 * 机构管理
 * @author Louis
 * @date Oct 29, 2018
 */
public interface SysDeptService extends CurdService<SysDept> {

	/**
	 * 查询机构树
	 * @param userId 
	 * @return
	 */
	List<SysDept> findTree();
	/**
	 * 查询下级机构
	 * @param userId 
	 * @return
	 */
	public List<SysDept> findTreeByDepdId(Long id) ;
}
