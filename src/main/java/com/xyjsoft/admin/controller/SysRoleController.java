package com.xyjsoft.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.dao.SysRoleMapper;
import com.xyjsoft.admin.model.SysRole;
import com.xyjsoft.admin.model.SysRoleMenu;
import com.xyjsoft.admin.service.SysRoleService;

/**
 * 角色控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("role")
@Api(tags="SysRoleController 集团角色管理")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody SysRole record) {
		SysRole role = sysRoleService.findById(record.getId());
		if(role != null) {
			if(SysConstants.ADMIN.equalsIgnoreCase(role.getName())) {
				return HttpResult.error("超级管理员不允许修改!");
			}
		}
		// 新增角色
		if((record.getId() == null || record.getId() ==0) && !sysRoleService.findByName(record.getName()).isEmpty()) {
			return HttpResult.error("角色名已存在!");
		}
		return HttpResult.ok(sysRoleService.save(record));
	}

	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody List<SysRole> records) {
		return HttpResult.ok(sysRoleService.delete(records));
	}

	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysRoleService.findPage(pageRequest));
	}
	
	@GetMapping(value="/findAll")
	@ApiOperation(value="获取集团角色列表", httpMethod = "GET", notes = "获取集团角色列表")
	public HttpResult findAll() {
		return HttpResult.ok(sysRoleService.findAll());
	}
	
	@GetMapping(value="/findRoleMenus")
	@ApiOperation(value="获取集团角色对应菜单", httpMethod = "GET", notes = "获取集团角色对应菜单")
	public HttpResult findRoleMenus(@RequestParam Long roleId) {
		return HttpResult.ok(sysRoleService.findRoleMenus(roleId));
	}
	
	@PostMapping(value="/saveRoleMenus")
	@ApiOperation(value="保存集团角色对应菜单", httpMethod = "POST", notes = "保存集团角色对应菜单")
	public HttpResult saveRoleMenus(@RequestBody List<SysRoleMenu> records) {
		for(SysRoleMenu record:records) {
			SysRole sysRole = sysRoleMapper.selectByPrimaryKey(record.getRoleId());
			if(SysConstants.ADMIN.equalsIgnoreCase(sysRole.getName())) {
				// 如果是超级管理员，不允许修改
				return HttpResult.error("超级管理员拥有所有菜单权限，不允许修改！");
			}
		}
		return HttpResult.ok(sysRoleService.saveRoleMenus(records));
	}
}
