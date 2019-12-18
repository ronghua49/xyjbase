package com.xyjsoft.core.agreement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author sjg
 * 2019年12月3日 下午3:30:23  
 * 广播消息格式
 */
@ApiModel(value = "RadioBroadcast",description = "广播消息") 
public class RadioBroadcast extends baseNews {
	/** 消息内容 */
	@ApiModelProperty(value="消息内容")
	private String MessageContent;
	/** 消息标题 */
	@ApiModelProperty(value="消息标题")
	private String title;
	public String getMessageContent() {
		return MessageContent;
	}
	public void setMessageContent(String messageContent) {
		MessageContent = messageContent;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
