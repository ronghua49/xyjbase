package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysBranchPostMapper;
import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchPost;
import com.xyjsoft.admin.model.SysBranchPostUser;
import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.service.SysBranchPostService;
import com.xyjsoft.admin.service.SysBranchPostUserService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;

/**
 * ---------------------------
 * 子公司岗位表
 (SysBranchPostServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
@Service
public class SysBranchPostServiceImpl extends AbstractManagerImpl<String, SysBranchPost> implements SysBranchPostService {

	@Autowired
	private SysBranchPostMapper sysBranchPostMapper;
	@Autowired
	private SysBranchPostUserService sysBranchPostUserService;

	@Override
	public int save(SysBranchPost record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchPostMapper.add(record);
		}
		return sysBranchPostMapper.update(record);
	}

	@Override
	public int delete(SysBranchPost record) {
		return sysBranchPostMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchPost> records) {
		for(SysBranchPost record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			SysBranchPost findById = sysBranchPostMapper.findById(id);
			if(findById != null) {
				if(findById.getCid().equals(0L)) {
					throw new RuntimeException("当前节点为根节点,不可删除!");
				}
			}
			sysBranchPostMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchPost findById(Long id) {
		return sysBranchPostMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchPostMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchPost> getDao() {
		return sysBranchPostMapper;
	}

	@Override
	public List<SysBranchPost> findTree(String branchCode) {
		List<SysBranchPost> sysDepts = new ArrayList<>();
		List<SysBranchPost> depts = sysBranchPostMapper.findAll(branchCode);
		for (SysBranchPost dept : depts) {
			if (dept.getCid() == null || dept.getCid() == 0) {
				sysDepts.add(dept);
			}
		}
		findChildren(sysDepts, depts);
		return sysDepts;
	}
	
	private void findChildren(List<SysBranchPost> sysDepts, List<SysBranchPost> depts) {
		for (SysBranchPost sysDept : sysDepts) {
			List<SysBranchPost> children = new ArrayList<>();
			for (SysBranchPost dept : depts) {
				if (sysDept.getId() != null && sysDept.getId().equals(dept.getCid())) {
					//修改父级名字
					children.add(dept);
				}
			}
			sysDept.setChildren(children);
			findChildren(children, depts);
		}
	}

	@Override
	public void saveNewPost(SysBranch record,Long userId) {
		SysBranchPost sysBranchPost = new SysBranchPost();
		sysBranchPost.setBranchId(record.getId());
		sysBranchPost.setBranchCode(record.getCode());
		sysBranchPost.setName(record.getName());
		sysBranchPost.setCode(record.getCode());
		sysBranchPost.setCid(0L);
		sysBranchPost.setQueryRole("0");
		sysBranchPost.setRemarks("");
		save(sysBranchPost);
		//为管理员添加岗位
		SysBranchPostUser sysBranchPostUser = new SysBranchPostUser();
		sysBranchPostUser.setIsMain("1");
		sysBranchPostUser.setSysPostId(sysBranchPost.getId());
		sysBranchPostUser.setUserId(userId);
		sysBranchPostUserService.save(sysBranchPostUser);
	}
	
}
