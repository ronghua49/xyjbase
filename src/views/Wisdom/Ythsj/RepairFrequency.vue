<template>
  <div>
    <el-row type="flex">
      <div id="myChart" :style="{width: '50%', height: '300px'}"></div>
      <div id="myChart2" :style="{width: '50%', height: '300px'}"></div>
    </el-row>
    <xyj-table ref="reftablamain" @update="getData"  id="1" v-bind="tablemain">

    </xyj-table>
  </div>
</template>

<script>

  export default {
    name: "RepairFrequency",
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
          tableName: "RepairVo2",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.partplatz,
            url: "/realty/frequency",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "RepairVo2",//如果为空系统默认为，表初始化关键字
            fileName: ""//自定义文件名,如果为空则根据实体类名称定义下载文件名
          },
        },
      }
    },
    mounted() {
      if (this.$route.name && this.tablemain.bExport) {
        this.tablemain.bExport.fileName = this.$route.name;
      }
    },
    methods: {

      getData(data,res){
        console.log("data",data);
        console.log("res", res);
        this.drawLine(res.data["pie"]);
        this.drawLine2(res.data);
      },
      drawLine(res) {
        if (res.length == 0) {
          return false;
        }
        let arr = [
          {value: res.repair_done, name: '报修受理'},
          {value: res.repair_report, name: '用户报修'},
        ];
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '处理进度占比',
            bottom: "0",
            left: "50%",
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
            },
            textAlign: "center",
          },
          // legend: {
          //   orient:"vertical",
          //   right:"60px",
          //   bottom: "45px",
          //   data:['有效','失效']
          // },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          series: [
            {
              name: '处理进度占比',
              type: 'pie',
              radius: '70%',
              data: arr
            }
          ]
        });
      },
      drawLine2(res) {
        console.log(res);
        let arr1 = [];
        let arr2 = [];
        res["barChar"].forEach((v, i) => {
          if (v) {
            arr1.push(Object.keys(v)[0]);
            arr2.push(v[Object.keys(v)[0]]);
          }
        });
        console.log(arr1, arr2);
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart2'));
        // 绘制图表
        myChart.setOption({
          color: ['#3398DB'],
          title: {
            left: "20px",
            top: "10px",
            text: '月度报修趋势',
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
            },
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: arr1,
            axisTick: {
              alignWithLabel: true
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '月度报修趋势',
            barWidth: '60%',
            data: arr2,
            type: 'bar'
          }]
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
