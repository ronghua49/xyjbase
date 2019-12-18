package com.xyjsoft.core.vo;

import com.xyjsoft.core.util.StringUtils;

public class Base64Vo {
	private String type;
	private String format;
	private String data;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public static Base64Vo formatBase64(String base64Str){
		//data:image/png;base64,
		if(StringUtils.isBlank(base64Str)) {
			throw new RuntimeException("base64图片为空");
		}
		Base64Vo base64Vo = new Base64Vo();
		String[] split = base64Str.split(",");
		if(split.length != 2) {
			throw new RuntimeException("base64图片格式错误");
		}
		base64Vo.setData(split[1]);
		String string = split[0];
		String[] split2 = string.split(":");
		if(split2.length != 2) {
			throw new RuntimeException("base64图片格式错误");
		}
		String string2 = split2[1];
		String[] split3 = string2.split("/");
		if(split3.length != 2) {
			throw new RuntimeException("base64图片格式错误");
		}
		base64Vo.setType(split3[0]);
		String string3 = split3[1];
		String[] split4 = string3.split(";");
		if(split4.length != 2) {
			throw new RuntimeException("base64图片格式错误");
		}
		base64Vo.setFormat(split4[0]);
		return base64Vo;
	}
	
}
