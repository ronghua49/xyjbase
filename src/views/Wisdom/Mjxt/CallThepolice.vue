<template>
  <div style="height: 100%;">
    <xyj-table ref="reftablamain" id="1" v-bind="tablemain" >

    </xyj-table>
  </div>
</template>

<script>
  export default {
    name: "CallThepolice",
    data() {
      return {
        tablemain: {
          //不显示表格操作列
          hasOperation: false,
          hasExport:true,//导出
          //el-table设置
          tableAttrs: {
            height: "86%",
            width: "800px",
          },
          //el-table事件设置
          tableOns: {

          },
          //表格中的操作列设置
          operationAttrs: {
            width: "200px"
          },
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            alertType: function (row, column, cellValue, index) {
              switch (cellValue) {
                case '1':
                  return "防区报警";
                  break;
                case '2':
                  return "紧急报警";
                  break;
                case '3':
                  return "消警";
                  break;
                case '6':
                  return "无线防区报警";
                  break;
                case '7':
                  return "边界报警";
                  break;
                case '9':
                  return "门磁报警";
                  break;
                case '12':
                  return "主机防拆";
                  break;
                case '13':
                  return "分区防拆";
                  break;
                case '14':
                  return "设备不在线";
                  break;
                case '15':
                  return "新无线报警";
                  break;
                default:
                  return "其他报警";
              }
            },

          },
          //single: false //单选
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "DfHousealert",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.doorUrl,
            url: "dfHousealert/callPolice",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          extraSelectData: {
            sendType:function () {
              return[
                {label:"发送方式",value:"1"},
              ]
            }
          },
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "DfHousealert",//如果为空系统默认为，表初始化关键字
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

    }
  }
</script>

<style scoped>

</style>
