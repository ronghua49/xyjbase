package com.xyjsoft.core.xyjenum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="流程处理枚举")
public enum TaskCompleteTypeEnum {
	@ApiModelProperty(name="endEvent",notes="结束事件")
	endEvent,
	@ApiModelProperty(name="taskComplete",notes="任务完成")
	taskComplete;
}
