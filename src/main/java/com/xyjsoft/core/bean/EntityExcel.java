package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("表格和实体类字段属性对应")
public class EntityExcel {

	@ApiModelProperty(value = "实体类字段名")
	private String filedName;
	@ApiModelProperty(value = "表格列")
	private Integer excelCell;
	
	public EntityExcel() {
		super();
	}
	public EntityExcel(String filedName, Integer excelCell) {
		super();
		this.filedName = filedName;
		this.excelCell = excelCell;
	}
	/**
	 * @return the filedName
	 */
	public String getFiledName() {
		return filedName;
	}
	/**
	 * @param filedName the filedName to set
	 */
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	/**
	 * @return the excelCell
	 */
	public Integer getExcelCell() {
		return excelCell;
	}
	/**
	 * @param excelCell the excelCell to set
	 */
	public void setExcelCell(Integer excelCell) {
		this.excelCell = excelCell;
	}
	
	
}
