package com.xyjsoft.core.agreement;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author sjg
 * 2019年12月3日 下午4:01:34  
 * 消息协议结构基类
 */
@ApiModel(description="消息协议结构基类")
public class baseNews {
	@ApiModelProperty(value="编号")
	private Long id;
	/**消息类型*/
	@ApiModelProperty(value="消息类型")
	private String mType;
	/** 发送用户ID */
	@ApiModelProperty(value="发送用户ID")
	private String fromUserid;
	/** 接收用户ID*/
	@ApiModelProperty(value="接收用户ID")
	private String toUserid;
	/** 发送时间*/
	@ApiModelProperty(value="发送时间")
	private Date sendDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	public String getFromUserid() {
		return fromUserid;
	}
	public void setFromUserid(String fromUserid) {
		this.fromUserid = fromUserid;
	}
	public String getToUserid() {
		return toUserid;
	}
	public void setToUserid(String toUserid) {
		this.toUserid = toUserid;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	} 
	
	
}
