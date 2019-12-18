package com.xyjsoft.core.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.core.export.jasperreport.ExportReportBase;
import com.xyjsoft.core.export.jasperreport.ExportReportBase.ExportReportType;
import com.xyjsoft.core.export.jasperreport.ExportReportObject;
import com.xyjsoft.core.export.jasperreport.ExportReportWebdataExport;
import com.xyjsoft.core.export.poireport.ExportNoReportBase;
import com.xyjsoft.core.export.poireport.ExportNoReportBase.ExportNoReportType;
import com.xyjsoft.core.export.poireport.ExportNoReportObject;
import com.xyjsoft.core.export.poireport.ExportNoReportWebdataExport;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.http.HttpStatus;
import com.xyjsoft.core.query.AppUtil;
import com.xyjsoft.core.util.AopTargetUtils;
import com.xyjsoft.core.util.HttpUtils;
import com.xyjsoft.core.vo.ExportNoReportWebData;
import com.xyjsoft.core.vo.ExportNoReportWebDataBase;
import com.xyjsoft.core.vo.ExportReportWebdata;
import com.xyjsoft.core.vo.ExportReportWebdataBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("export")
@Api(tags="CommonExportController 通用导出接口")
public class CommonExportController {
	private static Logger logger = LoggerFactory.getLogger(CommonExportController.class);
	
