package com.xyjsoft.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.core.http.HttpResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.xyjsoft.admin.model.SysDept;
import com.xyjsoft.admin.service.SysDeptService;

/**
 * 机构控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("dept")
@Api(tags="SysDeptController 机构管理")
public class SysDeptController {

	@Autowired
	private SysDeptService sysDeptService;
	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,机构数据", httpMethod = "POST", notes = "新增,机构数据")
	public HttpResult save(@RequestBody SysDept record) {
		return HttpResult.ok(sysDeptService.save(record));
	}

	@PostMapping(value="/delete")
	@ApiOperation(value = "删除,机构数据", httpMethod = "POST", notes = "删除,机构数据")
	public HttpResult delete(@RequestBody List<SysDept> records) {
		return HttpResult.ok(sysDeptService.delete(records));
	}

	@GetMapping(value="/findTree")
	@ApiOperation(value = "获取机构树型结构", httpMethod = "POST", notes = "获取机构树型结构")
	public HttpResult findTree() {
		return HttpResult.ok(sysDeptService.findTree());
	}

}
