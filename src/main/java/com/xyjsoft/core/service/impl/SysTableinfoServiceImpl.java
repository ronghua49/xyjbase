package com.xyjsoft.core.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyjsoft.core.base.MyBatisDao;
import com.xyjsoft.core.base.impl.AbstractManagerImpl;
import com.xyjsoft.core.controller.EureKaUtiles;
import com.xyjsoft.core.dao.SysTableinfoMapper;
import com.xyjsoft.core.http.HttpResult;
import com.xyjsoft.core.model.SysTableinfo;
import com.xyjsoft.core.model.Tableinfo;
import com.xyjsoft.core.page.MybatisPageHelper;
import com.xyjsoft.core.page.PageRequest;
import com.xyjsoft.core.page.PageResult;
import com.xyjsoft.core.service.SysTableinfoService;
import com.xyjsoft.core.util.StringUtils;
import com.xyjsoft.core.vo.classInfo;
import com.xyjsoft.core.vo.entityInfo;

/**
 * ---------------------------
 * 前端表格初始化数据 (SysTableinfoServiceImpl)
 * ---------------------------
 * 作者：  宋建国
 *
 * @date 2019-04-22 15:16:58
 * 说明：
 * ---------------------------
 */
@Service
public class SysTableinfoServiceImpl extends AbstractManagerImpl<String, SysTableinfo> implements SysTableinfoService {

    @Autowired
    private SysTableinfoMapper sysTableinfoMapper;

    @Autowired
    private EureKaUtiles eureKaUtiles;

    @Override
    public int save(SysTableinfo record) {
/*		Optional.of(record.getTablename()).ifPresent(str -> record.setRealTableName(StringUtils.camel2Under(str).toUpperCase()));
		Optional.of(record.getProp()).ifPresent(str -> record.setRealProp(StringUtils.camel2Under(str).toUpperCase()));*/
        if (record.getId() == null || record.getId() == 0) {
            return sysTableinfoMapper.add(record);
        }
        return sysTableinfoMapper.update(record);
    }

    @Override
    public SysTableinfo findById(Long id) {
        return sysTableinfoMapper.findById(id);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return MybatisPageHelper.findPage(pageRequest, sysTableinfoMapper);
    }

    @Override
    protected MyBatisDao<String, SysTableinfo> getDao() {
        return sysTableinfoMapper;
    }

    //递归处理表格初始化数据
    private void gainTableinfo(List<Tableinfo> parentTableinfo, List<SysTableinfo> allSysTableInfo) {
        SysTableinfo allTableColumn = null;
        for (int i = 0; i < parentTableinfo.size(); i++) {
            List<Tableinfo> childList = new ArrayList<Tableinfo>();
            for (int j = 0; j < allSysTableInfo.size(); j++) {
                if (parentTableinfo.get(i).getId().longValue() == allSysTableInfo.get(j).getUplevel().longValue()) {
                    allTableColumn = allSysTableInfo.get(j);
                    Tableinfo tableColumn = new Tableinfo();
                    tableColumn.setId(allTableColumn.getId());
                    tableColumn.setTablename(allTableColumn.getTablename());
                    tableColumn.setLabel(allTableColumn.getLabel());
                    tableColumn.setHeaderAlign(allTableColumn.getHeaderalign());
                    tableColumn.setProp(allTableColumn.getProp());
                    tableColumn.setWidth(allTableColumn.getWidth());
                    tableColumn.setFixed(allTableColumn.getFixed());
                    tableColumn.setResizable(allTableColumn.getResizable());
                    tableColumn.setShowOerflowTooltip(allTableColumn.getShowoerflowtooltip());
                    tableColumn.setAlign(allTableColumn.getAlign());
                    tableColumn.setSumtype(allTableColumn.getSumtype());
                    tableColumn.setFieldtype(allTableColumn.getFieldtype());
                    tableColumn.setRule(allTableColumn.getRule());
                    tableColumn.setQuick(allTableColumn.getQuick());
                    tableColumn.setEdit(allTableColumn.getEdit());
                    tableColumn.setIsshow(allTableColumn.getIsshow());
                    tableColumn.setArrange(allTableColumn.getArrange());
                    tableColumn.setDicttype(allTableColumn.getDicttype());
                    tableColumn.setEditshow(allTableColumn.getEditshow());
                    tableColumn.setDatatype(allTableColumn.getDatatype());
                    tableColumn.setDatalength(allTableColumn.getDatalength());
                    tableColumn.setNullable(allTableColumn.getNullable());
                    tableColumn.setHeadermodify(allTableColumn.getHeadermodify());
                    tableColumn.setColumncolor(allTableColumn.getColumncolor());
                    tableColumn.setIsAllowSearch(allTableColumn.getIsAllowSearch());
                    tableColumn.setIsCombine(allTableColumn.getIsCombine());
                    tableColumn.setTipsText(allTableColumn.getTipsText());
                    tableColumn.setFormatType(allTableColumn.getFormatType());
                    tableColumn.setMicroServiceName(allTableColumn.getMicroServiceName());
                    tableColumn.setRelationGroups(allTableColumn.getRelationGroups());
                    childList.add(tableColumn);
                }
            }
            if (childList.size() > 0) {
                parentTableinfo.get(i).setChildren(childList);
                gainTableinfo(childList, allSysTableInfo);
            }
        }
    }

