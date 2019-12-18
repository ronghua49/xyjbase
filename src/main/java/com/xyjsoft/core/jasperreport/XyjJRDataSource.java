package com.xyjsoft.core.jasperreport;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
/**
 * 填充JaserperReort数据
 * 
 */
public class XyjJRDataSource implements JRDataSource {
	private int index = -1;
	private List<Map> list=null;
	public XyjJRDataSource(List<Map> list)
	{
		this.list=list;
	}
	/**Gets the field value for the current position.
	 * @return
	 * an object containing the field value. The object type must be the field object type.
	 * */
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		
		Map map = list.get(index);
		value=map.get(fieldName);
		if(field.getValueClass().getName().equals("java.math.BigDecimal")||field.getValueClass().getName().equals("java.lang.Double")||field.getValueClass().getName().equals("java.lang.Float"))
		{
			return value!=null?value:0;
		}else if(field.getValueClass().getName().equals("java.sql.Timestamp"))
		{
			return value!=null?value:"";
		}else
		{
			return value!=null?value:"";
		}
	}
	/**Tries to position the cursor on the next element in the data source.
	 * @return
	 * true if there is a next record, false otherwise*/
	public boolean next() throws JRException {
			index++;
			return index < list.size();
	}

}
