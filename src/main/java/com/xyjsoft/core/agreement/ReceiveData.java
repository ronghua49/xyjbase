package com.xyjsoft.core.agreement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author yh
 *
 */
@ApiModel(value = "ReceiveData",description = "MQTT接收消息结构") 
public class ReceiveData {
	@ApiModelProperty(value="主题类型")
    private String topic;
	@ApiModelProperty(value="发送方聊天ID")
	private Long fromUserId;
	@ApiModelProperty(value="接收方聊天ID")
    private Long toUserId;
	@ApiModelProperty(value="消息类型")
    private String MessageType;
	@ApiModelProperty(value="接收方昵称")
    private String toUserNickName;
	@ApiModelProperty(value="消息内容")
    private String messageString;
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Long getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	public Long getToUserId() {
		return toUserId;
	}
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	public String getMessageType() {
		return MessageType;
	}
	public void setMessageType(String messageType) {
		MessageType = messageType;
	}
	public String getToUserNickName() {
		return toUserNickName;
	}
	public void setToUserNickName(String toUserNickName) {
		this.toUserNickName = toUserNickName;
	}
	public String getMessageString() {
		return messageString;
	}
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
    
}