    @Override
    public List<Tableinfo> findByTableName(String tablename) {
        List<SysTableinfo> allListTables = sysTableinfoMapper.findByTableName(tablename);
        List<Tableinfo> listTableinfo = new ArrayList<Tableinfo>();
        SysTableinfo allTableColumn = null;
        for (int i = 0; i < allListTables.size(); i++) {
            if (allListTables.get(i).getUplevel() == 0L) {
                Tableinfo tableColumn = new Tableinfo();
                allTableColumn = allListTables.get(i);
                tableColumn.setId(allTableColumn.getId());
                tableColumn.setTablename(allTableColumn.getTablename());
                tableColumn.setLabel(allTableColumn.getLabel());
                tableColumn.setHeaderAlign(allTableColumn.getHeaderalign());
                tableColumn.setProp(allTableColumn.getProp());
                tableColumn.setWidth(allTableColumn.getWidth());
                tableColumn.setFixed(allTableColumn.getFixed());
                tableColumn.setResizable(allTableColumn.getResizable());
                tableColumn.setShowOerflowTooltip(allTableColumn.getShowoerflowtooltip());
                tableColumn.setAlign(allTableColumn.getAlign());
                tableColumn.setSumtype(allTableColumn.getSumtype());
                tableColumn.setFieldtype(allTableColumn.getFieldtype());
                tableColumn.setRule(allTableColumn.getRule());
                tableColumn.setQuick(allTableColumn.getQuick());
                tableColumn.setEdit(allTableColumn.getEdit());
                tableColumn.setIsshow(allTableColumn.getIsshow());
                tableColumn.setArrange(allTableColumn.getArrange());
                tableColumn.setDicttype(allTableColumn.getDicttype());
                tableColumn.setEditshow(allTableColumn.getEditshow());
                tableColumn.setDatatype(allTableColumn.getDatatype());
                tableColumn.setDatalength(allTableColumn.getDatalength());
                tableColumn.setNullable(allTableColumn.getNullable());
                tableColumn.setHeadermodify(allTableColumn.getHeadermodify());
                tableColumn.setColumncolor(allTableColumn.getColumncolor());
                tableColumn.setIsAllowSearch(allTableColumn.getIsAllowSearch());
                tableColumn.setIsCombine(allTableColumn.getIsCombine());
                tableColumn.setTipsText(allTableColumn.getTipsText());
                tableColumn.setFormatType(allTableColumn.getFormatType());
                tableColumn.setMicroServiceName(allTableColumn.getMicroServiceName());
                tableColumn.setRelationGroups(allTableColumn.getRelationGroups());
                listTableinfo.add(tableColumn);
            }
        }
        if (listTableinfo.size() > 0) {
            gainTableinfo(listTableinfo, allListTables);
        }
        return listTableinfo;
    }

