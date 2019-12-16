package com.xyjsoft.admin.controller;

import java.util.List;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xyjsoft.admin.model.AppVersion;
import com.xyjsoft.admin.service.AppVersionService;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ---------------------------
 * app版本 (AppVersionController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-03-11 17:38:22
 * 说明：  我是由代码生成器生生成的
 * ---------------------------
 */
@RestController
@RequestMapping("appVersion")
@Api(tags="AppVersionController app版本")
public class AppVersionController {

	@Autowired
	private AppVersionService appVersionService;

	/**
	 * 保存app版本
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新app版本数据", httpMethod = "POST", notes = "新增,更新app版本数据")
	public HttpResult save(@ApiParam(name="AppVersion",value="app版本业务对象") @RequestBody AppVersion record) {
		return HttpResult.ok(appVersionService.save(record));
	}

    /**
     * 删除app版本
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除app版本记录", httpMethod = "POST", notes = "删除app版本记录")
	public HttpResult delete(@ApiParam(name="AppVersion列表",value="AppVersion列表")@RequestBody List<AppVersion> records) {
		return HttpResult.ok(appVersionService.delete(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="app版本数据列表", httpMethod = "POST", notes = "获取app版本列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(appVersionService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="app版本数据详情",httpMethod = "GET",notes = "app版本数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(appVersionService.findById(id));
	}
	
	/**
     * 查询
     * @param pageRequest
     * @return
     * @throws Exception 
     * @throws SystemException 
     */    
	@PostMapping(value="/query")
	@ApiOperation(value="query查询", httpMethod = "POST", notes = "query查询")
	public HttpResult query(@ApiParam(name="QueryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws SystemException, Exception {
		PageList<AppVersion> query = appVersionService.query(queryFilter);
		return HttpResult.ok(query);
	}
	@GetMapping(value="/getAppVersion/{id}")
	@ApiOperation(value="app版本数据详情",httpMethod = "GET",notes = "app版本数据详情")
	public String getAppVersion(@ApiParam(name="id",value="业务对象主键")@PathVariable Long id) {
		AppVersion appversion = appVersionService.findById(id);
		if(appversion != null) {
			return JSONObject.toJSONString(appversion);
		}else {
			return "";
		}
	}
}
