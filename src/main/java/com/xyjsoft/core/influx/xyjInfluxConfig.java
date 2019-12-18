package com.xyjsoft.core.influx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Configuration
@ApiModel(description="InfluxDb配置信息")
public class xyjInfluxConfig {
	private static Logger logger = LoggerFactory.getLogger(xyjInfluxConfig.class); 
	
	/**
	 * @describe 服务器网址，例如：http://127.0.0.1:9010
	 * @author sjg 
	 * @date 2019年12月13日 下午1:31:21
	 */
	@ApiModelProperty(name="url",notes="服务器网址，例如：http://127.0.0.1:9010")
	@Value("${xyjconfig.InfluxDb.url:http://117.78.5.161:9010}")
	private String url;
	/**
	 * @describe 用户名
	 * @author sjg 
	 * @date 2019年12月13日 下午1:48:05
	 */
	@ApiModelProperty(name="userName",notes="用户名")
	@Value("${xyjconfig.InfluxDb.userName:admin}")
	private String userName;
	/**
	 * @describe 密码
	 * @author sjg 
	 * @date 2019年12月13日 下午1:48:12
	 */
	@ApiModelProperty(name="password",notes="密码")
	@Value("${xyjconfig.InfluxDb.password:admin}")
	private String password;
	/**
	 * @describe 数据库名称
	 * @author sjg 
	 * @date 2019年12月13日 下午1:48:24
	 */
	@ApiModelProperty(name="database",notes="数据库名称")
	@Value("${xyjconfig.InfluxDb.database:xyjdb}")
	private String database;
	
	/**
	 * @describe 异步写入数据，如果大于等于当前设置值则自动写入influxDB数据库数据
	 * @author sjg 
	 * @date 2019年12月13日 下午1:38:40
	 */
	@ApiModelProperty(name="messageNumber",notes="异步写入数据，如果大于等于当前设置值则自动写入influxDB数据库数据")
	@Value("${xyjconfig.InfluxDb.messageNumber:100}")
	private int messageNumber;
	/**
	 * @describe 异步写入数据，如果消息写入空闲时间大于等于当前设置值(单位：毫秒)，则自动写入influxDB数据库数据
	 * @author sjg 
	 * @date 2019年12月13日 下午1:40:15
	 */
	@ApiModelProperty(name="timeWrite",notes="异步写入数据，如果消息写入空闲时间大于等于当前设置值(单位：毫秒)，则自动写入influxDB数据库数据")
	@Value("${xyjconfig.InfluxDb.timeWrite:2000}")
	private int timeWrite;
	
	/**
	 * @describe 数据保存策略
	 * @author sjg 
	 * @date 2019年12月13日 下午1:59:24
	 */
	@ApiModelProperty(name="retention",notes="数据保存策略")
	@Value("${xyjconfig.InfluxDb.retention:autogen}")
	private String retention;
	
	/**
	 * @describe 向Spring容器中增加实例
	 * @author sjg 
	 * @date 2019年12月13日 下午2:50:44
	 * @version 1.0
	 * @return
	 */
	@Bean
	public InfluxClient influxClient(){
		InfluxClient influxClient = new InfluxClient(this.url,this.userName,this.password,this.database,this.messageNumber,this.timeWrite,this.retention);
		influxClient.connectInfluxDB();
		return influxClient;
    }
	
	public String getRetention() {
		return retention;
	}

	public void setRetention(String retention) {
		this.retention = retention;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public int getTimeWrite() {
		return timeWrite;
	}

	public void setTimeWrite(int timeWrite) {
		this.timeWrite = timeWrite;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
		
}
