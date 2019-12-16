package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.FilePath;
import org.springframework.stereotype.Repository;

/**
 * ---------------------------
 *  (FilePathMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-05 10:58:04
 * ---------------------------
 */
@Repository
public interface FilePathMapper  extends MyBatisDao<String, FilePath>{

	/**
	 * 添加
	 * @param record
	 * @return
	 */
    int add(FilePath record);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改
     * @param record
     * @return
     */
    int update(FilePath record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    FilePath findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<FilePath> findPage();

    List<FilePath> findByType(String type);
}