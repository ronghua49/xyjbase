package com.xyjsoft.core.controller;

import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.service.impl.ExcelServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类名:ExcelController
 * 类描述:excel文件操作
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-09-04 8:37
 * @since JDK1.8
 */
@RestController("ExcelController")
@RequestMapping("excel")
@Api(tags="ExcelController excel文件操作")
public class ExcelController {
    @Autowired
    private ExcelServiceImpl excelService;
    @Autowired
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;

    /**
     * 保存前端表格初始化数据
     * @param queryFilter 查询类
     * @return
     */
    @PostMapping(value="/exportExcel")
    @ApiOperation(value = "将查询到的数据导出excel文件", notes = "{\n" +
            "\"selfDate\": {\n" +
            "\"url\": \"查询使用的网址\",\n" +
            "\"tableName\": \"表格渲染的类名\"\n" +
            "\"fileName\": \"文件名(可以不传)\"\n" +
            "\t}\n" +
            "}")
    public void exportExcel(@RequestBody QueryFilter queryFilter){
        excelService.exportExcel(queryFilter,request,response);
    }

}
