<template>
  <div style="height: 100%;">
    <xyj-table ref="reftablamain" id="1" @update="getData" v-bind="tablemain">
      <template slot="xyjcontainer" >
        <div style="display: flex;">
          <upload :uploadUrl="uploadUrl" @uploadSuccess="$refs['reftablamain'].getList()" style="margin-right: 10px;"/>
          <download :downloadUrl="downloadUrl"/>
        </div>
      </template>
    </xyj-table>
  </div>
</template>

<script>

  export default {
    name: "CallPoliceManage",
    data() {
      return {
        uploadUrl:{
          hostUrl: this.global.alarmUrl,
          url: "/cardInfo/importCard",
          content: '导入',
        },
        downloadUrl:{
          hostUrl: this.global.alarmUrl,
          url: "/cardInfo/download",
          method: "get",
        },
        orgOptions: {},
        tablemain: {
          //不显示表格操作列
          hasOperation: true,
          hasNew:true,
          hasEdit:true,
          hasDelete:true,
          hasExport: true,//导出
          //el-table设置
          tableAttrs: {
            height: "86%",
            width: "800px",
          },
          //el-table事件设置
          tableOns: {},
          //表格中的操作列设置
          operationAttrs: {
            width: "200px"
          },
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            status: function (row, column, cellValue, index) {
              switch (cellValue) {
                case '0':
                  return "在线";
                  break;
                case '1':
                  return "离线";
                  break;
                default:
                  return cellValue;
              }
            }
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "CardManagementVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.alarmUrl,
            url: "/cardInfo/query",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          bNewUrl: {
            hostUrl: this.global.alarmUrl,
            url: "cardInfo/save",
            method: "post"
          },
          bEditUrl: {
            hostUrl: this.global.alarmUrl,
            url: "cardInfo/save",
            method: "post"
          },
          bDeleteUrl: {
            hostUrl: this.global.alarmUrl,
            url: "cardInfo/delete",
            method: "post"
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "CardManagementVo",//如果为空系统默认为，表初始化关键字
            fileName: ""//自定义文件名,如果为空则根据实体类名称定义下载文件名
          },
        },
      }
    },
    mounted(){
      if (this.$route.name && this.tablemain.bExport) {
        this.tablemain.bExport.fileName = this.$route.name;
      }
    },
    methods: {
      getData(data, res) {
        console.log("data", data);
        console.log("res", res);
        // this.drawLine(res.data["flow_percent"]);
        // this.drawLine2(res.data["monthflow_trends"]);
      },

    }
  }
</script>

<style scoped>

</style>
