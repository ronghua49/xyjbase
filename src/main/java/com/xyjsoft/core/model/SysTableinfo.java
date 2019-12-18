package com.xyjsoft.core.model;
import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ---------------------------
 * 前端表格初始化数据 (SysTableinfo)         
 * ---------------------------
 * 作者：  sjg
 * @date 2019-05-08 12:48:17
 * 说明：
 * ---------------------------
 */
@Data
@ApiModel(value = "SysTableinfo",description = "前端表格初始化数据") 
public class SysTableinfo implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private static final long serialVersionUID = 1L;
	/** 自增ID */
	@ApiModelProperty(value="自增ID")
	private Long id;
	/** 实体类名称 */
	@ApiModelProperty(value="实体类名称")
	private String tablename;
	/** 字段中文标题 */
	@ApiModelProperty(value="字段中文标题")
	private String label;
	/** 表头列标题对齐方式，若不设置该项，则使用表格的对齐方式 left,center,right */
	@ApiModelProperty(value="表头列标题对齐方式，若不设置该项，则使用表格的对齐方式 left,center,right")
	private String headeralign;
	/** 列字段上级ID,对应自增ID字段，0代表一级表头 */
	@ApiModelProperty(value="列字段上级ID,对应自增ID字段，0代表一级表头")
	private Long uplevel;
	/** 字段名 */
	@ApiModelProperty(value="字段名")
	private String prop;
	/** 列宽度 */
	@ApiModelProperty(value="列宽度")
	private String width;
	/** 列是否固定在左侧或者右侧，1: 表示固定在左侧左侧 */
	@ApiModelProperty(value="列是否固定在左侧或者右侧，1: 表示固定在左侧左侧")
	private Integer fixed;
	/** 对应列是否可以通过拖动改变宽度（需要在 el-table 上设置 border 属性为真） */
	@ApiModelProperty(value="对应列是否可以通过拖动改变宽度（需要在 el-table 上设置 border 属性为真）")
	private Integer resizable;
	/** 当内容过长被隐藏时显示 tooltip */
	@ApiModelProperty(value="当内容过长被隐藏时显示 tooltip")
	private Integer showoerflowtooltip;
	/** 数据对齐方式,left,center,right */
	@ApiModelProperty(value="数据对齐方式,left,center,right")
	private String align;
	/** 合计类型，text，count,sum,avg */
	@ApiModelProperty(value="合计类型，text，count,sum,avg")
	private String sumtype;
	/** 编辑时，组件渲染类型，input，selection，select，radio-group，radio-button，textarea，date-picker，single-date，switch */
	@ApiModelProperty(value="编辑时，组件渲染类型，input，selection，select，radio-group，radio-button，textarea，date-picker，single-date，switch")
	private String fieldtype;
	/** 组件校验：QQ，phone，checkMoney，checkId，checkInt，checkLength，maxlength，checkMustFill，checkSelectValue，checkSelectArray */
	@ApiModelProperty(value="组件校验：QQ，phone，checkMoney，checkId，checkInt，checkLength，maxlength，checkMustFill，checkSelectValue，checkSelectArray")
	private String rule;
	/** 是否快速查询 */
	@ApiModelProperty(value="是否快速查询")
	private Integer quick;
	/** 是否编辑，1为编辑，0为不编辑 */
	@ApiModelProperty(value="是否编辑，1为编辑，0为不编辑")
	private Integer edit;
	/** 表格是否显示，1列显示，0列不显示 */
	@ApiModelProperty(value="表格是否显示，1列显示，0列不显示")
	private Integer isshow;
	/** 字段排列顺序 */
	@ApiModelProperty(value="字段排列顺序")
	private Long arrange;
	/** 平台字典表的类型（type）字段，代表前端多选组件的选择数据 */
	@ApiModelProperty(value="平台字典表的类型（type）字段，代表前端多选组件的选择数据")
	private String dicttype;
	/** 编辑，新增是否显示 1:新增与修改对话框不显示 0：查看对话框显示 */
	@ApiModelProperty(value="编辑，新增是否显示 1:新增与修改对话框不显示 0：查看对话框显示")
	private Integer editshow;
	/** 数据类型,与Oracle数据类型一直*/
	@ApiModelProperty(value="数据类型,与Oracle数据类型一直")
	private String datatype;
	/** 数据长度*/
	@ApiModelProperty(value="数据长度")
	private Long datalength;
	/** 字段是否为空*/
	@ApiModelProperty(value="字段是否为空")
	private String nullable;
	//TODO 东风日产添加，别的项目不用
	/**表头修改，0：不可修改，1：可修改，只在前端获取表头修改列信息时使用，目前日产新车内置成本系统中使用*/
	@ApiModelProperty(value = "表头修改，0：不可修改，1：可修改，只在前端获取表头修改列信息时使用，目前日产新车内置成本系统中使用")
	private Integer headermodify;
	/** 列颜色(前两位科为透明色,rgb/hex)*/
	@ApiModelProperty(value="列颜色")
	private String columncolor ;
	/** 是否列合并*/
	@ApiModelProperty(value = "是否列合并")
	private Integer isCombine=0;
	/** 是否允许搜索*/
	@ApiModelProperty(value = "是否允许搜索")
	private Integer isAllowSearch=1;
	/** 提醒文字*/
	@ApiModelProperty(value = "提醒文字")
	private String tipsText;
	/** 格式化类型*/
	@ApiModelProperty(value = "格式化类型")
	private String formatType;
	/** 微服务名称*/
	@ApiModelProperty(value = "微服务名称")
	private String microServiceName;

	/** 关系符数组*/
	@ApiModelProperty(value = "关系符数组")
	private String relationGroups = JSON.toJSONString(new String[]{"EQ","LE","GE"});;



}