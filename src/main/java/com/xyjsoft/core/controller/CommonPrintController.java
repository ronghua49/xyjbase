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

import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.http.HttpStatus;
import com.xyjsoft.core.print.jasperreport.PrintBase;
import com.xyjsoft.core.print.jasperreport.PrintObject;
import com.xyjsoft.core.print.jasperreport.WebdataPrint;
import com.xyjsoft.core.query.AppUtil;
import com.xyjsoft.core.util.AopTargetUtils;
import com.xyjsoft.core.util.HttpUtils;
import com.xyjsoft.core.vo.PrintReportWebdataBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("print")
@Api(tags="CommonPrintController 通用打印接口")
public class CommonPrintController {
	private static Logger logger = LoggerFactory.getLogger(CommonPrintController.class);

	/**
	 * <b>类名:CommonPrintController</b><br>
	 * <b>方法名:printWebData</b><br>
	 * <b>详细描述</b>:web前端自定义数据打印(预览模式时，使用Form表单post提交，target指定_blank)<br>
	 * @param <b>页面传入参数：</b><br>
	 *	[
	 *		{
	 *	    	 mbname:模版名(不带文件后缀)   选传，若传入则外层mbname参数失效，用于多模版合并pdf打印,
	 *			 parameters:{ parameter1:,parameter2:,... },必传
	 *			 detail:[
	 *						{ field1:,field2:,... }
	 *					]必传
	 *		}   
	 *	 ]    必传<br>
	 * @param <b>回传页面数据： </b><br>
	 * 	PDF输出流预览打印<br/>
	 * @exception 
	 * @since  1.0.0
	*/
	@PostMapping(value="/printWebData")
	@ApiOperation(value = "web前端自定义数据打印(预览模式时，使用Form表单post提交，target指定_blank)", httpMethod = "POST", notes = "web前端自定义数据打印(预览模式时，使用Form表单post提交，target指定_blank)")
	public void printWebData(@ApiParam(name = "classname",value = "web打印体",required = true) @RequestBody List<PrintReportWebdataBase> webdata,
			HttpServletRequest request,HttpServletResponse response) throws IOException{
		for(PrintReportWebdataBase printReportWebdataBase : webdata) {
			if(printReportWebdataBase.getMbname() == null || printReportWebdataBase.getMbname().length() == 0) {
				HttpResult.error("参数格式错误：mbname参数错误");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：mbname参数错误");
				return;
			}
			if(printReportWebdataBase.getParameters() == null && (printReportWebdataBase.getDetail() == null || printReportWebdataBase.getDetail().size() == 0)) {
				HttpResult.error("参数格式错误：parameters参数和detail参数二选一必传");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数格式错误：parameters参数和detail参数二选一必传");
				return;
			}
		}
		PrintBase printbase = new WebdataPrint();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("printData", webdata);
	    //打印前事件，事件函数返回true则阻断打印
	    if(printbase.beforePrint(map)){
	    	return;
	    }
	    
	    //打印数据获取
	    List<PrintObject> prints = printbase.getPrintData(map);
	    //打印
    	printbase.webPriewPdfPrint(prints, request, response,true);
	    //打印成功后事件，事件中用户可自定义实现
	    printbase.afterPrint(map);
	}
	/**
	 * <b>类名:CommonPrintController</b><br>
	 * <b>方法名:printDataByClass</b><br>
	 * <b>详细描述</b>:后台自定义类数据组装打印(预览模式时，使用Form表单post提交，target指定_blank)<br>
	 * @param <b>页面传入参数：</b><br>
	 *	 classname:自定义导出类javabean名  <br>
	 *   ...:前端任意传参数
	 * @param <b>回传页面数据： </b><br>
	 * 	PDF输出流预览打印<br/>
	 * @exception 
	 * @since  1.0.0
	*/
	@PostMapping(value="/printDataByClass")
	@ApiOperation(value = "后台自定义类数据组装打印(预览模式时，使用Form表单post提交，target指定_blank)", httpMethod = "POST", notes = "后台自定义类数据组装打印(预览模式时，使用Form表单post提交，target指定_blank")
	public void printDataByClass(@ApiParam(name = "classname",value = "自定义导出类javabean名",required = true) @RequestParam String classname,
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
		PrintBase printbase = null;
		try {
			Object bean = AppUtil.getBean(classname);
			if(bean == null) {
				HttpResult.error("classname定义的bean不存在");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "className定义的bean不存在");
				return;
			}
			Object printObj = AopTargetUtils.getTarget(bean);
			if(printObj instanceof PrintBase){
				printbase = (PrintBase) printObj;
			}else{
				HttpResult.error("["+classname+"]导出类必须继承PrintBase类");
				HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "["+classname+"]导出类必须继承PrintBase类");
				return;
			}			
		} catch (Exception e) {
			logger.error("获取导出类["+classname+"]错误:"+e.getMessage(), e);
			HttpResult.error("获取导出类["+classname+"]错误:"+e.getMessage());
			HttpUtils.print(response, HttpStatus.SC_INTERNAL_SERVER_ERROR, "获取导出类["+classname+"]错误:"+e.getMessage());
			return;
		}
		
		//打印前事件，事件函数返回true则阻断打印
	    if(printbase.beforePrint(map)){
	    	return;
	    }
	    
	    //打印数据获取
	    List<PrintObject> prints = printbase.getPrintData(map);
	    //打印
    	printbase.webPriewPdfPrint(prints, request, response,true);
	    
	    //打印成功后事件，事件中用户可自定义实现
	    printbase.afterPrint(map);
	}
}
