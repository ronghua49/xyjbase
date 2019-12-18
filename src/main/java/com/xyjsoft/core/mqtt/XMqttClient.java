package com.xyjsoft.core.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 * @author sjg
 * 2019年12月2日 上午11:45:26  
 *
 */
public class XMqttClient extends MqttClient {

	public XMqttClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
		super(serverURI, clientId, persistence);
	}
	/**
	 * @describe 通过集成暴露父类mqtt客户端实例
	 * @author sjg 
	 * @date 2019年12月2日 下午12:41:37
	 * @version 1.0
	 * @return
	 */
	public MqttAsyncClient getClient(){
		return this.aClient;
	}	
	
	/**
	 * @describe 扩充Mqtt客户端，发布数据函数，支持发送成功回调
	 * @author sjg 
	 * @date 2019年12月2日 上午11:53:36
	 * @version 1.0
	 * @param topic
	 * @param message
	 * @param userContext
	 * @param callback
	 * @throws MqttException
	 * @throws MqttPersistenceException
	 */
	public void publish(String topic, MqttMessage message, Object userContext,
			IMqttActionListener callback) throws MqttException,	MqttPersistenceException {
		aClient.publish(topic, message, userContext, callback).waitForCompletion(getTimeToWait());
	}
}
