package com.xyjsoft.admin.vo;
import java.util.List;

import com.xyjsoft.core.model.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 岗位表 (SysPost)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-27 10:23:13
 * ---------------------------
 */
@ApiModel(value = "SysPost",description = "岗位表") 
public class SysPostUserVo extends BaseModel{

	/**  */
	@ApiModelProperty(value="")
	private Long id;
	/** 岗位名称 */
	@ApiModelProperty(value="岗位名称")
	private String postName;
	/** 岗位编码 */
	@ApiModelProperty(value="岗位编码")
	private String code;
	/** 父ID */
	@ApiModelProperty(value="父ID")
	private Long cid;
	/** 岗位查询权限(0:全部,1:所有下属,2:自己) */
	@ApiModelProperty(value="岗位查询权限(0:全部,1:所有下属,2:自己)")
	private String queryRole;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remarks;
	/** 员工ID */
	@ApiModelProperty(value="员工ID")
	private Long userId;
	/** 是否主岗(0:否,1:是) */
	@ApiModelProperty(value="是否主岗(0:否,1:是)")
	private String isMain;
	/** 下级岗位*/
    private List<SysPostUserVo> children;
	/** 可查询用户iD*/
    private List<Long> queryUserIds;
    
	public String getIsMain() {
		return isMain;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public List<Long> getQueryUserIds() {
		return queryUserIds;
	}

	public void setQueryUserIds(List<Long> queryUserIds) {
		this.queryUserIds = queryUserIds;
	}

	public List<SysPostUserVo> getChildren() {
		return children;
	}

	public void setChildren(List<SysPostUserVo> children) {
		this.children = children;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
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