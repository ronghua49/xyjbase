package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 子公司用户角色表 (SysBranchUserRole)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@ApiModel(value = "SysBranchUserRole",description = "子公司用户角色表") 
public class SysBranchUserRole extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@ApiModelProperty(value="主键")
	private Long id;
	/** 角色ID */
	@ApiModelProperty(value="角色ID")
	private Long roleId;
	/** 用户ID */
	@ApiModelProperty(value="用户ID")
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}