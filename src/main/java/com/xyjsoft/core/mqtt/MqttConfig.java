package com.xyjsoft.core.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author sjg 2019年12月1日 下午8:56:58 MQTT 初始化类
 */
@Component
@ApiModel(description="mqtt配置信息")
public class MqttConfig {

	/**
	 * @describe 用户名
	 * @author sjg
	 * @date 2019年12月1日 下午8:57:39
	 */
	@ApiModelProperty(name="username",notes="用户名")
	@Value("${xyjconfig.mqtt.username}")
	private String username;

	/**
	 * @describe 密码
	 * @author sjg
	 * @date 2019年12月1日 下午8:57:56
	 */
	@ApiModelProperty(name="password",notes="密码")
	@Value("${xyjconfig.mqtt.password}")
	private String password;

	/**
	 * @describe MQTT-服务器连接地址，如果有多个，用逗号隔开，如：tcp://127.0.0.1:61613，tcp://192.168.2.133:61613
	 * @author sjg
	 * @date 2019年12月1日 下午8:58:09
	 */
	@ApiModelProperty(name="url",notes="MQTT-服务器连接地址，如果有多个，用逗号隔开，如：tcp://127.0.0.1:61613，tcp://192.168.2.133:61613")
	@Value("${xyjconfig.mqtt.url}")
	private String url;

	/**
	 * @describe 默认客户端id
	 * @author sjg
	 * @date 2019年12月1日 下午9:00:23
	 */
	@ApiModelProperty(name="clientid",notes="默认客户端id")
	@Value("${spring.application.name}")
	private String clientid;

	/**
	 * @describe 默认消息订阅主题，如果有多个可用逗号分割
	 * @author sjg
	 * @date 2019年12月1日 下午9:02:22
	 */
	@ApiModelProperty(name="defaultTopic",notes="默认消息订阅主题，如果有多个可用逗号分割")
	@Value("${xyjconfig.mqtt.defaultTopic}")
	private String defaultTopic;

	/**
	 * @describe 连接超时 秒为单位
	 * @author sjg
	 * @date 2019年12月1日 下午9:02:45
	 */
	@ApiModelProperty(name="connectionTimeout",notes="连接超时 秒为单位")
	@Value("${xyjconfig.mqtt.connectionTimeout:3000}")
	private int connectionTimeout;
	
	/**
	 * @describe 设置会话心跳时间,秒为单位
	 * @author sjg 
	 * @date 2019年12月2日 上午9:51:00
	 */
	@ApiModelProperty(name="keepAlive",notes="设置会话心跳时间,秒为单位")
	@Value("${xyjconfig.mqtt.keepAlive:60}")
	private int keepAlive;
	
	/**
	 * @describe 消息并发最大数
	 * @author sjg 
	 * @date 2019年12月2日 上午10:28:40
	 */
	@ApiModelProperty(name="maxInflight",notes="消息并发最大数")
	@Value("${xyjconfig.mqtt.maxInflight:10}")
	private int maxInflight;
	
	/**
	 * @describe #消息发送超时时间，毫秒单位，0：永久等待
	 * @author sjg 
	 * @date 2019年12月2日 上午11:08:41
	 */
	@ApiModelProperty(name="publishCompleteTimeOut",notes="消息发送超时时间，毫秒单位，0：永久等待")
	@Value("${xyjconfig.mqtt.publishCompleteTimeOut:0}")
	private int publishCompleteTimeOut;
	
	/**
	 * @describe 0：最多一次的传输 1：至少一次的传输，(鸡肋)2： 只有一次的传输
	 * @author sjg 
	 * @date 2019年12月2日 下午12:01:39
	 */
	@ApiModelProperty(name="qos",notes="0：最多一次的传输 1：至少一次的传输，(鸡肋)2： 只有一次的传输")
	@Value("${xyjconfig.mqtt.qos:2}")
	private int qos;
	
	/**
	 * @describe #true：消息服务器保留最后一条消息，false:不保留消息
	 * @author sjg 
	 * @date 2019年12月2日 下午12:12:06
	 */
	@ApiModelProperty(name="retained",notes="true：消息服务器保留最后一条消息，false:不保留消息")
	@Value("${xyjconfig.mqtt.retained:true}")
	private boolean retained;
	
	@ApiModelProperty(name="stopstate",notes="true：启用，false:停用")
	@Value("${xyjconfig.mqtt.stopstate:false}")
	private boolean stopstate;
	
	
	public boolean isStopstate() {
		return stopstate;
	}

	public void setStopstate(boolean stopstate) {
		this.stopstate = stopstate;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public int getPublishCompleteTimeOut() {
		return publishCompleteTimeOut;
	}

	public void setPublishCompleteTimeOut(int publishCompleteTimeOut) {
		this.publishCompleteTimeOut = publishCompleteTimeOut;
	}

	public int getMaxInflight() {
		return maxInflight;
	}

	public void setMaxInflight(int maxInflight) {
		this.maxInflight = maxInflight;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(int keepAlive) {
		this.keepAlive = keepAlive;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String[] getDefaultTopic() {
		return defaultTopic.split(",");
	}

	public void setDefaultTopic(String defaultTopic) {
		this.defaultTopic = defaultTopic;
	}

}
