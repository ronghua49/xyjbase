package com.xyjsoft.core.mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xyjsoft.core.agreement.ReceiveData;

/**
 * @author sjg 2019年12月1日 下午9:59:40
 *
 */
@Component
public class XyjMqttClient implements ApplicationRunner {
	private static Logger logger = LoggerFactory.getLogger(XyjMqttClient.class);
	
	/**
	 * @describe 消息接收处理
	 * @author sjg 
	 * @date 2019年12月3日 上午11:43:35
	 */
	private msgCallback callback = null;
	/**
	 * @describe 数据接收接口
	 * @author sjg 
	 * @date 2019年12月3日 下午12:27:15
	 */
	private IMessageProcess messageProcess = new MessageProcess();
	
	/**
	 * @describe Mqtt客户端实例
	 * @author sjg 
	 * @date 2019年12月2日 下午12:40:55
	 */
	private static XMqttClient client;

	/**
	 * @describe 默认订阅消息
	 * @author sjg 
	 * @date 2019年12月2日 下午12:40:43
	 */
	private static MqttTopic mqttTopic;

	@Autowired
	public MqttConfig mqttConfig;

	/* (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 * Sring-boot启动时执行
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(mqttConfig.isStopstate()){
			this.connect();
		}
	}

	/**
	 * @describe MQTT客户端连接
	 * @author sjg 
	 * @date 2019年12月2日 下午12:39:58
	 * @version 1.0
	 */
	private void connect() {
		try {
			client = new XMqttClient(mqttConfig.getUrl(), mqttConfig.getClientid()+UUID.randomUUID().toString(), new MemoryPersistence());
			//#消息发送超时时间，毫秒单位，0：永久等待
			client.setTimeToWait(mqttConfig.getPublishCompleteTimeOut());
		} catch (MqttException e1) {
			logger.error("MqttClient创建失败：" + e1);
		}
		MqttConnectOptions options = new MqttConnectOptions();
/*		false:消息传递将是可靠的消息
		指定的QOS，即使客户端、服务器或连接已重新启动。
		服务器将把订阅视为持久订阅*/
		options.setCleanSession(false);
		//用户名
		options.setUserName(mqttConfig.getUsername());
		//密码
		options.setPassword(mqttConfig.getPassword().toCharArray());
		// 设置超时时间，秒为单位
		options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
		// 设置会话心跳时间,秒为单位
		options.setKeepAliveInterval(mqttConfig.getKeepAlive());
		//消息并发最大数
		options.setMaxInflight(mqttConfig.getMaxInflight());
        //在客户端断开前发送的信息，遗咐
		//options.setWill(topic, payload, qos, retained);
		
		try {
			String[] msgtopic = mqttConfig.getDefaultTopic();
			int[] qos = null;
			if(!msgtopic[0].equals("")){
				qos = new int[msgtopic.length];
				for (int i = 0; i < msgtopic.length; i++) {
					qos[i] = mqttConfig.getQos();
				}
			}
			callback = new msgCallback(client, options, msgtopic, qos, messageProcess);
			client.setCallback(callback);
			client.connect(options);
			if(qos != null){
				client.subscribe(msgtopic, qos);	
			}
			logger.info("MQTT连接成功:" + mqttConfig.getClientid() + ":" + client);
		} catch (Exception e) {
			logger.error("MQTT连接异常：" + e);
		}
	}
	
	/**
	 * @describe 发布
	 * @author sjg 
	 * @date 2019年12月2日 上午11:56:53
	 * @version 1.0
	 * @param topic
	 * @param payload
	 * @param callback
	 * @throws  
	 * @throws UnsupportedEncodingException
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void send(String topic, ReceiveData data,IMqttActionListener callback){
/*		qos：消息质量，0，1，2
		retained： true：mqtt服务器会保留每次发布的消息，若订阅主题的客户端重启，则会把此主题之前发布的消息重新推送到客户端
	             false: mqtt服务器不保留每次发布的消息*/
		try{
			byte[] JsonStr = JSONObject.toJSONString(data).getBytes("UTF-8");
			MqttMessage message = new MqttMessage(JsonStr);
			message.setQos(mqttConfig.getQos());
			message.setRetained(mqttConfig.isRetained());
			XyjMqttClient.client.publish(topic, message, null, callback);
		}catch (Exception e) {
			try {
				throw new Exception(e);
			} catch (Exception e1) {
				
			}
		}
	}
	
	public void reConnect() throws MqttException, InterruptedException {
		if (null != client) {
			client.disconnect();
			Thread.sleep(2000);
			XyjMqttClient.client.reconnect();
		}
	}

	/**
	 * @describe 消息订阅
	 * @author sjg 
	 * @date 2019年12月2日 下午12:41:13
	 * @version 1.0
	 * @param topic
	 * @param qos
	 */
	public void subscribe(String topic, int qos) {
		try {
			client.subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static XMqttClient getClient() {
		return client;
	}

	public static void setClient(XMqttClient client) {
		XyjMqttClient.client = client;
	}

	public static MqttTopic getMqttTopic() {
		return mqttTopic;
	}

	public static void setMqttTopic(MqttTopic mqttTopic) {
		XyjMqttClient.mqttTopic = mqttTopic;
	}
	
	/**
	 * @describe 设置接收消息事件
	 * @author sjg 
	 * @date 2019年12月3日 上午11:54:59
	 * @version 1.0
	 * @param messageProcess
	 */
	public void setMessageProcess(IMessageProcess messageProcess){
		this.messageProcess = messageProcess;
		if(callback != null){
			callback.setMessageProcess(this.messageProcess);
		}
	}

}
