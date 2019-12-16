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

import com.xyjsoft.admin.model.SysBranchMenu;
import com.xyjsoft.admin.model.SysMenu;
import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.dao.SysBranchMenuMapper;
import com.xyjsoft.admin.service.SysBranchMenuService;

/**
 * ---------------------------
 * 子公司菜单表 (SysBranchMenuServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@Service
public class SysBranchMenuServiceImpl extends AbstractManagerImpl<String, SysBranchMenu> implements SysBranchMenuService {

	@Autowired
	private SysBranchMenuMapper sysBranchMenuMapper;

	@Override
	public int save(SysBranchMenu record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysBranchMenuMapper.add(record);
		}
		return sysBranchMenuMapper.update(record);
	}

	@Override
	public int delete(SysBranchMenu record) {
		return sysBranchMenuMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysBranchMenu> records) {
		for(SysBranchMenu record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysBranchMenuMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysBranchMenu findById(Long id) {
		return sysBranchMenuMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysBranchMenuMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysBranchMenu> getDao() {
		return sysBranchMenuMapper;
	}

	@Override
	public List<SysBranchMenu> findTree(String userName, int menuType) {
		List<SysBranchMenu> sysMenus = new ArrayList<>();
		List<SysBranchMenu> menus = findByUser(userName);
		for (SysBranchMenu menu : menus) {
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
	public List<SysBranchMenu> findByUser(String userName) {
		if(userName == null || "".equals(userName) || SysConstants.ADMIN.equalsIgnoreCase(userName)) {
			return sysBranchMenuMapper.findAll();
		}
		return sysBranchMenuMapper.findByUserName(userName);
	}
	
	private boolean exists(List<SysBranchMenu> sysMenus, SysBranchMenu sysMenu) {
		boolean exist = false;
		for(SysBranchMenu menu:sysMenus) {
			if(menu.getId().equals(sysMenu.getId())) {
				exist = true;
			}
		}
		return exist;
	}
	
	private void findChildren(List<SysBranchMenu> SysMenus, List<SysBranchMenu> menus, int menuType) {		
		for (SysBranchMenu SysMenu : SysMenus) {
			List<SysBranchMenu> children = new ArrayList<>();
			for (SysBranchMenu menu : menus) {
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
	
}
