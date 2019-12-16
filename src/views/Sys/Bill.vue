<template>
  <div style="height: 100%;">
    <div style="height: 100%;">
      <xyj-table ref="reftablamain"  id="1" v-bind="tablemain"> </xyj-table>
    </div>
  </div>
</template>

<script>
  import axios from "@/http/axios";
  import { getImg } from "@/utils/echarts";
    export default {
        name: "Bill",
      components: {},
      data() {
         return {
           tablemain: {
             hasDelete: true, //删除
             hasNew: true, //新增
             hasView: false, //查看
             hasEdit: true, //编辑
             disabledNew:false, // 新增按钮是否禁用
             //el-table设置
             tableAttrs: {
               height: "86%",
               width: "800px"
             },
             //el-table事件设置
             tableOns: {
             },
             //表格中的操作列设置
             operationAttrs: {
               width: "100px"
             },
             //格式化表格字段显示，按照类属性定义格式化函数
             formatters: {
               endrule: function (row, column, cellValue) {
                 switch (cellValue) {
                   case 1:
                     return "随机数";
                     break;
                   case 2:
                     return "累加";
                     break;
                   case 3:
                     return "累加(每日重置)";
                     break;
                 }
               }
             },
             //根据字段名定义函数
             extraSelectData: {
               daterule: function () {
                 return [
                   {label: "空", value: "空"},
                   {label: "yyyyMMdd", value: "yyyyMMdd"},
                   {label: "yyyy-MM-dd", value: "yyyy-MM-dd"},
                   {label: "yyyy.MM.dd", value: "yyyy.MM.dd"},
                   {label: "yyyyMM", value: "yyyyMM"},
                   {label: "yyMMdd", value: "yyMMdd"}
                 ];
               },
               timerule: function () {
                 return [
                   {label: "空", value: "空"},
                   {label: "HHmmss", value: "HHmmss"},
                   {label: "HHmmssSSS", value: "HHmmssSSS"},
                   {label: "HH_mm_ss", value: "HH_mm_ss"},
                   {label: "HH_mm_ss_SSS", value: "HH_mm_ss_SSS"},
                   {label: "HHmm", value: "HHmm"},
                   {label: "HH_mm", value: "HH_mm"}
                 ];
               },
               endrule: function () {
                 return [
                   {label: "随机数", value: 1},
                   {label: "累加", value: 2},
                   {label: "累加(每日重置)", value: 3},
                 ]
               },
               orderrule: function () {
                 return [
                   {label: "日期-时间-尾数", value: "日期-时间-尾数"},
                   {label: "日期-尾数-时间", value: "日期-尾数-时间"},
                   {label: "时间-日期-尾数", value: "时间-日期-尾数"},
                   {label: "时间-尾数-日期", value: "时间-尾数-日期"},
                   {label: "尾数-日期-时间", value: "尾数-日期-时间"},
                   {label: "尾数-时间-日期", value: "尾数-时间-日期"},
                 ]
               }
             },
             //single: false //单选
             //admin微服务网址
             hostUrl: this.global.baseUrl,
             //表初始化关键字，一般为表名
             tableName: "BillsRules",
             //查询时的排序字段
             sorter: [],
             //业务数据微服务,查询与高级查询
             bQuery: {
               hostUrl: this.global.baseUrl,
               url: "/billsRules/query",
               method: "post",
               notCondition: true //没有查询条件则查询全部数据
             },
             //业务数据微服务,新增网址
             bNewUrl: {
               hostUrl: this.global.baseUrl,
               url: "/billsRules/save",
               method: "post",
               clear: true, //清除新增对话框输入的数据
               close: true //关闭新增对话框
             },
             //业务数据微服务,修改网址
             bEditUrl: {
               hostUrl: this.global.baseUrl,
               url: "/billsRules/save",
               method: "post",
               clear: true, //清除新增对话框输入的数据
               close: true //关闭修改对话框
             },
             //业务数据微服务,删除网址
             bDeleteUrl: {
               hostUrl: this.global.baseUrl,
               url: "/billsRules/delete",
               method: "post"
             },
             //当点击修改时，默认修改调用前处理上传数据时
             onManualEdit: this.reftableitemManualEdit,
           },
         }
      },
      methods:{
        reftableitemManualEdit: function(data){
          if(data){
            data.id = this.$refs.reftablamain.selected[0].id;
          }
        },
      }
    }
</script>

<style scoped>
</style>
