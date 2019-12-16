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
        name: "FilePath",
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
             },
             //single: false //单选
             //admin微服务网址
             hostUrl: this.global.baseUrl,
             //表初始化关键字，一般为表名
             tableName: "FilePath",
             //查询时的排序字段
             sorter: [],
             //业务数据微服务,查询与高级查询
             bQuery: {
               hostUrl: this.global.baseUrl,
               url: "/filePath/query",
               method: "post",
               notCondition: true //没有查询条件则查询全部数据
             },
             //业务数据微服务,新增网址
             bNewUrl: {
               hostUrl: this.global.baseUrl,
               url: "/filePath/save",
               method: "post",
               clear: true, //清除新增对话框输入的数据
               close: true //关闭新增对话框
             },
             //业务数据微服务,修改网址
             bEditUrl: {
               hostUrl: this.global.baseUrl,
               url: "/filePath/save",
               method: "post",
               clear: true, //清除新增对话框输入的数据
               close: true //关闭修改对话框
             },
             //业务数据微服务,删除网址
             bDeleteUrl: {
               hostUrl: this.global.baseUrl,
               url: "/filePath/delete",
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