    @Override
    public HttpResult saveEntityClass(String servceId, String classFullName) {
        // TODO Auto-generated method stub
        //判断是否存在
        String[] str = classFullName.split("\\.");
        if (str.length <= 0) {
            throw new RuntimeException(classFullName + "，拆分实体类名称错误");
        }
        //通过feign获取远端实体类属性信息
        List<classInfo> cInfos = eureKaUtiles.getClassAttribute(servceId, classFullName);
        //默认插入多选框数据
        if (cInfos.size() > 0) {
            List<SysTableinfo> sysTableInfos = sysTableinfoMapper.findFieldNameExist(cInfos.get(0).getClassName(), "select");
            if (sysTableInfos.size() <= 0) {
                SysTableinfo selectrecord = new SysTableinfo();
                selectrecord.setTablename(cInfos.get(0).getClassName());
                selectrecord.setProp("select");
                selectrecord.setLabel("选择");
                selectrecord.setHeaderalign("center");
                selectrecord.setUplevel(0L);
                selectrecord.setWidth("50px");
                selectrecord.setFixed(1);
                selectrecord.setResizable(0);
                selectrecord.setShowoerflowtooltip(0);
                selectrecord.setAlign("center");
                selectrecord.setFieldtype("selection");
                selectrecord.setQuick(0);
                selectrecord.setEdit(0);
                selectrecord.setIsshow(0);
                selectrecord.setArrange(49L);
                selectrecord.setEditshow(1);
                selectrecord.setIsAllowSearch(0);
                selectrecord.setMicroServiceName(servceId);
                selectrecord.setRelationGroups(JSON.toJSONString(new String[]{"EQ","LE","GE"}));
                sysTableinfoMapper.add(selectrecord);
            }
        }
        int i = 0;
        String tableName;
        String fieldName;
        //第一个字段的排序为50，后面的字段依次+10，方便后期调整排序
        long arrangeDepart = 50;
        //循环插入实体类属性信息
        for (i = 0; i < cInfos.size(); i++) {
            classInfo ci = cInfos.get(i);
            List<SysTableinfo> sysTableInfos = sysTableinfoMapper.findFieldNameExist(ci.getClassName(), ci.getFieldName());
            if (sysTableInfos.size() <= 0) {
                SysTableinfo record = new SysTableinfo();
                record.setTablename(ci.getClassName());
                record.setProp(ci.getFieldName());
                tableName = StringUtils.camel2Under(ci.getClassName());
                fieldName = StringUtils.camel2Under(ci.getFieldName());
                List<Map<String, Object>> comments = sysTableinfoMapper.findComment(tableName.toUpperCase(), fieldName.toUpperCase());
                record.setLabel(ci.getAnnotation());
                if (comments.size() <= 0) {
                    record.setDatatype("VARCHAR");
                    record.setDatalength(0L);
                    record.setNullable("Y");
                } else {
                    if (StringUtils.isBlank(record.getLabel())) {
                        record.setLabel(StringUtils.InterceptString(comments.get(0).get("comments").toString(), 48));
                    }
                    record.setDatatype(comments.get(0).get("data_type").toString());
                    record.setDatalength(Long.valueOf(comments.get(0).get("data_length").toString()));
                    record.setNullable(comments.get(0).get("nullable").toString());
                }
                if (StringUtils.isBlank(record.getLabel())) {
                    record.setLabel(record.getProp());
                }
                record.setHeaderalign("center");
                record.setUplevel(0L);
                record.setWidth("120px");
                record.setFixed(0);
                record.setResizable(1);
                record.setShowoerflowtooltip(1);
                record.setAlign("left");
                record.setFieldtype("input");
                //record.setRule("");
                //record.setSumtype("");
                record.setQuick(0);
                if (ci.getFieldName().toLowerCase().equals("id")) {
                    record.setEdit(0);
                } else {
                    record.setEdit(1);
                }
                record.setIsshow(1);
                record.setArrange(arrangeDepart);
                arrangeDepart = +10;
                record.setMicroServiceName(servceId);
                //record.setDicttype("");
                record.setEditshow(1);
                sysTableinfoMapper.add(record);
            }
        }
        return HttpResult.ok(cInfos.size() + 1);
    }

