package com.xyjsoft.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.xyjsoft.core.util.SecurityUtils;


/**
 * 登录认证过滤器
 * @author gsh456
 * @date 2019-03-15 11:10
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
	
	@Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    	// 获取token, 并检查登录状态
        SecurityUtils.checkAuthentication(request);
/*        String appName = System.getProperty("user.dir");*/
        chain.doFilter(request, response);
    }
    
}