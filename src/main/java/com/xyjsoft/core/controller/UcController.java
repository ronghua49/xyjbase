package com.xyjsoft.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 类名:UcController
 * 类描述:获取用户信息
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-15 16:14
 * @since JDK1.8
 */
@RestController
@RequestMapping("UcController")
@Api(tags="UcController 用户信息获取")
public class UcController {
    /*@Resource(name="logEventBiz")
    LogEventBiz logEventBiz;
    *//**
     * 查询
     * @param
     * @return
     * @throws Exception
     * @throws
     *//*
    @PostMapping(value="/getLoginUser")
    @ApiOperation(value="获取当前用户信息", httpMethod = "GET", notes = "获取当前登录信息")
    public UserEntity getLoginUser(HttpServletRequest request){
        String token = JwtTokenUtils.getToken(request);
        return logEventBiz.getUserEntity(token);
    }*/
}
