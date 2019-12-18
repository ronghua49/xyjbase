package com.xyjsoft.core.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiModel;
@ApiModel(description="Mqtt接收消息类")
public class msgCallback implements MqttCallback {
	private static Logger logger = LoggerFactory.getLogger(msgCallback.class);
	private MqttClient client;
	private MqttConnectOptions options;
	private String[] topic;
	private int[] qos;
	/**
	 * @describe 消息处理类
	 * @author sjg 
	 * @date 2019年12月3日 上午11:46:34
	 */
	private IMessageProcess messageProcess = null;
	
	public msgCallback() {
	}
	
	public msgCallback(MqttClient client, MqttConnectOptions options, String[] topic, int[] qos,IMessageProcess messageProcess) {
		this.client = client;
		this.options = options;
		this.topic = topic;
		this.qos = qos;
		this.messageProcess = messageProcess;
	}

	public IMessageProcess getMessageProcess() {
		return messageProcess;
	}

	public void setMessageProcess(IMessageProcess messageProcess) {
		this.messageProcess = messageProcess;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
	 * 断网重连
	 */
	@Override
	public void connectionLost(Throwable cause) {
		logger.info(client.getClientId() + ",MQTT连接断开，发起重连");
		while (!client.isConnected()) {
			try {
				Thread.sleep(2000);
				client.connect(this.options);
				// 订阅消息
				client.subscribe(topic, qos);
				logger.info("MQTT重新连接成功:" + client.getClientId());
				break;
			} catch (Exception e) {
				logger.info("MQTT重新连接失败:" + e);
				continue;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 * 消息处理
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if(messageProcess != null){
			messageProcess.messageArrived(topic, message);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 * 接收到消息调用令牌中调用
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		if(messageProcess != null){
			messageProcess.deliveryComplete(token);
		}
	}
}
