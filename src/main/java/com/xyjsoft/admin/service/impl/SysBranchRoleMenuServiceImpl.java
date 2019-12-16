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

import com.xyjsoft.admin.model.SysBranchRoleMenu;
import com.xyjsoft.admin.dao.SysBranchRoleMenuMapper;
import com.xyjsoft.admin.service.SysBranchRoleMenuService;

/**
 * ---------------------------
 * 子公司角色菜单表 (SysBranchRoleMenuServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchRoleMenuServiceImpl extends AbstractManagerImpl<String, SysBranchRoleMenu> implements SysBranchRoleMenuService {

	@Autowired
	private SysBranchRoleMenuMapper sysBranchRoleMenuMapper;

	@Override
	public int save(SysBranchRoleMenu record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchRoleMenuMapper.add(record);
		}
		return sysBranchRoleMenuMapper.update(record);
	}

	@Override
	public int delete(SysBranchRoleMenu record) {
		return sysBranchRoleMenuMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchRoleMenu> records) {
		for(SysBranchRoleMenu record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysBranchRoleMenuMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchRoleMenu findById(Long id) {
		return sysBranchRoleMenuMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchRoleMenuMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchRoleMenu> getDao() {
		return sysBranchRoleMenuMapper;
	}
	
}
