package com.xyjsoft.admin.vo;

/**
 * 登录接口封装对象
 * @author Louis
 * @date Oct 29, 2018
 */
public class LoginBean {

	private String usertype;
	private String account;
	private String password;
	private String captcha;
	private String yzmState;
	private String wxauthcode;
	
	
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getYzmState() {
		return yzmState;
	}
	public void setYzmState(String yzmState) {
		this.yzmState = yzmState;
	}
	public String getWxauthcode() {
		return wxauthcode;
	}
	public void setWxauthcode(String wxauthcode) {
		this.wxauthcode = wxauthcode;
	}
	
}
