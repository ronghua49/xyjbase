package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 子公司用户表 (SysBranchUser)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@ApiModel(value = "SysBranchUser",description = "子公司用户表") 
public class SysBranchUser extends BaseModel{

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
	/** 用户名 */
	@ApiModelProperty(value="用户名")
	private String username;
	/** 密码 */
	@ApiModelProperty(value="密码")
	private String password;
	/** 盐 */
	@ApiModelProperty(value="盐")
	private String salt;
	/** 真实姓名 */
	@ApiModelProperty(value="真实姓名")
	private String realname;
	/** 邮箱 */
	@ApiModelProperty(value="邮箱")
	private String email;
	/** 手机号 */
	@ApiModelProperty(value="手机号")
	private String mobile;
	/** 是否停用(0:正常,1:停用) */
	@ApiModelProperty(value="是否停用(0:正常,1:停用)")
	private String status;
	/** 机构ID */
	@ApiModelProperty(value="机构ID")
	private Long deptId;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remark;
	/** 聊天ID */
	@ApiModelProperty(value="聊天ID")
	private Long chatid;
	/**非数据库字段     部门名称*/
    private String deptName;

	public Long getChatid() {
		return chatid;
	}

	public void setChatid(Long chatid) {
		this.chatid = chatid;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
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
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}