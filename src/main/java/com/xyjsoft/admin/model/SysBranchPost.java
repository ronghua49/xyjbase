package com.xyjsoft.admin.model;
import java.util.List;

import com.xyjsoft.core.model.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 子公司岗位表
 (SysBranchPost)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-21 10:19:01
 * ---------------------------
 */
@ApiModel(value = "SysBranchPost",description = "子公司岗位表") 
public class SysBranchPost extends BaseModel{

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
	/** 岗位名称 */
	@ApiModelProperty(value="岗位名称")
	private String name;
	/** 岗位编号 */
	@ApiModelProperty(value="岗位编号")
	private String code;
	/** 上级ID */
	@ApiModelProperty(value="上级ID")
	private Long cid;
	/** 岗位查询权限(0:全部,1:所有下属,2:自己) */
	@ApiModelProperty(value="岗位查询权限(0:全部,1:所有下属,2:自己)")
	private String queryRole;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remarks;
	
	
	/** 非数据库字段        员工ID */
	@ApiModelProperty(value="员工ID")
	private Long userId;
	/** 非数据库字段        是否主岗(0:否,1:是) */
	@ApiModelProperty(value="是否主岗(0:否,1:是)")
	private String isMain;
	
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

	/**非数据库字段 下级岗位*/
    private List<SysBranchPost> children;

	public List<SysBranchPost> getChildren() {
		return children;
	}

	public void setChildren(List<SysBranchPost> children) {
		this.children = children;
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
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getQueryRole() {
		return queryRole;
	}

	public void setQueryRole(String queryRole) {
		this.queryRole = queryRole;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}