package com.xyjsoft.core.print.jasperreport;

import java.util.List;
import java.util.Map;

/** 
 * 类名: PrintObject <br/> 
 * 类描述: Jasper Report打印实体. <br/> 
 * date: 2019-4-29 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public class PrintObject {
	/**打印模版名，打印文件*.jasper需放置在/resources/jasper/目录下*/
	private String printMbname;
	/**模版主参数，parameters*/
	private Map<String,Object> parameters;
	/**模版数据源，用于报表Detail区域循环打印可重复区域*/
	private List<Map> datasource;
	
	
	/**打印模版名，打印文件*.jasper需放置在/webapp/jasper/目录下*/
	public String getPrintMbname() {
		return printMbname;
	}
	/**打印模版名，打印文件*.jasper需放置在/webapp/jasper/目录下*/
	public void setPrintMbname(String printMbname) {
		this.printMbname = printMbname;
	}
	/**模版主参数，parameters*/
	public Map<String,Object> getParameters() {
		return parameters;
	}
	/**模版主参数，parameters*/
	public void setParameters(Map<String,Object> parameters) {
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
