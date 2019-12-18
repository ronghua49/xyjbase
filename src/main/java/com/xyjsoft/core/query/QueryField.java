package com.xyjsoft.core.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xyjsoft.core.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 构建查询条件
 * 
 * @author gsh456
 * @date 2018年4月4日
 */
@ApiModel(description="查询条件")
public class QueryField{
	@ApiModelProperty(name="property", notes="实体类属性")
	private String property;
	@ApiModelProperty(name="operation", notes="比较符")
	private QueryOP operation = QueryOP.EQUAL;
	@ApiModelProperty(name="value", notes="比较值")
	private Object value;
	@ApiModelProperty(name="relation", notes="同一个分组内的多个条件之间的组合关系")
	private FieldRelation relation = FieldRelation.AND;
	@ApiModelProperty(name="group", notes="查询条件分组，默认分组为main，多个分组将会按照and的关系组合在一起", value="main")
	private String group = "main";
	//值是否已初始化
	@ApiModelProperty(value="false")
	private Boolean hasInitValue = false;
	
	public QueryField(){}
	
	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 */
	public QueryField(String property, Object value){
		this(property, value, QueryOP.EQUAL, FieldRelation.AND);
		
	}
	
	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param operation 查询符号
	 */
	public QueryField(String property, Object value, QueryOP operation){
		this(property, value, operation, FieldRelation.AND);
	}
	
	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param relation 与其他查询条件的组合关系
	 */
	public QueryField(String property, Object value, FieldRelation relation){
		this(property, value, QueryOP.EQUAL, relation);
	}
	
	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param relation 与其他查询条件的组合关系
	 */
	public QueryField(String property, Object value, QueryOP operation, FieldRelation relation){
		this(property, value, operation, relation, null);
	}
	
