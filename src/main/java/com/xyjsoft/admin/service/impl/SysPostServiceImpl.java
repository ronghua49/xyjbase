package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysPostMapper;
import com.xyjsoft.admin.dao.SysPostUserMapper;
import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.model.SysPostUser;
import com.xyjsoft.admin.service.SysPostService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.bean.LoginInfo;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;

/**
 * ---------------------------
 * 岗位表 (SysPostServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
@Service
public class SysPostServiceImpl extends AbstractManagerImpl<String, SysPost> implements SysPostService {

	@Autowired
	private SysPostMapper sysPostMapper;
	@Autowired
	private SysPostUserMapper sysPostUserMapper;

	@Override
	public int save(SysPost record) {
		if(record.getId() == null || record.getId() == 0) {
			//验证编号是否已经使用
			List<SysPost> list = sysPostMapper.getPostByCode(record.getCode());
			if(list.size() > 0) {
				throw new RuntimeException("编号已经被使用,请调整数据后提交");
			}
			return sysPostMapper.add(record);
		}
		//验证编号是否已经使用
		List<SysPost> list = sysPostMapper.getPostByCodeAndNotId(record.getCode(),record.getId());
		if(list.size() > 0) {
			throw new RuntimeException("编号已经被使用,请调整数据后提交");
		}
		return sysPostMapper.update(record);
	}

	@Override
	public int delete(SysPost record) {
		record = sysPostMapper.findById(record.getId());
		if(record == null) {
			return 1;
		}
		if(record.getCid().equals(0l)) {
			throw new RuntimeException("根节点不能删除");
		}
		List<SysPost> sysPosts = new ArrayList<>();
		ListLowerByCid(sysPosts,record.getId());
		List<Long> ids = new ArrayList<Long>();
		for (SysPost sysPost : sysPosts) {
			ids.add(sysPost.getId());
		}
		ids.add(record.getId());
		List<SysPostUser> findByPostIds = sysPostUserMapper.findByPostIds(ids);
		if(findByPostIds.size() > 0) {
			throw new RuntimeException("当前岗位下有"+findByPostIds.size()+"个员工,不可删除,请处理员工数据后删除岗位!");
		}else{
			int deleteT = deleteT(ids);
			sysPostUserMapper.deleteByPostId(record.getId());
			return deleteT;
		}
	}

	@Override
	public int delete(List<SysPost> records) {
		for(SysPost record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysPostMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysPost findById(Long id) {
		return sysPostMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysPostMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysPost> getDao() {
		return sysPostMapper;
	}

	@Override
	public List<SysPost> findTree() {
		List<SysPost> sysDepts = new ArrayList<>();
		List<SysPost> depts = sysPostMapper.findAll();
		for (SysPost dept : depts) {
			if (dept.getCid() == null || dept.getCid() == 0) {
				sysDepts.add(dept);
			}
		}
		findChildren(sysDepts, depts);
		return sysDepts;
	}
	
	private void findChildren(List<SysPost> sysDepts, List<SysPost> depts) {
		for (SysPost sysDept : sysDepts) {
			List<SysPost> children = new ArrayList<>();
			for (SysPost dept : depts) {
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
	public SysPost findByUser(LoginInfo loginUserInfo) {
		SysPost sysPost = sysPostMapper.findByUser(Long.parseLong(loginUserInfo.getUserid()));
		List<Long> list = new ArrayList<>();
		if("0".equals(sysPost.getQueryRole())) {
			//查全部
		}else if("1".equals(sysPost.getQueryRole())){
			//查下级岗位
			List<SysPost> sysPosts = new ArrayList<>();
			ListLowerByCid(sysPosts,sysPost.getId());
			sysPosts.add(sysPost);
			for (SysPost sysPost2 : sysPosts) {
				list.add(sysPost2.getId());
			}
			sysPost.setQueryUserIds(list);
		}else{
			//查自己
			list.add(Long.parseLong(loginUserInfo.getUserid()));
		}
		sysPost.setQueryUserIds(list);
		return sysPost;
	}

	@Override
	public SysPost findLowerPost(String postid) {
		SysPost findById = sysPostMapper.findById(Long.parseLong(postid));
		findLowerByCid(findById);
		return findById;
	}
	
	private void findLowerByCid(SysPost depts) {
		List<SysPost> sysPosts = sysPostMapper.findByCid(depts.getId());
		if(sysPosts.size() == 0) {
			return;
		}else{
			depts.setChildren(sysPosts);
			for (SysPost sysPost : sysPosts) {
				findLowerByCid(sysPost);
			}
		}
	}
	
	private void ListLowerByCid(List<SysPost> depts,Long id) {
		List<SysPost> sysPosts = sysPostMapper.findByCid(id);
		if(sysPosts.size() == 0) {
			return;
		}else{
			depts.addAll(sysPosts);
			for (SysPost sysPost : sysPosts) {
				ListLowerByCid(depts,sysPost.getId());
			}
		}
	}
	
	
}
