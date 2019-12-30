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

import com.xyjsoft.admin.model.SysComponent;
import com.xyjsoft.admin.dao.SysComponentMapper;
import com.xyjsoft.admin.service.SysComponentService;

/**
 * ---------------------------
 * VUE组件源代码存放表 (SysComponentServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-12-27 12:27:43
 * ---------------------------
 */
@Service
public class SysComponentServiceImpl extends AbstractManagerImpl<String, SysComponent> implements SysComponentService {

	@Autowired
	private SysComponentMapper sysComponentMapper;

	@Override
	public int save(SysComponent record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysComponentMapper.add(record);
		}
		return sysComponentMapper.update(record);
	}
	
	@Override
	public SysComponent saveBean(SysComponent record) {
		if(record.getId() == null || record.getId() == 0) {
			sysComponentMapper.add(record);
			return record;
		}
		sysComponentMapper.update(record);
		return record;
	}
	
	
	

	@Override
	public int delete(SysComponent record) {
		return sysComponentMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysComponent> records) {
		for(SysComponent record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysComponentMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysComponent findById(Long id) {
		return sysComponentMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysComponentMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysComponent> getDao() {
		return sysComponentMapper;
	}
	
}
