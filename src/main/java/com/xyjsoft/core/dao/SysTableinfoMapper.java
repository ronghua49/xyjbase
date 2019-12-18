package com.xyjsoft.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.model.SysTableinfo;

/**
 * ---------------------------
 * 前端表格初始化数据 (SysTableinfoMapper)         
 * ---------------------------
 * 作者：  宋建国
 * @date 2019-04-22 15:16:58
 * 说明：  
 * ---------------------------
 */
public interface SysTableinfoMapper  extends MyBatisDao<String, SysTableinfo>{

	/**
	 * 添加前端表格初始化数据
	 * @param record
	 * @return
	 */
    int add(SysTableinfo record);

    /**
     * 删除前端表格初始化数据
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改前端表格初始化数据
     * @param record
     * @return
     */
    int update(SysTableinfo record);

    /**
     * 根据表名修改uplevel字段为0
     * @param tablename 表名
     * @param uplevel 上级id
     * @param value 要修改的值
     * @return
     */
    int updateUpLevel(@Param("tablename")String tablename, @Param("uplevel")Long uplevel,@Param("value")Long value);
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    SysTableinfo findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<SysTableinfo> findPage();
    
    
    /**
     * 根据tablename查询数据
     * @param tablename
     * @return
     */    
    List<SysTableinfo> findByTableName(String tablename);
	/**
	 * 查找比minArrange大的所有数据
	 * @param tablename 表名
	 * @param minArrange  排序字段
	 * @return
	 */
	List<SysTableinfo> findArrangeMax(@Param("tablename")String tablename, @Param("minArrange")Long minArrange);

	/**
	 * 修改顺序
	 * @param id 数据ID
	 * @param arrange 要修改的顺序值
	 * @return 
	 */
	int updateArrange(@Param("id")Long id, @Param("arrange")Long arrange);
    /**
     * 判断此实体类字段，是否在表格初始化表中存在
     * @param tableName 实体类名称
     * @param fieldName 实体类属性字段名称
     * @return
     */
    List<SysTableinfo> findFieldNameExist(@Param("tableName")String tableName, @Param("fieldName")String fieldName);

    /**
     * 通过oracle系统表查询表字段备注信息
     * @param tableName 表名
     * @param fieldName 字典名
     * @return
     */
    List<Map<String, Object>> findComment(@Param("tableName")String tableName, @Param("fieldName")String fieldName);
}