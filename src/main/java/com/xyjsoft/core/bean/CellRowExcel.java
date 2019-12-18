package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("指定表格行和列，以及插入的数据")
public class CellRowExcel {
	@ApiModelProperty(value = "指定的行")
	private Integer row;
	@ApiModelProperty(value = "指定的列")
	private Integer cell;
	@ApiModelProperty(value = "插入的数据")
	private String data;
	
	
	
	
	public CellRowExcel() {
		super();
	}
	public CellRowExcel(Integer row, Integer cell) {
		super();
		this.row = row;
		this.cell = cell;
	}
	

	public CellRowExcel(Integer row, Integer cell, String data) {
		super();
		this.row = row;
		this.cell = cell;
		this.data = data;
	}
	/**
	 * @return the row
	 */
	
	public Integer getRow() {
		return row-1;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}
	/**
	 * @return the cell
	 */
	public Integer getCell() {
		return cell-1;
	}
	/**
	 * @param cell the cell to set
	 */
	public void setCell(Integer cell) {
		this.cell = cell;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	
}
