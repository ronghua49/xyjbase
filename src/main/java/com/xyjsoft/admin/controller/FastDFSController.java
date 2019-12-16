package com.xyjsoft.admin.controller;

import com.xyjsoft.core.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xyjsoft.admin.constants.FastDFSClientWrapper;
import com.xyjsoft.core.http.HttpResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * ---------------------------
 * 会员充值卡 (FileUploadController)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-03-04 20:11:46
 * 说明：  我是由代码生成器生生成的
 * ---------------------------
 */
@RestController
@RequestMapping("FileUploadController")
@Api(tags="FileUploadController 文件上传")
public class FastDFSController {

    @Resource
    HttpServletResponse response;

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @PostMapping("/uploadFile")
	@ApiOperation(value = "上传文件方法", httpMethod = "POST", notes = "上传文件方法")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String imgUrl = dfsClient.uploadFile(file);
        return imgUrl;
    }
	
    @PostMapping("/deleteFile")
	@ApiOperation(value = "删除文件方法", httpMethod = "POST", notes = "删除文件方法")
    public HttpResult deleteFile(@ApiParam(name="url",value="文件url")@RequestParam("url") String url) {
    	if(url == null || url.length() == 0 || "null".equals(url)) {
    		throw new RuntimeException("文件路径不能为空");
    	}
    	dfsClient.deleteFile(url);
    	return HttpResult.ok("删除成功");
    }
    @PostMapping("/uploadFileType")
    @ApiOperation(value = "上传文件方法", httpMethod = "POST", notes = "上传文件方法")
    public String uploadFileType(@RequestParam("file") MultipartFile file,@RequestParam String type) {
        String imgUrl = "";
		try {
			imgUrl = dfsClient.uploadFileType(file,type);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return imgUrl;
    }
    @PostMapping("/downLoadFile")
    @ApiOperation(value = "下载文件方法", httpMethod = "POST", notes = "path是admin的绝对路径")
    public void downLoadFile(@ApiParam("绝对路径") @RequestParam String path)  {
        FileUtils.downloadFile(path,response);
    }

}
