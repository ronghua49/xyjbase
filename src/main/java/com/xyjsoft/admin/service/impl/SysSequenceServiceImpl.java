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

import com.xyjsoft.admin.model.SysSequence;
import com.xyjsoft.admin.dao.SysSequenceMapper;
import com.xyjsoft.admin.service.SysSequenceService;

/**
 * ---------------------------
 * 自增序列 (SysSequenceServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 16:05:08
 * ---------------------------
 */
@Service
public class SysSequenceServiceImpl implements SysSequenceService {

	@Autowired
	private SysSequenceMapper sysSequenceMapper;

	@Override
	public String setVal(SysSequence sysSequence) {
		sysSequenceMapper.setVal(sysSequence);
		return "OK";
	}

	@Override
	public String nextVal(String name) {
		return sysSequenceMapper.nextVal(name);
	}

	@Override
	public String currVal(String name) {
		return sysSequenceMapper.currVal(name);
	}

	
}
