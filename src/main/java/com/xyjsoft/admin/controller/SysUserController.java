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

import com.xyjsoft.admin.constants.SysConstants;
import com.xyjsoft.admin.model.SysRole;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.service.SysRoleService;
import com.xyjsoft.admin.service.SysUserService;
import com.xyjsoft.core.feign.XyjImFegin;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.util.PasswordUtils;

import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("user")
@Api(tags="SysUserController 集团用户管理")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private XyjImFegin xyjImFegin;
	@Autowired
	private SysRoleService sysRoleService;
	
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody SysUser record) {
		SysUser user = sysUserService.findById(record.getId());
		if(user != null) {
			if(SysConstants.ADMIN.equalsIgnoreCase(user.getName())) {
				throw new RuntimeException("超级管理员不允许修改!");
			}
		}
		if(record.getPassword() != null) {
			String salt = PasswordUtils.getSalt();
			if(user == null) {
				// 新增用户
				if(sysUserService.findByName(record.getName()) != null) {
					throw new RuntimeException("用户名已存在!");
				}
				String password = PasswordUtils.encode(record.getPassword(), salt);
				record.setSalt(salt);
				record.setPassword(password);
			} else {
				// 修改用户, 且修改了密码
				if(!record.getPassword().equals(user.getPassword())) {
					String password = PasswordUtils.encode(record.getPassword(), salt);
					record.setSalt(salt);
					record.setPassword(password);
				}
			}
		}
		return HttpResult.ok(sysUserService.save(record));
	}

	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody List<SysUser> records) {
		for(SysUser record:records) {
			SysUser sysUser = sysUserService.findById(record.getId());
			if(sysUser != null && SysConstants.ADMIN.equalsIgnoreCase(sysUser.getName())) {
				return HttpResult.error("超级管理员不允许删除!");
			}
		}
		return HttpResult.ok(sysUserService.delete(records));
	}
	
	@GetMapping(value="/findByName")
	@ApiOperation(value="根据用户名查询用户", httpMethod = "GET", notes = "根据用户名查询用户")
	public HttpResult findByUserName(@RequestParam String name) {
		return HttpResult.ok(sysUserService.findByName(name));
	}
	
	@GetMapping(value="/changeBind")
	@ApiOperation(value="更改绑定", httpMethod = "GET", notes = "更改绑定")
	public HttpResult changeBind(@ApiParam(name="userId",value="用户ID")@RequestParam Long userId,
			@ApiParam(name="imId",value="消息中心ID")@RequestParam Long imId) {
		sysUserService.changeBind(userId,imId);
		return HttpResult.ok("处理成功");
	}
	
	@GetMapping(value="/findPermissions")
	@ApiOperation(value="获取员工权限", httpMethod = "GET", notes = "获取员工权限")
	public HttpResult findPermissions(@RequestParam String name) {
		return HttpResult.ok(sysUserService.findPermissions(name));
	}
	
	@GetMapping(value="/findUserRoles")
	@ApiOperation(value="获取用户角色", httpMethod = "GET", notes = "获取用户角色")
	public HttpResult findUserRoles(@RequestParam Long userId) {
		return HttpResult.ok(sysUserService.findUserRoles(userId));
	}

	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysUserService.findPage(pageRequest));
	}
	@GetMapping(value="/findById")
	public HttpResult findById(@RequestParam Long id) {
		return HttpResult.ok(sysUserService.findById(id));
	}
	
	/**
	 * 修改密码
	 * @param record
	 * @return
	 */	
	@GetMapping(value="/updatePwd")
	@ApiOperation(value = "修改密码", httpMethod = "GET", notes = "修改密码")
	public HttpResult updatePwd(@ApiParam(name="name",value="用户名") @RequestParam String name,
			@ApiParam(name="password",value="旧密码") @RequestParam String password,
			@ApiParam(name="newPWD",value="新密码") @RequestParam String newPWD) {

		return HttpResult.ok(sysUserService.updatePwd(name,password,newPWD));
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
		PageList<SysUser> query = sysUserService.query(queryFilter);
		return HttpResult.ok(query);
	}
	
	/**
     * 查询
     * @param pageRequest
     * @return
     * @throws Exception 
     * @throws SystemException 
     */    
	@PostMapping(value="/listUserByDeptIds")
	@ApiOperation(value="根据机构下员工", httpMethod = "POST", notes = "根据机构下员工")
	public HttpResult listUserByDeptIds(@ApiParam(name="QueryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws SystemException, Exception {
		PageList<SysUser> query = sysUserService.listUserByDeptIds(queryFilter);
		return HttpResult.ok(query);
	}
	
	/**
	 * 修改密码
	 * @param record
	 * @return
	 */	
	@GetMapping(value="/test")
	@ApiOperation(value = "分布式事物测试", httpMethod = "GET", notes = "分布式事物测试")
	@GlobalTransactional(timeoutMills = 300000)
	public HttpResult test() {
		xyjImFegin.synSave(888L, "c", "分布式事物测试", "分布式事物测试", "分布式事物测试", "分布式事物测试");
		SysRole findById = sysRoleService.findById(15L);
		findById.setName("分布式事物测试");
		sysRoleService.save(findById);
//		int a = 1/0;
//		System.out.println(a);
		return HttpResult.ok("OK");
	}
	
}
