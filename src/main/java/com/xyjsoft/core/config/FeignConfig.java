package com.xyjsoft.core.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xyjsoft.core.util.JwtTokenUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(attributes != null) {
	        HttpServletRequest request = attributes.getRequest();
	        Enumeration<String> headerNames = request.getHeaderNames();
	        if (headerNames != null) {
	        	boolean b = false;
	            while (headerNames.hasMoreElements()) {
	                String name = headerNames.nextElement();
	                if("token".equals(name)) {
	                	b = true;
	                }
	                String values = request.getHeader(name);
	                requestTemplate.header(name, values);
	            }
	            if(!b) {
	                requestTemplate.header("token", JwtTokenUtils.TOKEN);
	            }
	        }
		}else {
			requestTemplate.header("token", JwtTokenUtils.TOKEN);
		}
	}

}
