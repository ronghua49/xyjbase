package com.xyjsoft.core.export.poireport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * 类名: ExportNoReportObject <br/> 
 * 类描述: 无模版导出实体——excel文件的一个工作簿或pdf(暂未支持)的一个逻辑页. <br/> 
 * date: 2019-4-28 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
@ApiModel(value = "ExportNoReportObject",description = "导出体") 
public class ExportNoReportObject {
	/**excel文件的一个worksheet名称或pdf(暂未支持)的一个逻辑页页名称*/
	@ApiModelProperty(value="excel文件的一个worksheet名称或pdf(暂未支持)的一个逻辑页页名称，选传",required = false)
	private String entityName;
	/**excel行高，默认值20*/
	@ApiModelProperty(value="excel行高，默认值20,选传",required = false)
	private Integer rowheight = 20;
	/**excel列宽，默认值15*/
	@ApiModelProperty(value="excel列宽，默认值15,选传",required = false)
	private Integer colwidth = 15;
	/**导出主项，用于输出excel的表(列中文描述信息)头和pdf的页/表头等<br>
	 * 此属性采用 有序的LinkedHashMap，该键值顺序为导出excel或pdf的表/页头的列顺序*/
	@ApiModelProperty(value="导出主项，用于输出excel的表(列中文描述信息)头和pdf的页/表头等，此属性采用 有序的LinkedHashMap，该键值顺序为导出excel或pdf的表/页头的列顺序,选传",required = false,example = "[ {col1:列1中文},{col2:列2中文}  ]")
	private LinkedHashMap<String,Object> parameters;
	/**导出明细项*/
	@ApiModelProperty(value="导出明细项，必传",required = true,example = "[ { col1:值,col2:值,... } ]")
	private List<Map> datasource;
	
	/**excel文件的一个工作簿名称或pdf(暂未支持)的一个逻辑页页名称*/
	public String getEntityName() {
		return entityName;
	}
	/**excel文件的一个工作簿名称或pdf(暂未支持)的一个逻辑页页名称*/
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	/**excel行高，默认值20*/
	public Integer getRowheight() {
		return rowheight;
	}
	/**excel行高，默认值20*/
	public void setRowheight(Integer rowheight) {
		this.rowheight = rowheight;
	}
	/**excel列宽，默认值15*/
	public Integer getColwidth() {
		return colwidth;
	}
	/**excel列宽，默认值15*/
	public void setColwidth(Integer colwidth) {
		this.colwidth = colwidth;
	}
	/**导出主项，用于输出excel的表头和pdf(暂未支持)的页/表头等<br>
	 * 此属性采用 有序的LinkedHashMap，该键值顺序为导出excel或pdf的表/页头的列顺序*/
	public LinkedHashMap<String, Object> getParameters() {
		return parameters;
	}
	/**导出主项，用于输出excel的表头和pdf(暂未支持)的页/表头等<br>
	 * 此属性采用 有序的LinkedHashMap，该键值顺序为导出excel或pdf的表/页头的列顺序*/
	public void setParameters(LinkedHashMap<String, Object> parameters) {
		this.parameters = parameters;
	}
	/**导出明细项*/
	public List<Map> getDatasource() {
		return datasource;
	}
	/**导出明细项*/
	public void setDatasource(List<Map> datasource) {
		this.datasource = datasource;
	}
	
}