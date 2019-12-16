package com.xyjsoft.admin.model;
import com.xyjsoft.core.model.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 子公司用户岗位表
 (SysBranchPostUser)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
@ApiModel(value = "SysBranchPostUser",description = "子公司用户岗位表") 
public class SysBranchPostUser extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** id */
	@ApiModelProperty(value="id")
	private Long id;
	/** 岗位ID */
	@ApiModelProperty(value="岗位ID")
	private Long sysPostId;
	/** 员工ID */
	@ApiModelProperty(value="员工ID")
	private Long userId;
	/** 是否主岗(0:否,1:是) */
	@ApiModelProperty(value="是否主岗(0:否,1:是)")
	private String isMain;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getSysPostId() {
		return sysPostId;
	}

	public void setSysPostId(Long sysPostId) {
		this.sysPostId = sysPostId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
}