package com.xyjsoft.core.feign;

import java.util.List;

import com.xyjsoft.core.util.JwtTokenUtils;
import com.xyjsoft.core.vo.XyjControllerURL;
import com.xyjsoft.core.vo.classInfo;
import com.xyjsoft.core.vo.entityName;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

@Headers({"Content-Type: application/json", "Accept: application/json", "token: " + JwtTokenUtils.TOKEN})
public interface IclassAttribute {	
	
	/**
	 * @param servceid //微服务serviceid
	 * @param classname //此微服务实例下的类名称
	 * @return 获取实体类属性信息
	 */
	@RequestLine("GET /EureKaUtiles/getClassAttr?classFullName={classFullName}")
	public List<classInfo> getClassAttr(@Param(value = "classFullName") String classFullName);
	/**
	 * @return 返回微服务中的实体类列表
	 */
	@RequestLine("GET /EureKaUtiles/getEntityClass")
	public List<entityName> getEntityClass();
	
	/**
	 * @return 返回微服务中的实体类列表
	 */
	@RequestLine("GET /EureKaUtiles/getAllURL")
	public List<XyjControllerURL> getAllURL();

}
