package com.xyjsoft.core.export.poireport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xyjsoft.core.vo.ExportNoReportWebDataBase;

/** 
 * 类名: ExportNoReportWebdataExport <br/> 
 * 类描述: 前端发送数据导出类-不需要jasper模版. <br/> 
 * date: 2019-4-28 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public class ExportNoReportWebdataExport extends ExportNoReportBase {

	/**
	 * <b>类名:ExportNoReportWebdataExport</b><br>
	 * <b>方法名:getExportData</b><br>
	 * <b>详细描述</b>:待导出数据组装<br>
	* @param map
	 * {
	 *		exportData:[
	 *					{
	 *					    entityName:excel文件的一个worksheet名称或pdf(暂未支持)的一个逻辑页页名称   选传，一个excel中多worksheet导出,
	 *					    rowheight:默认行高, 选传
	 *						colwidth:默认列宽, 选传
	 *						cols:[ {col1:列1中文},{col2:列2中文},... ],选传,不传时将没有列头信息
	 *					    data:[
	 *								  { col1:值,col2:值,... }
	 *							 ] 必传
	 *				     }   
	 *				  ] <br>
	 * }
	 * @return
	 * @see xyj.soft.export.noreport.ExportNoReportBase#getExportData(java.util.Map)
	 * @exception 
	 * @since  1.0.0
	 */
	@Override
	public List<ExportNoReportObject> getExportData(Map<String, Object> map) {
		//参数合法性检测
		if(!map.containsKey("exportData")){
			return null;
		}
		try {
			List<ExportNoReportWebDataBase> webData = (List<ExportNoReportWebDataBase>) map.get("exportData");
			List<ExportNoReportObject> exports = new ArrayList<ExportNoReportObject>();
			for(ExportNoReportWebDataBase exportNoReportWebDataBase : webData) {
				ExportNoReportObject exportnoreportobject = new ExportNoReportObject();
				exportnoreportobject.setEntityName(exportNoReportWebDataBase.getEntityName());
				exportnoreportobject.setColwidth(exportNoReportWebDataBase.getColwidth());
				exportnoreportobject.setRowheight(exportNoReportWebDataBase.getRowheight());
				if(exportNoReportWebDataBase.getParameters().size() > 0) {
					LinkedHashMap<String, Object> keyMap = new LinkedHashMap<String, Object>();
					for(int ii = 0; ii < exportNoReportWebDataBase.getParameters().size(); ii++){
						Map mapP = exportNoReportWebDataBase.getParameters().get(ii); 
						for(Object keyT : mapP.keySet()){
							keyMap.put(keyT.toString(),mapP.get(keyT));
						}
					}
					if(!keyMap.isEmpty()){
						exportnoreportobject.setParameters(keyMap);	
					}
				}
				exportnoreportobject.setDatasource(exportNoReportWebDataBase.getDatasource());
				exports.add(exportnoreportobject);
			}
			if(exports.size() == 0) {
				return null;
			}else {
				return exports;
			}
		}catch(Exception e) {
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
