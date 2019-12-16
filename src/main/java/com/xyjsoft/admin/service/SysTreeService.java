package com.xyjsoft.admin.service;
import java.util.List;
import java.util.Map;

import com.xyjsoft.admin.model.SysTree;
import com.xyjsoft.core.base.Manager;
import com.xyjsoft.core.service.CurdService;

/**
 * ---------------------------
 * 分类管理表 (SysTreeService)         
 * ---------------------------
 * 作者：  xyjsoft
 * 时间：  2019-08-26 11:08:47
 * ---------------------------
 */
public interface SysTreeService extends CurdService<SysTree>,Manager<String, SysTree>  {
    /**
     * @Author wangzhengwen
     * @Description 查询全部数据
     * @Date 12:01 2019/8/26 0026
     * @Param
     * @return java.lang.String
     **/
    List<Object> fiandAll();
    /*
     * @Author wangzhengwen
     * @Description 向下查询
     * @Date 17:47 2019/8/29 0029
     * @Param [id]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String,Object> fiandDownById(Long id);
    /**
     * @Author wangzhengwen
     * @Description 向上查询
     * @Date 17:47 2019/8/29 0029
     * @Param [id]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String,Object> fiandUpById(Long id);

    Map<String,Object> fiandByCode(String code);
}
