package com.xyjsoft.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.dao.SysMenuMapper;
import com.xyjsoft.admin.dao.SysRoleMapper;
import com.xyjsoft.admin.dao.SysRoleMenuMapper;
import com.xyjsoft.admin.model.SysMenu;
import com.xyjsoft.admin.model.SysRole;
import com.xyjsoft.admin.model.SysRoleMenu;
import com.xyjsoft.admin.service.SysRoleService;
import com.xyjsoft.core.page.ColumnFilter;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.util.SecurityUtils;

@Service
public class SysRoleServiceImpl  implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysRoleMenuMapper sysRoleMenuMapper;
	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Override
	public int save(SysRole record) {
		String name = SecurityUtils.getUsername();
		if(record.getId() == null || record.getId() == 0) {
			record.setCreateBy(name);
			record.setCreateTime(new Date());
			return sysRoleMapper.insertSelective(record);
		}
		record.setLastUpdateBy(name);
		record.setLastUpdateTime(new Date());
		return sysRoleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(SysRole record) {
		return sysRoleMapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public int delete(List<SysRole> records) {
		for(SysRole record:records) {
			delete(record);
		}
		return 1;
	}

	@Override
	public int deleteT(List<Long> ids) {
		for(Long id : ids) {
			sysRoleMapper.deleteByPrimaryKey(id);
		}
		return 0;
	}
	
	@Override
	public SysRole findById(Long id) {
		return sysRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		ColumnFilter columnFilter = pageRequest.getColumnFilter("name");
		if(columnFilter != null && columnFilter.getValue() != null) {
			return MybatisPageHelper.findPage(pageRequest, sysRoleMapper, "findPageByName", columnFilter.getValue());
		}
		return MybatisPageHelper.findPage(pageRequest, sysRoleMapper);
	}

	@Override
	public List<SysRole> findAll() {
		return sysRoleMapper.findAll();
	}

	public SysRoleMapper getSysRoleMapper() {
		return sysRoleMapper;
	}

	public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
		this.sysRoleMapper = sysRoleMapper;
	}

	@Override
	public List<SysMenu> findRoleMenus(Long roleId) {
		SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
		if(SysConstants.ADMIN.equalsIgnoreCase(sysRole.getName())) {
			// 如果是超级管理员，返回全部
			return sysMenuMapper.findAll();
		}
		return sysMenuMapper.findRoleMenus(roleId);
	}

	@Transactional
	@Override
	public int saveRoleMenus(List<SysRoleMenu> records) {
		if(records == null || records.isEmpty()) {
			return 1;
		}
		Long roleId = records.get(0).getRoleId(); 
		sysRoleMenuMapper.deleteByRoleId(roleId);
		for(SysRoleMenu record:records) {
			sysRoleMenuMapper.insertSelective(record);
		}
		return 1;
	}

	@Override
	public List<SysRole> findByName(String name) {
		return sysRoleMapper.findByName(name);
	}
	
}
