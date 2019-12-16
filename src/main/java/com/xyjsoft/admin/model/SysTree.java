package com.xyjsoft.admin.model;

import com.xyjsoft.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ---------------------------
 * 分类管理表 (SysTree)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-26 11:08:47
 * ---------------------------
 */
@Data
@ApiModel(value = "SysTree",description = "分类管理表") 
public class SysTree extends BaseModel {

	private static final long serialVersionUID = 1L;
	/** 主键id */
	@ApiModelProperty(value="主键id")
	private Long id;
	/** 名称 */
	@ApiModelProperty(value="名称")
	private String name;
	/** 关联的父id */
	@ApiModelProperty(value="关联的父id")
	private Long cid;
	/** code值 */
	@ApiModelProperty(value="code值")
	private String code;
	/** 备注 */
	@ApiModelProperty(value="备注")
	private String remark;
}