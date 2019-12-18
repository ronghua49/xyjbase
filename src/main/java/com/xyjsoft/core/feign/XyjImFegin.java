package com.xyjsoft.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyjsoft.core.http.HttpResult;

import io.swagger.annotations.ApiParam;

@FeignClient("xyj-im")
public interface XyjImFegin {
	//保存用户时同步插入至消息服务
	@RequestMapping(method = RequestMethod.POST, value = "/imUsertable/synSave")
	public HttpResult synSave(@ApiParam(name="userId",value="用户ID")@RequestParam Long userId,
			@ApiParam(name="userName",value="用户姓名")@RequestParam String userName,
			@ApiParam(name="userNickName",value="用户昵称")@RequestParam String userNickName,
			@ApiParam(name="phone",value="用户手机号")@RequestParam String phone,
			@ApiParam(name="headportrait",value="用户头像")@RequestParam String headportrait,
			@ApiParam(name="memo",value="备注")@RequestParam String memo);
}
