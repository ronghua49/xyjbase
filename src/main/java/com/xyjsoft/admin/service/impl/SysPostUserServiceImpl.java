package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysPostMapper;
import com.xyjsoft.admin.dao.SysPostUserMapper;
import com.xyjsoft.admin.dao.SysUserMapper;
import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.model.SysPostUser;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.service.SysPostUserService;
import com.xyjsoft.admin.vo.SysPostUserVo;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.util.StringUtils;

/**
 * ---------------------------
 * 岗位员工表 (SysPostUserServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
@Service
public class SysPostUserServiceImpl extends AbstractManagerImpl<String, SysPostUser> implements SysPostUserService {

	@Autowired
	private SysPostUserMapper sysPostUserMapper;
	@Autowired
	private SysPostMapper sysPostMapper;
	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public int save(SysPostUser record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysPostUserMapper.add(record);
		}
		return sysPostUserMapper.update(record);
	}

	@Override
	public int delete(SysPostUser record) {
		return sysPostUserMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysPostUser> records) {
		for(SysPostUser record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysPostUserMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysPostUser findById(Long id) {
		return sysPostUserMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysPostUserMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysPostUser> getDao() {
		return sysPostUserMapper;
	}
	
	@Override
	public void deleteByUserId(Long id) {
		sysPostUserMapper.deleteByUserId(id);
	}
	
	@Override
	public void saveList(SysPostUser[] record) {
		int a = 0;
		for (SysPostUser sysPostUser : record) {
			if("1".equals(sysPostUser.getIsMain())) {
				a++;
			}
		}
		if(a > 1) {
			throw new RuntimeException("员工只能有一个主岗,请检查数据重新提交!");
		}
		for (SysPostUser sysPostUser : record) {
			if(sysPostUser.getSysPostId() == null || sysPostUser.getUserId() == null || StringUtils.isBlank(sysPostUser.getIsMain())) {
				throw new RuntimeException("提交数据字段缺失,请验证后重新提交");
			}
			save(sysPostUser);
		}
	}

	@Override
	public List<SysPostUser> findByPostIds(List<SysPost> sysPosts) {
		if(sysPosts.size() == 0) {
			return new ArrayList<SysPostUser>();
		}
		List<Long> postIds = new ArrayList<Long>();
		for (SysPost sysPost : sysPosts) {
			postIds.add(sysPost.getId());
		}
		List<SysPostUser> list = sysPostUserMapper.findByPostIds(postIds);
		return list;
	}

	@Override
	public List<SysUser> findUserById(Long id) {
		List<SysUser> users = sysUserMapper.findUserByPostId(id);
		return users;
	}

	@Override
	public List<SysPostUserVo> findPostByUserId(Long id) {
		return sysPostMapper.findPostByUserId(id);
	}
	
}
