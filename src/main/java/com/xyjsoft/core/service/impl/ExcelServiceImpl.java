package com.xyjsoft.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyjsoft.core.config.XyjconfigProperties;
import com.xyjsoft.core.controller.EureKaUtiles;
import com.xyjsoft.core.feign.IclassAttribute;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.log.impl.ILogfeign;
import com.xyjsoft.core.model.Tableinfo;
import com.xyjsoft.core.query.PageBean;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.service.ExcelService;
import com.xyjsoft.core.util.FileUtils;
import com.xyjsoft.core.util.StringUtils;
import com.xyjsoft.core.vo.entityName;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类名:ExcelServiceImpl
 * 类描述: Excel 操作
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-09-04 8:39
 * @since JDK1.8
 */
@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    SysTableinfoServiceImpl sysTableInfoService;
    @Autowired
    EureKaUtiles eureKaUtiles;
    @Autowired
    private XyjconfigProperties xyjconfigProperties;
    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * <b>类名:</b> ExcelServiceImpl <br>
     * <b>方法名</b>: exportExcel <br>
     * <b>详细描述</b>:   <br>
     * @param queryFilter 查询条件
     * @param request 请求
     * @param response  响应
     * @return java.lang.String 返回信息
     * @since JDK1.8
     * @author gsh456
     * @date 2019/9/4 8:49
     */
    @Override
    public String exportExcel(QueryFilter queryFilter, HttpServletRequest request, HttpServletResponse response) {
        String tableNameKey = "tableName";
        String fileNameKey = "fileName";
        ServletOutputStream outStream = null;
        try {
            response.reset();
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            Map<String, String> selfDate = queryFilter.getSelfDate();
            String tableName = Optional.ofNullable(selfDate.get(tableNameKey)).orElseThrow(() -> new RuntimeException("表名不能为空"));
            tableName = tableName.trim();
            String fileName = selfDate.get(fileNameKey);
            //如果前端没有设定文件名，则使用表名为下载的文件名
            //宋建国，2019-09-06
            if(StringUtils.isBlank(fileName)){
                fileName= tableName;
            }
            fileName+= ".xlsx";
            response.setHeader("fileName", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "fileName");
            List<Tableinfo> byTableName = findByTableName(tableName);
            List<List> data = getData(queryFilter, request,byTableName);
            Workbook excel = createExcel(data);
            outStream = response.getOutputStream();
            excel.write(outStream);
            outStream.close();
            // FileUtils.exportExcel(response,excel,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //为了处理前端下载文件时无法接收到具体错误信息，解决方案是向Header信息中增加“xyjError”字段传出，前端获取
                //宋建国，2019-09-06
                String xyjError = e.getMessage();
                response.setHeader("xyjError", URLEncoder.encode(xyjError, "UTF-8"));
                response.setHeader("Access-Control-Expose-Headers", "xyjError");
                if(outStream != null){
                    outStream.close();
                }
                throw new RuntimeException(xyjError);
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new RuntimeException(e1.getMessage());
            }
        }
        return null;
    }

    private List<Tableinfo> findByTableName(String tableName) {
        /*自定义日志feign，微服务指向xyj-admin*/
        try {
            String serviceId = xyjconfigProperties.getLogServiceId(),
                    url = "";
            for(String s: discoveryClient.getServices()){
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(s);
                for(ServiceInstance si : serviceInstances){
                    if(serviceId.equalsIgnoreCase(si.getServiceId())) {
                        url = si.getUri().toString();
                        break;
                    }
                }
            }
            ILogfeign logfeign = (ILogfeign) Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .options(new Request.Options(1000,3500))
                    .retryer(new Retryer.Default(5000,5000,3))
                    .target(ILogfeign.class,url);
            HttpResult result = logfeign.findByTableName(tableName);
            Object data = result.getData();
            List<Tableinfo> parseArray = JSONObject.parseArray(JSONObject.toJSONString(data),Tableinfo.class);
            return parseArray;
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用xyj-admin查找表名方法出错");
        }
    }

    /**
     * 数据创建
     * @param list 数据
     * @return Workbook  创建好的excel包装
     * @since JDK1.8
     * @author gsh456
     * @date 2019/9/5 9:14
     */
    private Workbook createExcel(List<List> list){
        Workbook workbook = new XSSFWorkbook();
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        Sheet sheet = workbook.createSheet();
        for (int i = 0; i < list.size(); i++) {
            //空出一行
            Row row = sheet.createRow(i+1);
            List list1 = list.get(i);
            for (int i1 = 0; i1 < list1.size(); i1++) {
                if(i==0){
                    Cell cell0 = row.createCell(0);//创建单元格
                    cell0.setCellValue("序号");
                }else{
                    Cell cell0 = row.createCell(0);//创建单元格
                    cell0.setCellValue(i);
                    cell0.setCellType(CellType.NUMERIC);
                }
                Cell cell = row.createCell(i1+1);//创建单元格
                String data = (String)list1.get(i1);
                if (data.matches("^(-?\\d+)(\\.\\d+)?$")) {
                    //符合数字型
                    cell.setCellValue(Double.parseDouble(data));
                    cell.setCellType(CellType.NUMERIC);
                } else {
                    cell.setCellValue(data);
                }
            }
        }
        return workbook;
    }
    /**
     * 调用rest服务获取数据
     * @param queryFilter 查询条件
     * @param request 请求
     * @param tableInfos 表头
     * @return java.util.List<java.util.List> 数据
     * @since JDK1.8
     * @author gsh456
     * @date 2019/9/5 9:15
     */
    private List<List> getData(QueryFilter queryFilter,HttpServletRequest request,List<Tableinfo> tableInfos){
        final String urlKey ="url";
        Map<String, String> selfDate = queryFilter.getSelfDate();
        String url = selfDate.get(urlKey);
        if(StringUtils.isBlank(url)){
            throw new RuntimeException("网址不存在");
        }
        PageBean pageBean = queryFilter.getPageBean();
        //默认导出1000条
        pageBean.setPageSize(1000);
        PageList data = getData(url, queryFilter, request);
        String tableNameKey = "tableName";
        String tableName = Optional.ofNullable(selfDate.get(tableNameKey)).orElseThrow(() -> new RuntimeException("表名不能为空"));
        tableName =tableName.trim();
        return data2List(data,tableInfos,tableName);
    }
    /**
     * 调用rest服务获取数据
     * @param url 请求网址
     * @param queryFilter 查询条件
     * @param request  请求
     * @return com.xyjsoft.core.query.PageList 返回数据
     * @since JDK1.8
     * @author gsh456
     * @date 2019/9/5 9:16
     */
    private PageList getData(String url, QueryFilter queryFilter,HttpServletRequest request){
        String queryFilterJson = JSON.toJSONString(queryFilter);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        String token = request.getHeader("token");
        requestHeaders.add("token", token);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        requestHeaders.setContentType(type);
        requestHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> requestEntity = new HttpEntity<>(queryFilterJson, requestHeaders);
        PageList list;
        try {
            JSONObject json = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class).getBody();
            if(json==null){
                throw new RuntimeException("查询不到数据");
            }
            if(json.containsKey("data")){
                JSONObject data1 = json.getJSONObject("data");
                list=JSONObject.parseObject(data1.toJSONString(),PageList.class);
            }else if(json.containsKey("rows")){
                list=JSONObject.parseObject(json.toJSONString(),PageList.class);
            }else{
                throw new RuntimeException("数据查询错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据获取失败");
        }
        return list;
    }
    /**
     * 数据汇总
     * @param data 明细表数据
     * @param tableInfos  表头数据
     * @return java.util.List<java.util.List> 数据
     * @since JDK1.8
     * @author gsh456
     * @date 2019/9/5 9:19
     */
    private List<List>  data2List(PageList data,List<Tableinfo> tableInfos,String tableName){
        List rows = data.getRows();
        if(rows==null||rows.size()==0){
            throw new RuntimeException("导出数据不能为空");
        }
        List<List> list =new ArrayList<>();
        List<String> listTmp =new ArrayList<>();
        list.add(listTmp);
        List<entityName> entityClass = eureKaUtiles.getEntityClass();
        List<entityName> collect =entityClass.stream().filter(e -> tableName.equalsIgnoreCase(e.getclassName())).collect(Collectors.toList());
        if(collect.size()!=1){
            throw new RuntimeException("未在项目中查到实体类，通用导出不支持该表名");
        }
        for (Object row : rows) {
            List<String> listTmp1 =new ArrayList<>();
            Class<?> clazz ;
            try {
                clazz=Class.forName(collect.get(0).getClassFullName());
            }catch (Exception e){
                throw new RuntimeException("不支持类型");
            }
            Field[] declaredFields = clazz.getDeclaredFields();
            Object invokes;
            Object bean = JSONObject.parseObject(JSONObject.toJSONString(row),clazz);
            for (Tableinfo tableInfo : tableInfos) {
                String prop = tableInfo.getProp();
                for (Field field : declaredFields) {
                    String name = field.getName();
                    //必须是表格渲染设置里面显示的字段名
                    if(prop.equals(name)){
                        if(!listTmp.contains(tableInfo.getLabel())){
                            listTmp.add(tableInfo.getLabel());
                        }
                        Method m;
                        try {
                            m = clazz.getMethod("get" + replaceFirst(field.getName()));
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            //没有get方法就跳过
                            continue;
                        }
                        try {
                            invokes =  m.invoke(bean);
                            if(invokes==null){
                                listTmp1.add("");
                            }else{
                                listTmp1.add(invokes.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //执行get方法错误，就报错并且停止运行
                            throw new RuntimeException("获取数据异常");
                        }
                    }
                }
            }
            list.add(listTmp1);
        }
        return list;
    }
    /**
     * 大写字符串第一个字符。
     * @param str 制度穿
     * @return 处理后的字符串
     */
    private String replaceFirst(String str){
        return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
    }
}
