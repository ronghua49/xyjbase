package com.xyjsoft.admin.service;

import com.xyjsoft.admin.model.SysSequence;

/**
 * ---------------------------
 * 自增序列 (SysSequenceService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-11-19 16:05:08
 * ---------------------------
 */
public interface SysSequenceService {

	String setVal(SysSequence sysSequence);

	String nextVal(String name);

	String currVal(String name);

}
