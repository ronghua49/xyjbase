<template>
  <div style="height: 100%;" class="DailyFeeVo">
    <xyj-table ref="reftablamain" id="1" v-bind="tablemain" >

    </xyj-table>
  </div>
</template>

<script>
  export default {
    name: "DailyFeeVo",
    data() {
      return {
        form:{},
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
          tableName: "DailyFeeVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.partplatz,
            url: "/realty/dailyFee",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {

          }, //表格初始化时查询条件
          // codeVaule:this.codeVaule,
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "DailyFeeVo",//如果为空系统默认为，表初始化关键字
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
      codeVaule(data){
        let date = new Date();
        let month = date.getMonth() >= 9 ? date.getMonth() + 1: '0'+date.getMonth()+1;
        let firstDay = '01';

        var nextMonth = date.getMonth() + 1;
        var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
        var oneDay=1000*60*60*24;
        let lastDate = new Date(nextMonthFirstDay-oneDay);
        console.log(lastDate,lastDate.getDate());
        let lastDay = lastDate.getDate()  > 9 ?lastDate.getDate():'0'+ lastDate.getDate();

        let pay_begin_date = date.getFullYear() + '-' + month + '-' + firstDay;
        let pay_end_date = date.getFullYear() + '-' + month + '-' + lastDay;
        this.$nextTick(() => {
          let obj = {hasInitValue: false, operation: "EQUAL", property: "pay_begin_date", relation: "AND", value:pay_begin_date };
          let obj2 = {hasInitValue: false, operation: "EQUAL", property: "pay_end_date", relation: "AND", value:pay_end_date };
          data.querys.push(obj)
          data.querys.push(obj2)
        })
      },
    }
  }
</script>

<style scoped>

</style>
