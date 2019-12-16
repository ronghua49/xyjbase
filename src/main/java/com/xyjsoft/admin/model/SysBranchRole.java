package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 子公司角色表 (SysBranchRole)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@ApiModel(value = "SysBranchRole",description = "子公司角色表") 
public class SysBranchRole extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@ApiModelProperty(value="主键")
	private Long id;
	/** 子公司ID */
	@ApiModelProperty(value="子公司ID")
	private Long branchId;
	/** 子公司编号 */
	@ApiModelProperty(value="子公司编号")
	private String branchCode;
	/** 角色名称 */
	@ApiModelProperty(value="角色名称")
	private String name;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}