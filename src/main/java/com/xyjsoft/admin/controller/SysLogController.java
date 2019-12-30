package com.xyjsoft.admin.controller;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xyjsoft.core.config.XyjconfigProperties;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.influx.InfluxClient;
import com.xyjsoft.core.log.impl.SysLogService;
import com.xyjsoft.core.model.SysLog;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.query.PageBean;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 日志控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("log")
@Api(tags="SysLogController 日志记录")
public class SysLogController {

	@Autowired
	private SysLogService sysLogServiceImpl;
	@Autowired
	private InfluxClient influxClient;
	@Autowired
	private XyjconfigProperties xyjconfigProperties;
	@PostMapping(value="/query")
	@ApiOperation(value = "日志查询", httpMethod = "POST", notes = "日志查询")
	public HttpResult query(@ApiParam(name="queryFilter",value="查询条件")@RequestBody QueryFilter queryFilter) {
		if("0".equals(xyjconfigProperties.getLogType())) {
			try {
				PageBean pageBean = queryFilter.getPageBean();
				PageList queryByQueryFilter = influxClient.queryByQueryFilter(queryFilter, "sys_log");
				return HttpResult.ok(queryByQueryFilter);
			} catch (Exception e) {
				return HttpResult.error("日志查询异常");
			}
		}else{
			try {
				PageList<SysLog> query = sysLogServiceImpl.query(queryFilter);
				return HttpResult.ok(query);
			} catch (SystemException e) {
				e.printStackTrace();
				return HttpResult.error("日志查询异常");
			} catch (Exception e) {
				e.printStackTrace();
				return HttpResult.error("日志查询异常");
			}
		}
		
	}
	
	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		return HttpResult.ok(sysLogServiceImpl.findPage(pageRequest));
	}
	
	@PostMapping(value="/save")
	public int save(@RequestBody SysLog record){
		int save = sysLogServiceImpl.save(record);
		return save;
	}
}
