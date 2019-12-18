package com.xyjsoft.core.controller;

import com.google.common.collect.Lists;
import com.xyjsoft.core.bean.FileInfo;
import com.xyjsoft.core.config.FileDirConfigProperties;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

/**
 * 类名:UploadController
 * 类描述:文件上传通用接受
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-13 11:06
 * @since JDK1.8
 */
@RestController
public class UploadController {
	private Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    FileDirConfigProperties fileDirConfigProperties;
    @Autowired
    HttpServletRequest request;
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @PostMapping(value = "/uploadTemplate")
    @ApiOperation(value = "上传模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件流对象,接收数组格式", required = true, dataType = "__File"),
            @ApiImplicitParam(name = "id", value = "模板id", required = true),
            @ApiImplicitParam(name = "fileDir", value = "文件相对位置")}
    )
    @ResponseBody
    public void uploadTemplate(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "fileDir",required = false) String fileDir,
            @RequestParam(value = "id",required = true) Long id) throws IOException {
        List<MultipartFile> a=Lists.newArrayList();
        a.add(file);
        if(StringUtils.isNotEmpty(fileDir)){
            List<FileInfo> uploadFiles= upload(a.toArray(new MultipartFile[]{}));
        }

    }

    private List<FileInfo> upload( MultipartFile[] files, String fileDir) {
        String realPath = request.getSession().getServletContext().getRealPath(fileDir);
        List<FileInfo> uploadFiles = Lists.newArrayList();
        for (MultipartFile file : files) {
            File targetFile = upload(file, realPath);
            uploadFiles.add(new FileInfo().setFileName(targetFile.getName()).setFileUrl(targetFile.getPath()));
        }
        return uploadFiles;
    }
    private List<FileInfo> upload( MultipartFile[] files) {
        String realPath = request.getSession().getServletContext().getRealPath(fileDirConfigProperties.getBaseDir());
        List<FileInfo> uploadFiles = Lists.newArrayList();
        for (MultipartFile file : files) {
            File targetFile = upload(file, realPath);
            uploadFiles.add(new FileInfo().setFileName(targetFile.getName()).setFileUrl(targetFile.getPath()));
        }
        return uploadFiles;
    }

    private File upload(MultipartFile file, String realPath) {
        File realFile = new File(realPath);
        if (!realFile.exists()) {
            realFile.mkdirs();
        }
        File targetFile = new File(realPath, file.getOriginalFilename());
        try {
            file.transferTo(targetFile);
            LOGGER.debug("文件" + file.getOriginalFilename() + "上传成功");
        } catch (IOException e) {
        	logger.error("[UploadController:upload:IOException]", e);
        }
        return targetFile;
    }
}
