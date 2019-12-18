package com.xyjsoft.core.query;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Restful接口返回的分页列表数据
 * @author gsh456
 */
@ApiModel(description="任务查询参数对象")
public class PageList<E>{
	@ApiModelProperty(name="rows",notes="结果列表")
	List<E> rows;
	@ApiModelProperty(name="total",notes="总记录数")
	long total;
	@ApiModelProperty(name="page",notes="当前页码")
	int page;
	@ApiModelProperty(name="pageSize",notes="每页条数")
	int pageSize;
	@ApiModelProperty(name="map",notes="统计查询")
	Map<String,Object> map = new HashMap<>();
	
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public PageList(){
	}
	
	public PageList(List<E> c){
		if(c instanceof Page){
			Page<E> page = (Page<E>)c;
			this.rows = page.getResult();
			this.total = page.getTotal();
			this.page = page.getPageNum();
			this.pageSize = page.getPageSize();
		}
		else{
			this.rows = c;
			this.total = this.rows.size();
		}
	}
	
	public PageList(List<E> c,Map<String,Object> map){
		if(c instanceof Page){
			Page<E> page = (Page<E>)c;
			this.rows = page.getResult();
			this.total = page.getTotal();
			this.page = page.getPageNum();
			this.pageSize = page.getPageSize();
			this.map = map;
		}
		else{
			this.rows = c;
			this.total = this.rows.size();
			this.map = map;
		}
	}
	
	public PageList(Page<E> page){
		this.rows = page.getResult();
		this.total = page.getTotal();
		this.page = page.getPageNum();
		this.pageSize = page.getPageSize();
	}
	
	public List<E> getRows() {
		return rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
