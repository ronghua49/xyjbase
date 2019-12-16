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

import com.xyjsoft.admin.model.SysMenuRoute;
import com.xyjsoft.admin.dao.SysMenuRouteMapper;
import com.xyjsoft.admin.service.SysMenuRouteService;

/**
 * ---------------------------
 * 菜单路由表 (SysMenuRouteServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-10-28 18:45:36
 * ---------------------------
 */
@Service
public class SysMenuRouteServiceImpl extends AbstractManagerImpl<String, SysMenuRoute> implements SysMenuRouteService {

	@Autowired
	private SysMenuRouteMapper sysMenuRouteMapper;

	@Override
	public int save(SysMenuRoute record) {
		if(record.getId() == null || record.getId() == 0) {
			//路由重复验证
			List<SysMenuRoute> list = sysMenuRouteMapper.findByRoute(record.getMenuRoute());
			return sysMenuRouteMapper.add(record);
		}
		//路由重复验证
		List<SysMenuRoute> list = sysMenuRouteMapper.findByRouteAndNotId(record.getMenuRoute(),record.getId());
		return sysMenuRouteMapper.update(record);
	}

	@Override
	public int delete(SysMenuRoute record) {
		return sysMenuRouteMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysMenuRoute> records) {
		for(SysMenuRoute record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysMenuRouteMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysMenuRoute findById(Long id) {
		return sysMenuRouteMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysMenuRouteMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysMenuRoute> getDao() {
		return sysMenuRouteMapper;
	}
	
}
