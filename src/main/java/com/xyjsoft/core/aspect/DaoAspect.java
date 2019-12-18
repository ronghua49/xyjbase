package com.xyjsoft.core.aspect;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xyjsoft.core.bean.LoginInfo;
import com.xyjsoft.core.controller.SysRedisController;
import com.xyjsoft.core.util.JwtTokenUtils;
import com.xyjsoft.core.util.SecurityUtils;
import com.xyjsoft.core.util.Snowflake;

/**
 * DAO切面，插入创建人，创建时间，修改人，修改时间
 * @author Louis
 * @date Oct 29, 2018
 */
@Aspect
@Component
@Configuration
public class DaoAspect {
	@Autowired
	private SysRedisController sysRedisController;
	@Autowired
	private Snowflake snowflake;
	
	private static final String createBy = "createBy";
	private static final String createTime = "createTime";
	private static final String lastUpdateBy = "lastUpdateBy";
	private static final String lastUpdateTime = "lastUpdateTime";
	private static final String postId = "postId";
	private static final String id = "id";

	@Pointcut("execution(* com.xyjsoft.*.dao.*.update*(..))")
	public void daoUpdate() {
	}
	
	@Pointcut("execution(* com.xyjsoft.*.dao.*.insert*(..))	|| execution(* com.xyjsoft.*.dao.*.add*(..))")
	public void daoCreate() {
	}
	@Around("daoUpdate()")
	public Object doAroundUpdate(ProceedingJoinPoint pjp) throws Throwable {
		LoginInfo loginInfo = null;
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String token = request.getHeader("token");
			loginInfo = getUserName(token);
		}
		Object[] objects = pjp.getArgs();
		if (objects != null && objects.length > 0) {
			for (Object arg : objects) {
				try {
					if (loginInfo != null) {
						/*获取到用户，则写入用户*/
						BeanUtils.setProperty(arg, lastUpdateBy, loginInfo.getUsername());
					}
					BeanUtils.setProperty(arg, lastUpdateTime, new Date());	
				}catch(Exception e) {
				}
			}
		}
		Object object = pjp.proceed();
		return object;

	}

	@Around("daoCreate()")
	public Object doAroundCreate(ProceedingJoinPoint pjp) throws Throwable {
		LoginInfo loginInfo = null;
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String token = request.getHeader("token");
			loginInfo = getUserName(token);
		}
		Object[] objects = pjp.getArgs();
		if (objects != null && objects.length > 0) {
			for (Object arg : objects) {
				try {
					if (loginInfo != null) {
						/*获取到用户，则写入用户*/
						BeanUtils.setProperty(arg, createBy, loginInfo.getUsername());
						BeanUtils.setProperty(arg, postId, loginInfo.getPostId());	
					}
					BeanUtils.setProperty(arg, createTime, new Date());
					BeanUtils.setProperty(arg, id, snowflake.nextId());	
				}catch(Exception e) {
				}
			}
		}
		Object object = pjp.proceed();
		return object;
	}

	private LoginInfo getUserName(String token) {	
		if(token != null && token.trim().length() > 0) {
			LoginInfo loginUserInfo = sysRedisController.getLoginUserInfo(token);
			return loginUserInfo;
		}else {
			return null;
		}
	}
}
