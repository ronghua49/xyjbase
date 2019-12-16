package com.xyjsoft.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyjsoft.core.model.BaseModel;

import io.swagger.annotations.ApiModelProperty;

public class SysUser extends BaseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
    private String name;

    private String password;

    private String salt;

    private String email;

    private String mobile;

    private Byte status;

    private Long deptId;
    
    private String deptName;
    
    private String realname;
    
    private Byte delFlag;
    
    private String roleNames;
    
	@ApiModelProperty(value="聊天ID")
    private Long chatid;
    
    private List<SysUserRole> userRoles = new ArrayList<>();
    
    private List<SysPostUser> postUsers = new ArrayList<>();

	public Long getChatid() {
		return chatid;
	}

	public void setChatid(Long chatid) {
		this.chatid = chatid;
	}

	public List<SysPostUser> getPostUsers() {
		return postUsers;
	}

	public void setPostUsers(List<SysPostUser> postUsers) {
		this.postUsers = postUsers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Byte getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Byte delFlag) {
		this.delFlag = delFlag;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public List<SysUserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<SysUserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}