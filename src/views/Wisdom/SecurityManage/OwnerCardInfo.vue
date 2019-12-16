<template>
  <div style="height: 100%;">
    <xyj-table ref="reftablamain" v-show="!isShowDetails" id="1" @update="getData" v-bind="tablemain">

    </xyj-table>
    <div v-show="isShowDetails">
      <owner-card-view @goBack="goBack" :rowId="rowId"/>
    </div>
  </div>
</template>

<script>
  import ownerCardView from "./OwnerCardView";
  export default {
    name: "OwnerCardInfo",
    components:{
      ownerCardView,
    },
    data() {
      return {
        rowId:"",
        isShowDetails:false,
        orgOptions: {},
        tablemain: {
          //不显示表格操作列
          hasOperation: true,
          hasExport: true,//导出
          hasEdit:false,
          //el-table设置
          tableAttrs: {
            height: "86%",
            width: "800px",
          },
          extraButtons: [
            {
              label: "查看", text: "查看", atClick: (row) => {
                return new Promise((resolve,reject)=>{
                  this.openView(row);
                  resolve(false);
                })
              }
            }
          ],
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
          tableName: "CardHolderVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.alarmUrl,
            url: "/cardholderInfo/cardHolder",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "CardHolderVo",//如果为空系统默认为，表初始化关键字
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
      openView(row) {
        this.isShowDetails = true;
        this.rowId = row.id;
        console.log("当前行", row);
        let url = "cardholderInfo/detail/{id}"
        /*业主基本信息：cardholderInfo/detail/{id}
          绑卡信息：cardholderInfo/bindDetail
          报警记录:alarmRecord/query
          点击查看图片：cardholderInfo//photo/{id*/
      },
      goBack(){
        this.isShowDetails = false;
        this.rowId = "";
      }

    }
  }
</script>

<style scoped>

</style>
