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

import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.dao.SysBranchRoleMapper;
import com.xyjsoft.admin.model.SysBranchRole;
import com.xyjsoft.admin.model.SysBranchRoleMenu;
import com.xyjsoft.admin.model.SysRole;
import com.xyjsoft.admin.model.SysRoleMenu;
import com.xyjsoft.admin.service.SysBranchRoleService;

/**
 * ---------------------------
 * 子公司角色表 (SysBranchRoleController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@RestController
@RequestMapping("sysBranchRole")
@Api(tags="SysBranchRoleController 子公司角色表")
public class SysBranchRoleController {

	@Autowired
	private SysBranchRoleService sysBranchRoleService;
	@Autowired
	private SysBranchRoleMapper sysBranchRoleMapper;

	/**
	 * 保存子公司角色表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新子公司角色表数据", httpMethod = "POST", notes = "新增,更新子公司角色表数据")
	public HttpResult save(@ApiParam(name="SysBranchRole",value="子公司角色表业务对象") @RequestBody SysBranchRole record) {
		return HttpResult.ok(sysBranchRoleService.save(record));
	}

    /**
     * 删除子公司角色表
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除子公司角色表记录", httpMethod = "POST", notes = "删除子公司角色表记录")
	public HttpResult delete(@ApiParam(name="SysBranchRole列表",value="SysBranchRole列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysBranchRoleService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="子公司角色表数据列表", httpMethod = "POST", notes = "获取子公司角色表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysBranchRoleService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="子公司角色表数据详情",httpMethod = "GET",notes = "子公司角色表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysBranchRoleService.findById(id));
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
		PageList<SysBranchRole> query = sysBranchRoleService.query(queryFilter);
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
		Boolean b = sysBranchRoleService.disposeApply(xyjActivitiPuth);
		return HttpResult.ok(b);
	}
	
	@GetMapping(value="/findRoleMenus")
	@ApiOperation(value="获取子公司角色对应菜单", httpMethod = "GET", notes = "获取集团角色对应菜单")
	public HttpResult findRoleMenus(@RequestParam Long roleId) {
		return HttpResult.ok(sysBranchRoleService.findRoleMenus(roleId));
	}
	
	@PostMapping(value="/saveRoleMenus")
	@ApiOperation(value="保存子公司角色对应菜单", httpMethod = "POST", notes = "保存集团角色对应菜单")
	public HttpResult saveRoleMenus(@RequestBody List<SysBranchRoleMenu> records) {
		return HttpResult.ok(sysBranchRoleService.saveRoleMenus(records));
	}
}
