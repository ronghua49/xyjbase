package com.xyjsoft.admin.security;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xyjsoft.admin.dao.SysBranchUserMapper;
import com.xyjsoft.admin.dao.SysUserMapper;
import com.xyjsoft.admin.model.SysBranchUser;
import com.xyjsoft.admin.model.SysUser;
import com.xyjsoft.admin.service.SysUserService;
import com.xyjsoft.admin.vo.UserType;
import com.xyjsoft.core.security.BaseUserDetailsService;
import com.xyjsoft.core.security.JwtUserDetails;


/**
 * 用户登录认证信息查询
 * @author 宋建国
 * @date 2019-08-04
 */
/**
 *	
 */
@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl extends BaseUserDetailsService {
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysBranchUserMapper sysBranchUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserType parseObject = JSON.parseObject(username, UserType.class);
    	SysUser suser = null;
    	SysBranchUser puser = null;
    	if("0".equals(parseObject.getUserType())) {
    		suser = sysUserMapper.findByName(parseObject.getUserName());
    	}else if("1".equals(parseObject.getUserType())) {
    		puser = sysBranchUserMapper.findByName(parseObject.getUserName());
    	}
    	
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
    	Set<String> permissions = sysUserService.findPermissions("admin");
        /*List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());*/
    	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
    	if(suser!= null) {
        	return new JwtUserDetails(suser.getName(), suser.getPassword(), suser.getSalt(), grantedAuthorities);
        }else if(puser!= null){
        	return new JwtUserDetails(puser.getUsername(), puser.getPassword(), puser.getSalt(), grantedAuthorities);
        }else{
        	throw new RuntimeException("登入信息异常");
        }
    }
}