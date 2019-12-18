package com.xyjsoft.core.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.regex.Pattern;


/**
 * 排序对象
 * 
 * @author gsh456
 * @date 2018年4月4日
 */
@ApiModel(description="排序对象")
public class FieldSort implements Serializable{
	private static final long serialVersionUID = -1712830705595375365L;
	private static String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";

	@ApiModelProperty(name="direction",notes="排序方向")
	private Direction direction;
	@ApiModelProperty(name="property",notes="排序字段")
	private String property;

	public FieldSort(){}

	public FieldSort(String property) {
		this(property, Direction.ASC);
	}

	public FieldSort(String property, Direction direction) {
		this.direction = direction;
		this.property = property;
	}

	/**
	 * 构造器
	 * @param property 排序属性
	 * @param direction 排序方向
	 * @param clazz 实体类
	 * @return
	 */
	public static FieldSort create(String property, String direction){
		return new FieldSort(property, Direction.fromString(direction));
	}

	public Direction getDirection() {
		return direction;
	}

	public String getProperty() {
		return property;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 是否SQL注入
	 * @param str SQL语句
	 * @return 是否为SQL注入
	 */
	public static boolean isSQLInjection(String str){
		return !Pattern.matches(INJECTION_REGEX, str);
	}
	
	/**
	 * 装换为SQL语句
	 * @param clazz 实体类型
	 * @return SQL语句
	 */
	public String toSql(Class<?> clazz) {
		if(isSQLInjection(property)){
			throw new IllegalArgumentException("SQLInjection property: " + property);
		}
		// 将实体类属性转换为数据库列名
		property = FieldConvertUtil.property2Field(property, clazz);
		return property + (direction == null ? "" : " " + direction.name());
	}
}