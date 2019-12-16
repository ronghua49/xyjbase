package com.xyjsoft.admin.controller;

import java.util.List;

import com.xyjsoft.admin.model.SysTree;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.admin.service.SysTreeService;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ---------------------------
 * 分类管理表 (SysTreeController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-26 11:08:47
 * ---------------------------
 */
@RestController
@RequestMapping("sysTree")
@Api(tags="SysTreeController 分类管理表")
public class SysTreeController {
	@Autowired
	private SysTreeService sysTreeService;

	/**
	 * 保存分类管理表
	 * @param
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新分类管理表数据", httpMethod = "POST", notes = "新增,更新分类管理表数据")
	public HttpResult save(@ApiParam(name="sysTree",value="分类信息") @RequestBody SysTree sysTree) {
		return HttpResult.ok(sysTreeService.save(sysTree));
	}

    /**
     * 删除分类管理表
     * @param ids
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除分类管理表记录", httpMethod = "POST", notes = "删除分类管理表记录")
	public HttpResult delete(@ApiParam(name="id",value="业务对象主键")@RequestBody List<Long> ids) {
		return HttpResult.ok(sysTreeService.deleteT(ids));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="分类管理表数据列表", httpMethod = "POST", notes = "获取分类管理表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysTreeService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="分类管理表数据详情",httpMethod = "GET",notes = "分类管理表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysTreeService.findById(id));
	}
	
	/**
     * 查询
     * @param
     * @return
     * @throws Exception
     * @throws SystemException
     */
	@PostMapping(value="/query")
	@ApiOperation(value="query查询", httpMethod = "POST", notes = "query查询")
	public HttpResult query(@ApiParam(name="QueryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws SystemException, Exception {
		PageList<SysTree> query = sysTreeService.query(queryFilter);
		return HttpResult.ok(query);
	}
	/**
	 * @Author wangzhengwen
	 * @Description 查询全部用于展示
	 * @Date 11:28 2019/8/27 0027
	 * @Param []
	 * @return com.xyjsoft.core.http.HttpResult
	 **/
	@GetMapping(value = "/list")
	@ApiOperation(value = "查询全部",httpMethod = "GET",notes = "查询全部")
	public HttpResult fiandAll(){
		List<Object>list =sysTreeService.fiandAll();
		return HttpResult.ok(list);
	}
	/**
	 * @Author wangzhengwen
	 * @Description 根据id向下查询
	 * @Date 17:18 2019/8/29 0029
	 * @Param []
	 * @return com.xyjsoft.core.http.HttpResult
	 **/
    @GetMapping(value="/fiandDownById")
    @ApiOperation(value="查询id下的所有分类",httpMethod = "GET",notes = "查询id下的所有分类")
	public HttpResult fiandDownById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id){
        return HttpResult.ok(sysTreeService.fiandDownById(id));
    }

    @GetMapping(value="/fiandUpById")
    @ApiOperation(value="查询id上的所有父类",httpMethod = "GET",notes = "查询id下的所有父类")
    public HttpResult fiandUpById(@ApiParam(name = "id",value = "业务主键id")@RequestParam Long id){
        return HttpResult.ok(sysTreeService.fiandUpById(id));
    }

    @GetMapping("/fiandByCode")
	@ApiOperation(value = "根据code值进行查询",httpMethod = "GET",notes = "根据code值进行查询")
    public HttpResult fiandByCode(@ApiParam(name = "code",value = "分类code值")@RequestParam String code){
    	return HttpResult.ok(sysTreeService.fiandByCode(code));
	}
}
