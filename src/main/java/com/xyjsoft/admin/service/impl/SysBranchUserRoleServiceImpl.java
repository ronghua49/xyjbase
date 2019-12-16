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
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysBranchUserRole;
import com.xyjsoft.admin.dao.SysBranchUserRoleMapper;
import com.xyjsoft.admin.service.SysBranchUserRoleService;

/**
 * ---------------------------
 * 子公司用户角色表 (SysBranchUserRoleServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchUserRoleServiceImpl extends AbstractManagerImpl<String, SysBranchUserRole> implements SysBranchUserRoleService {

	@Autowired
	private SysBranchUserRoleMapper sysBranchUserRoleMapper;

	@Override
	public int save(SysBranchUserRole record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchUserRoleMapper.add(record);
		}
		return sysBranchUserRoleMapper.update(record);
	}

	@Override
	public int delete(SysBranchUserRole record) {
		return sysBranchUserRoleMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchUserRole> records) {
		for(SysBranchUserRole record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysBranchUserRoleMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchUserRole findById(Long id) {
		return sysBranchUserRoleMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchUserRoleMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchUserRole> getDao() {
		return sysBranchUserRoleMapper;
	}

	@Override
	public void saveNewRole(SysBranchUser saveNewUser) {
		SysBranchUserRole sysBranchUserRole = new SysBranchUserRole();
		sysBranchUserRole.setUserId(saveNewUser.getId());
		sysBranchUserRole.setRoleId(1L);
		save(sysBranchUserRole);
	}
	
}
