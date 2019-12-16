package com.xyjsoft.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.log.impl.SysLogService;
import com.xyjsoft.core.model.SysLog;
import com.xyjsoft.core.page.PageRequest;

/**
 * 日志控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("log")
public class SysLogController {

	@Autowired
	private SysLogService sysLogServiceImpl;

	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysLogServiceImpl.findPage(pageRequest));
	}
	
	@PostMapping(value="/save")
	public int save(@RequestBody SysLog record){
		int save = sysLogServiceImpl.save(record);
		return save;
	}
}
