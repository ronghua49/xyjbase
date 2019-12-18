package com.xyjsoft.core.service;
import java.util.List;

import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.model.SysTableinfo;
import com.xyjsoft.core.model.Tableinfo;
import com.xyjsoft.core.service.CurdService;


/**
 * ---------------------------
 * 前端表格初始化数据 (SysTableinfoService)         
 * ---------------------------
 * 作者： 宋建国
 * @date 2019-04-22 15:16:58
 * 说明：  
 * ---------------------------
 */
public interface SysTableinfoService extends CurdService<SysTableinfo>,Manager<String,SysTableinfo>  {
	
	/**
	 * @param tablename 表名称
	 * @return List<SysTableinfo> 
	 */
	public List<Tableinfo> findByTableName(String tablename);
	
	/**
	 * @param servceId 微服务UIR
	 * @param classFullName  带包名实体类名称
	 * @return
	 */
	public HttpResult saveEntityClass(String servceId, String classFullName);
	

	/**
	 * @param label 列标题
	 * @param records 多级表头中的子列信息
	 * @return
	 */
	public HttpResult createHeader(String label, List<SysTableinfo> records);

	/**
	 * 初始化所有实体类对应表格数据
	 * @return
	 */
	public HttpResult saveAllEntityClass();

	public int deleteT(List<Long> ids);
}
