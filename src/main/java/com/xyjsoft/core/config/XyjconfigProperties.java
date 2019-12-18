package com.xyjsoft.core.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "xyjconfig")
public class XyjconfigProperties {
	/**
	 * 标识日志数据库所在微服务,默认值为xyj-admin
	 * */
	private String logServiceId = "xyj-admin";
	/**
	 * 生成ID使用,数据中心编码[雪花算法],每个微服务需不同,取值范围 0~31,默认值0
	 * */
	private long datacenterId = 0;
	/**
	 * 生成ID使用,机器或进程编码[雪花算法],每个微服务需不同,取值范围 0~31,默认值0
	 * */
	private long workerId = 0;
	/**
	 * 标识审批流所在微服务,默认值为activity6
	 * */
	private String activitiServerId = "activity6";
	
	/**
	 * 标识可被扫描到的实体类所在包，用于讯宜捷前端框架
	 */
	private List<String> scanmodel = new ArrayList<String>(); 
	/**
	 * restTemplate请求超时时间,根据前端变化而变化
	 */
	private int restTemplateTimeOut = 50;
	/**
	 * http://192.168.3.111:8011/
	 */
	private  String uploadAddress = "";
}
