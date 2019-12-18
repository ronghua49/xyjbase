package com.xyjsoft.core.base.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.controller.SysRedisController;
import com.xyjsoft.core.query.PageBean;
import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryField;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.query.QueryUtils;
import com.xyjsoft.core.util.StringUtils;
import com.xyjsoft.core.vo.XyjActivitiPuth;

/**
 * 抽象实体业务管理类实现
 * 
 * @company
 * @author gsh456
 * 
 * @date 2018年4月10日
 */
@Transactional
public abstract class AbstractManagerImpl<PK extends Serializable, T extends Serializable> implements Manager<PK, T> {
	private Logger logger = LoggerFactory.getLogger(AbstractManagerImpl.class);
	protected final static String VERSION_ID_NOT_EXIST = "版本id不存在";
	protected final static String CAR_TYPE_ID_NOT_EXIST = "基本车型信息id不存在";
	protected final static String CAN_NOT_COMPUTE = "无法计算";
	protected final static String GET_PRECONDITION_FAIL = "获取前提条件的工程预算信息错误";
	protected final static String EXAMINE_STATUS_ERROR = "审批状态错误";
	protected final static String DATA_ERROR = "数据错误，请联系管理员处理";
	protected final static String DATA_NOT_EMPTY = "信息不能完全为空";
/*	@Autowired
	private SysRedisController sysRedisController;*/

	// 获取基础类
	protected abstract MyBatisDao<PK, T> getDao();

	/**
	 * 获取当前泛型的类型
	 * 
	 * @return 类型
	 */
	@SuppressWarnings("unchecked")
	private Class<? super T> getTypeClass() {
		// 获取第二个泛型(T)对应的class
		Class<? super T> rawType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
		return rawType;
	}

	/**
	 * 检查通用查询对象中的实体类型
	 * 
	 * <pre>
	 * 1.若实体类型为空，使用当前的泛型补充；
	 * 2.若实体类不为空，使用当前泛型类检查，不一致则抛出异常。
	 * </pre>
	 * 
	 * @param queryFilter
	 * @throws SystemException
	 */
	public void handleQueryFilter(QueryFilter queryFilter) throws SystemException {
		if (QueryUtils.isEmpty(queryFilter)) {
			throw new RuntimeException("QueryFilter通用查询对象不能为空.");
		}
		Class<?> clazz = queryFilter.getClazz();
		Class<? super T> typeClass = getTypeClass();
		if (QueryUtils.isEmpty(clazz)) {
			// 所传入的通用查询器未指定 对应实体类时，从当前泛型中获取
			queryFilter.setClazz(typeClass);
		} else {
			if (!clazz.equals(typeClass)) {
				throw new RuntimeException(String.format("QueryFilter中的实体类:%s与Dao泛型中的实体类:%s不一致.", clazz, typeClass));
			}
		}
	}

	public PageList<T> query(QueryFilter queryFilter) throws Exception {
		// 获取子类泛型
		handleQueryFilter(queryFilter);

		PageBean pageBean = queryFilter.getPageBean();
		if (QueryUtils.isEmpty(pageBean)) {
			PageHelper.startPage(1, Integer.MAX_VALUE, false);
		} else {
			PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		}
		Map<String, Object> params = queryFilter.getParams();
		MyBatisDao<PK, T> dao = getDao();
		List<T> query = dao.query(params);
		String string = params.get("summaryBySql").toString();
		params.put("summaryBySql", string.toUpperCase());
		Map<String, Object> map = dao.summary(params);
		return new PageList<T>(query, map);
	}

	public T getUnique(String column, Object value) throws Exception {
		QueryFilter queryFilter = QueryFilter.build().withDefaultPage().withQuery(new QueryField(column, value));
		PageList<T> query = this.query(queryFilter);
		long total = query.getTotal();
		if (total <= 0) {
			return null;
		} else if (total == 1) {
			return query.getRows().get(0);
		} else {
			throw new RuntimeException(String.format("符合查询条件的记录为:%s条，超过1条.", total));
		}
	}

