package com.xyjsoft.core.feign.impl;

import com.xyjsoft.core.feign.XyjImFegin;
import com.xyjsoft.core.http.HttpResult;

public class XyjImFeginImpl implements XyjImFegin {

	@Override
	public HttpResult synSave(Long userId, String userName, String userNickName, String phone, String headportrait,
			String memo) {
		throw new RuntimeException("服务器调用异常!");
	}

}
