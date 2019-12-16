package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 菜单路由表 (SysMenuRoute)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-10-28 18:45:36
 * ---------------------------
 */
@ApiModel(value = "SysMenuRoute",description = "菜单路由表") 
public class SysMenuRoute extends BaseModel{

	private static final long serialVersionUID = 1L;
	/** id */
	@ApiModelProperty(value="id")
	private Long id;
	/** 菜单名称 */
	@ApiModelProperty(value="菜单名称")
	private String menuName;
	/** 菜单路由 */
	@ApiModelProperty(value="菜单路由")
	private String menuRoute;
	/** 分类编号 */
	@ApiModelProperty(value="分类编号")
	private String treeCode;

	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
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
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuRoute() {
		return menuRoute;
	}

	public void setMenuRoute(String menuRoute) {
		this.menuRoute = menuRoute;
	}
}