package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * VUE组件源代码存放表 (SysComponent)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-12-27 12:27:43
 * ---------------------------
 */
@ApiModel(value = "SysComponent",description = "VUE组件源代码存放表") 
public class SysComponent extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** id */
	@ApiModelProperty(value="id")
	private Long id;
	/** 分类编号 */
	@ApiModelProperty(value="分类编号")
	private String treeCode;
	/** 组件名称 */
	@ApiModelProperty(value="组件名称")
	private String componentName;
	/** HTML标签名称 */
	@ApiModelProperty(value="HTML标签名称")
	private String elementName;
	/** VUE组件源代码 */
	@ApiModelProperty(value="VUE组件源代码")
	private String componentCode;
	/** 组件版本 */
	@ApiModelProperty(value="组件版本")
	private String componentVersion;
	/** 状态(0:停用，1:启用) */
	@ApiModelProperty(value="状态(0:停用，1:启用)")
	private Integer enabledState;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getComponentCode() {
		return componentCode;
	}

	public void setComponentCode(String componentCode) {
		this.componentCode = componentCode;
	}
	public String getComponentVersion() {
		return componentVersion;
	}

	public void setComponentVersion(String componentVersion) {
		this.componentVersion = componentVersion;
	}
	public Integer getEnabledState() {
		return enabledState;
	}

	public void setEnabledState(Integer enabledState) {
		this.enabledState = enabledState;
	}
}