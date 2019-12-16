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
import com.xyjsoft.admin.model.SysBranchDept;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.dao.SysBranchMapper;
import com.xyjsoft.admin.service.SysBranchDeptService;
import com.xyjsoft.admin.service.SysBranchPostService;
import com.xyjsoft.admin.service.SysBranchRoleService;
import com.xyjsoft.admin.service.SysBranchService;
import com.xyjsoft.admin.service.SysBranchUserRoleService;
import com.xyjsoft.admin.service.SysBranchUserService;
import com.xyjsoft.admin.service.SysSequenceService;

/**
 * ---------------------------
 * 子公司表 (SysBranchServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchServiceImpl extends AbstractManagerImpl<String, SysBranch> implements SysBranchService {

	@Autowired
	private SysBranchMapper sysBranchMapper;
	@Autowired
	private SysBranchUserService sysBranchUserService;
	@Autowired
	private SysBranchDeptService sysBranchDeptService;
	@Autowired
	private SysBranchUserRoleService sysBranchUserRoleService;
	@Autowired
	private SysSequenceService sysSequenceService;
	@Autowired
	private SysBranchPostService sysBranchPostService;

	@Override
	public int save(SysBranch record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchMapper.add(record);
		}
		return sysBranchMapper.update(record);
	}
	
	@Override
	public SysBranch saveBean(SysBranch record) {
		if(record.getId() == null || record.getId() == 0) {
			//创建公司信息
			//需要处理,从编号功能获取编号
			record.setCode(sysSequenceService.nextVal("branchCode"));
			record.setDelFlag("0");
			sysBranchMapper.add(record);
			//创建根机构
			SysBranchDept saveNewDept = sysBranchDeptService.saveNewDept(record);
			//创建管理员
			SysBranchUser saveNewUser = sysBranchUserService.saveNewUser(record,saveNewDept.getId());
			//分配权限
			sysBranchUserRoleService.saveNewRole(saveNewUser);
			//创建根岗位
			sysBranchPostService.saveNewPost(record,saveNewUser.getId());
			return record;
		}else{
			sysBranchMapper.update(record);
			return record;
		}
	}

	@Override
	public int delete(SysBranch record) {
		return sysBranchMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranch> records) {
		for(SysBranch record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysBranchMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranch findById(Long id) {
		return sysBranchMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranch> getDao() {
		return sysBranchMapper;
	}

	@Override
	public String StartOrStop(Long id) {
		SysBranch findById = sysBranchMapper.findById(id);
		if(findById == null) {
			throw new RuntimeException("公司不存在或已经被删除!");
		}
		if("0".equals(findById.getDelFlag())) {
			findById.setDelFlag("1");
			save(findById);
			return "1";
		}else{
			findById.setDelFlag("0");
			save(findById);
			return "0";
		}
	}
	
}
