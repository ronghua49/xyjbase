package com.xyjsoft.admin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.admin.dao.SysDictMapper;
import com.xyjsoft.admin.model.SysDict;
import com.xyjsoft.admin.service.SysDictService;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.page.ColumnFilter;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;

@Service
public class SysDictServiceImpl extends AbstractManagerImpl<String, SysDict>  implements SysDictService {

	@Autowired
	private SysDictMapper sysDictMapper;

	@Override
	public int save(SysDict record) {
		if(record.getId() == null || record.getId() == 0) {
			return sysDictMapper.insertSelective(record);
		}
		return sysDictMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(SysDict record) {
		return sysDictMapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public int delete(List<SysDict> records) {
		for(SysDict record:records) {
			delete(record);
		}
		return 1;
	}
	
	@Override
	public int deleteT(List<Long> ids) {
		for(Long id : ids) {
			sysDictMapper.deleteByPrimaryKey(id);
		}
		return 0;
	}
	
	@Override
	public SysDict findById(Long id) {
		return sysDictMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		ColumnFilter columnFilter = pageRequest.getColumnFilter("label");
		if(columnFilter != null) {
			return MybatisPageHelper.findPage(pageRequest, sysDictMapper, "findPageByLabel", columnFilter.getValue());
		}
		return MybatisPageHelper.findPage(pageRequest, sysDictMapper);
	}

	@Override
	public List<SysDict> findByLable(String lable) {
		return sysDictMapper.findByLable(lable);
	}

	@Override
	public List<SysDict> findByType(String type) {
		// TODO Auto-generated method stub
		return sysDictMapper.findByType(type);
	}

	@Override
	public List<Map<String, Object>> findByGroupType(Integer delFlag) {
		return sysDictMapper.findByGroupType(delFlag);
	}

	@Override
	protected MyBatisDao<String, SysDict> getDao() {
		return sysDictMapper;
	}

}
