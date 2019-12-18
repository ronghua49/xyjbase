package com.xyjsoft.core.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;

import com.xyjsoft.core.agreement.ReceiveData;

public abstract class IActionListener implements IMqttActionListener {
	/**
	 * @describe 发送的当前数据
	 * @author sjg 
	 * @date 2019年12月3日 下午3:00:27
	 */
	private ReceiveData data;
	/**
	 * @param data
	 */
	public IActionListener(ReceiveData data) {
		this.data = data;
	}
	public ReceiveData getData() {
		return data;
	}
	public void setData(ReceiveData data) {
		this.data = data;
	}
}
