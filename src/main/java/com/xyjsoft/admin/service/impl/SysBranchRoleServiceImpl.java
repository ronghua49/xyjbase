package com.xyjsoft.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchMenu;
import com.xyjsoft.admin.model.SysBranchRole;
import com.xyjsoft.admin.model.SysBranchRoleMenu;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysMenu;
import com.xyjsoft.admin.model.SysRole;
import com.xyjsoft.admin.model.SysRoleMenu;
import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.dao.SysBranchMenuMapper;
import com.xyjsoft.admin.dao.SysBranchRoleMapper;
import com.xyjsoft.admin.dao.SysBranchRoleMenuMapper;
import com.xyjsoft.admin.service.SysBranchRoleService;

/**
 * ---------------------------
 * 子公司角色表 (SysBranchRoleServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchRoleServiceImpl extends AbstractManagerImpl<String, SysBranchRole> implements SysBranchRoleService {

	@Autowired
	private SysBranchRoleMapper sysBranchRoleMapper;
	@Autowired
	private SysBranchMenuMapper sysBranchMenuMapper;
	@Autowired
	private SysBranchRoleMenuMapper sysBranchRoleMenuMapper;

	@Override
	public int save(SysBranchRole record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchRoleMapper.add(record);
		}
		return sysBranchRoleMapper.update(record);
	}

	@Override
	public int delete(SysBranchRole record) {
		return sysBranchRoleMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchRole> records) {
		for(SysBranchRole record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysBranchRoleMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchRole findById(Long id) {
		return sysBranchRoleMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchRoleMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchRole> getDao() {
		return sysBranchRoleMapper;
	}

	@Override
	public List<SysBranchMenu> findRoleMenus(Long roleId) {
		return sysBranchMenuMapper.findRoleMenus(roleId);
	}

	@Override
	public int saveRoleMenus(List<SysBranchRoleMenu> records) {
		if(records == null || records.isEmpty()) {
			return 1;
		}
		Long roleId = records.get(0).getRoleId(); 
		sysBranchRoleMenuMapper.deleteByRoleId(roleId);
		for(SysBranchRoleMenu record:records) {
			sysBranchRoleMenuMapper.insertSelective(record);
		}
		return 1;
	}


}
