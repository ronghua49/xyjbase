package com.xyjsoft.core.agreement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author sjg
 * 2019年12月3日 下午4:09:55  
 * 加好友确认推送消息结构
 */
@ApiModel(value = "addFriends",description = "addFriends") 
public class AddFriends extends baseNews {
	/** 消息内容 */
	@ApiModelProperty(value="确认消息提示")
	private String MessageContent;
	/** 消息标题 */
	@ApiModelProperty(value="确认消息标题")
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
