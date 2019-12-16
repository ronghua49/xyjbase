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
import com.xyjsoft.core.query.QueryField;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.xyjsoft.admin.model.SysKeyword;
import com.xyjsoft.admin.service.SysKeywordService;

/**
 * ---------------------------
 * 关键词 (SysKeywordController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-25 15:13:46
 * ---------------------------
 */
@RestController
@RequestMapping("sysKeyword")
@Api(tags="SysKeywordController 关键词")
public class SysKeywordController {

	@Autowired
	private SysKeywordService sysKeywordService;

	/**
	 * 保存关键词
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新关键词数据", httpMethod = "POST", notes = "新增,更新关键词数据")
	public HttpResult save(@ApiParam(name="SysKeyword",value="关键词业务对象") @RequestBody SysKeyword record) {
		return HttpResult.ok(sysKeywordService.save(record));
	}

    /**
     * 删除关键词
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除关键词记录", httpMethod = "POST", notes = "删除关键词记录")
	public HttpResult delete(@ApiParam(name="SysKeyword列表",value="SysKeyword列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysKeywordService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="关键词数据列表", httpMethod = "POST", notes = "获取关键词列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysKeywordService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="关键词数据详情",httpMethod = "GET",notes = "关键词数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysKeywordService.findById(id));
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
		List<QueryField> querys = queryFilter.getQuerys();
		QueryField remvceField = null;
		for (QueryField queryField : querys) {
			if(queryField.getValue().equals("KeyWord")) {
				remvceField = queryField;
			}
		}
		if(remvceField != null) {
			querys.remove(remvceField);
		}
		PageList<SysKeyword> query = sysKeywordService.query(queryFilter);
		return HttpResult.ok(query);
	}
}