    @Override
    public HttpResult createHeader(String label, List<SysTableinfo> records) {
        // TODO Auto-generated method stub
        //查找LIST中最小值
        if (records.size() <= 1) {
            throw new RuntimeException("创建多级标题时最少选择两个字段");
        }
        Long minArrange = records.get(0).getArrange().longValue();
        for (int j = 1; j < records.size(); j++) {
            if (records.get(j).getArrange().longValue() < minArrange) {
                minArrange = records.get(j).getArrange().longValue();
            }
        }

        //处理顺序字段加一
        List<SysTableinfo> maxRecords = sysTableinfoMapper.findArrangeMax(records.get(0).getTablename(), minArrange);
        if (maxRecords.size() <= 0) {
            minArrange = minArrange + 1;
        } else {
            for (int z = 0; z < maxRecords.size(); z++) {
                sysTableinfoMapper.updateArrange(maxRecords.get(z).getId(), maxRecords.get(z).getArrange() + 1);
            }
        }

        //插入父标题
        SysTableinfo tableInfo = new SysTableinfo();
        tableInfo.setTablename(records.get(0).getTablename());
        tableInfo.setLabel(label);
        tableInfo.setHeaderalign("center");
        tableInfo.setUplevel(0L);
        tableInfo.setProp(minArrange.toString());
        tableInfo.setWidth("200px");
        tableInfo.setFixed(0);
        tableInfo.setResizable(1);
        tableInfo.setShowoerflowtooltip(1);
        tableInfo.setAlign("center");
        tableInfo.setFieldtype("input");
        tableInfo.setQuick(0);
        tableInfo.setEdit(0);
        tableInfo.setIsshow(1);
        tableInfo.setArrange(minArrange);
        tableInfo.setEditshow(1);
        sysTableinfoMapper.add(tableInfo);

        for (int i = 0; i < records.size(); i++) {
            SysTableinfo record = new SysTableinfo();
            record.setId(records.get(i).getId());
            record.setUplevel(tableInfo.getId());
            sysTableinfoMapper.update(record);
        }
        return HttpResult.ok("成功创建多级表头");
    }


    public int delete(Long id) {
        SysTableinfo findById = sysTableinfoMapper.findById(id);
        sysTableinfoMapper.updateUpLevel(findById.getTablename(), findById.getId(), 0L);
        return sysTableinfoMapper.delete(id);
    }

    @Override
    public int delete(List<SysTableinfo> records) {
        for (int i = 0; i < records.size(); i++) {
            SysTableinfo findById = sysTableinfoMapper.findById(records.get(i).getId());
            sysTableinfoMapper.updateUpLevel(findById.getTablename(), findById.getId(), 0L);
            sysTableinfoMapper.delete(records.get(i).getId());
        }
        return 1;
    }

    @Override
    public HttpResult saveAllEntityClass() {
        // TODO Auto-generated method stub
        List<entityInfo> allEntityInfo = eureKaUtiles.getAllEntityClass();
        for (entityInfo entity : allEntityInfo) {
            this.saveEntityClass(entity.getHttpUrl(), entity.getEntityName());
        }
        return HttpResult.ok(allEntityInfo.size());
    }

    @Override
    public int deleteT(List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {
            delete(ids.get(i));
        }
        return 0;
    }

    @Override
    public int delete(SysTableinfo record) {
        delete(record.getId());
        sysTableinfoMapper.updateUpLevel(record.getTablename(), record.getId(), 0L);
        return 0;
    }

}
