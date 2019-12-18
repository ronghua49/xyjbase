package com.xyjsoft.core.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface IMessageProcess {
	/**
	 * @describe 接收消息处理
	 * @author sjg 
	 * @date 2019年12月3日 上午11:45:43
	 * @version 1.0
	 * @param topic
	 * @param message
	 * @throws Exception
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception;
	/**
	 * @describe 
	 * @author sjg 
	 * @date 2019年12月3日 上午11:45:55
	 * @version 1.0
	 * @param token
	 */
	public void deliveryComplete(IMqttDeliveryToken token);
}
