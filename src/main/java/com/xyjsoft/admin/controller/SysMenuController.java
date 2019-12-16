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

import com.xyjsoft.admin.model.SysMenu;
import com.xyjsoft.admin.service.SysMenuService;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.log.impl.SysLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 菜单控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("menu")
@Api(tags="SysMenuController 集团菜单管理")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;
	
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody SysMenu record) {
		return HttpResult.ok(sysMenuService.save(record));
	}
	
	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody List<SysMenu> records) {
		return HttpResult.ok(sysMenuService.delete(records));
	}

	@GetMapping(value="/findNavTree")
	@ApiOperation(value="获取集团菜单列表", httpMethod = "GET", notes = "获取集团菜单列表")
	public HttpResult findNavTree(@RequestParam String userName) {
		return HttpResult.ok(sysMenuService.findTree(userName, 1));
	}
	
	@GetMapping(value="/findMenuTree")
	@ApiOperation(value="获取集团菜单列表", httpMethod = "GET", notes = "获取集团菜单列表")
	public HttpResult findMenuTree() {
		return HttpResult.ok(sysMenuService.findTree(null, 0));
	}
}
