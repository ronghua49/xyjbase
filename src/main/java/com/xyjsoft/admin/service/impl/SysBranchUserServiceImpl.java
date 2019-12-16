package com.xyjsoft.admin.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysBranchMapper;
import com.xyjsoft.admin.dao.SysBranchMenuMapper;
import com.xyjsoft.admin.dao.SysBranchUserMapper;
import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchMenu;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.service.SysBranchMenuService;
import com.xyjsoft.admin.service.SysBranchUserService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.feign.XyjImFegin;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.util.PasswordUtils;

/**
 * ---------------------------
 * 子公司用户表 (SysBranchUserServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchUserServiceImpl extends AbstractManagerImpl<String, SysBranchUser> implements SysBranchUserService {

	@Autowired
	private SysBranchUserMapper sysBranchUserMapper;
	@Autowired
	private SysBranchMapper sysBranchMapper;
	@Autowired
	private SysBranchMenuService sysBranchMenuService;
	@Autowired
	private XyjImFegin xyjImFegin;

	@Override
	public int save(SysBranchUser record) {
		if(record.getId() == null || record.getId() == 0) {
			int add = sysBranchUserMapper.add(record);
			//处理消息用户新增
			HttpResult synSave = xyjImFegin.synSave(record.getId(), record.getRealname(),record.getUsername(), record.getMobile(), "", record.getRemark());
			long imId = Long.parseLong(synSave.getData().toString());
			if(imId != 0) {
				//新增了数据
				record.setChatid(imId);
				sysBranchUserMapper.update(record);
			}
			return add;
		}
		return sysBranchUserMapper.update(record);
	}

	@Override
	public int delete(SysBranchUser record) {
		return sysBranchUserMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchUser> records) {
		for(SysBranchUser record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			SysBranchUser findById = sysBranchUserMapper.findById(id);
			if(findById != null) {
				//查询登入编号是否与公司编号相同,如果相同表示为该子公司管理员 不可删除
				SysBranch findById2 = sysBranchMapper.findById(findById.getBranchId());
				if(findById2.getCode().equals(findById.getUsername())) {
					throw new RuntimeException("该用户为系统管理员,不可删除!");
				}
			}
			sysBranchUserMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchUser findById(Long id) {
		return sysBranchUserMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchUserMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchUser> getDao() {
		return sysBranchUserMapper;
	}

	@Override
	public SysBranchUser saveBean(SysBranchUser record) {
		if(record.getId() == null || record.getId() == 0) {
			sysBranchUserMapper.add(record);
			HttpResult synSave = xyjImFegin.synSave(record.getId(), record.getRealname(),record.getUsername(), record.getMobile(), "", record.getRemark());
			long imId = Long.parseLong(synSave.getData().toString());
			if(imId != 0) {
				//新增了数据
				record.setChatid(imId);
				sysBranchUserMapper.update(record);
			}
			return record;
		}
		sysBranchUserMapper.update(record);
		return record;
	}
	
	@Override
	public SysBranchUser saveNewUser(SysBranch record,Long deptId) {
		SysBranchUser sysBranchUser = new SysBranchUser();
		String salt = PasswordUtils.getSalt();
		sysBranchUser.setBranchId(record.getId());
		sysBranchUser.setBranchCode(record.getCode());
		sysBranchUser.setUsername(record.getCode());
		sysBranchUser.setPassword(PasswordUtils.encode(record.getCode(), salt));
		sysBranchUser.setDeptId(deptId);
		sysBranchUser.setSalt(salt);
		sysBranchUser.setRealname(record.getName());
		sysBranchUser.setEmail(record.getEmail());
		sysBranchUser.setMobile(record.getMobile());
		sysBranchUser.setStatus("0");
		sysBranchUser.setRemark(record.getRemark());
		SysBranchUser saveBean = saveBean(sysBranchUser);
		//处理消息用户新增
		HttpResult synSave = xyjImFegin.synSave(sysBranchUser.getId(), sysBranchUser.getRealname(),sysBranchUser.getUsername(), sysBranchUser.getMobile(), "", sysBranchUser.getRemark());
		long imId = Long.parseLong(synSave.getData().toString());
		if(imId != 0) {
			//新增了数据
			saveBean.setChatid(imId);
			sysBranchUserMapper.update(saveBean);
		}
		return saveBean;
	}

	@Override
	public SysBranchUser findByName(String username) {
		return sysBranchUserMapper.findByName(username);
		
	}

	@Override
	public Set<String> findPermissions(String userName) {
		Set<String> perms = new HashSet<>();
		List<SysBranchMenu> sysMenus = sysBranchMenuService.findByUser(userName);
		for (SysBranchMenu sysMenu : sysMenus) {
			if (sysMenu.getPerms() != null && !"".equals(sysMenu.getPerms())) {
				perms.add(sysMenu.getPerms());
			}
		}
		return perms;
	}

	@Override
	public void changeBind(Long userId, Long imId) {
		SysBranchUser findById = sysBranchUserMapper.findById(userId);
		if(findById == null) {
			throw new RuntimeException("未查询到用户信息");
		}
		findById.setChatid(imId);
		sysBranchUserMapper.update(findById);
	}
	
}
