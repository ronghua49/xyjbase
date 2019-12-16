package com.xyjsoft.admin.dao;

import java.util.List;
import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.admin.model.BillsRules;
import org.springframework.stereotype.Repository;

/**
 * ---------------------------
 *  (BillsRulesMapper)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-04 16:17:23
 * ---------------------------
 */
@Repository
public interface BillsRulesMapper  extends MyBatisDao<String, BillsRules>{

	/**
	 * 添加
	 * @param record
	 * @return
	 */
    int add(BillsRules record);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);
    
    /**
     * 修改
     * @param record
     * @return
     */
    int update(BillsRules record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */    
    BillsRules findById(Long id);

    /**
     * 基础分页查询
     * @param record
     * @return
     */    
    List<BillsRules> findPage();

    List<BillsRules> findByType(String type);
}