package com.xyjsoft.core.vo;

import io.swagger.annotations.ApiModelProperty;

public class SwiftpassTradepayMessage {
	/**商户订单号*/
	@ApiModelProperty(value="商户订单号")
	private String payCode;;
	/**订单总金额，单位为分 */
	@ApiModelProperty(value="订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]")
	private Long totalAmount;
	/**支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准*/
	@ApiModelProperty(value="支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准")
	private String authCode;
	/**订单描述*/
	@ApiModelProperty(value="订单描述")
	private String body;
	@ApiModelProperty(value="业务类型")
	private String ywType;
	
    /**商户订单号*/
    public String getPayCode() {
		return payCode;
	}
    /**商户订单号*/
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	/**订单总金额，单位为分 */
	public Long getTotalAmount() {
		return totalAmount;
	}
	/**订单总金额，单位为分 */
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准*/
	public String getAuthCode() {
		return authCode;
	}
	/**支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准*/
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	/**订单描述*/
	public String getBody() {
		return body;
	}
	/**订单描述*/
	public void setBody(String body) {
		this.body = body;
	}
	public String getYwType() {
		return ywType;
	}
	public void setYwType(String ywType) {
		this.ywType = ywType;
	}
}
