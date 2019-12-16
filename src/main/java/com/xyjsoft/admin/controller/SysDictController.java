package com.xyjsoft.admin.controller;

import java.util.List;
import java.util.Map;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.admin.model.SysDict;
import com.xyjsoft.admin.service.SysDictService;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 字典控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("dict")
@Api(tags="SysDictController 数据字典接口")
public class SysDictController {

	@Autowired
	private SysDictService sysDictService;
	
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody SysDict record) {
		return HttpResult.ok(sysDictService.save(record));
	}

	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody List<SysDict> records) {
		return HttpResult.ok(sysDictService.delete(records));
	}

	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysDictService.findPage(pageRequest));
	}
	
	@GetMapping(value="/findByLable")
	@ApiOperation(value = "根据标签名查询", httpMethod = "GET", notes = "根据标签名查询")
	public HttpResult findByLable(@RequestParam String lable) {
		return HttpResult.ok(sysDictService.findByLable(lable));
	}
	
	@ApiOperation(value = "根据类型查询字典数据", httpMethod = "GET", notes = "根据类型查询字典数据")
	@GetMapping(value="/findByType")
	public HttpResult findByType(@ApiParam(name="type",value="字典中的数据类型")@RequestParam String type){
		return HttpResult.ok(sysDictService.findByType(type));
	}
	
	@GetMapping(value="/findByGroupType")
	@ApiOperation(value = "查询字典中是否删除状态为0（正常）-1（已删除）的所有类型信息", httpMethod = "GET", notes = "notes查询字典中是否删除状态为0（正常）-1（已删除）的所有类型信息")
	public HttpResult findByGroupType(@ApiParam(name="delFlag",value="删除状态，0：未删除，-1：已删除",required=true)@RequestParam Integer delFlag) {
		List<Map<String, Object>> list = sysDictService.findByGroupType(delFlag);
		return HttpResult.ok(list);
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
		PageList<SysDict> query = sysDictService.query(queryFilter);
		return HttpResult.ok(query);
	}
}
