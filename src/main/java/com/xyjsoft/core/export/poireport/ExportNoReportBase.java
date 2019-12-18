package com.xyjsoft.core.export.poireport;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * 类名: ExportNoReportBase <br/> 
 * 类描述: 导出基类-不需要模版文件. <br/> 
 * date: 2019-4-28 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public abstract class ExportNoReportBase {
	private static Logger logger = LoggerFactory.getLogger(ExportNoReportBase.class);
	
	/**支持的导出类型,目前只支持xls*/
	public enum ExportNoReportType{
		xls
	}
	public abstract List<ExportNoReportObject> getExportData(Map<String,Object> map);
	public abstract boolean beforeExport(Map<String,Object> map);
	public abstract void afterExport(Map<String,Object> map);
	
	/**
	 * <b>类名:ExportNoReportBase</b><br>
	 * <b>方法名:webExport</b><br>
	 * <b>详细描述</b>:web端导出文件<br>
	 * @param exportType 导出文件类型 :目前只支持xls
	 * @param fileName 导出文件名,可为null
	 * @param exports 导出数据
	 * @param request 
	 * @param response
	 * void
	 * @exception 
	 * @since  1.0.0
	*/
	public void webExport(ExportNoReportType exportType,String fileName,List<ExportNoReportObject> exports,HttpServletRequest request,HttpServletResponse response){
		try{
			//校验数据
			if(exportType == null){
				response.getWriter().print("<script>parent.alert('未指定导出文件类型')</script>");
			    return;
			}
			if(!(ExportNoReportType.xls == exportType
				 /*|| ExportNoReportType.pdf == exportType)*/)){
				response.getWriter().print("<script>parent.alert('不支持的导出文件类型["+exportType+"]')</script>");
			    return;
			}
			if(exports == null || exports.size() == 0){
				response.getWriter().print("<script>parent.alert('导出数据为空')</script>");
			    return;
			}
			
			//输出导出文档流
			response.setCharacterEncoding("UTF-8");
			if(fileName==null){
				response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + "."+exportType+"\"");
			}else{
				response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(fileName, "UTF-8") + "."+exportType+"\"");
			}
		    ServletOutputStream ouputStream = response.getOutputStream();  
		    
		    if(ExportNoReportType.xls == exportType){
		    	response.setContentType("application/xls");
		    	//创建excel文档
		    	HSSFWorkbook workbook = new HSSFWorkbook();
		    	
		    	HSSFFont font = workbook.createFont();
	    		font.setFontHeightInPoints((short) 9);
	    		font.setFontName("宋体");
	    		font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
	    		
	    		HSSFCellStyle style = workbook.createCellStyle();
				/*style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);*/
				style.setFont(font); // 调用字体样式对象
				/*设置单元格换行*/
				/*style.setWrapText(true);*/
				// 增加表格边框的样式 例子
				/*style.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
				style.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
				style.setTopBorderColor(HSSFColor.GOLD.index);
				style.setLeftBorderColor(HSSFColor.PLUM.index);*/
	    		
		    	//创建worksheet
		    	for(int i = 0; i < exports.size(); i++){
		    		ExportNoReportObject exportnoreportobject = exports.get(i);
		    		HSSFSheet sheet = null;
		    		if(exportnoreportobject.getEntityName() != null 
		    				&& !exportnoreportobject.getEntityName().trim().isEmpty()){
		    			sheet = workbook.createSheet(exportnoreportobject.getEntityName().trim());	
		    		}else{
		    			sheet = workbook.createSheet("Sheet"+(i+1));
		    		}
		    		/*设置默认行高*/
		    		if(exportnoreportobject.getRowheight() != null){
		    			sheet.setDefaultRowHeightInPoints(exportnoreportobject.getRowheight());
		    		}else{
		    			sheet.setDefaultRowHeightInPoints(20);	
		    		}
		    		/*设置默认列宽*/
		    		if(exportnoreportobject.getColwidth() != null){
		    			sheet.setDefaultColumnWidth(exportnoreportobject.getColwidth()*256);
		    		}else{
		    			sheet.setDefaultColumnWidth(15);	
		    		}
					
					//记录列顺序
					LinkedHashMap<String, Object> mapKey = new LinkedHashMap<String, Object>();
					int rowindex = 0;
					if(exportnoreportobject.getParameters() != null){
						//创建列头信息
			    		HSSFRow row = sheet.createRow(rowindex);
			    		int row_colindex = 0;
			    		for(String key : exportnoreportobject.getParameters().keySet()){
			    			sheet.setDefaultColumnStyle(row_colindex, style);
			    			if(exportnoreportobject.getColwidth() != null){
			    				sheet.setColumnWidth(row_colindex, exportnoreportobject.getColwidth()*256);
				    		}else{
				    			sheet.autoSizeColumn(row_colindex, true);
				    		}
			    			
			    			HSSFCell cell = row.createCell(row_colindex++);
			    			cell.setCellType(CellType.STRING);
			    			cell.setCellValue(exportnoreportobject.getParameters().get(key).toString());
			    		}
			    		rowindex++;
			    		
			    		mapKey = exportnoreportobject.getParameters();
					}else{
						if(exportnoreportobject.getDatasource() != null 
			    				&& exportnoreportobject.getDatasource().size() > 0){
							Map map = exportnoreportobject.getDatasource().get(0);
							int colindex = 0;
							for(Object key : map.keySet()){
								mapKey.put(key.toString(), colindex++);
	    					}
						}
						
					}
		    		if(exportnoreportobject.getDatasource() != null 
		    				&& exportnoreportobject.getDatasource().size() > 0){
		    			for(int j = 0; j < exportnoreportobject.getDatasource().size(); j++){
		    				Map map = exportnoreportobject.getDatasource().get(j);
		    				
		    				HSSFRow row = sheet.createRow(rowindex);
		    				int row_colindex = 0;
	    					//按照列头顺序插入数据
	    					for(String key : mapKey.keySet()){
	    						sheet.setDefaultColumnStyle(row_colindex, style);
	    						if(exportnoreportobject.getColwidth() != null){
				    				sheet.setColumnWidth(row_colindex, exportnoreportobject.getColwidth()*256);
					    		}else{
					    			sheet.autoSizeColumn(row_colindex, true);
					    		}
	    						HSSFCell cell = row.createCell(row_colindex++);
	    						if(map.containsKey(key)){
	    							Object obj = map.get(key);
	    							if(obj instanceof java.sql.Date){
	    								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		                
	    				                String tmpString = new String(sdf.format((java.sql.Date) obj));
	    				                cell.setCellType(CellType.STRING);
	    				                cell.setCellValue(tmpString);
	    							}else if(obj instanceof java.util.Date){
	    								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		   
	    								String tmpString = new String(sdf.format((java.util.Date) obj));
	    								cell.setCellType(CellType.STRING);
	    				                cell.setCellValue(tmpString);
	    							}else if(obj instanceof java.math.BigDecimal
	    									|| obj instanceof java.lang.Double){
	    								cell.setCellType(CellType.NUMERIC);//CELL_TYPE_NUMERIC
	    								cell.setCellValue(Double.parseDouble(obj.toString()));
	    							}else if(obj instanceof java.lang.Integer){
	    								cell.setCellType(CellType.NUMERIC);//CELL_TYPE_NUMERIC
	    								cell.setCellValue(Integer.parseInt(obj.toString()));
	    							}else if(obj instanceof java.lang.Boolean){
	    								cell.setCellType(CellType.BOOLEAN);//CELL_TYPE_BOOLEAN
	    								cell.setCellValue((java.lang.Boolean)obj);
	    							}else if(obj instanceof java.lang.String){
	    								cell.setCellType(CellType.STRING);
	    				                cell.setCellValue(obj.toString());
	    							}
	    						}else{
	    							cell.setCellType(CellType.BLANK);//CELL_TYPE_BLANK
	    						}
	    					}
		    				rowindex++;
		    			}
		    		}
		    	}
		    	workbook.write(ouputStream);
		    	workbook.close();
		    }/*else if("pdf".equalsIgnoreCase(exportType)){
		    	response.setContentType("application/pdf");
		    	
		    }*/
		    ouputStream.flush(); 
	        ouputStream.close();
		}catch(Exception e){
			logger.error("ExportNoReportBase:webExport ERROR",e);
			throw new RuntimeException(e.getMessage()); 
		}
	}
}
