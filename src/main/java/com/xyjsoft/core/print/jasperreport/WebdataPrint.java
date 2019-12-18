package com.xyjsoft.core.print.jasperreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xyjsoft.core.vo.PrintReportWebdataBase;

/** 
 * 类名: WebdataPrint <br/> 
 * 类描述: 前端发送数据打印类. <br/> 
 * date: 2019-4-29 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public class WebdataPrint extends PrintBase {

	/**
	 * <b>类名:WebdataPrint</b><br>
	 * <b>方法名:getPrintData</b><br>
	 * <b>详细描述</b>:前端发送数据打印数据组装<br>
	 * @param map 
	 * {
	 *		printData:[
	 *					{
	 *					    mbname:模版名(不带文件后缀)   选传，用于多模版合并pdf打印,
	 *					    parameters:{ parameter1:,parameter2:,... },必传
	 *					    detail:[
	 *								   { field1:,field2:,... }
	 *							   ]必传
	 *				     }   
	 *				  ] 必传<br>
	 * }
	 * @return
	 * @see xyj.soft.print.jasperreport.PrintBase#getPrintData(java.util.Map)
	 * @exception 
	 * @since  1.0.0
	*/
	@Override
	public List<PrintObject> getPrintData(Map<String,Object> map) {
		//参数合法性检测
		if(!map.containsKey("printData")){
			return null;
		}
		try{
			List<PrintReportWebdataBase> webData = (List<PrintReportWebdataBase>) map.get("printData");
			List<PrintObject> prints = new ArrayList<PrintObject>();
			
			for(PrintReportWebdataBase printReportWebdataBase : webData) {
				PrintObject printObject = new PrintObject();
				printObject.setPrintMbname(printReportWebdataBase.getMbname());
				printObject.setParameters(printReportWebdataBase.getParameters());
				printObject.setDatasource(printReportWebdataBase.getDetail());
				prints.add(printObject);
			}
			if(prints.size() == 0) {
				return null;
			}else {
				return prints;
			}
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean beforePrint(Map<String,Object> map) {
		return false;
	}

	@Override
	public void afterPrint(Map<String,Object> map) {
		
	}
	
}
