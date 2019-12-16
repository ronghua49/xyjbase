package com.xyjsoft.admin.dao;

import org.apache.ibatis.annotations.Param;

import com.xyjsoft.admin.model.SysSequence;

/**
 * ---------------------------
 * 自增序列 (SysSequenceMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 16:05:08
 * ---------------------------
 */
public interface SysSequenceMapper {

	void setVal(SysSequence sysSequence);

	String nextVal(@Param("name")String name);

	String currVal(@Param("name")String name);

}