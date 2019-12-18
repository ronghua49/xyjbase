package com.xyjsoft.core.vo;

import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ExportNoReportWebData",description = "Web端导出体")
public class ExportNoReportWebData {
	/**导出文件名，选传*/
	@ApiModelProperty(value="导出文件名，选传",required = false,example = "导出文件名，选传")
	private String filename;
	/**Web端导出体*/
	@ApiModelProperty(value="Web端导出体",required = true)
	private List<ExportNoReportWebDataBase> exports;
	
	/**导出文件名，选传*/
	public String getFilename() {
		return filename;
	}
	/**导出文件名，选传*/
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**Web端导出体*/
	public List<ExportNoReportWebDataBase> getExports() {
		return exports;
	}
	/**Web端导出体*/
	public void setExports(List<ExportNoReportWebDataBase> exports) {
		this.exports = exports;
	}
	
}
