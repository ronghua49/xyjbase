<template>
  <div style="height: 100%;">
    <!--    <el-row type="flex">-->
    <!--      &lt;!&ndash;      <div id="myChart" :style="{width: '50%', height: '300px'}"></div>&ndash;&gt;-->
    <!--      <div id="myChart2" :style="{width: '50%', height: '300px'}"></div>-->
    <!--    </el-row>-->
    <xyj-table ref="reftablamain" id="1" @update="getData" v-bind="tablemain">

    </xyj-table>
  </div>
</template>

<script>

  export default {
    name: "ChargeCarCount",
    data() {
      return {
        orgOptions: {},
        tablemain: {
          //不显示表格操作列
          hasOperation: false,
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
          tableName: "FeeStatVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.partplatz,
            url: "cprfeerecord/feeStat",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: " FeeStatVo",//如果为空系统默认为，表初始化关键字
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
      getData(data, res) {
        console.log("data", data);
        console.log("res", res);
        // this.drawLine(res.data["flow_percent"]);
        // this.drawLine2(res.data["monthflow_trends"]);
      },
      drawLine(res) {
        let arr1 = [];
        arr1 = [
          {name:"15分钟",value:res[0].title_15m},
          {name:"1小时",value:res[0].title_1h},
          {name:"2小时",value:res[0].title_2h},
          {name:"3小时",value:res[0].title_3h},
          {name:"4小时",value:res[0].title_4h},
          {name:"5小时",value:res[0].title_5h},
          {name:"6-12小时",value:res[0].title_6_12h},

        ]

        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '车流时段占比',
            bottom: "0",
            left: "50%",
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
            },
            textAlign: "center",
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          // legend: {
          //   orient: 'vertical',
          //   left: 'left',
          //   data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
          // },
          series: [
            {
              name: '车流时段占比',
              type: 'pie',
              radius: '70%',
              // center: ['50%', '60%'],
              data: arr1,
              //   [
              //   {value:335, name:'直接访问'},
              //   {value:310, name:'邮件营销'},
              //   {value:234, name:'联盟广告'},
              //   {value:135, name:'视频广告'},
              //   {value:1548, name:'搜索引擎'}
              // ],
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        });
      },
      drawLine2(res) {
        console.log(res);
        let arr1 = [];
        let arr2 = [];
        res.forEach((v, i) => {
          if (Object.keys(v)[0]) {
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
            text: '近30天访客流量趋势',
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
            name: '近30天访客流量趋势',
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

</style>
