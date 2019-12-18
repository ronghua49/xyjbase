package com.xyjsoft.core.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * ---------------------------
 * 支付信息 (PaySign)         
 * ---------------------------
 * @author gsh456
 * @date 2019-03-06 17:23:11
 *   @since JDK1.8
 * ---------------------------
 */
@ApiModel(value = "PaySign",description = "支付订单签名") 
public class PaySign implements Serializable{

	//private static final long serialVersionUID = 1L;
	/** 订单号 */
	@ApiModelProperty(value="订单号")
	private String code;
	/** 签名 */
	@ApiModelProperty(value="签名")
	private String sign;
	public PaySign() {
		super();
	}
	public PaySign(String code, String sign) {
		super();
		this.code = code;
		this.sign = sign;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	
	

}