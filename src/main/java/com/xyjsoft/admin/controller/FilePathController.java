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

import com.xyjsoft.admin.model.FilePath;
import com.xyjsoft.admin.service.FilePathService;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ---------------------------
 *  (FilePathController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-05 10:58:04
 * ---------------------------
 */
@RestController
@RequestMapping("filePath")
@Api(tags="FilePathController 文件路径接口")
public class FilePathController {

	@Autowired
	private FilePathService filePathService;

	/**
	 * 保存
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新数据", httpMethod = "POST", notes = "新增,更新数据")
	public HttpResult save(@ApiParam(name="FilePath",value="业务对象") @RequestBody FilePath record) {
		return HttpResult.ok(filePathService.save(record));
	}

    /**
     * 删除
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除记录", httpMethod = "POST", notes = "删除记录")
	public HttpResult delete(@ApiParam(name="FilePath列表",value="FilePath列表")@RequestBody List<Long> records) {
		return HttpResult.ok(filePathService.deleteT(records));
	}

    /**
     * 根据主键查询
     * @param id 主键
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="数据详情",httpMethod = "GET",notes = "数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(filePathService.findById(id));
	}
	
	/**
     * 查询
     * @param queryFilter 查询对象
     * @return HttpResult
     * @throws Exception 
     * @throws SystemException 
     */    
	@PostMapping(value="/query")
	@ApiOperation(value="query查询", httpMethod = "POST", notes = "query查询")
	public HttpResult query(@ApiParam(name="QueryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws SystemException, Exception {
		PageList<FilePath> query = filePathService.query(queryFilter);
		return HttpResult.ok(query);
	}
}
