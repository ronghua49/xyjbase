package com.xyjsoft.core.xyjenum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="流程处理枚举")
public enum DisposeTaskEnum {
	@ApiModelProperty(name="agree",notes="根据流程图流转")
	agree,
	@ApiModelProperty(name="selectAgree",notes="允许选择下一步候选人，审批")
	selectAgree,
	@ApiModelProperty(name="transferSignature",notes="转签")
	transferSignature,
	@ApiModelProperty(name="reject",notes="驳回上一节点")
	reject,
	@ApiModelProperty(name="rejectToStart",notes="驳回至发起人")
	rejectToStart,
	@ApiModelProperty(name="rejectToAssign",notes="驳回至指定节点")
	rejectToAssign,
	@ApiModelProperty(name="processStart",notes="流程发起")
	processStart;	
}
