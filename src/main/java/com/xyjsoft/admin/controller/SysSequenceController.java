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

import com.xyjsoft.admin.model.SysSequence;
import com.xyjsoft.admin.service.SysSequenceService;

/**
 * ---------------------------
 * 自增序列 (SysSequenceController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 16:05:08
 * ---------------------------
 */
@RestController
@RequestMapping("sysSequence")
@Api(tags="SysSequenceController 自增序列")
public class SysSequenceController {

	@Autowired
	private SysSequenceService sysSequenceService;


    /**
     * 设置新序列
     * @param sysSequence
     * @return
     */    
	@PostMapping(value="/setVal")
	@ApiOperation(value="设置序列", httpMethod = "POST", notes = "设置序列")
	public HttpResult setVal(@ApiParam(name="sysSequence",value="序列对象")@RequestBody SysSequence sysSequence) {
		return HttpResult.ok(sysSequenceService.setVal(sysSequence));
	}
	
    /**
     * 获取下一个序列
     * @param name
     * @return
     */ 	
	@GetMapping(value="/nextVal")
	@ApiOperation(value="获取下一个序列",httpMethod = "GET",notes = "获取下一个序列")
	public HttpResult nextVal(@ApiParam(name="name",value="序列名称")@RequestParam String name) {
		return HttpResult.ok(sysSequenceService.nextVal(name));
	}
	
    /**
     * 获取序列当前值
     * @param name
     * @return
     */ 	
	@GetMapping(value="/currVal")
	@ApiOperation(value="获取序列当前值",httpMethod = "GET",notes = "获取序列当前值")
	public HttpResult currVal(@ApiParam(name="name",value="序列名称")@RequestParam String name) {
		return HttpResult.ok(sysSequenceService.currVal(name));
	}


}
