package com.xyjsoft.core.query;

import com.xyjsoft.core.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.*;

@ApiModel(description = "通用查询器")
public class QueryFilter {
    /**
     * 条件SQL的KEY
     */
    public static final String WHERE_SQL_TAG = "whereSql";
    /**
     * 排序SQL的KEY
     */
    public static final String ORDER_SQL_TAG = "orderBySql";
    /**
     * 统计查询的KEY
     */
    public static final String SUMMARY_BY_SQL = "summaryBySql";
    
    @ApiModelProperty(name = "pageBean", notes = "分页信息")
    private PageBean pageBean;
    @ApiModelProperty(name = "sorter", notes = "排序字段")
    private List<FieldSort> sorter = new ArrayList<FieldSort>();
    @ApiModelProperty(name = "params", notes = "查询参数")
    private Map<String, Object> params = new LinkedHashMap<String, Object>();
    @ApiModelProperty(name = "querys", notes = "查询条件组")
    private List<QueryField> querys = new ArrayList<QueryField>();
    @ApiModelProperty(name = "selfDate", notes = "自定义数据")
    private Map<String, String> selfDate = new HashMap<>();
    @ApiModelProperty(name = "showTotal", notes = "字段运算组")
    private List<QueryTotal> showTotal = new ArrayList<QueryTotal>();
    
    
    private Class<?> clazz;

    public PageBean getPageBean() {
        return pageBean;
    }

    private QueryFilter() {
    }

