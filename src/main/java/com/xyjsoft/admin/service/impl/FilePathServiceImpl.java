package com.xyjsoft.admin.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.xyjsoft.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import com.xyjsoft.admin.model.FilePath;
import com.xyjsoft.admin.dao.FilePathMapper;
import com.xyjsoft.admin.service.FilePathService;

/**
 * ---------------------------
 *  (FilePathServiceImpl)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-05 10:58:04
 * ---------------------------
 */
@Service
public class FilePathServiceImpl extends AbstractManagerImpl<String, FilePath> implements FilePathService {

	@Autowired
	private FilePathMapper filePathMapper;

	@Override
	public int save(FilePath record) {
		String type = record.getType();
		String path = record.getPath();
		if(StringUtils.isBlank(type)){
			throw new RuntimeException("类型不能为空");
		}
		Long id = Optional.ofNullable(record.getId()).orElse(0L);
		List<FilePath> filePaths = filePathMapper.findByType(type).stream().filter(e->!e.getId().equals(id)).collect(Collectors.toList());
		if(filePaths.size()>0){
			throw new RuntimeException("重复类型");
		}
		try {
			File file = new File(path);
			if(!file.exists()){
				boolean mkdirs = file.mkdirs();
				if(!mkdirs){
					throw new RuntimeException("目录创建失败，请检查目录路径");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("目录创建失败，请检查目录路径");
		}
		if(record.getId() == null || record.getId() == 0) {
			return filePathMapper.add(record);
		}
		return filePathMapper.update(record);
	}

	@Override
	public int delete(FilePath record) {
		FilePath findById = filePathMapper.findById(record.getId());
		if(findById != null) {
			if("FastDFS".equals(findById.getType())) {
				throw new RuntimeException("上传至图片服务器为系统默认上传类型,不可删除!!");
			}
			if("XyjDFS".equals(findById.getType())) {
				throw new RuntimeException("上传至服务器为系统默认上传类型,不可删除!!");
			}
		}
		return filePathMapper.delete(record.getId());
	}

	@Override
	public int delete(List<FilePath> records) {
		for(FilePath record:records) {
			delete(record);
		}
		return 1;
	}
	
    @Override
	public int deleteT(List<Long> records) {
		for(Long id:records) {
			filePathMapper.delete(id);
		}
		return 1;
	}
	
	@Override
	public FilePath findById(Long id) {
		return filePathMapper.findById(id);
	}

	@Override
	public PageResult findPage(PageRequest pageRequest) {
		return MybatisPageHelper.findPage(pageRequest, filePathMapper);
	}
	
	@Override
	protected MyBatisDao<String, FilePath> getDao() {
		return filePathMapper;
	}

	@Override
	public List<FilePath> findByType(String type) {
		return filePathMapper.findByType(type);
	}

}
