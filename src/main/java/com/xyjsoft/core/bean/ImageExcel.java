package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("插入图片")
public class ImageExcel {
	@ApiModelProperty(value = "起始位置")
	private Integer left;
	@ApiModelProperty(value = "结束位置")
	private Integer right;
	@ApiModelProperty(value = "图片地址")
	private String urlImage;
	//偏移量
	@ApiModelProperty(value = "偏移量")
	private Integer offset;
	
	@ApiModelProperty(value = "文本内容")
	private String text;
	@ApiModelProperty(value = "年份")
	private String year;
	@ApiModelProperty(value = "开始月")
	private String startMonth;
	@ApiModelProperty(value = "结束月")
	private String endMonth;
	
	
	
	
	public ImageExcel(Integer left, Integer right, String urlImage, Integer offset, String text, String year,
			String startMonth, String endMonth) {
		super();
		this.left = left;
		this.right = right;
		this.urlImage = urlImage;
		this.offset = offset;
		this.text = text;
		this.year = year;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
	}
	public ImageExcel() {
		super();
	}
	public ImageExcel(Integer left, Integer right, String urlImage, Integer offset,String text) {
		super();
		this.left = left;
		this.right = right;
		this.urlImage = urlImage;
		this.offset = offset;
		this.text = text;
	}
	
	
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * @return the startMonth
	 */
	public String getStartMonth() {
		return startMonth;
	}
	/**
	 * @param startMonth the startMonth to set
	 */
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
	/**
	 * @return the endMonth
	 */
	public String getEndMonth() {
		return endMonth;
	}
	/**
	 * @param endMonth the endMonth to set
	 */
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the left
	 */
	public Integer getLeft() {
		return left;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(Integer left) {
		this.left = left;
	}
	/**
	 * @return the right
	 */
	public Integer getRight() {
		return right;
	}
	/**
	 * @param right the right to set
	 */
	public void setRight(Integer right) {
		this.right = right;
	}
	/**
	 * @return the urlImage
	 */
	public String getUrlImage() {
		return urlImage;
	}
	/**
	 * @param urlImage the urlImage to set
	 */
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	
}
