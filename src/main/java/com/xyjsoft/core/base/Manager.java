package com.xyjsoft.core.base;

import org.omg.CORBA.SystemException;

import com.xyjsoft.core.query.PageList;
import com.xyjsoft.core.query.QueryFilter;
import com.xyjsoft.core.vo.XyjActivitiPuth;

import java.io.Serializable;

public abstract interface Manager<PK extends Serializable, T> {

	public abstract PageList<T> query(QueryFilter paramQueryFilter) throws SystemException, Exception;

	public abstract T getUnique(String paramString, Object paramObject) throws SystemException, Exception;

	public abstract boolean disposeApply(XyjActivitiPuth xyjActivitiPuth) throws Exception;

	/**
	 * @describe 保存实体类并返回实体类
	 * @author sjg
	 * @date 2019年11月1日 下午3:09:36
	 * @version 1.0
	 * @param bean
	 * @return
	 */
	public abstract T saveBean(T bean);

}