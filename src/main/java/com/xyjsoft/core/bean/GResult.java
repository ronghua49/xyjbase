package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 类名:GResult
 * 类描述:数据传递
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-08 11:08
 * @since JDK1.8
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ImportBean",description = "数据实体类")
public class GResult {
    @ApiModelProperty(value="true:前端提示成功 false:前端提示报错")
    private boolean isSuc;
    @ApiModelProperty(value="提示信息")
    private String  msg;
    @ApiModelProperty(value="返回的参数")
    private Object  obj;

    public GResult() {
    }
    public GResult(boolean isSuc, String msg) {
        this.isSuc = isSuc;
        this.msg = msg;
    }
    public GResult(boolean isSuc, Object obj) {
        this.isSuc = isSuc;
        this.obj = obj;
    }
    public GResult(boolean isSuc, String msg, Object obj) {
        this.isSuc = isSuc;
        this.msg = msg;
        this.obj = obj;
    }
}
