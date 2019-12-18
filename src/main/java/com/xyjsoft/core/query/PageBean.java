package com.xyjsoft.core.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * <pre>
 * 继承自RowBounds，可做为SqlSessionTemplate查询时的分页参数
 * </pre>
 * @author gsh456
 * @date 2018年4月4日
 */
@ApiModel(description="分页对象")
public class PageBean extends RowBounds implements Serializable{
	private static final long serialVersionUID = -3758630357085712072L;
	/**
	 * 默认每页显示的记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * 默认显示第一页
	 */
	public final static int NO_PAGE = 1;

	@ApiModelProperty(name="page",notes="页号")
	protected int page = NO_PAGE;
	@ApiModelProperty(name="pageSize",notes="分页大小")
	protected int pageSize = DEFAULT_PAGE_SIZE;
	@ApiModelProperty(name="showTotal",notes="是否进行汇总",example = "true")
	protected boolean showTotal = true;
	@ApiModelProperty(name="summary",notes="统计查询结构")
	protected List<QuerySummary> summary = new  ArrayList<QuerySummary>();
	

	public List<QuerySummary> getSummary() {
		return summary;
	}

	public void setSummary(List<QuerySummary> summary) {
		this.summary = summary;
	}

	public PageBean(){}

	public PageBean(Integer page){
		this.page = page;
	}

	public PageBean(Boolean showTotal){
		this.showTotal = showTotal;
	}

	public PageBean(Integer page, Integer pageSize){
		this.page = page;
		this.pageSize = pageSize;
	}

	public PageBean(Integer page, Integer pageSize, Boolean showTotal){
		this.page = page;
		this.pageSize = pageSize;
		this.showTotal = showTotal;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public boolean showTotal() {
		return showTotal;
	}

	public void setShowTotal(boolean showTotal) {
		this.showTotal = showTotal;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@JsonIgnore
	@Override
	public int getLimit() {
		return this.pageSize;
	}

	/**
	 * 获取起始行号
	 * @return 起始行号
	 */
	@JsonIgnore
	@Override
	public int getOffset() {
		return this.page > 0 ? (this.page - 1) * this.pageSize : 0;
	}
}
