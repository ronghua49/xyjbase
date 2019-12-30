package com.xyjsoft.admin.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.xyjsoft.admin.dao.SysBranchMapper;
import com.xyjsoft.admin.model.SysBranch;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysPost;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.service.SysBranchUserService;
import com.xyjsoft.admin.service.SysPostService;
import com.xyjsoft.admin.service.SysUserService;
import com.xyjsoft.admin.vo.LoginBean;
import com.xyjsoft.admin.vo.UserType;
import com.xyjsoft.core.bean.LoginInfo;
import com.xyjsoft.core.bean.LoginInfo.LoginUserType;
import com.xyjsoft.core.exception.XyjException;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.security.JwtAuthenticatioToken;
import com.xyjsoft.core.util.IOUtils;
import com.xyjsoft.core.util.JwtTokenUtils;
import com.xyjsoft.core.util.PasswordUtils;
import com.xyjsoft.core.util.SecurityUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 登录控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@Api(tags="SysLoginController 用户登入")
public class SysLoginController {
	@Resource 
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysBranchUserService sysBranchUserService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private SysBranchMapper sysBranchMapper;
	@Autowired
	private SysPostService sysPostService;

	
	@GetMapping("captcha.jpg")
	@ApiOperation(value = "验证码", httpMethod = "GET", notes = "验证码")
	public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到验证码到 session
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);	
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录接口
	 */
	@PostMapping(value = "/login")
	@ApiOperation(value = "用户登入", httpMethod = "POST", notes = "用户登入")
	public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
		String usertype = loginBean.getUsertype();
		String username = loginBean.getAccount();
		String password = loginBean.getPassword();
		String captcha = loginBean.getCaptcha();
		String yzmState = loginBean.getYzmState();
		String wxauthcode = loginBean.getWxauthcode();
		
		// 从session中获取之前保存的验证码跟前台传来的验证码进行匹配
		//Object kaptcha = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(usertype == null || "0".equals(usertype)) {
			// 用户信息
			SysUser user = sysUserService.findByName(username);
			// 账号不存在、密码错误
			if (user == null) {
				return HttpResult.error("账号不存在");
			}
			if (!PasswordUtils.matches(user.getSalt(), password, user.getPassword())) {
				return HttpResult.error("密码不正确");
			}
			// 账号锁定
			if (user.getStatus() == 0) {
				return HttpResult.error("账号已被锁定,请联系管理员");
			}
			UserType ut = new UserType();
			ut.setUserName(username);
			ut.setUserType("0");
			String jsonString = JSON.toJSONString(ut);
			// 系统登录认证
			JwtAuthenticatioToken token = SecurityUtils.login(request, jsonString, password, authenticationManager);
			//获取方法权限与按钮权限 宋建国 2019-07-25
			//去掉获取权限  原浩  219-10-29
//			Set<String> permissions = sysUserService.findPermissions(username);
			
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setLoginUserType(LoginUserType.SYSUSER);
			loginInfo.setUser(user);
			loginInfo.setPermissions(new HashSet<>());	
			loginInfo.setUserid(String.valueOf(user.getId()));
			loginInfo.setUsername(user.getName());
			loginInfo.setEmpname(user.getRealname());
			loginInfo.setChatId(user.getChatid());
			SysPost findByUser = sysPostService.findByUser(loginInfo);
			if(findByUser == null) {
				loginInfo.setPostId(-1l);
			}else{
				loginInfo.setPostId(findByUser.getId());
			}
			try{
			    redisTemplate.opsForValue().set(token.getToken(),JSON.toJSONString(loginInfo), JwtTokenUtils.EXPIRE_TIME,TimeUnit.MILLISECONDS);
			}catch(Exception e) {
				throw new XyjException("Redis操作异常");
			}
			Map map = new HashMap<>();
			map.put("token", token);
			map.put("chatid", loginInfo.getChatId());
			map.put("loginInfo", loginInfo);
			return HttpResult.ok(map);
		}else if("1".equals(usertype)) {
			
			SysBranchUser user = sysBranchUserService.findByName(username);
			// 账号不存在、密码错误
			if (user == null) {
				return HttpResult.error("账号不存在");
			}
			if (!PasswordUtils.matches(user.getSalt(), password, user.getPassword())) {
				return HttpResult.error("密码不正确");
			}
			// 账号锁定
			if ("1".equals(user.getStatus())) {
				return HttpResult.error("账号已被锁定,请联系管理员!");
			}
			//判断子公司状态的状态
			SysBranch findById = sysBranchMapper.findById(user.getBranchId());
			if(findById == null) {
				return HttpResult.error("您的公司不存在或已经被删除!");
			}
			if ("1".equals(findById.getDelFlag())) {
				return HttpResult.error("您的公司已被停用!");
			}
			UserType ut = new UserType();
			ut.setUserName(username);
			ut.setUserType("1");
			String jsonString = JSON.toJSONString(ut);
			// 系统登录认证
			JwtAuthenticatioToken token = SecurityUtils.login(request, jsonString, password, authenticationManager);
			
			
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setLoginUserType(LoginUserType.BRANCHUSER);
			loginInfo.setUser(user);
			loginInfo.setPermissions(new HashSet<>());
			loginInfo.setUserid(String.valueOf(user.getId()));
			loginInfo.setUsername(user.getUsername());
			loginInfo.setEmpname(user.getRealname());
			loginInfo.setChatId(user.getChatid());
			try{
		        redisTemplate.opsForValue().set(token.getToken(),JSON.toJSONString(loginInfo), JwtTokenUtils.EXPIRE_TIME,TimeUnit.MILLISECONDS);
			}catch(Exception e) {
				throw new XyjException("Redis操作异常");
			}
			Map map = new HashMap<>();
			map.put("token", token);
			map.put("branchCode", user.getBranchCode());
			map.put("chatid", loginInfo.getChatId());
			map.put("loginInfo", loginInfo);
			return HttpResult.ok(map);
		}
		return null;
	}
	
    /**
     * 获取登入人信息
     * @param id
     * @return
     */ 	
	@GetMapping(value="/getLoginUser/{username}/{type}")
	@ApiOperation(value="获取登入人信息",httpMethod = "GET",notes = "获取登入人信息")
	public HttpResult findById(@ApiParam(name="username",value="业务对象主键")@PathVariable String username,
			@ApiParam(name="type",value="业务对象主键")@PathVariable String type) {
		SysUser obj = null;
		SysBranchUser obj1 = null;
		if("0".equals(type)) {
			obj = sysUserService.findByName(username);
			obj.setPassword("");
			return HttpResult.ok(obj);
		}else if("1".equals(type)) {
			obj1 = sysBranchUserService.findByName(username);
			obj1.setPassword("");
			return HttpResult.ok(obj1);
		}else{
			return HttpResult.error("获取失败");
		}
	}
	
}
