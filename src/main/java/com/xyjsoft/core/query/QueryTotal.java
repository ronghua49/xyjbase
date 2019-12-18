package com.xyjsoft.core.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名:ShowTotal
 * 类描述:TODO
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-16 15:44
 * @since JDK1.8
 */
@Data
@ApiModel("合计")
@NoArgsConstructor
public class QueryTotal {
    @ApiModelProperty("字段名")
    private String property;
    @ApiModelProperty(value="count:数量,sum:求和,avg:平均值,text:文本",example = "sum")
    private String totalType;
    @ApiModelProperty(value = "是否查全部数据的汇总信息,如果不填则为true",example = "true")
    private Boolean showContainAllData=true;

}
