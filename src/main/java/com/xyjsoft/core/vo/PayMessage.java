package com.xyjsoft.core.vo;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 支付信息 (PayMessage)         
 * ---------------------------
 * 作者：  原浩
 * 时间：  2019-03-06 17:23:11
 * 说明：  我是自己写的
 * ---------------------------
 */
@ApiModel(value = "PayMessage",description = "支付信息") 
public class PayMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 支付订单号 */
	@ApiModelProperty(value="支付订单号")
	private String payCode;
	/** 支付金额(单位:分) */
	@ApiModelProperty(value="支付金额(单位:分)")
	private long payPrice;
	/** ywType */
	@ApiModelProperty(value="业务类型")
	private String ywType;
	@ApiModelProperty(value="用户IP")
	private String userIP;
	public PayMessage() {
		super();
	}
	
	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	public PayMessage(String payCode, long payPrice, String ywType) {
		super();
		this.payCode = payCode;
		this.payPrice = payPrice;
		this.ywType = ywType;
	}

	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public long getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(long payPrice) {
		this.payPrice = payPrice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getYwType() {
		return ywType;
	}
	public void setYwType(String ywType) {
		this.ywType = ywType;
	}
	

}