package com.xyjsoft.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.AppVersionMapper;
import com.xyjsoft.admin.model.AppVersion;
import com.xyjsoft.admin.service.AppVersionService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;

/**
 * ---------------------------
 * app版本 (AppVersionServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-03-11 17:38:22
 * 说明：  我是由代码生成器生生成的
 * ---------------------------
 */
@Service
public class AppVersionServiceImpl extends AbstractManagerImpl<String, AppVersion> implements AppVersionService {

	@Autowired
	private AppVersionMapper appVersionMapper;

	@Override
	public int save(AppVersion record) {
		if(record.getId() == null || record.getId() == 0) {
			return appVersionMapper.add(record);
		}
		return appVersionMapper.update(record);
	}

	@Override
	public int delete(AppVersion record) {
		return appVersionMapper.delete(record.getId());
	}

	@Override
	public int delete(List<AppVersion> records) {
		for(AppVersion record:records) {
			delete(record);
		}
		return 1;
	}
	
	@Override
	public int deleteT(List<Long> ids) {
		for(Long id : ids) {
			appVersionMapper.delete(id);
		}
		return 0;
	}
	
	@Override
	public AppVersion findById(Long id) {
		return appVersionMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, appVersionMapper);
	}
	
	@Override
	protected MyBatisDao<String, AppVersion> getDao() {
		return appVersionMapper;
	}
	
}
