package com.xyjsoft.core.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类名:FileUploadAutoConfigProperties
 * 类描述:常用文件夹配置
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-13 8:29
 * @since JDK1.8
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "file.dir")
public class FileDirConfigProperties {
    @ApiModelProperty(value = "基础文件夹")
    private String baseDir;
    @ApiModelProperty(value = "图像文件夹")
    private String docDir;
    @ApiModelProperty(value = "文档文件夹")
    private String imgDir;
    @ApiModelProperty(value = "个人文件夹")
    private String personDir="d://tmp";
    @ApiModelProperty(value = "临时文件夹")
    private String tmpDir;
    @ApiModelProperty(value = "指定文件夹")
    private String fileDir;

}
