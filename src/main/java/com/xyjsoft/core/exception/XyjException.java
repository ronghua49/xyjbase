package com.xyjsoft.core.exception;

/**
 * 自定义异常
 * @author gsh456
 * @date Aug 21, 2018
 */
public class XyjException extends RuntimeException {
	//private static final long serialVersionUID = 1L;
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
    private int code = 500;
    
    public XyjException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public XyjException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public XyjException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public XyjException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}