	/**
	 * <b>类名:CommonExportController</b><br>
	 * <b>方法名:exportNoReportWebData</b><br>
	 * <b>详细描述</b>:前端发送数据导出类-不需要jasper模版<br>
	 * @param <b>页面传入参数：</b><br>
	 * {
	 *      filename:文件名
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
	 * @param <b>回传页面数据： </b><br>
	 * 文件输出流导出
	 * <br>
	 * void
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	*/
	@PostMapping(value="/exportNoReportWebData")
	@ApiOperation(value = "无模板导出前端传入内容(使用Form表单post提交，target指定_blank)", httpMethod = "POST", notes = "无模板导出前端传入内容(使用Form表单post提交，target指定_blank)")
	public void exportNoReportWebData(@ApiParam(name="ExportNoReportWebData",value="Web端导出体") @RequestBody ExportNoReportWebData export,
			HttpServletRequest request,HttpServletResponse response) throws IOException{
		//前端数据获取和校验
		if(export.getExports() == null || export.getExports().size() == 0){
			HttpResult.error("参数格式错误：未传入导出数据");
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：未传入导出数据");
			return;
		}
		for(ExportNoReportWebDataBase exportNoReportWebDataBase : export.getExports()) {
			if(exportNoReportWebDataBase.getDatasource() == null || exportNoReportWebDataBase.getDatasource().size() == 0) {
				HttpResult.error("参数格式错误：未传入导出数据");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：未传入导出数据");
				return;
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("exportData", export.getExports());
		
		ExportNoReportBase exportbase = new ExportNoReportWebdataExport();
	    
		//导出前事件，事件函数返回true则阻断导出
	    if(exportbase.beforeExport(map)){
	    	return;
	    }
	    
	    //导出数据获取
	    List<ExportNoReportObject> exports = exportbase.getExportData(map);
	    //导出
	    /* String exportType = map.get("exportType");*/
	    exportbase.webExport(ExportNoReportType.xls,
	    					((export.getFilename()!=null && !export.getFilename().isEmpty()) ? export.getFilename() : null),
	    					exports, request, response);
	    
	    //导出成功后事件，事件中用户可自定义实现
	    exportbase.afterExport(map);
	}
	
	/**
	 * <b>类名:CommonExportController</b><br>
	 * <b>方法名:exportNoReportByClass</b><br>
	 * <b>详细描述</b>:自定义类无模版打印<br>
	 * @param <b>页面传入参数：</b><br>
	 *    filename:导出文件名
	 *	  classname:自定义导出类javabean名  <br>
	 *    ...:前端任意传参数
	 * @param <b>回传页面数据： </b><br>
	 * 文件输出流导出
	 * void
	 * @exception 
	 * @since  1.0.0
	*/
	@PostMapping(value="/exportNoReportByClass")
	@ApiOperation(value = "无模板导出自定义类导出(使用Form表单post提交，target指定_blank)", httpMethod = "POST", notes = "无模板导出自定义类导出(使用Form表单post提交，target指定_blank)")
	public void exportNoReportByClass(
			@ApiParam(name = "filename",value = "导出文件名",required = false) @RequestParam(name = "filename",required = false) String filename,
			@ApiParam(name = "classname",value = "自定义导出类javabean名",required = true) @RequestParam String classname,
			HttpServletRequest request,HttpServletResponse response) throws IOException{
		//前端数据获取和校验
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, String[]> requestMap = request.getParameterMap();
		for(Object key : requestMap.keySet()){
			String[] v = requestMap.get(key);
			map.put(key.toString(), v[0]);
		}
		if(classname == null || classname.length() == 0){
			HttpResult.error("classname参数错误");
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "className参数错误");
			return;
		}
		
		ExportNoReportBase exportbase = null;
		
		try {
			Object bean = AppUtil.getBean(classname);
			if(bean == null) {
				HttpResult.error("classname定义的bean不存在");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "className定义的bean不存在");
				return;
			}
			Object exportObj = AopTargetUtils.getTarget(bean);
			if(exportObj instanceof ExportNoReportBase){
				exportbase = (ExportNoReportBase) exportObj;
			}else{
				HttpResult.error("["+classname+"]导出类必须继承ExportNoReportBase类");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "["+classname+"]导出类必须继承ExportNoReportBase类");
				return;
			}			
		} catch (Exception e) {
			logger.error("获取导出类["+classname+"]错误:"+e.getMessage(), e);
			HttpResult.error("获取导出类["+classname+"]错误:"+e.getMessage());
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "获取导出类["+classname+"]错误:"+e.getMessage());
			return;
		}
		
		//导出前事件，事件函数返回true则阻断导出
	    if(exportbase.beforeExport(map)){
	    	return;
	    }
		//导出数据获取
	    List<ExportNoReportObject> exports = exportbase.getExportData(map);
	    //导出
	    /* String exportType = map.get("exportType");*/
	    if(filename == null || filename.isEmpty()){
	    	exportbase.webExport(ExportNoReportType.xls, null, exports, request, response);
	    }else{
	    	exportbase.webExport(ExportNoReportType.xls, filename, exports, request, response);
	    }
	    
	    //导出成功后事件，事件中用户可自定义实现
	    exportbase.afterExport(map);
	}
	
	/**
	 * <b>类名:CommonExportController</b><br>
	 * <b>方法名:exportReportWebData</b><br>
	 * <b>详细描述</b>:前端数据导出(支持xls/pdf/doc)-需要jasper模版配合使用<br>
	 * @param <b>页面传入参数：</b><br>
	 * {
	 *   filename:文件名-选传
	 *   exportType:导出文件类型(xls/pdf/doc)-必传
	 *   exportData:[
	 *					{
	 *					    mbname:模版名(不带文件后缀)   选传，用于多模版合并pdf打印,
	 *					    parameters:{ parameter1:,parameter2:,... },必传
	 *					    detail:[
	 *								   { field1:,field2:,... }
	 *							   ]必传
	 *				     }   
	 *				 ] <br>
	 * }
	 * @param <b>回传页面数据： </b><br>
	 * 文件输出流导出
	 * void
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	*/
	@PostMapping(value="/exportReportWebData")
	@ApiOperation(value = "jasper模板导出前端传入内容(使用Form表单post提交，target指定_blank)", httpMethod = "POST", notes = "jasper模板导出前端传入内容(使用Form表单post提交，target指定_blank)")
	public void exportReportWebData(@ApiParam(name="ExportReportWebdata",value="Web端导出体") @RequestBody ExportReportWebdata export,
			HttpServletRequest request,HttpServletResponse response) throws IOException{
		//前端数据获取和校验
		if(export.getExportType() == null) {
			HttpResult.error("参数格式错误：exportType参数错误");
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：exportType参数错误");
			return;
		}
		if(export.getExports() == null || export.getExports().size() == 0) {
			HttpResult.error("参数格式错误：未传入导出数据");
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：未传入导出数据");
			return;
		}
		for(ExportReportWebdataBase exportReportWebdataBase:export.getExports()) {
			if(exportReportWebdataBase.getMbname() == null || exportReportWebdataBase.getMbname().length() == 0) {
				HttpResult.error("参数格式错误：mbname参数错误");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：mbname参数错误");
				return;
			}
			if(exportReportWebdataBase.getParameters() == null && (exportReportWebdataBase.getDetail() == null || exportReportWebdataBase.getDetail().size() == 0)) {
				HttpResult.error("参数格式错误：parameters参数和detail参数二选一必传");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：parameters参数和detail参数二选一必传");
				return;
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("exportData", export.getExports());
		
		ExportReportBase exportbase = new ExportReportWebdataExport();
		//导出前事件，事件函数返回true则阻断导出
	    if(exportbase.beforeExport(map)){
	    	return;
	    }
		//导出数据获取
	    List<ExportReportObject> exports = exportbase.getExportData(map);
	    //导出
	    exportbase.webExport(export.getExportType(),
	    			         ((export.getFilename()!=null && !export.getFilename().isEmpty()) ? export.getFilename() : null),
	    			         exports, request, response);
	    
	    //导出成功后事件，事件中用户可自定义实现
	    exportbase.afterExport(map);
	}
	/**
	 * <b>类名:CommonExportController</b><br>
	 * <b>方法名:exportReportByClass</b><br>
	 * <b>详细描述</b>:自定义类导出(支持xls/pdf/doc)-需要jasper模版配合使用<br>
	 * @param <b>页面传入参数：</b><br>
	 *   filename:导出文件名
	 *   classname:自定义导出类javabean名-必传  
	 *   exportType:导出文件类型，xls/pdf/doc,必传
	 *   ...:前端任意传参数
	 * @param <b>回传页面数据： </b><br>
	 * 文件输出流导出
	 * void
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	*/
	@PostMapping(value="/exportReportByClass")
	@ApiOperation(value = "jasper模板导出自定义类导出(使用Form表单post提交，target指定_blank)", httpMethod = "POST", notes = "jasper模板导出自定义类导出(使用Form表单post提交，target指定_blank)")
	public void exportReportByClass(@ApiParam(name = "filename",value = "导出文件名",required = false) @RequestParam(name = "filename",required = false) String filename,
			@ApiParam(name = "classname",value = "自定义导出类javabean名",required = true) @RequestParam String classname,
			@ApiParam(name = "exporttype",value = "导出文件类型，xls/pdf/doc,必传",required = true) @RequestParam ExportReportType exporttype,
			HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, String[]> requestMap = request.getParameterMap();
		for(Object key : requestMap.keySet()){
			String[] v = requestMap.get(key);
			map.put(key.toString(), v[0]);
		}
		if(classname == null || classname.length() == 0){
			HttpResult.error("classname参数错误");
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "className参数错误");
			return;
		}
		if(exporttype == null){
			HttpResult.error("exporttype参数错误");
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "exporttype参数错误");
			return;
		}
		ExportReportBase exportbase = null;
		try {
			Object bean = AppUtil.getBean(classname);
			if(bean == null) {
				HttpResult.error("classname定义的bean不存在");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "className定义的bean不存在");
				return;
			}
			Object exportObj = AopTargetUtils.getTarget(bean);
			if(exportObj instanceof ExportReportBase){
				exportbase = (ExportReportBase) exportObj;
			}else{
				HttpResult.error("["+classname+"]导出类必须继承ExportReportBase类");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "["+classname+"]导出类必须继承ExportReportBase类");
				return;
			}			
		} catch (Exception e) {
			logger.error("获取导出类["+classname+"]错误:"+e.getMessage(), e);
			HttpResult.error("获取导出类["+classname+"]错误:"+e.getMessage());
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "获取导出类["+classname+"]错误:"+e.getMessage());
			return;
		}
		
		//导出前事件，事件函数返回true则阻断导出
	    if(exportbase.beforeExport(map)){
	    	return;
	    }
		//导出数据获取
	    List<ExportReportObject> exports = exportbase.getExportData(map);
	    //导出
	    /* String exportType = map.get("exportType");*/
	    if(filename == null || filename.isEmpty()){
	    	exportbase.webExport(exporttype, null, exports, request, response);
	    }else{
	    	exportbase.webExport(exporttype, filename, exports, request, response);
	    }
	    
	    //导出成功后事件，事件中用户可自定义实现
	    exportbase.afterExport(map);
	}
	
}
