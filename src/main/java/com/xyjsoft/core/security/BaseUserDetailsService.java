package com.xyjsoft.core.security;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 用户登录认证信息查询
 * @author 宋建国
 * @date 2019-08-04
 */
/**
 * 安全认证基类，为实现所依赖项目不用创建此类
 */
@Service
public class BaseUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	return null;
    }
}