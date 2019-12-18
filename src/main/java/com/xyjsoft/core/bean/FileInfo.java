package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 类名:FileInfo
 * 类描述:TODO
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-13 11:36
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileInfo {
    @ApiModelProperty("文件名称")
    private String fileName;
    @ApiModelProperty("文件路径")
    private String fileUrl;
}
