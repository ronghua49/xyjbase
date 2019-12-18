package com.xyjsoft.core.mqtt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcess implements IMessageProcess {
	private static Logger logger = LoggerFactory.getLogger(MessageProcess.class);
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("messageArrived请设置微服务接收数据事件");
		logger.info("数据抛弃topic:" + topic);
		logger.info("数据抛弃message:" + message.getPayload());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("deliveryComplete请设置微服务接收数据事件");
		try {
			logger.info("数据抛弃message:" + token.getMessage().getPayload());
		} catch (MqttException e) {
			logger.error("deliveryComplete,错误信息" + e);
		}
	}
	
	/**
	 * @describe 对象转化为字节码
	 * @author sjg 
	 * @date 2019年12月1日 下午10:11:08
	 * @version 1.0
	 * @param obj
	 * @return
	 * @throws IOException 
	 */
	public byte[] getBytesFromObject(Serializable obj) throws IOException {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		return bo.toByteArray();
	}

}
