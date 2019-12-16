package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xyjsoft.admin.dao.SysRoleMapper;
import com.xyjsoft.admin.dao.SysUserMapper;
import com.xyjsoft.admin.dao.SysUserRoleMapper;
import com.xyjsoft.admin.model.SysDept;
import com.xyjsoft.admin.model.SysMenu;
import com.xyjsoft.admin.model.SysPostUser;
import com.xyjsoft.admin.model.SysRole;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.model.SysUserRole;
import com.xyjsoft.admin.service.SysDeptService;
import com.xyjsoft.admin.service.SysMenuService;
import com.xyjsoft.admin.service.SysPostUserService;
import com.xyjsoft.admin.service.SysUserService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.exception.XyjException;
import com.xyjsoft.core.feign.XyjImFegin;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.ColumnFilter;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryField;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.query.QueryOP;
import com.xyjsoft.core.util.PasswordUtils;

@Service
public class SysUserServiceImpl extends AbstractManagerImpl<String, SysUser> implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysPostUserService sysPostUserService;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private XyjImFegin xyjImFegin;

	@Transactional
	@Override
	public int save(SysUser record) {
		Long id = null;
		if (record.getId() == null || record.getId() == 0) {
			// 判断用户名是否重复
			SysUser sysUser = sysUserMapper.findByName(record.getName());
			if (sysUser != null) {
				throw new XyjException("用户名已注册");
			}
			sysUserMapper.insertSelective(record);
			id = record.getId();

		} else {
			// 更新用户信息
			id = record.getId();
			sysUserMapper.updateByPrimaryKeySelective(record);
		}
		// 更新用户角色
		if (id != null && id == 0) {
			throw new XyjException("用户提交数据异常");
		}
		//先删除所有角色
		sysUserRoleMapper.deleteByUserId(id);
		if (record.getUserRoles().size() != 0) {
			if (id != null) {
				for (SysUserRole sysUserRole : record.getUserRoles()) {
					sysUserRole.setUserId(id);
				}
			} else {
				sysUserRoleMapper.deleteByUserId(record.getId());
			}
			for (SysUserRole sysUserRole : record.getUserRoles()) {
				sysUserRoleMapper.insertSelective(sysUserRole);
			}
		}
		// 更新用户岗位
		// 先删除旧岗位
		sysPostUserService.deleteByUserId(record.getId());
		// 新增新岗位
		List<SysPostUser> postUsers = record.getPostUsers();
		if (postUsers.size() == 0) {
			throw new RuntimeException("请选择至少一个主岗位");
		}

		for (SysPostUser sysPostUser : postUsers) {
			sysPostUser.setUserId(record.getId());
		}
		sysPostUserService.saveList(postUsers.toArray(new SysPostUser[0]));
		//处理消息用户新增
		HttpResult synSave = xyjImFegin.synSave(record.getId(), "",record.getName(), record.getMobile(), "", "");
		long imId = Long.parseLong(synSave.getData().toString());
		if(imId != 0) {
			//新增了数据
			record.setChatid(id);
			sysUserMapper.updateByPrimaryKeySelective(record);
		}
		// 新增用户
		return 1;
	}

	@Override
	public int delete(SysUser record) {
		// 删除员工下岗位信息
		sysPostUserService.deleteByUserId(record.getId());
		return sysUserMapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public int delete(List<SysUser> records) {
		for (SysUser record : records) {
			delete(record);
		}
		return 1;
	}

	@Override
	public int deleteT(List<Long> ids) {
		for (Long id : ids) {
			sysUserMapper.deleteByPrimaryKey(id);
		}
		return 0;
	}

	@Override
	public SysUser findById(Long id) {
		return sysUserMapper.selectByPrimaryKey(id);
	}

	@Override
	public SysUser findByName(String name) {
		return sysUserMapper.findByName(name);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		PageResult pageResult = null;
		String name = getColumnFilterValue(pageRequest, "name");
		String email = getColumnFilterValue(pageRequest, "email");
		if (name != null) {
			if (email != null) {
				pageResult = MybatisPageHelper.findPage(pageRequest, sysUserMapper, "findPageByNameAndEmail", name,
						email);
			} else {
				pageResult = MybatisPageHelper.findPage(pageRequest, sysUserMapper, "findPageByName", name);
			}
		} else {
			pageResult = MybatisPageHelper.findPage(pageRequest, sysUserMapper);
		}
		// 加载用户角色信息
		findUserRoles(pageResult);
		return pageResult;
	}

	/**
	 * 获取过滤字段的值
	 * 
	 * @param filterName
	 * @return
	 */
	public String getColumnFilterValue(PageRequest pageRequest, String filterName) {
		String value = null;
		ColumnFilter columnFilter = pageRequest.getColumnFilter(filterName);
		if (columnFilter != null) {
			value = columnFilter.getValue();
		}
		return value;
	}

	/**
	 * 加载用户角色
	 * 
	 * @param pageResult
	 */
	private void findUserRoles(PageResult pageResult) {
		List<?> content = pageResult.getContent();
		for (Object object : content) {
			SysUser sysUser = (SysUser) object;
			List<SysUserRole> userRoles = findUserRoles(sysUser.getId());
			sysUser.setUserRoles(userRoles);
			sysUser.setRoleNames(getRoleNames(userRoles));
		}
	}

	private String getRoleNames(List<SysUserRole> userRoles) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<SysUserRole> iter = userRoles.iterator(); iter.hasNext();) {
			SysUserRole userRole = iter.next();
			SysRole sysRole = sysRoleMapper.selectByPrimaryKey(userRole.getRoleId());
			if (sysRole == null) {
				continue;
			}
			sb.append(sysRole.getRemark());
			if (iter.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	@Override
	public Set<String> findPermissions(String userName) {
		Set<String> perms = new HashSet<>();
		List<SysMenu> sysMenus = sysMenuService.findByUser(userName);
		for (SysMenu sysMenu : sysMenus) {
			if (sysMenu.getPerms() != null && !"".equals(sysMenu.getPerms())) {
				perms.add(sysMenu.getPerms());
			}
		}
		return perms;
	}

	@Override
	public List<SysUserRole> findUserRoles(Long userId) {
		return sysUserRoleMapper.findUserRoles(userId);
	}

	@Override
	public String updatePwd(String name, String password, String newPWD) {
		// TODO Auto-generated method stub
		// 校验密码
		String msg = "密码修改成功";
		SysUser user = sysUserMapper.findByName(name);
		if (user == null) {
			return msg = "账号不存在";
		}
		if (password.equals(newPWD)) {
			return msg = "旧密码与新密码相同，无需修改";
		}
		if (!PasswordUtils.matches(user.getSalt(), password, user.getPassword())) {
			msg = "密码校验失败";
			return msg;
		}
		// 加密
		String salt = PasswordUtils.getSalt();
		String pwd = PasswordUtils.encode(newPWD, salt);
		user.setSalt(salt);
		user.setPassword(pwd);
		sysUserMapper.updateByPrimaryKey(user);
		return msg;
	}

	@Override
	protected MyBatisDao<String, SysUser> getDao() {
		return sysUserMapper;
	}

	@Override
	public PageList<SysUser> listUserByDeptIds(QueryFilter queryFilter) throws Exception {
		// 取出deptId并查找机构下所有机构
		if (queryFilter == null) {
			return new PageList<SysUser>(sysUserMapper.findPage());
		}
		List<QueryField> querys = queryFilter.getQuerys();
		if (querys == null || querys.size() == 0) {
			return new PageList<SysUser>(sysUserMapper.findPage());
		}
		for (QueryField queryField : querys) {
			if ("deptId".equals(queryField.getProperty())) {
				String deptId = queryField.getValue().toString();
				// 根据机构ID查询下级所有机构
				List<Long> deptIds = new ArrayList<Long>();
				List<SysDept> findTreeByDepdId = sysDeptService.findTreeByDepdId(Long.parseLong(deptId));
				for (SysDept sysDept : findTreeByDepdId) {
					deptIds.add(sysDept.getId());
				}
				// 封装query
				if (deptIds.size() == 0) {
					queryField.setOperation(QueryOP.IN);
					deptIds.add(Long.parseLong(deptId));
					queryField.setValue(deptIds);
					queryField.setHasInitValue(false);
				} else {
					queryField.setOperation(QueryOP.IN);
					queryField.setValue(deptIds);
					queryField.setHasInitValue(false);
				}
			}
		}
		queryFilter.setClazz(SysUser.class);
		Map<String, Object> params = queryFilter.getParams();
		List<SysUser> query = sysUserMapper.query(params);
		return new PageList<SysUser>(query);
	}

	@Override
	public void changeBind(Long userId, Long imId) {
		SysUser findById = sysUserMapper.findById(userId);
		if(findById == null) {
			throw new RuntimeException("未查询到用户信息");
		}
		findById.setChatid(imId);
		sysUserMapper.update(findById);
		
	}
}
