package com.xyjsoft.admin.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.xyjsoft.core.bean.LoginInfo;
import com.xyjsoft.core.controller.SysRedisController;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.service.SysPostService;

/**
 * ---------------------------
 * 岗位表 (SysPostController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
@RestController
@RequestMapping("sysPost")
@Api(tags="SysPostController 岗位表")
public class SysPostController {

	@Autowired
	private SysPostService sysPostService;
	@Autowired
	private SysRedisController sysRedisController;

	/**
	 * 保存岗位表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新岗位表数据", httpMethod = "POST", notes = "新增,更新岗位表数据")
	public HttpResult save(@ApiParam(name="SysPost",value="岗位表业务对象") @RequestBody SysPost record) {
		return HttpResult.ok(sysPostService.save(record));
	}

    /**
     * 删除岗位表
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除岗位表记录", httpMethod = "POST", notes = "删除岗位表记录")
	public HttpResult delete(@ApiParam(name="SysPost列表",value="SysPost列表")@RequestBody List<Long> records) {
		if(records.size() < 0) {
			throw new RuntimeException("请选择要删除的岗位");
		}
		SysPost sysPost = new SysPost();
		sysPost.setId(records.get(0));
		return HttpResult.ok(sysPostService.delete(sysPost));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="岗位表数据列表", httpMethod = "POST", notes = "获取岗位表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysPostService.findPage(pageRequest));
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="岗位表数据详情",httpMethod = "GET",notes = "岗位表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysPostService.findById(id));
	}
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */ 	
	@GetMapping(value="/findTree")
	@ApiOperation(value="获取岗位树结构",httpMethod = "GET",notes = "获取岗位树结构")
	public HttpResult findTree() {
		return HttpResult.ok(sysPostService.findTree());
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
		PageList<SysPost> query = sysPostService.query(queryFilter);
		return HttpResult.ok(query);
	}
	
	/**
	 * 获取用户岗位及查询权限
	 * @param id
	 * @return
	 */ 	
	@GetMapping(value="/findByUser")
	@ApiOperation(value="获取用户岗位及查询权限",httpMethod = "GET",notes = "获取用户岗位及查询权限")
	public HttpResult findByUser(HttpServletRequest request) {
		String header = request.getHeader("token");
		LoginInfo loginUserInfo = sysRedisController.getLoginUserInfo(header);
		return HttpResult.ok(sysPostService.findByUser(loginUserInfo));
	}
	
	/**
	 * 根据岗位ID获取岗位信息及子岗位信息
	 * @param id
	 * @return
	 */ 	
	@GetMapping(value="/findLowerPost")
	@ApiOperation(value="根据岗位ID获取岗位信息及子岗位信息",httpMethod = "GET",notes = "根据岗位ID获取岗位信息及子岗位信息")
	public HttpResult findLowerPost(@ApiParam(name="postid",value="岗位ID")@RequestParam String postid) {
		return HttpResult.ok(sysPostService.findLowerPost(postid));
	}
	
}
