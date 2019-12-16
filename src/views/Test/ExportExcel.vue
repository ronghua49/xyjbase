<template>
  <div style="height: 100%;">
    <div style="height: 100%;">
      <xyj-table ref="otable"  id="1" v-bind="tableConfig"></xyj-table>
    </div>
  </div>
</template>

<script>
  import XyjTable from '../../components/xyjtable/xyjTable';
  import { baseUrl } from '@/utils/global.js';
  import jqueryAjax from "@/http/jqueryAjax";
  import axios from '@/http/axios';
  export default {
    name: "ExportExcel",
    components: {XyjTable},
    data(){
      return {
        selectload: false,
        query:[],
        tableConfig: {
          hasOperation:false,//操作列不显示
          hasExport:true,//导出
          hasDelete: true, //删除
          hasNew: false, //新增
          hasView: true, //查看
          hasEdit: true, //编辑
          hasSearch:true, // 快速查询是否显示
          //el-table设置
          tableAttrs:{
            height: "86%",
            width: "800px",
          },
          //el-table事件设置
          tableOns: {
            select: function (selection,row) {

            }
          },
          //表格中的操作列设置
          operationAttrs:{
            width: "200px"
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: baseUrl,
          //表初始化关键字，一般为表名
          tableName: "SysTableinfo",
          //查询时的排序字段
          sorter: [{direction: "ASC",property: "tablename"},{direction: "ASC",property: "arrange"}],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: baseUrl,
            url: "sysTableinfo/query",
            method: 'post',
            notCondition: true
          },
          //业务数据微服务,导出
          bExport: {
             // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
              tableName: "",//如果为空系统默认为，表初始化关键字
              fileName: ""//自定义文件名,如果为空则根据实体类名称定义下载文件名
          },
          //业务数据微服务,新增网址
          bNewUrl: {
            hostUrl: baseUrl,
            url: "sysTableinfo/save",
            method: 'post',
            // clear: true, //清除新增对话框输入的数据
            close: true, //关闭新增对话框
          },
          //业务数据微服务,修改网址
          bEditUrl: {
            hostUrl: baseUrl,
            url: "sysTableinfo/save",
            method: 'post',
            // clear: true, //清除新增对话框输入的数据
            close: true  //关闭修改对话框
          },
          //业务数据微服务,删除网址
          bDeleteUrl: {
            hostUrl: baseUrl,
            url: "/sysTableinfo/delete",
            method: 'post'
          },

          //根据字段名定义函数
          extraSelectData: {
            rule: function () {
              return [
                {label: '验证QQ号',value: 'QQ'},
                {label: '非必填QQ号',value: 'notQQ'},
                {label: '验证手机号',value: 'phone'},
                {label: '非必填手机号',value: 'notPhone'},
                {label: '两位小数',value: 'checkMoney'},
                {label: '非必填两位小数',value: 'notCheckMoney'},
                {label: '身份证',value: 'checkId'},
                {label: '非必填身份证',value: 'notCheckId'},
                {label: '整数',value: 'checkInt'},
                {label: '非必填整数',value: 'notCheckInt'},
                {label: '数字',value: 'checkNum'},
                {label: '非必填数字',value: 'notcheckNum'},
                {label: '输入框必填项',value: 'checkMustFill'},
                {label: '选择框必填项',value: 'checkSelectValue'},
                {label: '复选框必选项',value: 'checkSelectArray'},
              ];
            },
            align: function () {
              return [
                {label: '左对齐',value: 'left'},
                {label: '右对齐',value: 'right'},
                {label: '居中',value: 'center'}
              ];
            },
            fieldtype: function () {
              return [
                {label: '文本框',value: 'input'},
                {label: '下拉框',value: 'select'},
                {label: '单选组',value: 'radio-group'},
                {label: '复选组',value: 'checkbox-group'},
                {label: '多文本',value: 'textarea'},
                //{label: '双日期',value: 'date-picker'},
                {label: '日期时间',value: 'single-date'},
                {label: '日期',value: 'date'},
                {label: '小时分钟',value: 'hourminute'},
                {label: '小时',value: 'hour'},
                {label: '开关',value: 'switch'},
                {label: '上传文件',value: 'upload'},
              ];
            },
            headeralign: function () {
              return [
                {label: '左对齐',value: 'left'},
                {label: '右对齐',value: 'right'},
                {label: '居中',value: 'center'}
              ];
            },
            sumtype: function () {
              return [
                {label: '文本',value: 'text'},
                {label: '求和',value: 'sum'},
                {label: '平均值',value: 'avg'},
                {label: '记录数',value: 'count'}
              ];
            },
            dicttype: function (column) {
              let tdata = [];
              let tmpUrl = baseUrl + '/Dict/findByGroupType?delFlag=0';
              jqueryAjax({
                url: tmpUrl,
                xyjSuccess: function (data) {
                  data.data.forEach((column,idx) => {
                    tdata.push({label: column.type, value: column.type})
                  });
                }
              });
              return tdata;
            },
          }
        }
      }
    },
    methods:{}
  }
</script>

<style scoped lang="scss">
</style>
