package com.xyjsoft.admin.controller;

import java.util.List;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.admin.model.SysPostUser;
import com.xyjsoft.admin.service.SysPostUserService;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ---------------------------
 * 岗位员工表 (SysPostUserController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
@RestController
@RequestMapping("sysPostUser")
@Api(tags="SysPostUserController 岗位员工表")
public class SysPostUserController {

	@Autowired
	private SysPostUserService sysPostUserService;

	/**
	 * 保存岗位员工表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新岗位员工表数据", httpMethod = "POST", notes = "新增,更新岗位员工表数据")
	public HttpResult save(@ApiParam(name="SysPostUser",value="岗位员工表业务对象") @RequestBody SysPostUser record) {
		return HttpResult.ok(sysPostUserService.save(record));
	}
	/**
	 * 保存岗位员工表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/saveList")
	@ApiOperation(value = "新增,更新岗位员工表数据", httpMethod = "POST", notes = "新增,更新岗位员工表数据")
	public HttpResult saveList(@ApiParam(name="SysPostUser",value="岗位员工表业务对象") @RequestBody SysPostUser... record) {
		sysPostUserService.saveList(record);
		return HttpResult.ok("保存成功");
	}

    /**
     * 删除岗位员工表
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除岗位员工表记录", httpMethod = "POST", notes = "删除岗位员工表记录")
	public HttpResult delete(@ApiParam(name="SysPostUser列表",value="SysPostUser列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysPostUserService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="岗位员工表数据列表", httpMethod = "POST", notes = "获取岗位员工表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysPostUserService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="岗位员工表数据详情",httpMethod = "GET",notes = "岗位员工表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysPostUserService.findById(id));
	}
	
    /**
     * 查询岗位下员工
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findUserById")
	@ApiOperation(value="查询岗位下员工",httpMethod = "GET",notes = "查询岗位下员工")
	public HttpResult findUserById(@ApiParam(name="id",value="岗位ID")@RequestParam Long id) {
		return HttpResult.ok(sysPostUserService.findUserById(id));
	}
	/**
	 * 查询员工下岗位
	 * @param id
	 * @return
	 */ 	
	@GetMapping(value="/findPostByUserId")
	@ApiOperation(value="查询员工下岗位",httpMethod = "GET",notes = "查询岗位下员工")
	public HttpResult findPostByUserId(@ApiParam(name="id",value="员工ID")@RequestParam Long id) {
		return HttpResult.ok(sysPostUserService.findPostByUserId(id));
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
		PageList<SysPostUser> query = sysPostUserService.query(queryFilter);
		return HttpResult.ok(query);
	}
	
	
	
}
