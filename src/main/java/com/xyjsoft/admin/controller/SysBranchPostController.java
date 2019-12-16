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

import com.xyjsoft.admin.model.SysBranchPost;
import com.xyjsoft.admin.service.SysBranchPostService;

/**
 * ---------------------------
 * 子公司岗位表
 (SysBranchPostController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
@RestController
@RequestMapping("sysBranchPost")
@Api(tags="SysBranchPostController 子公司岗位表")
public class SysBranchPostController {

	@Autowired
	private SysBranchPostService sysBranchPostService;

	/**
	 * 保存子公司岗位表

	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新子公司岗位表数据", httpMethod = "POST", notes = "新增,更新子公司岗位表数据")
	public HttpResult save(@ApiParam(name="SysBranchPost",value="子公司岗位表业务对象") @RequestBody SysBranchPost record) {
		return HttpResult.ok(sysBranchPostService.save(record));
	}

    /**
     * 删除子公司岗位表

     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除子公司岗位表记录", httpMethod = "POST", notes = "删除子公司岗位表记录")
	public HttpResult delete(@ApiParam(name="SysBranchPost列表",value="SysBranchPost列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysBranchPostService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="子公司岗位表数据列表", httpMethod = "POST", notes = "获取子公司岗位表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysBranchPostService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="子公司岗位表数据详情",httpMethod = "GET",notes = "子公司岗位表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysBranchPostService.findById(id));
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
		PageList<SysBranchPost> query = sysBranchPostService.query(queryFilter);
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
		Boolean b = sysBranchPostService.disposeApply(xyjActivitiPuth);
		return HttpResult.ok(b);
	}
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */ 	
	@GetMapping(value="/findTree")
	@ApiOperation(value="获取岗位树结构",httpMethod = "GET",notes = "获取岗位树结构")
	public HttpResult findTree(@ApiParam(name="branchCode",value="公司编号")@RequestParam String branchCode) {
		return HttpResult.ok(sysBranchPostService.findTree(branchCode));
	}
}
