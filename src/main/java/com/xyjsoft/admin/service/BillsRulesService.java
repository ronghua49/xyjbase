package com.xyjsoft.admin.service;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.admin.model.BillsRules;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 *  (BillsRulesService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-09-04 16:17:23
 * ---------------------------
 */
public interface BillsRulesService extends CurdService<BillsRules>,Manager<String,BillsRules>  {

    String createRuleNo(Long id);
    String createRuleNo(String type);
    String createRuleNo(BillsRules rule);
}
