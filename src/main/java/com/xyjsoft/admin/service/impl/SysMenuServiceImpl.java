package com.xyjsoft.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.dao.SysMenuMapper;
import com.xyjsoft.admin.model.SysMenu;
import com.xyjsoft.admin.service.SysMenuService;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.util.SecurityUtils;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Override
	public int save(SysMenu record) {
		String name = SecurityUtils.getUsername();
		if(record.getId() == null || record.getId() == 0) {
			record.setCreateBy(name);
			record.setCreateTime(new Date());
			int result = sysMenuMapper.insertSelective(record);
			//只有增加菜单时，增加此页面下的所有按钮权限，宋建国 2019-07-31
			if(record.getButtons() != null & record.getType() == 1){
			//批量增加按钮权限 宋建国 2019-07-31
				SysMenu sysMenu = new SysMenu();
				for(SysMenu button:record.getButtons()) {
					sysMenu.setId(null);
					sysMenu.setName(button.getName());
					sysMenu.setParentId(record.getId());
					sysMenu.setPerms(button.getPerms());
					sysMenu.setType(2);
					sysMenu.setOrderNum(0);
					sysMenu.setCreateBy(name);
					sysMenu.setCreateTime(new Date());
					sysMenu.setDelFlag((byte) 0);
					sysMenuMapper.insertSelective(sysMenu);
				}
			}
			return result;
		}
		if(record.getParentId() == null) {
			record.setParentId(0L);
		}
		record.setLastUpdateBy(name);
		record.setLastUpdateTime(new Date());
		return sysMenuMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(SysMenu record) {
		return sysMenuMapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public int delete(List<SysMenu> records) {
		for(SysMenu record:records) {
			delete(record);
		}
		return 1;
	}
	
	@Override
	public int deleteT(List<Long> ids) {
		for(Long id : ids) {
			sysMenuMapper.deleteByPrimaryKey(id);
		}
		return 0;
	}
	
	@Override
	public SysMenu findById(Long id) {
		return sysMenuMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysMenuMapper);
	}
	
	@Override
	public List<SysMenu> findTree(String userName, int menuType) {
		List<SysMenu> sysMenus = new ArrayList<>();
		List<SysMenu> menus = findByUser(userName);
		for (SysMenu menu : menus) {
			if (menu.getParentId() == null || menu.getParentId() == 0) {
				menu.setLevel(0);;
				if(!exists(sysMenus, menu)) {
					sysMenus.add(menu);
				}
			}
		}
		sysMenus.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
		findChildren(sysMenus, menus, menuType);
		return sysMenus;
	}

	@Override
	public List<SysMenu> findByUser(String userName) {
		if(userName == null || "".equals(userName) || SysConstants.ADMIN.equalsIgnoreCase(userName)) {
			return sysMenuMapper.findAll();
		}
		return sysMenuMapper.findByUserName(userName);
	}

	private void findChildren(List<SysMenu> SysMenus, List<SysMenu> menus, int menuType) {		
		for (SysMenu SysMenu : SysMenus) {
			List<SysMenu> children = new ArrayList<>();
			for (SysMenu menu : menus) {
				if(menuType == 1 && menu.getType() == 2) {
					// 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
					continue ;
				}
				if (SysMenu.getId() != null && SysMenu.getId().equals(menu.getParentId())) {
					menu.setParentName(SysMenu.getName());
					menu.setLevel(SysMenu.getLevel() + 1);
					if(!exists(children, menu)) {
						children.add(menu);
					}
				}
			}
			SysMenu.setChildren(children);
			children.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
			findChildren(children, menus, menuType);
		}
	}

	private boolean exists(List<SysMenu> sysMenus, SysMenu sysMenu) {
		boolean exist = false;
		for(SysMenu menu:sysMenus) {
			if(menu.getId().equals(sysMenu.getId())) {
				exist = true;
			}
		}
		return exist;
	}
	
}
