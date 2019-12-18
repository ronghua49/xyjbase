package com.xyjsoft.core.model;
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * 前端表格初始化数据 (SysTableinfo)         
 * ---------------------------
 * 作者：  sjg
 * @date 2019-04-22 15:16:58
 * 说明：
 * ---------------------------
 */
@ApiModel(value = "Tableinfo",description = "临时表,前端表格初始化数据") 
public class Tableinfo implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private static final long serialVersionUID = 1L;
	/** 自增ID */
	@ApiModelProperty(value="自增ID")
	private Long id;
	/** 表格信息类别字段，一般表名 */
	@ApiModelProperty(value="表格信息类别字段，一般表名")
	private String tablename;
	/** 字段排列顺序 */
	@ApiModelProperty(value="字段排列顺序")
	private Long arrange;
	/** 字段中文标题 */
	@ApiModelProperty(value="字段中文标题")
	private String label;
	/** 表头列标题对齐方式，若不设置该项，则使用表格的对齐方式 */
	@ApiModelProperty(value="表头列标题对齐方式，若不设置该项，则使用表格的对齐方式")
	private String headerAlign;
	/** 字段名 */
	@ApiModelProperty(value="字段名")
	private String prop;
	/** 对应宽度 */
	@ApiModelProperty(value="对应宽度")
	private String width;
	/** 列是否固定在左侧或者右侧，true 表示固定在左侧true, left, right */
	@ApiModelProperty(value="列是否固定在左侧或者右侧，true 表示固定在左侧true, left, right")
	private Integer fixed;
	/** 对应列是否可以通过拖动改变宽度（需要在 el-table 上设置 border 属性为真） */
	@ApiModelProperty(value="对应列是否可以通过拖动改变宽度（需要在 el-table 上设置 border 属性为真）")
	private Integer resizable;
	/** 当内容过长被隐藏时显示 tooltip */
	@ApiModelProperty(value="当内容过长被隐藏时显示 tooltip")
	private Integer showOerflowTooltip;
	/** 数据对齐方式 */
	@ApiModelProperty(value="数据对齐方式")
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
	/** 子表头数据 */
	@ApiModelProperty(value="子表头数据")
	private List<Tableinfo> children;

	/** 平台字典表的类型（type）字段，代表前端多选组件的选择数据 */
	@ApiModelProperty(value="取值来源是平台字典表的type字段")
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
	@ApiModelProperty(value = "是否允许搜索（0：不可搜索，1：搜索展现，2：搜索不展现")
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
	private String relationGroups = JSON.toJSONString(new String[]{"EQ","LE","GE"});


	public Integer getHeadermodify() {
		return headermodify;
	}
	public void setHeadermodify(Integer headermodify) {
		this.headermodify = headermodify;
	}
	
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public Long getDatalength() {
		return datalength;
	}
	public void setDatalength(Long datalength) {
		this.datalength = datalength;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public Integer getEditshow() {
		return editshow;
	}
	public void setEditshow(Integer editshow) {
		this.editshow = editshow;
	}
	public String getDicttype() {
		return dicttype;
	}
	public void setDicttype(String dicttype) {
		this.dicttype = dicttype;
	}
	public Long getId() {
		return id;
	}
	public String getTablename() {
		return tablename;
	}
	public Long getArrange() {
		return arrange;
	}
	public String getLabel() {
		return label;
	}
	public String getHeaderAlign() {
		return headerAlign;
	}
	public String getProp() {
		return prop;
	}
	public String getWidth() {
		return width;
	}
	public Integer getFixed() {
		return fixed;
	}
	public Integer getResizable() {
		return resizable;
	}
	public Integer getShowOerflowTooltip() {
		return showOerflowTooltip;
	}
	public String getAlign() {
		return align;
	}
	public String getSumtype() {
		return sumtype;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public String getRule() {
		return rule;
	}
	public Integer getQuick() {
		return quick;
	}
	public Integer getEdit() {
		return edit;
	}
	public Integer getIsshow() {
		return isshow;
	}
	public List<Tableinfo> getChildren() {
		return children;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public void setArrange(Long arrange) {
		this.arrange = arrange;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setHeaderAlign(String headerAlign) {
		this.headerAlign = headerAlign;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public void setFixed(Integer fixed) {
		this.fixed = fixed;
	}
	public void setResizable(Integer resizable) {
		this.resizable = resizable;
	}
	public void setShowOerflowTooltip(Integer showOerflowTooltip) {
		this.showOerflowTooltip = showOerflowTooltip;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public void setSumtype(String sumtype) {
		this.sumtype = sumtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public void setQuick(Integer quick) {
		this.quick = quick;
	}
	public void setEdit(Integer edit) {
		this.edit = edit;
	}
	public void setIsshow(Integer isshow) {
		this.isshow = isshow;
	}
	public void setChildren(List<Tableinfo> children) {
		this.children = children;
	}

	public Integer getIsCombine() {
		return isCombine;
	}

	public void setIsCombine(Integer isCombine) {
		this.isCombine = isCombine;
	}

	public Integer getIsAllowSearch() {
		return isAllowSearch;
	}

	public void setIsAllowSearch(Integer isAllowSearch) {
		this.isAllowSearch = isAllowSearch;
	}

	public String getTipsText() {
		return tipsText;
	}

	public void setTipsText(String tipsText) {
		this.tipsText = tipsText;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getMicroServiceName() {
		return microServiceName;
	}

	public void setMicroServiceName(String microServiceName) {
		this.microServiceName = microServiceName;
	}

	public String getColumncolor() {
		return columncolor;
	}

	public void setColumncolor(String columncolor) {
		this.columncolor = columncolor;
	}

	public String getRelationGroups() {
		return relationGroups;
	}

	public void setRelationGroups(String relationGroups) {
		this.relationGroups = relationGroups;
	}
}