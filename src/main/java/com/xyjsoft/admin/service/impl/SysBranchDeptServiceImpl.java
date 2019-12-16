package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
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
import com.xyjsoft.admin.model.SysDept;
import com.xyjsoft.admin.dao.SysBranchDeptMapper;
import com.xyjsoft.admin.service.SysBranchDeptService;

/**
 * ---------------------------
 * 子公司机构表 (SysBranchDeptServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchDeptServiceImpl extends AbstractManagerImpl<String, SysBranchDept> implements SysBranchDeptService {

	@Autowired
	private SysBranchDeptMapper sysBranchDeptMapper;

	@Override
	public int save(SysBranchDept record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchDeptMapper.add(record);
		}
		return sysBranchDeptMapper.update(record);
	}

	@Override
	public int delete(SysBranchDept record) {
		return sysBranchDeptMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchDept> records) {
		for(SysBranchDept record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			SysBranchDept findById = sysBranchDeptMapper.findById(id);
			if(findById != null) {
				throw new RuntimeException("当前节点为根节点,不可删除!");
			}
			sysBranchDeptMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchDept findById(Long id) {
		return sysBranchDeptMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchDeptMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchDept> getDao() {
		return sysBranchDeptMapper;
	}

	@Override
	public SysBranchDept saveNewDept(SysBranch record) {
		SysBranchDept sysBranchDept = new SysBranchDept();
		sysBranchDept.setBranchId(record.getId());
		sysBranchDept.setBranchCode(record.getCode());
		sysBranchDept.setName(record.getName());
		sysBranchDept.setParentId(0L);
		sysBranchDept.setOrderNum(0);
		sysBranchDept.setRemark("");
		save(sysBranchDept);
		return sysBranchDept;
	}

	@Override
	public List<SysBranchDept> findTree(String branchCode) {
		List<SysBranchDept> sysDepts = new ArrayList<>();
		List<SysBranchDept> depts = sysBranchDeptMapper.findAllByBranchCode(branchCode);
		for (SysBranchDept dept : depts) {
			if (dept.getParentId() == null || dept.getParentId() == 0) {
				dept.setLevel(0);
				sysDepts.add(dept);
			}
		}
		findChildren(sysDepts, depts);
		return sysDepts;
	}
	
	private void findChildren(List<SysBranchDept> sysDepts, List<SysBranchDept> depts) {
		for (SysBranchDept sysDept : sysDepts) {
			List<SysBranchDept> children = new ArrayList<>();
			for (SysBranchDept dept : depts) {
				if (sysDept.getId() != null && sysDept.getId().equals(dept.getParentId())) {
					//修改父级名字
					//dept.setParentName(dept.getName());
					dept.setParentName(sysDept.getName());
					dept.setLevel(sysDept.getLevel() + 1);
					children.add(dept);
				}
			}
			sysDept.setChildren(children);
			findChildren(children, depts);
		}
	}
	
}
