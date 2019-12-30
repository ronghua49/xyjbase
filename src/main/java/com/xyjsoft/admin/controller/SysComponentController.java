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

import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.vo.XyjActivitiPuth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.xyjsoft.admin.model.SysComponent;
import com.xyjsoft.admin.service.SysComponentService;

/**
 * ---------------------------
 * VUE组件源代码存放表 (SysComponentController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-12-27 12:27:43
 * ---------------------------
 */
@RestController
@RequestMapping("sysComponent")
@Api(tags="SysComponentController VUE组件源代码存放表")
public class SysComponentController {

	@Autowired
	private SysComponentService sysComponentService;

	/**
	 * 保存VUE组件源代码存放表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新VUE组件源代码存放表数据", httpMethod = "POST", notes = "新增,更新VUE组件源代码存放表数据")
	public HttpResult save(@ApiParam(name="SysComponent",value="VUE组件源代码存放表业务对象") @RequestBody SysComponent record) {
		return HttpResult.ok(sysComponentService.saveBean(record));
	}

    /**
     * 删除VUE组件源代码存放表
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除VUE组件源代码存放表记录", httpMethod = "POST", notes = "删除VUE组件源代码存放表记录")
	public HttpResult delete(@ApiParam(name="SysComponent列表",value="SysComponent列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysComponentService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="VUE组件源代码存放表数据列表", httpMethod = "POST", notes = "获取VUE组件源代码存放表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysComponentService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="VUE组件源代码存放表数据详情",httpMethod = "GET",notes = "VUE组件源代码存放表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysComponentService.findById(id));
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
		PageList<SysComponent> query = sysComponentService.query(queryFilter);
		return HttpResult.ok(query);
	}
		/**
	 * 审批处理
	 * @param pageRequest
	 * @return
	 * @throws Exception 
	 * @throws SystemException 
	 */    
	@PostMapping(value="/disposeApply")
	@ApiOperation(value="审批处理", httpMethod = "POST", notes = "审批处理")
	public HttpResult disposeApply(@ApiParam(name="XyjActivitiPuth",value="审批数据对象")@RequestBody XyjActivitiPuth xyjActivitiPuth) throws SystemException, Exception {
		Boolean b = sysComponentService.disposeApply(xyjActivitiPuth);
		return HttpResult.ok(b);
	}
}
