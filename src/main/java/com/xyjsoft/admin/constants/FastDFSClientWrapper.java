package com.xyjsoft.admin.constants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.xyjsoft.admin.model.FilePath;
import com.xyjsoft.admin.service.FilePathService;
import com.xyjsoft.admin.service.impl.FilePathServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xyjsoft.admin.config.AppConfig;

@Component
public class FastDFSClientWrapper {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private AppConfig appConfig;   // 项目参数配置
    @Autowired
    private FilePathServiceImpl filePathService;

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
    	InputStream inputStream = file.getInputStream();
    	long size = file.getSize();
    	String originalFilename = file.getOriginalFilename();
    	String extension = FilenameUtils.getExtension(originalFilename);
        StorePath storePath = storageClient.uploadFile(inputStream,size, extension,null);
        return getResAccessUrl(storePath);
    }

    /**
     * 将一段字符串生成一个文件上传
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
        return getResAccessUrl(storePath);
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = "http://" + appConfig.getResHost()
                + ":" + appConfig.getStoragePort() + "/" + storePath.getFullPath();
        return fileUrl;
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
        	throw new RuntimeException("删除失败");
        }
    }

    public String uploadFileType(MultipartFile file, String type) throws IOException {
        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse(file.getName());
        List<FilePath> filePathes = filePathService.findByType(type);
        if(filePathes==null||filePathes.size()==0){
            throw new RuntimeException("类型未找到");
        }
        FilePath filePath = filePathes.get(0);
        //判断是否为图片服务器
        if("FastDFS".equals(filePath.getType())) {
        	String uploadFile = uploadFile(file);
        	return uploadFile;
        }else{
        	String path = filePath.getPath();
            if(StringUtils.isEmpty(path)){
                throw new RuntimeException("目录为空");
            }
            File fileLocation = new File(path);
            if(!fileLocation.exists()){
                boolean mkdirs = fileLocation.mkdirs();
                if(!mkdirs){
                    throw new RuntimeException("文件目录创建失败，请确认配置是否正确");
                }
            }
            String fileLocation1 = createFileLocation(path);
            File targetFile = new File(fileLocation1,filename );
            try {
                file.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件转换失败");
            }
            return targetFile.getAbsolutePath();
        }
    }
    private String createFileLocation(String path){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss_SSS");
        String timeStr = timeFormat.format(date);
        String dateStr = dateFormat.format(date);
        //进行路径判定
        String realPath ;
        String replace = path.replace("\\", "/");
        if(replace.endsWith("/")){
            realPath= replace+dateStr+"/"+timeStr+"/";
        }else{
            realPath= replace+"/"+dateStr+"/"+timeStr+"/";
        }
        //创建这个文件夹
        new File(realPath).mkdirs();
        return realPath;
    }
}