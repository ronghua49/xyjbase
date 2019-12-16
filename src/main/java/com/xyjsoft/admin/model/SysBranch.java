package com.xyjsoft.admin.model;
import java.util.List;

import com.xyjsoft.core.model.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 子公司表 (SysBranch)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@ApiModel(value = "SysBranch",description = "子公司表") 
public class SysBranch extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@ApiModelProperty(value="主键")
	private Long id;
	/** 子公司名称 */
	@ApiModelProperty(value="子公司名称")
	private String name;
	/** 子公司编号 */
	@ApiModelProperty(value="子公司编号")
	private String code;
	/** 是否停用(0:正常,1:停用) */
	@ApiModelProperty(value="是否停用(0:正常,1:停用)")
	private String delFlag;
	/** 公司邮箱 */
	@ApiModelProperty(value="公司邮箱")
	private String email;
	/** 公司联系方式 */
	@ApiModelProperty(value="公司联系方式")
	private String mobile;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remark;


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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}