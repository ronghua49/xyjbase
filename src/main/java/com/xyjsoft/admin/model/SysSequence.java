package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.xyjsoft.core.model.BaseModel;
/**
 * ---------------------------
 * 自增序列 (SysSequence)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 16:05:08
 * ---------------------------
 */
@ApiModel(value = "SysSequence",description = "自增序列") 
public class SysSequence  {

	private static final long serialVersionUID = 1L;
	/** 序列名称 */
	@ApiModelProperty(value="序列名称")
	private String name;
	/** 序列当前值 */
	@ApiModelProperty(value="序列当前值")
	private Integer currentValue;
	/** 序列每次增加值 */
	@ApiModelProperty(value="序列每次增加值")
	private Integer increment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}
	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}
}