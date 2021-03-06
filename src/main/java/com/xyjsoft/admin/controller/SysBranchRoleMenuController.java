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

import com.xyjsoft.admin.model.SysBranchRoleMenu;
import com.xyjsoft.admin.service.SysBranchRoleMenuService;

/**
 * ---------------------------
 * 子公司角色菜单表 (SysBranchRoleMenuController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@RestController
@RequestMapping("sysBranchRoleMenu")
@Api(tags="SysBranchRoleMenuController 子公司角色菜单表")
public class SysBranchRoleMenuController {

	@Autowired
	private SysBranchRoleMenuService sysBranchRoleMenuService;

	/**
	 * 保存子公司角色菜单表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新子公司角色菜单表数据", httpMethod = "POST", notes = "新增,更新子公司角色菜单表数据")
	public HttpResult save(@ApiParam(name="SysBranchRoleMenu",value="子公司角色菜单表业务对象") @RequestBody SysBranchRoleMenu record) {
		return HttpResult.ok(sysBranchRoleMenuService.save(record));
	}

    /**
     * 删除子公司角色菜单表
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除子公司角色菜单表记录", httpMethod = "POST", notes = "删除子公司角色菜单表记录")
	public HttpResult delete(@ApiParam(name="SysBranchRoleMenu列表",value="SysBranchRoleMenu列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysBranchRoleMenuService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="子公司角色菜单表数据列表", httpMethod = "POST", notes = "获取子公司角色菜单表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysBranchRoleMenuService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="子公司角色菜单表数据详情",httpMethod = "GET",notes = "子公司角色菜单表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysBranchRoleMenuService.findById(id));
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
		PageList<SysBranchRoleMenu> query = sysBranchRoleMenuService.query(queryFilter);
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
		Boolean b = sysBranchRoleMenuService.disposeApply(xyjActivitiPuth);
		return HttpResult.ok(b);
	}
}
