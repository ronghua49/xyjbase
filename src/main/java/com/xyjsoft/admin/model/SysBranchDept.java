package com.xyjsoft.admin.model;
import java.util.List;

import com.xyjsoft.core.model.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 子公司机构表 (SysBranchDept)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@ApiModel(value = "SysBranchDept",description = "子公司机构表") 
public class SysBranchDept extends BaseModel{

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
	/** 机构名称 */
	@ApiModelProperty(value="机构名称")
	private String name;
	/** 上级机构ID，一级机构为0 */
	@ApiModelProperty(value="上级机构ID，一级机构为0")
	private Long parentId;
	/** 排序 */
	@ApiModelProperty(value="排序")
	private Integer orderNum;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remark;

    // 非数据库字段
    private List<SysBranchDept> children;
    // 非数据库字段
    private String parentName;
    // 非数据库字段
    private Integer level;
    
    
	public List<SysBranchDept> getChildren() {
		return children;
	}

	public void setChildren(List<SysBranchDept> children) {
		this.children = children;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}