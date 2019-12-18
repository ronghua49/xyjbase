package com.xyjsoft.core.controller;

import java.util.List;

import com.xyjsoft.core.model.Tableinfo;
import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.model.SysTableinfo;
import com.xyjsoft.core.service.SysTableinfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * ---------------------------
 * 前端表格初始化数据 (SysTableinfoController)         
 * ---------------------------
 * 作者：  宋建国
 * @date 2019-04-22 15:16:58
 * 说明： 
 * ---------------------------
 */
@RestController
@RequestMapping("sysTableinfo")
@Api(tags="SysTableinfoController 前端表格初始化数据")
public class SysTableinfoController {

	@Autowired
	private SysTableinfoService sysTableinfoService;

	/**
	 * 保存前端表格初始化数据
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新前端表格初始化数据数据", httpMethod = "POST", notes = "新增,更新前端表格初始化数据数据")
	public HttpResult save(@ApiParam(name="SysTableinfo",value="前端表格初始化数据业务对象") @RequestBody SysTableinfo record) {
		return HttpResult.ok(sysTableinfoService.save(record));
	}
	
	/**
	 * 通过微服务Feign获取远程实体类信息，保存表格初始化数据"
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/saveEntityClass")
	@ApiOperation(value = "通过微服务Feign获取远程实体类信息，保存表格初始化数据", httpMethod = "POST", notes = "通过微服务Feign获取远程实体类信息，保存表格初始化数据")
	public HttpResult saveEntityClass(@ApiParam(name="servceId",value="微服务URL")@RequestParam String servceId, 
			@ApiParam(name="classFullName",value="带包名实体类名称")@RequestParam String classFullName) {
		if(servceId == null){
			throw new RuntimeException("微服务URL不能为null");
		}
		if(servceId.trim().length()<=0){
			throw new RuntimeException("微服务URL不能为空");
		}
		if(classFullName == null){
			throw new RuntimeException("实体类名称不能为null");
		}
		if(classFullName.trim().length()<=0){
			throw new RuntimeException("实体类名称不能为空");
		}
		return sysTableinfoService.saveEntityClass(servceId,classFullName);
	}
	
	@PostMapping(value="/saveAllEntityClass")
	@ApiOperation(value = "初始化所有微服务的实体类对应表格数据",httpMethod = "POST",notes = "调用前要确保，所有微服务都已经注册到发现服务器（eurek,zeekeeper）")
	public HttpResult saveAllEntityClass(){
		//TODO 功能太危险，暂时不用
		return sysTableinfoService.saveAllEntityClass();
		//return HttpResult.ok("功能太危险，暂时不用");
	}

    /**
     * 删除前端表格初始化数据
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除前端表格初始化数据记录", httpMethod = "POST", notes = "删除前端表格初始化数据记录")
	public HttpResult delete(@ApiParam(name="SysTableinfo列表",value="SysTableinfo列表")@RequestBody List<Long> ids) {
		return HttpResult.ok(sysTableinfoService.deleteT(ids));
	}
	
	@PostMapping(value="/createHeader")
	@ApiOperation(value = "创建多级表头接口", httpMethod = "POST", notes = "创建多级表头接口")
	public HttpResult createHeader(@ApiParam(name="label",value="多级表头标题")@RequestParam String label,
			@ApiParam(name="records",value="对象中必填ID,tableName字段，其它字段可以为空")@RequestBody List<SysTableinfo> records){
		if(label == null){
			throw new RuntimeException("多级表头标题不能为null");
		}
		if(label.trim().length()<=0){
			throw new RuntimeException("多级表头标题不能为空");
		}
		if(records == null){
			throw new RuntimeException("选中行信息不能为null");
		}
		if(records.size()<=1){
			throw new RuntimeException("最少选中两行信息，才能创建表头信息");
		}
		if(records.get(0).getTablename() == null){
			throw new RuntimeException("表名字段不能为null");
		}
		if(records.get(0).getTablename().length()<=0){
			throw new RuntimeException("表名字段不能为空");
		}
		return sysTableinfoService.createHeader(label,records);
	}

    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="前端表格初始化数据数据详情",httpMethod = "GET",notes = "前端表格初始化数据数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysTableinfoService.findById(id));
	}
	
    /**
     * 根据tableName查询表初始化数据
     * @param tablename
     * @return
     */ 	
	@GetMapping(value="/findByTableName")
	@ApiOperation(value="根据表格信息类别字段，一般表名，查询前端表初始化数据",httpMethod = "GET",notes = "查询前端表初始化数据")
	public HttpResult findByTableName(@ApiParam(name="tablename",value="表格信息类别字段，一般表名")@RequestParam String tablename) {
		return HttpResult.ok(sysTableinfoService.findByTableName(tablename));
	}
    /**
     * 根据tableName查询表初始化数据
     * @param tablename
     * @return
     */
	@GetMapping(value="/findByTableNameTmp")
	@ApiIgnore
	@ApiOperation(value="根据tableName查询表初始化数据",httpMethod = "GET",notes = "根据tableName查询表初始化数据")
	public List<Tableinfo> findByTableNameTmp(@ApiParam(name="tablename",value="表格信息类别字段，一般表名")@RequestParam String tablename) {
		return sysTableinfoService.findByTableName(tablename);
	}
   	/**
     * 查询
     * @param queryFilter
     * @return
     * @throws Exception 
     * @throws SystemException 
     */    
	@PostMapping(value="/query")
	@ApiOperation(value="query查询", httpMethod = "POST", notes = "query查询")
	public HttpResult query(@ApiParam(name="QueryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws SystemException, Exception {
		PageList<SysTableinfo> query = sysTableinfoService.query(queryFilter);
		return HttpResult.ok(query);
	}
}
