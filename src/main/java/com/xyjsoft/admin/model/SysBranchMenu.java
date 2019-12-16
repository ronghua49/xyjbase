package com.xyjsoft.admin.model;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 子公司菜单表 (SysBranchMenu)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 15:09:27
 * ---------------------------
 */
@ApiModel(value = "SysBranchMenu",description = "子公司菜单表") 
public class SysBranchMenu extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@ApiModelProperty(value="主键")
	private Long id;
	/** 菜单名称 */
	@ApiModelProperty(value="菜单名称")
	private String name;
	/** 父菜单ID，一级菜单为0 */
	@ApiModelProperty(value="父菜单ID，一级菜单为0")
	private Long parentId;
	/** 菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址) */
	@ApiModelProperty(value="菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)")
	private String url;
	/** 授权(多个用逗号分隔，如：sys:user:add,sys:user:edit) */
	@ApiModelProperty(value="授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)")
	private String perms;
	/** 类型 0：目录 1：菜单 2：按钮 */
	@ApiModelProperty(value="类型 0：目录 1：菜单 2：按钮")
	private Integer type;
	/** 菜单图标 */
	@ApiModelProperty(value="菜单图标")
	private String icon;
	/** 排序 */
	@ApiModelProperty(value="排序")
	private Integer orderNum;
	/** 流程定义KEY */
	@ApiModelProperty(value="流程定义KEY")
	private String processKey;
	/** 流程名称 */
	@ApiModelProperty(value="流程名称")
	private String processName;
	/** 流程ID */
	@ApiModelProperty(value="流程ID")
	private String processId;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remark;
	
    // 非数据库字段
    private String parentName;
    // 非数据库字段
    private Integer level;
    // 非数据库字段
    private List<SysBranchMenu> children;
    //非数据库字段，接收前端传入的按钮权限标识，宋建国，2019-07-31
    private List<SysBranchMenu> buttons;

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

	public List<SysBranchMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysBranchMenu> children) {
		this.children = children;
	}

	public List<SysBranchMenu> getButtons() {
		return buttons;
	}

	public void setButtons(List<SysBranchMenu> buttons) {
		this.buttons = buttons;
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
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}