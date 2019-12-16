package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 *  (FilePath)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-05 10:58:04
 * ---------------------------
 */
@ApiModel(value = "FilePath",description = "") 
public class FilePath implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@ApiModelProperty(value="主键")
	private Long id;
	/** 文件路径 */
	@ApiModelProperty(value="文件路径")
	private String path;
	/** 路径类型 */
	@ApiModelProperty(value="路径类型")
	private String type;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String memo;
	/** 匹配格式（json数组格式） */
	@ApiModelProperty(value="匹配格式（json数组格式）")
	private String fileFormat;
	/** 创建人 */
	@ApiModelProperty(value="创建人" ,readOnly = true)
	private String createBy;
	/** 创建时间 */
	@ApiModelProperty(value="创建时间",readOnly = true)
	private java.util.Date createTime;
	/** 最后修改人 */
	@ApiModelProperty(value="最后修改人",readOnly = true)
	private String lastUpdateBy;
	/** 最后修改时间 */
	@ApiModelProperty(value="最后修改时间",readOnly = true)
	private java.util.Date lastUpdateTime;
	/** 岗位ID */
	@ApiModelProperty(value="岗位ID",readOnly = true)
	private Long postId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public java.util.Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

}