    private QueryFilter(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static QueryFilter build() {
        return new QueryFilter();
    }


    public Map<String, String> getSelfDate() {
        return selfDate;
    }

    public void setSelfDate(Map<String, String> selfDate) {
        this.selfDate = selfDate;
    }

    public List<QueryTotal> getShowTotal() {
        return showTotal;
    }

    public void setShowTotal(List<QueryTotal> showTotal) {
        this.showTotal = showTotal;
    }

    /**
     * 构造器
     *
     * @param clazz 当前通用查询所对应实体类
     * @return
     */
    public static QueryFilter build(Class<?> clazz) {
        return new QueryFilter(clazz);
    }

    public QueryFilter withDefaultPage() {
        this.pageBean = new PageBean();
        return this;
    }

    public QueryFilter withPage(PageBean pageBean) {
        this.pageBean = pageBean;
        return this;
    }

    public QueryFilter withSorter(FieldSort fieldSort) {
        this.sorter.add(fieldSort);
        return this;
    }

    public QueryFilter withQuery(QueryField queryField) {
        this.querys.add(queryField);
        return this;
    }

    public QueryFilter withParam(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() throws Exception {
        // 生成SQL语句到params中
        generatorSQL();
        // 初始化查询元素中的查询参数到params中
        initParams(querys);
        return params;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public QueryFilter addFilter(String property, Object value, QueryOP operation, FieldRelation relation) {
        QueryField queryField = new QueryField(property, value, operation, relation);
        querys.add(queryField);
        return this;
    }

    public QueryFilter addFilter(String property, Object value, QueryOP operation, FieldRelation relation, String group) {
        QueryField queryField = new QueryField(property, value, operation, relation, group);
        querys.add(queryField);
        return this;
    }

    public QueryFilter addFilter(String property, Object value, QueryOP operation) {
        QueryField queryField = new QueryField(property, value, operation, FieldRelation.AND);
        querys.add(queryField);
        return this;
    }

    public void addParams(String property, Object value) {
        params.put(property, value);
    }

    public List<FieldSort> getSorter() {
        return sorter;
    }

    public void setSorter(List<FieldSort> sorter) {
        this.sorter = sorter;
    }

    public List<QueryField> getQuerys() {
        return querys;
    }

    public void setQuerys(List<QueryField> querys) {
        this.querys = querys;
    }

    /**
     * 获取当前通用查询所对应的实体类
     *
     * @return 实体类
     */
    public Class<?> getClazz() {
        return this.clazz;
    }

    /**
     * 设置当前通用查询缩对应的实体类
     *
     * @param clazz 实体类
     */
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    private void generatorSQL() throws Exception {
        // 生成查询的SQL语句
        String querySQL = generatorQuerySQL();
        if (QueryUtils.isNotEmpty(querySQL)) {
            params.put(WHERE_SQL_TAG, querySQL);
        }
        int orderSize = sorter.size();
        if (orderSize > 0) {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            for (FieldSort fieldSort : sorter) {
                if (i++ > 0) {
                    sb.append(",");
                }
                sb.append(fieldSort.toSql(clazz));
            }
            params.put(ORDER_SQL_TAG, sb.toString());
        }
        if(pageBean == null) {
			String summaryBySql = getSummaryBySql(new ArrayList<QuerySummary>());
        	params.put(SUMMARY_BY_SQL, summaryBySql);
        }else{
        	String summaryBySql = getSummaryBySql(pageBean.getSummary());
        	params.put(SUMMARY_BY_SQL, summaryBySql);
        }
    }
    
    
    private String getSummaryBySql(List<QuerySummary> summary) {
    	StringBuffer sb = new StringBuffer(" ");
    	if(summary == null || summary.size() == 0) {
    		//没有需要统计的数据,返回一个count
    		return " count(*) as allCount ";
    	};
    	String datasourceName = AppUtil.getDataSourceName();
    	//mysql求和SQL
    	if("mysql".equalsIgnoreCase(datasourceName)) {
    		for (QuerySummary querySummary : summary) {
        		if("text".equals(querySummary.getType())) {
        			continue;
        		}else if("sum".equalsIgnoreCase(querySummary.getType())) {
        			sb.append("SUM(IFNULL( "+StringUtils.camel2Under(querySummary.getFieldName())+",0)) as "+querySummary.getFieldName()+",");
        		}else if("avg".equalsIgnoreCase(querySummary.getType())) {
        			sb.append("avg(IFNULL("+StringUtils.camel2Under(querySummary.getFieldName())+",0)) as "+querySummary.getFieldName()+",");
        		}else if("count".equalsIgnoreCase(querySummary.getType())) {
        			sb.append("count("+StringUtils.camel2Under(querySummary.getFieldName())+") as "+querySummary.getFieldName()+",");
        		}
    		}		
    	}
    	//oracle求和SQL
    	if("oracle".equalsIgnoreCase(datasourceName)) {
    		for (QuerySummary querySummary : summary) {
    		      if("text".equals(querySummary.getType())) {
    		         continue;
    		      }else if("sum".equals(querySummary.getType())) {
    		         sb.append("nvl(sum("+StringUtils.camel2Under(querySummary.getFieldName())+"),0) as "+querySummary.getFieldName()+",");
    		      }else if("avg".equals(querySummary.getType())) {
    		         sb.append("nvl(avg("+StringUtils.camel2Under(querySummary.getFieldName())+"),0) as "+querySummary.getFieldName()+",");
    		      }else if("count".equals(querySummary.getType())) {
    		         sb.append("nvl(count("+StringUtils.camel2Under(querySummary.getFieldName())+"),0) as "+querySummary.getFieldName()+",");
    		      }
    		}
    	}
    	//sqlserver求和
		if ("SQLServer".equalsIgnoreCase(datasourceName)) {
			for (QuerySummary querySummary : summary) {
        		if("text".equals(querySummary.getType())) {
        			continue;
        		}else if("sum".equalsIgnoreCase(querySummary.getType())) {
        			sb.append("SUM(ISNULL( "+StringUtils.camel2Under(querySummary.getFieldName())+",0)) as "+querySummary.getFieldName()+",");
        		}else if("avg".equalsIgnoreCase(querySummary.getType())) {
        			sb.append("avg(ISNULL("+StringUtils.camel2Under(querySummary.getFieldName())+",0)) as "+querySummary.getFieldName()+",");
        		}else if("count".equalsIgnoreCase(querySummary.getType())) {
        			sb.append("count("+StringUtils.camel2Under(querySummary.getFieldName())+") as "+querySummary.getFieldName()+",");
        		}
    		}		
		}
    	String string = sb.toString();
    	if(string.length() == 0) {
    		return " count(*) as allCount ";
    	}else{
    		String substring = string.substring(0, string.length() -1 );
    		return substring;
    	}
	}

	private String generatorQuerySQL() throws Exception {
        int size = querys.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return querys.get(0).toSql(clazz,0);
        }
        String propertyTmp ="";
        int num = 0;
        if (size > 1) {
            Map<String, List<QueryField>> map = new HashMap<String, List<QueryField>>();
            // 按照分组归类查询条件
            for (QueryField element : querys) {
                String group = element.getGroup();
                List<QueryField> list = map.get(group);
                if (QueryUtils.isEmpty(list)) {
                    list = new ArrayList<QueryField>();
                    map.put(group, list);
                }
                list.add(element);
            }
            List<StringBuffer> sbList = new ArrayList<StringBuffer>();

            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                StringBuffer sqlBuf = new StringBuffer();
                String group = it.next();
                List<QueryField> list = map.get(group);
                QueryField firstField = list.get(0);
                String relation = firstField.getRelation().value();
                int fieldList = list.size();
                for (int i = 0; i < fieldList; i++) {
                    if (i > 0) {
                        sqlBuf.append(" " + relation + " ");
                    }
                    String property=list.get(i).getProperty();
                    if(property.equals(propertyTmp)){
                        num++;
                    }else{
                        //需要对条件进行排序
                        num=0;
                        propertyTmp=property;
                    }
                    sqlBuf.append(list.get(i).toSql(clazz,num));
                }
                if (fieldList > 1) {
                    sqlBuf.insert(0, " (");
                    sqlBuf.append(") ");
                }
                sbList.add(sqlBuf);
            }

            StringBuffer result = new StringBuffer();
            for (int i = 0; i < sbList.size(); i++) {
                if (i > 0) {
                    result.append(" " + FieldRelation.AND.value() + " ");
                }
                result.append(sbList.get(i).toString());
            }
            return result.toString();
        }
        return "";
    }

    //初始化参数
    private void initParams(List<QueryField> querys) {
        if (QueryUtils.isEmpty(querys)){
            return;
        }
        for (QueryField element : querys) {
            QueryField queryField = (QueryField) element;
            QueryOP operation = queryField.getOperation();
            if (QueryOP.IS_NULL.equals(operation) || QueryOP.NOTNULL.equals(operation)
                    || QueryOP.IN.equals(operation)) {
                continue;
            }
            String property = queryField.getProperty();
            if (property.indexOf(".") > -1) {
                property = property.substring(property.indexOf(".") + 1);
            }
            Object value = queryField.getValue();
            this.params.put(property, value);
        }
    }
   
    
}
