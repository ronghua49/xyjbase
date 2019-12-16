package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 *  (BillsRules)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-04 18:11:54
 * ---------------------------
 */
@ApiModel(value = "BillsRules",description = "") 
public class BillsRules implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 主键 */
	@ApiModelProperty(value="主键",readOnly = true)
	private Long id;
	/** 日期规则 */
	@ApiModelProperty(value="日期规则")
	private String dateRule;
	/** 时间规则 */
	@ApiModelProperty(value="时间规则")
	private String timeRule;
	/** 结尾规则（1:随机数,2:累加,3:累加(每日重置)） */
	@ApiModelProperty(value="结尾规则（1:随机数,2:累加,3:累加(每日重置)）")
	private Integer endRule;
	/** 结尾尾数数量（0:自由尾数，其他数值规定数量） */
	@ApiModelProperty(value="结尾尾数数量（0:自由尾数，其他数值规定数量）")
	private Integer endNum;
	/** 第一个和第二个间隔 */
	@ApiModelProperty(value="第一个和第二个间隔")
	private String oneTwo;
	/** 第二个和第三个间隔 */
	@ApiModelProperty(value="第二个和第三个间隔")
	private String twoThree;
	/** 1-2-3;3-2-1 等分别代表，日期-时间-数字 */
	@ApiModelProperty(value="日期-时间-数值")
	private String orderRule;
	/** 关联数据id */
	@ApiModelProperty(value="关联数据id",readOnly = true)
	private String dataId;
	/** 关联数据类型 */
	@ApiModelProperty(value="关联数据类型")
	private String dataType;
	/** 上一个数字 */
	@ApiModelProperty(value="上一个数字",readOnly = true)
	private Integer lastNum;
	/** 当前日期 */
	@ApiModelProperty(value="当前日期",readOnly = true)
	private String lastDate;
	/** 创建人 */
	@ApiModelProperty(value="创建人",readOnly = true)
	private String createBy;
	/** 创建时间 */
	@ApiModelProperty(value="创建时间",readOnly = true)
	private java.util.Date createTime;
	/** 最后修改人 */
	@ApiModelProperty(value="最后修改人",readOnly = true)
	private String lastUpdateBy;
	/** 最后修改时间 */
	@ApiModelProperty(value="最后修改时间",readOnly = true)
	private java.util.Date lastUpdateTime;
	/** 岗位ID */
	@ApiModelProperty(value="岗位ID",readOnly = true)
	private Long postId;
	/** 父ID */
	@ApiModelProperty(value="父ID",readOnly = true)
	private Long cid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateRule() {
		return dateRule;
	}

	public void setDateRule(String dateRule) {
		this.dateRule = dateRule;
	}

	public String getTimeRule() {
		return timeRule;
	}

	public void setTimeRule(String timeRule) {
		this.timeRule = timeRule;
	}

	public Integer getEndRule() {
		return endRule;
	}

	public void setEndRule(Integer endRule) {
		this.endRule = endRule;
	}

	public Integer getEndNum() {
		return endNum;
	}

	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}

	public String getOneTwo() {
		return oneTwo;
	}

	public void setOneTwo(String oneTwo) {
		this.oneTwo = oneTwo;
	}

	public String getTwoThree() {
		return twoThree;
	}

	public void setTwoThree(String twoThree) {
		this.twoThree = twoThree;
	}

	public String getOrderRule() {
		return orderRule;
	}

	public void setOrderRule(String orderRule) {
		this.orderRule = orderRule;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getLastNum() {
		return lastNum;
	}

	public void setLastNum(Integer lastNum) {
		this.lastNum = lastNum;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public java.util.Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

}