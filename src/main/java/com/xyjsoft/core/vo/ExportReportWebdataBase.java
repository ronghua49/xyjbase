package com.xyjsoft.core.vo;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ExportReportWebdataBase",description = "Web端导出体") 
public class ExportReportWebdataBase {
	/**模版名(不带文件后缀), 必传*/
	@ApiModelProperty(value="模版名(不带文件后缀), 必传",required = true)
	private String mbname;
	/**对应jasperReport中Parameters区域，与detail二选一必传*/
	@ApiModelProperty(value="对应jasperReport中Parameters区域，与detail二选一必传",required = true)
	private Map<String,Object> parameters;
	/**对应jasperReport中Fields区域，与Parameters二选一必传*/
	@ApiModelProperty(value="对应jasperReport中Fields区域，与Parameters二选一必传",required = true)
	private List<Map> detail;
	
	/**模版名(不带文件后缀), 必传*/
	public String getMbname() {
		return mbname;
	}
	/**模版名(不带文件后缀), 必传*/
	public void setMbname(String mbname) {
		this.mbname = mbname;
	}
	/**对应jasperReport中Parameters区域，与detail二选一必传*/
	public Map<String, Object> getParameters() {
		return parameters;
	}
	/**对应jasperReport中Parameters区域，与detail二选一必传*/
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	/**对应jasperReport中Fields区域，与Parameters二选一必传*/
	public List<Map> getDetail() {
		return detail;
	}
	/**对应jasperReport中Fields区域，与Parameters二选一必传*/
	public void setDetail(List<Map> detail) {
		this.detail = detail;
	}
}
