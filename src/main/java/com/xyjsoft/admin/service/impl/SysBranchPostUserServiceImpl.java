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
import com.xyjsoft.admin.model.SysBranchPost;
import com.xyjsoft.admin.model.SysBranchPostUser;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.dao.SysBranchPostMapper;
import com.xyjsoft.admin.dao.SysBranchPostUserMapper;
import com.xyjsoft.admin.dao.SysBranchUserMapper;
import com.xyjsoft.admin.service.SysBranchPostUserService;

/**
 * ---------------------------
 * 子公司用户岗位表
 (SysBranchPostUserServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
@Service
public class SysBranchPostUserServiceImpl extends AbstractManagerImpl<String, SysBranchPostUser> implements SysBranchPostUserService {

	@Autowired
	private SysBranchPostUserMapper sysBranchPostUserMapper;
	@Autowired
	private SysBranchUserMapper sysBranchUserMapper;
	@Autowired
	private SysBranchPostMapper sysBranchPostMapper;

	@Override
	public int save(SysBranchPostUser record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchPostUserMapper.add(record);
		}
		return sysBranchPostUserMapper.update(record);
	}

	@Override
	public int delete(SysBranchPostUser record) {
		return sysBranchPostUserMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchPostUser> records) {
		for(SysBranchPostUser record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysBranchPostUserMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchPostUser findById(Long id) {
		return sysBranchPostUserMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchPostUserMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchPostUser> getDao() {
		return sysBranchPostUserMapper;
	}

	@Override
	public List<SysBranchUser> findUserById(Long id) {
		List<SysBranchUser> users = sysBranchUserMapper.findUserByPostId(id);
		return users;
	}

	@Override
	public List<SysBranchPost> findPostByUserId(Long id) {
		return sysBranchPostMapper.findPostByUserId(id);
	}
	
}
