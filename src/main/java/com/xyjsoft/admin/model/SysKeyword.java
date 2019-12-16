package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 关键词 (SysKeyword)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-25 15:13:46
 * ---------------------------
 */
@ApiModel(value = "SysKeyword",description = "关键词") 
public class SysKeyword extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** id */
	@ApiModelProperty(value="id")
	private Long id;
	/** 关键词 */
	@ApiModelProperty(value="关键词")
	private String name;
	/** 编号 */
	@ApiModelProperty(value="编号")
	private String code;
	/** 分类编号 */
	@ApiModelProperty(value="分类编号")
	private String treecode;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remarks;

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
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getTreecode() {
		return treecode;
	}

	public void setTreecode(String treecode) {
		this.treecode = treecode;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}