package com.xyjsoft.core.export.jasperreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xyjsoft.core.vo.ExportReportWebdataBase;

/** 
 * 类名: ExportReportWebdataExport <br/> 
 * 类描述: 前端数据导出类-配合jasper模版使用. <br/> 
 * date: 2019-4-29 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public class ExportReportWebdataExport extends ExportReportBase {

	/**
	 * <b>类名:ExportReportWebdataExport</b><br>
	 * <b>方法名:getExportData</b><br>
	 * <b>详细描述</b>:待导出数据组装<br>
	 * @param map
	 * {
	 *		exportData:[
	 *					{
	 *					    mbname:模版名(不带文件后缀)   选传，用于多模版合并pdf打印,
	 *					    parameters:{ parameter1:,parameter2:,... },必传
	 *					    detail:[
	 *								   { field1:,field2:,... }
	 *							   ]必传
	 *				     }   
	 *				  ] <br>
	 * }
	 * @return
	 * @see xyj.soft.export.jasperreport.ExportReportBase#getExportData(java.util.Map)
	 * @exception 
	 * @since  1.0.0
	 */
	@Override
	public List<ExportReportObject> getExportData(Map<String,Object> map) {
		//参数合法性检测
		if(!map.containsKey("exportData")){
			return null;
		}
		try{
			List<ExportReportWebdataBase> webData = (List<ExportReportWebdataBase>) map.get("exportData");
			List<ExportReportObject> exports = new ArrayList<ExportReportObject>();
			
			for(ExportReportWebdataBase exportReportWebdataBase : webData){
				ExportReportObject exportobject = new ExportReportObject();
				exportobject.setExportMbname(exportReportWebdataBase.getMbname());
				exportobject.setParameters(exportReportWebdataBase.getParameters());
				exportobject.setDatasource(exportReportWebdataBase.getDetail());
				exports.add(exportobject);
			}
			if(exports.size() == 0) {
				return null;
			}else {
				return exports;
			}
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean beforeExport(Map<String, Object> map) {
		return false;
	}

	@Override
	public void afterExport(Map<String, Object> map) {
		
	}

}
