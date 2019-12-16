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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.xyjsoft.admin.model.BillsRules;
import com.xyjsoft.admin.service.BillsRulesService;

/**
 * ---------------------------
 *  (BillsRulesController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-04 16:17:23
 * ---------------------------
 */
@RestController
@RequestMapping("billsRules")
@Api(tags="BillsRulesController 单据规则接口")
public class BillsRulesController {

	@Autowired
	private BillsRulesService billsRulesService;

	/**
	 * 保存
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新数据", httpMethod = "POST", notes = "新增,更新数据")
	public HttpResult save(@ApiParam(name="BillsRules",value="业务对象") @RequestBody BillsRules record) {
		return HttpResult.ok(billsRulesService.save(record));
	}

    /**
     * 删除
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除记录", httpMethod = "POST", notes = "删除记录")
	public HttpResult delete(@ApiParam(name="BillsRules列表",value="BillsRules列表")@RequestBody List<Long> records) {
		return HttpResult.ok(billsRulesService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="数据列表", httpMethod = "POST", notes = "获取列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(billsRulesService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="数据详情",httpMethod = "GET",notes = "数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(billsRulesService.findById(id));
	}
    /**
     * 根据主键查询
     * @param id
     * @return
     */
	@GetMapping(value="/createRuleNo")
	@ApiOperation(value="根据规则id生成",notes = "根据现有规则进行生成")
	public HttpResult createRuleNo(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(billsRulesService.createRuleNo(id));
	}
    /**
     * 根据主键查询
     * @param id
     * @return
     */
	@GetMapping(value="/createRuleNoByType")
	@ApiOperation(value="根据规则类型生成",notes = "根据现有规则进行生成")
	public HttpResult createRuleNoByType(@ApiParam(name="type",value="规则类型")@RequestParam String type) {
		return HttpResult.ok(billsRulesService.createRuleNo(type));
	}

	/**
     * 查询
     * @param queryFilter 查询
     * @return
     * @throws Exception 
     * @throws SystemException 
     */    
	@PostMapping(value="/query")
	@ApiOperation(value="query查询", httpMethod = "POST", notes = "query查询")
	public HttpResult query(@ApiParam(name="QueryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws SystemException, Exception {
		PageList<BillsRules> query = billsRulesService.query(queryFilter);
		return HttpResult.ok(query);
	}
}
