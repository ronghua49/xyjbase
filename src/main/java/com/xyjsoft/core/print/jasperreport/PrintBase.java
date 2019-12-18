package com.xyjsoft.core.print.jasperreport;

import java.io.File;
import java.io.IOException;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/** 
 * 类名: PrintBase <br/> 
 * 类描述: 打印基类. <br/> 
 * date: 2019-4-29 下午6:38:04 <br/> 
 * 
 * @author 王浩伟 
 * @version 2.0
 * @since JDK 1.8 
 */
public abstract class PrintBase {
	private static Logger logger = LoggerFactory.getLogger(PrintBase.class);
	
	public abstract List<PrintObject> getPrintData(Map<String,Object> map);
	public abstract boolean beforePrint(Map<String,Object> map);
	public abstract void afterPrint(Map<String,Object> map);
	
	/**
	 * <b>类名:PrintBase</b><br>
	 * <b>方法名:webPriewPdfPrint</b><br>
	 * <b>详细描述</b>:web端PDF(直接/预览)打印<br>
	 * 预览打印时，浏览器电脑需要安装Adobe PDF reader;<br>
	 * 直接打印时，是服务端打印机打印。<br>
	 * @param prints List<PrintObject>
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param isPriew 是否预览,此参数只有在prints数组长度为1时有意义;否则失效。默认为预览打印，设置isPriew为false且prints数组长度为1时可直接打印
	 * void
	 * @exception 
	 * @since  1.0.0
	*/
	public void webPriewPdfPrint(List<PrintObject> prints,HttpServletRequest request,HttpServletResponse response,Boolean isPriew){
		try{
			//校验打印数据
			if(prints == null || prints.size() == 0){
				response.getWriter().print("<script>parent.alert('打印数据为空')</script>");
			    return;
			}
		    //组装打印数据
			List<JasperPrint> jasperPrintlist = new ArrayList<JasperPrint>();
			for(PrintObject printobject : prints){
				ClassPathResource mbResource = new ClassPathResource("jaspertemplate" + File.separator + printobject.getPrintMbname() + ".jasper");
				/*try {
					mbResource.getFile();	
				}catch(IOException e) {
					response.getWriter().print("<script>parent.alert('模板["+printobject.getPrintMbname()+"]不存在')</script>");
				    return;
				}*/
				/*在线编译jrxml
				 * InputStream inputStream = getClass().getResourceAsStream("jaspertemplate" + File.separator + printobject.getPrintMbname() + ".jrxml");
	            JasperDesign jd = JRXmlLoader.load(inputStream);
	            jasperReport = (JasperCompileManager.compileReport(jd));*/
				
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(mbResource.getInputStream());
				
			    Map<String, Object> parameterValue = null;
			    if(printobject.getParameters() == null){
			    	parameterValue = new HashMap<String, Object>();
			    }else{
			    	parameterValue = printobject.getParameters();
			    }
			    JRDataSource dataSource = null;
			    if(printobject.getDatasource() == null){
			    	dataSource = new JREmptyDataSource();
			    }else{
			    	dataSource = new XyjJRDataSource(printobject.getDatasource());
			    }
			    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameterValue, dataSource);
			    jasperPrintlist.add(jasperPrint);
			}
			//默认为预览打印
			boolean priew = true;
			if(isPriew == false && jasperPrintlist.size() == 1){
				priew = false;
			}
			
			if(priew){
				//输出pdf文档流
				response.setContentType("application/pdf");
			    response.setCharacterEncoding("UTF-8");
			    ServletOutputStream ouputStream = response.getOutputStream();  
			    // 使用JRPdfExproter导出器导出pdf  
		        JRPdfExporter exporter = new JRPdfExporter();  
		        /*//方法已过时
		         * exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintlist);
		        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);*/
		    	exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintlist));
		    	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
		        exporter.exportReport();
		        ouputStream.flush(); 
		        ouputStream.close();
			}else{
				//直接打印
				JasperPrintManager.printReport(jasperPrintlist.get(0), true);
			}
		}catch(Exception e){
			logger.error("PrintBase:webPriewPdfPrint error", e);
			throw new RuntimeException(e.getMessage()); 
		}
	}
}