	/**
	 * 将为null的字符串转为""，其他情况直接return
	 * 
	 * @param str
	 *            字符串
	 * @return java.lang.String 非null制度穿
	 * @author gsh456
	 * @since 2019/5/11 15:49
	 */
	public String getNull2Blank(String str) {
		return Optional.ofNullable(str).orElse("");
	}

	/**
	 * 获取当前时间的通用方法--方便后期改造
	 * 
	 * @return 当前日期时间点
	 * @author gsh456
	 * @since 2019/5/13 15:49
	 */
	public Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取一个实体类的double属性的和
	 * 
	 * @param t
	 *            实体类
	 * @return double 和
	 * @since 1.0.0
	 * @author gsh456
	 * @date 2019/5/16 13:43
	 */
	public double getDoubleSum(Object t)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		double back = 0.0;
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			Double invokes;
			if ("Double".equals(field.getType().getSimpleName())) {
				Method m = t.getClass().getMethod("get" + replaceFirst(field.getName()));
				invokes = (Double) m.invoke(t);
			} else {
				continue;
			}
			if (invokes != null) {
				back = add(back, invokes);
			}
		}
		return back;
	}

	/**
	 * 获取一个实体类的double属性的和
	 * 
	 * @param t
	 *            实体类
	 * @param t
	 *            字段的指定开头
	 * @return double 和
	 * @since 1.0.0
	 * @author gsh456
	 * @date 2019/5/16 13:43
	 */
	public double getDoubleSum(Object t, String startWith)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		double back = 0.0;
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			Double invokes = null;
			if ("Double".equals(field.getType().getSimpleName())) {
				String name = field.getName();
				if (StringUtils.isBlank(startWith)) {
					return 0.0;
				} else if (name.toUpperCase().startsWith(startWith.toUpperCase())) {
					Method m = t.getClass().getMethod("get" + replaceFirst(name));
					invokes = (Double) m.invoke(t);
				}
			} else {
				continue;
			}
			if (invokes != null) {
				back = add(back, invokes);
			}
		}
		return back;
	}

	/**
	 * 获取一个实体类的double属性的和
	 * 
	 * @param t
	 *            实体类
	 * @param exclude
	 *            要排除的字段
	 * @param ignoreUpper
	 *            字段是否区分大小写
	 * @return double 和
	 * @since 1.0.0
	 * @author gsh456
	 * @date 2019/5/16 13:43
	 */
	public double getDoubleSum(Object t, List<String> exclude, boolean ignoreUpper) throws Exception {
		double back = 0.0;
		if (ignoreUpper) {
			exclude = exclude.stream().map(String::toUpperCase).collect(Collectors.toList());
		}
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			Double invokes;
			String prop = field.getName();
			if (ignoreUpper) {
				prop = prop.toUpperCase();
			}
			// 如果含有属性名称被排除则不进行计算
			if (exclude.contains(prop)) {
				continue;
			}
			if ("Double".equals(field.getType().getSimpleName())) {
				Method m = t.getClass().getMethod("get" + replaceFirst(field.getName()));
				invokes = (Double) m.invoke(t);
			} else {
				continue;
			}
			if (invokes != null) {
				back = add(back, invokes);
			}
		}
		return back;
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	private double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 大写字符串第一个字符。
	 *
	 * @param str
	 *            制度穿
	 * @return 处理后的字符串
	 */
	private String replaceFirst(String str) {
		return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
	}

	/**
	 * 判断一个实体类除了第一个字段（一般是id）和最后一个字段（版本id），其他字段是否为空。
	 *
	 * @param object
	 *            实体类
	 * @return 处理后的字符串
	 */
	public boolean checkAllProIsNullWithOutId(Object object) {
		if (null == object) {
			return true;
		}
		Field[] declaredFields = object.getClass().getDeclaredFields();
		for (int i = 1; i < declaredFields.length - 1; i++) {
			Field f = declaredFields[i];
			try {
				f.setAccessible(true);
				if (f.get(object) != null && !StringUtils.isBlank(f.get(object).toString())) {
					return false;
				}
			} catch (Exception e) {
				logger.error("[AbstractManagerImpl:checkAllProIsNullWithOutId]", e);
			}
		}
		return true;
	}

	public boolean disposeApply(XyjActivitiPuth xyjActivitiPuth) throws Exception {
		T findById = null;
		Class<? extends Serializable> class1 = null;
		Field[] fields = null;
		boolean bool = false;
		boolean disposeApplyToManage = false;
		switch (xyjActivitiPuth.getActionname()) {
		case processStart:
			//流程发起
			findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
			if(findById == null) {
				throw new RuntimeException("未查询到业务数据");
			}
			class1= findById.getClass();
			fields=class1.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
			    if(fields[i].getName().equals("applyId")){
			        bool=true;
			        break;
			    }
			}
			if(bool){
				//有审批ID及状态
				Method method = class1.getMethod("setApplyId", Long.class);
				Method method2 = class1.getMethod("setApplyState", String.class);
				method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
				method2.invoke(findById, "0");
			}
			getDao().update(findById);
			// 调用业务类
			disposeApplyToManage = processStart(xyjActivitiPuth);
			if (disposeApplyToManage) {
				return true;
			} else {
				return false;
			}		
		case agree:
			// 通过
			switch (xyjActivitiPuth.getEventtype()) {
			case endEvent:
				// 审批流完成,修改数据状态
				findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
				if(findById == null) {
					throw new RuntimeException("未查询到业务数据");
				}
				class1= findById.getClass();
				fields=class1.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
				    if(fields[i].getName().equals("applyId")){
				        bool=true;
				        break;
				    }
				}
				if(bool){
					//有审批ID及状态
					Method method = class1.getMethod("setApplyId", Long.class);
					Method method2 = class1.getMethod("setApplyState", String.class);
					method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
					method2.invoke(findById, "3");
				}
				getDao().update(findById);
				// 调用业务类
				disposeApplyToManage = agreeAndEndEvent(xyjActivitiPuth);
				if (disposeApplyToManage) {
					return true;
				} else {
					return false;
				}
			case taskComplete:
				// 任务处理
				findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
				if(findById == null) {
					throw new RuntimeException("未查询到业务数据");
				}
				class1= findById.getClass();
				fields=class1.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
				    if(fields[i].getName().equals("applyId")){
				        bool=true;
				        break;
				    }
				}
				if(bool){
					//有审批ID及状态
					Method method = class1.getMethod("setApplyId", Long.class);
					Method method2 = class1.getMethod("setApplyState", String.class);
					method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
					method2.invoke(findById, "1");
				}
				getDao().update(findById);
				// 调用业务类
				disposeApplyToManage = agreeAndTaskComplete(xyjActivitiPuth);
				if (disposeApplyToManage) {
					return true;
				} else {
					return false;
				}
			}
			break;
		case selectAgree:
			findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
			if(findById == null) {
				throw new RuntimeException("未查询到业务数据");
			}
			class1= findById.getClass();
			fields=class1.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
			    if(fields[i].getName().equals("applyId")){
			        bool=true;
			        break;
			    }
			}
			if(bool){
				//有审批ID及状态
				Method method = class1.getMethod("setApplyId", Long.class);
				Method method2 = class1.getMethod("setApplyState", String.class);
				method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
				method2.invoke(findById, "9");
			}
			getDao().update(findById);
			// 调用业务类
			disposeApplyToManage = selectAgree(xyjActivitiPuth);
			if (disposeApplyToManage) {
				return true;
			} else {
				return false;
			}
		case transferSignature:
			findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
			if(findById == null) {
				throw new RuntimeException("未查询到业务数据");
			}
			class1= findById.getClass();
			fields=class1.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
			    if(fields[i].getName().equals("applyId")){
			        bool=true;
			        break;
			    }
			}
			if(bool){
				//有审批ID及状态
				Method method = class1.getMethod("setApplyId", Long.class);
				Method method2 = class1.getMethod("setApplyState", String.class);
				method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
				method2.invoke(findById, "8");
			}
			getDao().update(findById);
			// 调用业务类
			disposeApplyToManage = transferSignature(xyjActivitiPuth);
			if (disposeApplyToManage) {
				return true;
			} else {
				return false;
			}
		case reject:
			findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
			if(findById == null) {
				throw new RuntimeException("未查询到业务数据");
			}
			class1= findById.getClass();
			fields=class1.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
			    if(fields[i].getName().equals("applyId")){
			        bool=true;
			        break;
			    }
			}
			if(bool){
				//有审批ID及状态
				Method method = class1.getMethod("setApplyId", Long.class);
				Method method2 = class1.getMethod("setApplyState", String.class);
				method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
				method2.invoke(findById, "7");
			}
			getDao().update(findById);
			// 调用业务类
			disposeApplyToManage = reject(xyjActivitiPuth);
			if (disposeApplyToManage) {
				return true;
			} else {
				return false;
			}
		case rejectToStart:
			findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
			if(findById == null) {
				throw new RuntimeException("未查询到业务数据");
			}
			class1= findById.getClass();
			fields=class1.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
			    if(fields[i].getName().equals("applyId")){
			        bool=true;
			        break;
			    }
			}
			if(bool){
				//有审批ID及状态
				Method method = class1.getMethod("setApplyId", Long.class);
				Method method2 = class1.getMethod("setApplyState", String.class);
				method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
				method2.invoke(findById, "6");
			}
			getDao().update(findById);
			// 调用业务类
			disposeApplyToManage = rejectToStart(xyjActivitiPuth);
			if (disposeApplyToManage) {
				return true;
			} else {
				return false;
			}
		case rejectToAssign:
			findById = getDao().findById(Long.parseLong(xyjActivitiPuth.getBusinesskey()));
			if(findById == null) {
				throw new RuntimeException("未查询到业务数据");
			}
			class1= findById.getClass();
			fields=class1.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
			    if(fields[i].getName().equals("applyId")){
			        bool=true;
			        break;
			    }
			}
			if(bool){
				//有审批ID及状态
				Method method = class1.getMethod("setApplyId", Long.class);
				Method method2 = class1.getMethod("setApplyState", String.class);
				method.invoke(findById, Long.parseLong(xyjActivitiPuth.getInstid()));
				method2.invoke(findById, "2");
			}
			getDao().update(findById);
			// 调用业务类
			disposeApplyToManage = rejectToAssign(xyjActivitiPuth);
			if (disposeApplyToManage) {
				return true;
			} else {
				return false;
			}
		}
		return disposeApplyToManage;
	}

	/**
	 * @describe 流程发起,可以在业务处理子类中覆盖此方法实现业务操作
	 * @author sjg 
	 * @date 2019年11月6日 下午2:42:02
	 * @version 1.0
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean processStart(XyjActivitiPuth xyjActivitiPuth){
		return true;
	}
	
	/**
	 * 审批通过并且流程结束
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean agreeAndEndEvent(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	/**
	 * 审批通过并且流程未结束
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean agreeAndTaskComplete(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	/**
	 * 选签
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean selectAgree(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	/**
	 * 转办
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean transferSignature(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	/**
	 * 驳回上一级
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean reject(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	/**
	 * 驳回至开始节点
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean rejectToStart(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	/**
	 * 驳回至指定节点
	 * @param xyjActivitiPuth
	 * @return
	 */
	public boolean rejectToAssign(XyjActivitiPuth xyjActivitiPuth) {
		return true;
	}
	
	@Override
	public T saveBean(T record) {
		return null;
	}
}
