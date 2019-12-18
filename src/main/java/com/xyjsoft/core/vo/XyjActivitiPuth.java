package com.xyjsoft.core.vo;

import java.util.HashMap;
import java.util.Map;

import com.xyjsoft.core.xyjenum.DisposeTaskEnum;
import com.xyjsoft.core.xyjenum.TaskCompleteTypeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "XyjActivitiPuth", description = "审批流调用数据传递")
public class XyjActivitiPuth {
	@ApiModelProperty(value = "处理状态")
	private DisposeTaskEnum actionname;
	@ApiModelProperty(value = "任务完成状态")
	private TaskCompleteTypeEnum eventtype;
	@ApiModelProperty(value = "流程部署ID")
	private String procdefid;
	@ApiModelProperty(value = "流程KEY")
	private String flowkey;
	@ApiModelProperty(value = "流程实例ID")
	private String instid;
	@ApiModelProperty(value = "流程任务ID")
	private String taskid;
	@ApiModelProperty(value = "业务主键")
	private String businesskey;
	@ApiModelProperty(value = "流程名")
	private String procdefname;
	@ApiModelProperty(value = "流程变量")
	private Map<String, Object> variablemap = new HashMap<String, Object>();
	@ApiModelProperty(value = "拓展数据")
	private Map<String, Object> custommap = new HashMap<String, Object>();

	public DisposeTaskEnum getActionname() {
		return actionname;
	}

	public void setActionname(DisposeTaskEnum actionname) {
		this.actionname = actionname;
	}

	public TaskCompleteTypeEnum getEventtype() {
		return eventtype;
	}

	public void setEventtype(TaskCompleteTypeEnum eventtype) {
		this.eventtype = eventtype;
	}

	public String getProcdefid() {
		return procdefid;
	}

	public void setProcdefid(String procdefid) {
		this.procdefid = procdefid;
	}

	public String getFlowkey() {
		return flowkey;
	}

	public void setFlowkey(String flowkey) {
		this.flowkey = flowkey;
	}

	public String getInstid() {
		return instid;
	}

	public void setInstid(String instid) {
		this.instid = instid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBusinesskey() {
		return businesskey;
	}

	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}

	public String getProcdefname() {
		return procdefname;
	}

	public void setProcdefname(String procdefname) {
		this.procdefname = procdefname;
	}

	public Map<String, Object> getVariablemap() {
		return variablemap;
	}

	public void setVariablemap(Map<String, Object> variablemap) {
		this.variablemap = variablemap;
	}

	public Map<String, Object> getCustommap() {
		return custommap;
	}

	public void setCustommap(Map<String, Object> custommap) {
		this.custommap = custommap;
	}

}
