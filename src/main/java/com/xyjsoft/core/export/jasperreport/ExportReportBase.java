package com.xyjsoft.core.export.jasperreport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.xyjsoft.core.jasperreport.XyjJRDataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/** 
 * 类名: ExportReportBase <br/> 
 * 类描述: 导出基类-导出需要jasper文件. <br/> 
 * date: 2019-4-29 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public abstract class ExportReportBase {
	private static Logger logger = LoggerFactory.getLogger(ExportReportBase.class);
	/**支持的导出类型*/
	public enum ExportReportType{
		/**Excel文件*/
		xls,
		/**PDF文件*/
		pdf,
		/**word文档*/
		doc
	}
	public abstract List<ExportReportObject> getExportData(Map<String,Object> map);
	public abstract boolean beforeExport(Map<String,Object> map);
	public abstract void afterExport(Map<String,Object> map);
	
	/**导出文件类型：xls,pdf,doc*/
	public void webExport(ExportReportType exportType,String fileName,List<ExportReportObject> exports,HttpServletRequest request,HttpServletResponse response){
		try{
			//校验数据
			if(exportType == null){
				response.getWriter().print("<script>parent.alert('未指定导出文件类型')</script>");
			    return;
			}
			if(!(ExportReportType.xls == exportType 
				 || ExportReportType.pdf == exportType
				 || ExportReportType.doc == exportType)){
				response.getWriter().print("<script>parent.alert('不支持的导出文件类型["+exportType+"]')</script>");
			    return;
			}
			if(exports == null || exports.size() == 0){
				response.getWriter().print("<script>parent.alert('导出数据为空')</script>");
			    return;
			}
		    //组装导出数据
			List<JasperPrint> jasperPrintlist = new ArrayList<JasperPrint>();
			for(ExportReportObject exportobject : exports){
				ClassPathResource mbResource = new ClassPathResource("jaspertemplate" + File.separator + exportobject.getExportMbname() + ".jasper");
				/*try {
					mbResource.getFile();	
				}catch(IOException e) {
					response.getWriter().print("<script>parent.alert('模板["+exportobject.getExportMbname()+"]不存在')</script>");
				    return;
				}*/
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(mbResource.getInputStream());
				
			    Map<String, Object> parameters = null;
			    if(exportobject.getParameters() == null){
			    	parameters = new HashMap<String, Object>();
			    }else{
			    	parameters = exportobject.getParameters();
			    }
			    JRDataSource jRDataSource = null;
			    if(exportobject.getDatasource() == null){
			    	jRDataSource = new JREmptyDataSource();
			    }else{
			    	jRDataSource = new XyjJRDataSource(exportobject.getDatasource());
			    }
			    
			    /*已废弃，可通过在ireport中勾选整个模版属性的“Float column footer”和“Ignore pagination”解决分页引起的列头重复打印和文档末尾大空行
			     * if(ExportReportType.xls == exportType){
			    	  prepareXlsReport(report);
			       }
			    */
			    
			    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, jRDataSource);
			    jasperPrintlist.add(jasperPrint);
			}
			
			//输出导出文档流
			response.setCharacterEncoding("UTF-8");
			if(fileName!=null){
				response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(fileName, "UTF-8") + "."+exportType+"\"");
			}else{
				response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + "."+exportType+"\"");
			}
		    ServletOutputStream ouputStream = response.getOutputStream();  
		    
		    if(ExportReportType.xls == exportType){
		    	response.setContentType("application/xls");
		    	
		    	JRXlsExporter exporter=new JRXlsExporter();
		    	
		        /*//方法已过时
		         * exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintlist);
		        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);*/
		    	exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintlist));
		    	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
		       
		    	
		    	/*方法已过时
		        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		        //设置忽略行间的空行，自动删除行与行之间的空行，此处不用该属性，保留模版原样式
		        //exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		        //设置忽略列间的空行，自动删除列与列直接的空列，此处不用该属性，保留模版原样式
		        //exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
		        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
		        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);*/
		    	
		    	SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
		    	xlsReportConfiguration.setDetectCellType(true);
		    	xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(false);
		    	xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(false);
                xlsReportConfiguration.setOnePagePerSheet(false);
                xlsReportConfiguration.setWhitePageBackground(false);
		        exporter.setConfiguration(xlsReportConfiguration);
		        exporter.exportReport();
		    }else if(ExportReportType.pdf == exportType){
		    	response.setContentType("application/pdf");
		    	
		        JRPdfExporter exporter = new JRPdfExporter();  
		        
		        /* 方法已过时
		         * exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintlist);
		        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);*/
		        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintlist));
		    	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
		        exporter.exportReport();
		    }else if(ExportReportType.doc == exportType){
		    	response.setContentType("application/msword;charset=utf-8");
		    	
		    	JRRtfExporter exporter=new JRRtfExporter();
		 	    /* 方法已过时 
		 	     * exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintlist);
		 	    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);*/
		    	
		    	exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintlist));
			    exporter.setExporterOutput(new SimpleWriterExporterOutput(ouputStream));
		 	    exporter.exportReport();
		    }
		    ouputStream.flush(); 
	        ouputStream.close();
		}catch(Exception e){
			logger.error("ExportReportBase:webExport error", e);
			throw new RuntimeException(e.getMessage()); 
		}
	}
	
	/**已废弃，可通过在ireport中勾选整个模版属性的“Float column footer”和“Ignore pagination”解决分页引起的列头重复打印和文档末尾大空行
	 * excel jasper模版文件去掉周围的margin,
	 * 解决跨页因模版margin引起列头重复打印问题*/
	private void prepareXlsReport(JasperReport jasperReport) {
	   try {
		    Field margin = JRBaseReport.class.getDeclaredField("leftMargin");
		    margin.setAccessible(true);
		    margin.setInt(jasperReport, 0);
		    margin = JRBaseReport.class.getDeclaredField("topMargin");
		    margin.setAccessible(true);
		    margin.setInt(jasperReport, 0);
		    margin = JRBaseReport.class.getDeclaredField("bottomMargin");
		    margin.setAccessible(true);
		    margin.setInt(jasperReport, 0);
		    Field pageHeight = JRBaseReport.class.getDeclaredField("pageHeight");
		    pageHeight.setAccessible(true);
		    pageHeight.setInt(jasperReport, 2147483647);
	   }catch (Exception exception) {
		   
	   }
	}
}