	/**
	 * 构造函数
	 * @param property 实体类属性
	 * @param value 查询值
	 * @param operation 查询符号
	 * @param relation 与其他查询条件的组合关系
	 */
	public QueryField(String property, Object value, QueryOP operation, FieldRelation relation, String group){
		this.property = property;
		this.value = value;
		this.operation = operation;
		this.relation = relation;
		if(group == null) {
			this.group = group;
		}
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public QueryOP getOperation() {
		return operation;
	}
	public void setOperation(QueryOP operation) {
		this.operation = operation;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	@JsonIgnore
	public Boolean isGroup() {
		return false;
	}
	public FieldRelation getRelation() {
		return relation;
	}
	public void setRelation(FieldRelation relation) {
		this.relation = relation;
	}
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	public Boolean getHasInitValue() {
		return hasInitValue;
	}

	public void setHasInitValue(Boolean hasInitValue) {
		this.hasInitValue = hasInitValue;
	}

	private void initSqlValue(){
		if(hasInitValue){
			// 初始化值的操作只允许执行一次
			return;
		}
		hasInitValue = true;
		if (QueryOP.IN.equals(operation))
		{
			this.value = getInValueSql();
		}
		if(QueryUtils.isNotEmpty(value)){
			if(QueryOP.LIKE.equals(operation)&&!value.toString().startsWith("%")&&!value.toString().endsWith("%")){
				this.value = "%"+value+"%";
			}else if(QueryOP.LEFT_LIKE.equals(operation)&&!value.toString().startsWith("%")){
				this.value = "%"+value;
			}else if(QueryOP.RIGHT_LIKE.equals(operation)&&!value.toString().endsWith("%")){
				this.value = value+"%";
			}else if(QueryOP.EQUAL_IGNORE_CASE.equals(operation)){
				this.value = this.value.toString().toUpperCase();
			}
		}
	}
	 
	/**
	 * 将查询条件生成为SQL语句
	 * @param isFirst	当前是否为第一个查询条件
	 * @param clazz		查询条件所对应的实体类
	 * @return			SQL语句
	 * @throws SystemException
	 */
	@SuppressWarnings("rawtypes")
	public String toSql(Class clazz,int num) throws Exception {
		//根据operation替换value的值,转换为sql
		initSqlValue();
		if (operation == null)
		{
			operation = QueryOP.EQUAL;
		}
		String tmp ="";
		if(num>0){
			tmp=property+num;
		}else{
			tmp=property;
		}
		String fieldParam;
		if(property==null){
			throw new RuntimeException("The 'property' in QueryField can not be empty.");
		}
		if (property.indexOf(".") > -1)
		{
			fieldParam = "#{" + tmp.substring(property.indexOf(".") + 1) + "}";
		} else
		{
			fieldParam = "#{" + tmp + "}";
		}
		String sql = FieldConvertUtil.property2Field(property, clazz);
		sql =generalDate(sql);
		property=tmp;
		if (QueryOP.EQUAL.equals(operation))
		{
			sql += " = " + fieldParam;
		}else if (QueryOP.EQUAL_IGNORE_CASE.equals(operation))
		{
			sql = "upper("+sql+")" + " = " + fieldParam;
		} else if (QueryOP.LESS.equals(operation))
		{
			sql += " < " + fieldParam;
		} else if (QueryOP.LESS_EQUAL.equals(operation))
		{
			sql += " <= " + fieldParam;
		} else if (QueryOP.GREAT.equals(operation))
		{
			sql += " > " + fieldParam;
		} else if (QueryOP.GREAT_EQUAL.equals(operation))
		{
			sql += " >= " + fieldParam;
		} else if (QueryOP.NOT_EQUAL.equals(operation))
		{
			sql += " != " + fieldParam;
		} else if (QueryOP.LEFT_LIKE.equals(operation))
		{
			sql += " like " + fieldParam;
		} else if (QueryOP.RIGHT_LIKE.equals(operation))
		{
			sql += " like  " + fieldParam;
		} else if (QueryOP.LIKE.equals(operation))
		{
			sql += " like  " + fieldParam;
		} else if (QueryOP.IS_NULL.equals(operation))
		{
			sql += " is null ";
		} else if (QueryOP.NOTNULL.equals(operation))
		{
			sql += " is not null";
		} else if (QueryOP.IN.equals(operation))
		{
			String valueStr = this.value.toString();
			if(valueStr.startsWith("(")&&valueStr.endsWith(")")){
				valueStr = valueStr.replace("(", "");
				valueStr = valueStr.replace(")", "");
				String[] strList = valueStr.split(",");
				int len = strList.length;
				//防止当in中元素大于1000时，执行语句报错
				if(len>1000){
					sql = getOutLimitInSql(sql, strList);
				}else{
					sql += " in  " + this.value;
				}
			}else{
				sql += " in  " + this.value;
			}
		} else if (QueryOP.BETWEEN.equals(operation))
		{
			sql += getBetweenSql();
		} else
		{
			sql += " =  " + fieldParam;
		}
		return sql;
	}


	/**
	 * 识别value中的日期格式
	 * @param sql sql
	 * @return String sql
	 * @since JDK1.8
	 * @author gsh456
	 * @date 2019/7/14 10:14
	 */
	private String generalDate(String sql){
		String dataSourceName = AppUtil.getDataSourceName();
		String str = this.value.toString();
		if(StringUtils.isBlank(str)){
			return sql;
		}
		String localDate = " LZ";
		String localDateTime = " LZT";
		String localTimeHours = " LTX";
		String localTime = " LT";
		String localTimeMinutes = " LTXF";
		String localYearMonth = " LNY ";
		//java时间校验规则
		String format ="";
		//数据库校验规则 oracle
		String oracleFormat ="";
		//数据库校验规则 mysql
		String mysqlFormat ="";
		//数据库校验规则 sqlserver
//		Integer sqlserverFormat=0;
		String sqlserverFormat = "";
		//符合日期格式的字符串
		String newStr="";
		TemporalAccessor datetime;
		if(str.endsWith(localDate)){
			//纯日期
			newStr=str.replace(localDate,"");
			format ="yyyy-MM-dd";
			oracleFormat="yyyy-mm-dd";
			mysqlFormat="%Y-%m-%d";
//			sqlserverFormat=23;
			sqlserverFormat="datename(YYYY, "+sql+") + '-' + datename(MM, "+sql+") + '-' + RIGHT('00'+ Datename(DD,"+sql+"),2)";
		}else if(str.endsWith(localDateTime)){
			//日期（包括时间）
			newStr=str.replace(localDateTime,"");
			format ="yyyy-MM-dd HH:mm:ss";
			oracleFormat="yyyy-mm-dd hh24:mi:ss";
			mysqlFormat="%Y-%m-%d %H:%i:%s";
			sqlserverFormat="120";
		}else if(str.endsWith(localTimeHours)){
			//纯时间
			newStr=str.replace(localTimeHours,"");
			format= "HH";
			oracleFormat= "hh24";
			mysqlFormat="%H";
		} else if (str.endsWith(localTimeMinutes)) {
			newStr = str.replace(localTimeMinutes, "");
			format = "HH:mm";
			oracleFormat = "hh24:mi";
			mysqlFormat="%H:%i";
		} else if (str.endsWith(localTime)) {
			newStr = str.replace(localTime, "");
			format = "HH:mm:ss";
			oracleFormat = "hh24:mi:ss";
			mysqlFormat="%H:%i:%s";
			sqlserverFormat="120";
		} else if (str.endsWith(localYearMonth)){
			newStr = str.replace(localYearMonth, "");
			format = "yyyy-MM";
			oracleFormat = "YY-MM";
			mysqlFormat="%Y-%m";
			sqlserverFormat="datename(YYYY, "+sql+") + '-' + datename(MM, "+sql+")";

		}else {
			return sql;
		}
		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
			TemporalAccessor parse = dateTimeFormatter.parse(newStr);
		} catch (Exception e) {
			e.printStackTrace();
			return sql;
		}
		if ("oracle".equalsIgnoreCase(dataSourceName)) {
			sql = " to_char(" + sql + ",'" + oracleFormat + "') ";
			this.value = newStr;
			return sql;
		} else if ("mysql".equalsIgnoreCase(dataSourceName)) {
			sql = " DATE_FORMAT(" + sql + ",'" + mysqlFormat + "') ";
			this.value = newStr;
			return sql;
		} else if ("sqlserver".equalsIgnoreCase(dataSourceName)) {
//			sql = " CONVERT(" + "varchar(100)," + sql +","+sqlserverFormat+") ";
			sql = sqlserverFormat;
			this.value = newStr;
			return sql;
		}
		return sql;

	}




	/**
	 * 针对in查询方式，根据传入的value的类型做不同的处理。 value 是 string，则分隔字符串，然后合并为符合in规范的字符串。
	 * value 是 list，则直接合并为符合in规范的字符串。
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getInValueSql()
	{
	  String inValueSql="";
		if (value instanceof String)
		{ // 字符串形式，通过逗号分隔
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			StringTokenizer st = new StringTokenizer(value.toString(), ",");
			while (st.hasMoreTokens())
			{
				sb.append("'");
				sb.append(st.nextToken());
				sb.append("'");
				sb.append(",");
			}
			sb = new StringBuilder(sb.substring(0, sb.length() - 1));
			sb.append(")");
			inValueSql= sb.toString();
		} else if (value instanceof List)
		{ // 列表形式
			List<Object> objList = (List<Object>) value;
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for (Object obj : objList)
			{
				sb.append("'");
				sb.append(obj.toString());
				sb.append("'");
				sb.append(",");
			}
			sb = new StringBuilder(sb.substring(0, sb.length() - 1));
			sb.append(")");
			inValueSql= sb.toString();
		} else if (value instanceof String[])
		{ // 列表形式
			String[] objList = (String[]) value;
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for (Object obj : objList)
			{
				sb.append("'");
				sb.append(obj.toString());
				sb.append("'");
				sb.append(",");
			}
			sb = new StringBuilder(sb.substring(0, sb.length() - 1));
			sb.append(")");
			inValueSql= sb.toString();
		}
		return inValueSql;
	}
	
	@SuppressWarnings("unchecked")
	private String getBetweenSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" between ");
		if (this.value instanceof List)
		{
			List<Object> objList = (List<Object>) value;
			for (int i = 0; i < objList.size(); i++)
			{
				Object obj = objList.get(i);
				if (i == 1)
				{
					sb.append(" and ");
				}
				if (obj instanceof LocalDateTime){
					String dateString = QueryUtils.format((LocalDateTime) obj, StringPool.DATE_FORMAT_DATETIME);
						sb.append("'" + dateString + "'");
				} else {
					String dataStr = obj.toString();
					if(dataStr.endsWith("Z")){
						try {
							dataStr = dataStr.replace("Z", " UTC");//UTC是本地时间  
							DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
							LocalDateTime date = LocalDateTime.parse(dataStr, format);  
							dataStr = QueryUtils.format(date, StringPool.DATE_FORMAT_DATETIME);
						} catch (Exception e) {}
					}
//					if(SQLConst.DB_ORACLE.equals(dbType)){
//						sb.append("TO_DATE(substr('" + dataStr + "',1,19),'yyyy-mm-dd hh24:mi:ss')");
//					}else{
						sb.append("'" + dataStr + "'");
//					}
				}
			}
		}
		sb.append(" ");
		return sb.toString();
	}
	
	/**
	 * 当in语句中包含元素大于1000个时的处理
	 * @param sql
	 * @param strList
	 * @return
	 */
	private String getOutLimitInSql(String sql,String[] strList){
		int index = 0;
		int times = 0;
		int i = 0;
		int len = strList.length;
		String field = sql;
		String[] newValue = new String[1000];
		StringBuilder newSql = new StringBuilder();
		newSql.append(" ( ");
		for (String str : strList) {
			newValue[index] = str;
			index++;
			i++;
			if(index%1000==0){
				if(times>0){
					newSql.append(" or ");
				}
				times++;
				newSql.append(" ");
				newSql.append(field);
				newSql.append(" in ") ;
				newSql.append("(");
				newSql.append(QueryUtils.convertArrayToString(newValue));
				newSql.append(")");
				newSql.append(" ");
				int size = len-1000*times;
				newValue = size>=1000? new String[1000]:new String[size];
				index = 0;
			}else{
				if(i==len){
					if(times>0){
						newSql.append(" or ");
					}
					times++;
					newSql.append(" ");
					newSql.append(field);
					newSql.append(" in ") ;
					newSql.append("(");
					newSql.append(QueryUtils.convertArrayToString(newValue));
					newSql.append(")");
					newSql.append(" ");
				}
			}
		}
		newSql.append(" ) ");
		return newSql.toString();
	}

}
