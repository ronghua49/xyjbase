<template>
  <div>
    <el-row type="flex">
      <div id="myChart" :style="{width: '50%', height: '300px'}"></div>
      <div id="myChart2" :style="{width: '50%', height: '300px'}"></div>
    </el-row>
    <xyj-table ref="reftablamain" id="1" v-bind="tablemain">

    </xyj-table>
  </div>
</template>

<script>

  export default {
    name: "DeviceState",
    data() {
      return {
        orgOptions: {},
        tablemain: {
          //不显示表格操作列
          hasOperation: false,
          hasExport: true,//导出
          //el-table设置
          tableAttrs: {
            height: "400px",
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
          tableName: "TotalDecicesVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.doorUrl,
            url: "dfDevice/totalDevices",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "TotalDecicesVo",//如果为空系统默认为，表初始化关键字
            fileName: ""//自定义文件名,如果为空则根据实体类名称定义下载文件名
          },
        },
      }
    },
    mounted() {
      this.getData();
      if (this.$route.name && this.tablemain.bExport) {
        this.tablemain.bExport.fileName = this.$route.name;
      }
    },
    methods: {
      getData(){
        this.$api.wisdom.post({
          url:"/dfDevice/totalDevicesPie",
          data:{}
        }).then(res=>{
          console.log(res);
          this.drawLine(res.data);
        })
      },
      drawLine(res) {
        if (res.length == 0) {
          return false;
        }
        let arr = [
          {value: res[0].oonline, name: '在线'},
          {value: res[0].offline, name: '离线'},
        ]
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '当前设备状态占比',
            bottom: "0",
            left: "50%",
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
            },
            textAlign: "center",
          },
          tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: "vertical",
            right: "60px",
            bottom: "45px",
            data: ['在线', '离线']
          },
          series: [
            {
              name: '当前设备状态占比',
              type: 'pie',
              radius: '70%',
              data: arr
            }
          ]
        });
      },
      drawLine2() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart2'));
        // 绘制图表
        myChart.setOption({
          title: {
            left: "20px",
            top: "10px",
            text: '近30天在线设备趋势',
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
            },
          },
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {data: [180, 240, 160, 319, 240, 170, 150], type: 'line'},
            {data: [150, 290, 190, 399, 290, 210, 320], type: "line"},
          ],
        });
      }

    }
  }
</script>

<style scoped>
  .echarts {
    height: 200px;
  }
</style>
