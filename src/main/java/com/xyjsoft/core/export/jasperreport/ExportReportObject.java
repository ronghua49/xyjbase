package com.xyjsoft.core.export.jasperreport;

import java.util.List;
import java.util.Map;



/** 
 * 类名: ExportReportObject <br/> 
 * 类描述: Jasper Report导出实体. <br/> 
 * date: 2019-4-29 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public class ExportReportObject {
	/**导出模版名，导出文件*.jasper需放置在/resources/jasper/目录下*/
	private String exportMbname;
	/**模版主参数，parameters*/
	private Map<String,Object> parameters;
	/**模版数据源，用于报表Detail区域循环打印可重复区域*/
	private List<Map> datasource;
	
	/**导出模版名，导出文件*.jasper需放置在/webapp/jasper/目录下*/
	public String getExportMbname() {
		return exportMbname;
	}
	/**导出模版名，导出文件*.jasper需放置在/webapp/jasper/目录下*/
	public void setExportMbname(String exportMbname) {
		this.exportMbname = exportMbname;
	}
	/**模版主参数，parameters*/
	public Map<String, Object> getParameters() {
		return parameters;
	}
	/**模版主参数，parameters*/
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	/**模版数据源，用于报表Detail区域循环打印可重复区域*/
	public List<Map> getDatasource() {
		return datasource;
	}
	/**模版数据源，用于报表Detail区域循环打印可重复区域*/
	public void setDatasource(List<Map> datasource) {
		this.datasource = datasource;
	}
}
