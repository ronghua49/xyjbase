package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysDeptMapper;
import com.xyjsoft.admin.model.SysDept;
import com.xyjsoft.admin.service.SysDeptService;
import com.xyjsoft.core.exception.XyjException;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;

@Service
public class SysDeptServiceImpl implements SysDeptService {

	@Autowired
	private SysDeptMapper sysDeptMapper;

	@Override
	public int save(SysDept record) {
		Long parentId= record.getParentId();
		if(parentId==null){
			parentId=0L;
		}
		record.setParentId(parentId);		
		if(record.getId() == null || record.getId() == 0) {
			return sysDeptMapper.insertSelective(record);
		}else{
			//sysDeptMapper.findAll();
			if(record.getId().equals(parentId)){
			    throw new XyjException("不能移动部门到该部门下");
            }
			//查询再数据库的实际情况
			String childId = sysDeptMapper.getChildId(record.getId());
			if (childId.contains(parentId.toString())) {
				throw new XyjException("不能移动部门到该部门下");
			}
		}
		return sysDeptMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(SysDept record) {
		return sysDeptMapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public int delete(List<SysDept> records) {
		for(SysDept record:records) {
			delete(record);
		}
		return 1;
	}

	@Override
	public int deleteT(List<Long> ids) {
		for(Long id : ids) {
			sysDeptMapper.deleteByPrimaryKey(id);
		}
		return 0;
	}
	
	@Override
	public SysDept findById(Long id) {
		return sysDeptMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysDeptMapper);
	}
	
	public List<SysDept> findTreeByDepdId(Long id) {
		List<SysDept> sysDepts = new ArrayList<>();
		SysDept selectByPrimaryKey = sysDeptMapper.selectByPrimaryKey(id);
		if(selectByPrimaryKey == null) {
			return sysDepts;
		}
		findTreeByDepdId(sysDepts,selectByPrimaryKey);
		return sysDepts;
	}
	
	public void findTreeByDepdId(List<SysDept> sysDepts,SysDept dept) {
		sysDepts.add(dept);
		List<SysDept> sonDept = sysDeptMapper.findByUpid(dept.getId());
		if(sonDept.size() == 0) {
			return;
		}
		for (SysDept sysDept : sonDept) {
			findTreeByDepdId(sysDepts,sysDept);
		}
	}
	
	@Override
	public List<SysDept> findTree() {
		List<SysDept> sysDepts = new ArrayList<>();
		List<SysDept> depts = sysDeptMapper.findAll();
		for (SysDept dept : depts) {
			if (dept.getParentId() == null || dept.getParentId() == 0) {
				dept.setLevel(0);
				sysDepts.add(dept);
			}
		}
		findChildren(sysDepts, depts);
		return sysDepts;
	}

	private void findChildren(List<SysDept> sysDepts, List<SysDept> depts) {
		for (SysDept sysDept : sysDepts) {
			List<SysDept> children = new ArrayList<>();
			for (SysDept dept : depts) {
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
