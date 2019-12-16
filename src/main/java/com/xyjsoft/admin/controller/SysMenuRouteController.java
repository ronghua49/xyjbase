package com.xyjsoft.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryField;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.util.JwtTokenUtils;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.xyjsoft.admin.model.SysMenuRoute;
import com.xyjsoft.admin.service.SysMenuRouteService;

/**
 * ---------------------------
 * 菜单路由表 (SysMenuRouteController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-10-28 18:45:36
 * ---------------------------
 */
@RestController
@RequestMapping("sysMenuRoute")
@Api(tags="SysMenuRouteController 菜单路由表")
public class SysMenuRouteController {

	@Autowired
	private SysMenuRouteService sysMenuRouteService;
	@Autowired
	private RestTemplate restTemplate;
	/**
	 * 保存菜单路由表
	 * @param record
	 * @return
	 */	
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新菜单路由表数据", httpMethod = "POST", notes = "新增,更新菜单路由表数据")
	public HttpResult save(@ApiParam(name="SysMenuRoute",value="菜单路由表业务对象") @RequestBody SysMenuRoute record) {
		return HttpResult.ok(sysMenuRouteService.save(record));
	}

    /**
     * 删除菜单路由表
     * @param records
     * @return
     */
	@PostMapping(value="/delete")
	@ApiOperation(value = "删除菜单路由表记录", httpMethod = "POST", notes = "删除菜单路由表记录")
	public HttpResult delete(@ApiParam(name="SysMenuRoute列表",value="SysMenuRoute列表")@RequestBody List<Long> records) {
		return HttpResult.ok(sysMenuRouteService.deleteT(records));
	}

    /**
     * 基础分页查询
     * @param pageRequest
     * @return
     */    
	@PostMapping(value="/findPage")
	@ApiOperation(value="菜单路由表数据列表", httpMethod = "POST", notes = "获取菜单路由表列表")
	public HttpResult findPage(@ApiParam(name="PageRequest",value="查询对象")@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysMenuRouteService.findPage(pageRequest));
	}
	/**
	 * 基础分页查询
	 * @param pageRequest
	 * @return
	 */    
	@PostMapping(value="/test")
	@ApiOperation(value="菜单路由表数据列表", httpMethod = "POST", notes = "获取菜单路由表列表")
	public HttpResult test() {
		HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("token", JwtTokenUtils.TOKEN);
        //body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
        
        ResponseEntity<String> rss = restTemplate.exchange("http://XYJ-LEAVE/xyjLeave/test", HttpMethod.GET, requestEntity,String.class, new HashMap<>());
        String body = rss.getBody();
        return HttpResult.ok(body);
	}
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */ 	
	@GetMapping(value="/findById")
	@ApiOperation(value="菜单路由表数据详情",httpMethod = "GET",notes = "菜单路由表数据详情")
	public HttpResult findById(@ApiParam(name="id",value="业务对象主键")@RequestParam Long id) {
		return HttpResult.ok(sysMenuRouteService.findById(id));
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
			if(queryField.getValue().equals("MenuRoute")) {
				remvceField = queryField;
			}
		}
		if(remvceField != null) {
			querys.remove(remvceField);
		}
		PageList<SysMenuRoute> query = sysMenuRouteService.query(queryFilter);
		return HttpResult.ok(query);
	}
}
