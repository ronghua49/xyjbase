<template>
  <div style="height: 100%;">
    <el-row v-if="isShowDetails" :key="detailsKey" v-loading="detailsLoading">
      <div class="header">
        <div class="header-left">
          <el-button size="mini" type="primary" @click="goBack">返回</el-button>
        </div>
        <b>业主基本信息</b>
      </div>
      <el-row type="flex">
        <div id="myChart" :style="{width: '50%', height: '300px'}"></div>
        <div id="myChart2" :style="{width: '50%', height: '300px'}"></div>
      </el-row>
      <el-table
        v-loading="detailsLoading"
        stripe border
        size="mini"
        height="360"
        :data="tableData.rows"
        style="width: 100%;margin-top: 20px;">
        <el-table-column
          type="index"
          label="序号"
          width="60"
          align="center">
        </el-table-column>
        <el-table-column
          v-for="(item,index) in columns"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          min-width="120"
          align="center">
        </el-table-column>

      </el-table>
<!--      <el-row type="flex" justify="end" style="padding: 20px;">-->
<!--        <el-pagination-->
<!--          background-->
<!--          @size-change="handleSizeChange"-->
<!--          @current-change="handleCurrentChange"-->
<!--          :current-page="tableData.page"-->
<!--          :page-sizes="[10, 20, 30, 50]"-->
<!--          :page-size="tableData.pageSize"-->
<!--          layout="total, sizes, prev, pager, next, jumper"-->
<!--          :total="Number(tableData.total)">-->
<!--        </el-pagination>-->
<!--      </el-row>-->
    </el-row>
    <xyj-table ref="reftablamain" v-else id="1" @update="getData" v-bind="tablemain">

    </xyj-table>
  </div>
</template>

<script>

  export default {
    name: "IncomeSummaryVo",
    data() {
      return {
        detailsLoading:false,
        detailsKey:"",
        rowData:{},
        pageBean:{
          "page": 1,
          "pageSize": 10,
          "showTotal": true,
        },
        columns: [
          {prop: "feeProperty", label: "收费类型", width: "120"},
          {prop: "payMoney", label: "缴费金额", width: "120"},
          {prop: "lackMoney", label: "未缴金额", width: "120"},
          // {prop: "", label: "合计", width: "120"},
        ],
        tableData: {rows: []},
        isShowDetails: false,
        orgOptions: {},
        tablemain: {
          //不显示表格操作列
          hasOperation: true,
          hasEdit: false,
          hasExport: true,//导出
          //el-table设置
          tableAttrs: {
            height: "86%",
            width: "800px",
          },
          extraButtons: [
            {
              label: "查看", text: "查看", atClick: (row) => {
                return new Promise((resolve, reject) => {
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
          tableName: "IncomeSummaryVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.partplatz,
            url: "/realty/income_summary",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "IncomeSummaryVo",//如果为空系统默认为，表初始化关键字
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
      goBack() {
        this.isShowDetails = false;
        this.pageBean.page = 1;
        this.pageBean.pageSize = 10;
      },
      openView(row) {
        console.log(row);
        this.isShowDetails = true;
        this.$nextTick(() => {
          this.$api.wisdom.partPost({
            url: "realty/income_detail?community=" + row.community,
            data: {}
          }).then(res => {
            console.log(res);
            this.tableData.rows = res.data.list;
            this.drawLine(res);
            this.drawLine2(res)

          });
          this.rowData = row;

        })
      },
      getData(data, res) {
        console.log("data", data);
        console.log("res", res);
        // this.drawLine(res.data["flow_percent"]);
        // this.drawLine2(res.data["monthflow_trends"]);
      },
      drawLine(res) {
        console.log(res);
        let arr1 = [];
        arr1 = [
          {name: "物业费合计", value: res.data["pie_yes"].sum_property_fee},
          {name: "生活垃圾处置费合计", value: res.data["pie_yes"].sum_rabbish_fee},
          {name: "水费合计", value: res.data["pie_yes"].sum_water_fee},
          {name: "电费合计", value: res.data["pie_yes"].sum_electric_fee},
          {name: "装修管理费合计", value: res.data["pie_yes"].sum_decorate_fee},
          {name: "装修垃圾清运费合计", value: res.data["pie_yes"].sum_decorate_rabbish_fee},
          {name: "物业费合计", value: res.data["pie_yes"].sum_ad_fee},

        ];

        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '缴费项目',
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
              name: '缴费项目',
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
        arr1 = [
          {name: "物业费合计", value: res.data["pie_not"].sum_property_fee},
          {name: "生活垃圾处置费合计", value: res.data["pie_not"].sum_rabbish_fee},
          {name: "水费合计", value: res.data["pie_not"].sum_water_fee},
          {name: "电费合计", value: res.data["pie_not"].sum_electric_fee},
          {name: "装修管理费合计", value: res.data["pie_not"].sum_decorate_fee},
          {name: "装修垃圾清运费合计", value: res.data["pie_not"].sum_decorate_rabbish_fee},
          {name: "物业费合计", value: res.data["pie_not"].sum_ad_fee},

        ];
        /* /!* 物业费合计 *!/
         private String  sum_property_fee;
         //生活垃圾处置费合计
         private String sum_rabbish_fee;

         //水费合计
         private String     sum_water_fee;
         //电费合计
         private String  sum_electric_fee;

         //装修管理费合计
         private String  sum_decorate_fee;
         //装修垃圾清运费合计
         private String  sum_decorate_rabbish_fee;

         //广告费合计费合计
         private String  sum_ad_fee;*/
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart2'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '未缴费项目',
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
              name: '未缴费项目',
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

    }
  }
</script>

<style scoped lang="scss">
  .header {
    background-color: #f5f5f5;
    position: relative;
    text-align: center;
    height: 38px;

    .header-left {
      position: absolute;
      left: 0;
      top: 5px;
      margin-left: 10px;
    }

    b {
      line-height: 38px;
      text-align: center;
    }
  }
</style>
