package com.xyjsoft.core.vo;

import java.util.List;

import com.xyjsoft.core.export.jasperreport.ExportReportBase.ExportReportType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ExportReportWebdata",description = "Web端导出体")
public class ExportReportWebdata {
	/**导出文件名(不带文件后缀)，选传*/
	@ApiModelProperty(value="导出文件名(不带文件后缀)，选传",required = false,example = "导出文件名(不带文件后缀)，选传")
	private String filename;
	/**导出文件类型，xls/pdf/doc,必传*/
	@ApiModelProperty(value="导出文件类型，xls/pdf/doc,必传",required = true,example = "导出文件类型，xls/pdf/doc,必传")
	private ExportReportType exportType;
	/**Web端导出体,必传*/
	@ApiModelProperty(value="Web端导出体",required = true)
	private List<ExportReportWebdataBase> exports;
	
	/**导出文件名(不带文件后缀)，选传*/
	public String getFilename() {
		return filename;
	}
	/**导出文件名(不带文件后缀)，选传*/
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**导出文件类型，xls/pdf/doc,必传*/
	public ExportReportType getExportType() {
		return exportType;
	}
	/**导出文件类型，xls/pdf/doc,必传*/
	public void setExportType(ExportReportType exportType) {
		this.exportType = exportType;
	}
	/**Web端导出体,必传*/
	public List<ExportReportWebdataBase> getExports() {
		return exports;
	}
	/**Web端导出体,必传*/
	public void setExports(List<ExportReportWebdataBase> exports) {
		this.exports = exports;
	}
}
