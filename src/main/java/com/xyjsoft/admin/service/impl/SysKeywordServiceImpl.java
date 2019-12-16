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

import com.xyjsoft.admin.model.SysKeyword;
import com.xyjsoft.admin.dao.SysKeywordMapper;
import com.xyjsoft.admin.service.SysKeywordService;

/**
 * ---------------------------
 * 关键词 (SysKeywordServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-25 15:13:46
 * ---------------------------
 */
@Service
public class SysKeywordServiceImpl extends AbstractManagerImpl<String, SysKeyword> implements SysKeywordService {

	@Autowired
	private SysKeywordMapper sysKeywordMapper;

	@Override
	public int save(SysKeyword record) {
		if(record.getId() == null || record.getId() == 0) {
			//根据编码查询
			List<SysKeyword> list = sysKeywordMapper.getByCode(record.getCode());
			if(list.size() == 0) {
				return sysKeywordMapper.add(record);
			}
			throw new RuntimeException("编码已经被使用,请调整后提交!");
		}else{
			//根据编码与ID查询
			List<SysKeyword> list = sysKeywordMapper.getByCodeAndId(record.getCode(),record.getId());
			if(list.size() == 0) {
				return sysKeywordMapper.update(record);
			}
			throw new RuntimeException("编码已经被使用,请调整后提交!");
		}
	}

	@Override
	public int delete(SysKeyword record) {
		return sysKeywordMapper.delete(record.getId());
	}

	@Override
	public int delete(List<SysKeyword> records) {
		for(SysKeyword record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			sysKeywordMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public SysKeyword findById(Long id) {
		return sysKeywordMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, sysKeywordMapper);
	}
	
	@Override
	protected MyBatisDao<String, SysKeyword> getDao() {
		return sysKeywordMapper;
	}
	
}
