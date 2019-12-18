package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 类名:ImportBean
 * 类描述:导入操作的参数实体类
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-08 11:01
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "ImportBean",description = "导入参数实体类")
public class ImportBean {
    @ApiModelProperty(value="文件路径")
    private String filePath;

}
