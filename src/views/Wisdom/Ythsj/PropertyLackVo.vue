<template>
  <div style="height: 100%;">
    <el-row v-if="isShowDetails" :key="detailsKey">
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

      <el-form ref="form" :model="filterForm" style="justify-content: end;" size="small" label-width="80px">
        <el-col :span="8">
          <el-form-item label="房间号">
            <el-input v-model="filterForm.room_num" clearable placeholder="请输入房间号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="缴费状态">
            <el-select v-model="filterForm.state" clearable style="width: 100%;" placeholder="请选择缴费状态">
              <el-option label="正常" value="0"></el-option>
              <el-option label="欠费" value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item style="text-align: left;">
            <el-button type="primary" size="mini" @click="getDetailsList">查询</el-button>
          </el-form-item>
        </el-col>

      </el-form>

      <el-table
        v-loading="detailsLoading"
        stripe border
        size="mini"
        height="360"
        :data="tableData.rows"
        style="width: 100%">
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
          :formatter="item.formatter"
          min-width="120"
          align="center">
        </el-table-column>

      </el-table>
      <el-row type="flex" justify="end" style="padding: 20px;">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="tableData.page"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="tableData.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="Number(tableData.total)">
        </el-pagination>
      </el-row>
    </el-row>
    <xyj-table ref="reftablamain" v-else id="1" @update="getData" v-bind="tablemain">

    </xyj-table>
  </div>
</template>

<script>

  export default {
    name: "PropertyLackVo",
    data() {
      return {
        detailsLoading: false,
        detailsKey: "",
        rowData: {},
        filterForm: {
          room_num: "",
          state: "",
        },
        pageBean: {
          "page": 1,
          "pageSize": 10,
          "showTotal": true,
        },
        columns: [
          {prop: "building_no", label: "楼栋号", width: "120"},
          {prop: "unit_no", label: "单元号", width: "120"},
          {prop: "room_num", label: "房号", width: "120"},
          {prop: "state", label: "缴费状态", width: "120",
            formatter:(row,colum,cellValue)=>{
              if (cellValue == '0'){
                return '正常'
              } else if (cellValue == '1'){
                return '欠费'
              }
            }
          },
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
          tableName: "PropertyLackVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.partplatz,
            url: "/realty/payment/list",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "PropertyLackVo",//如果为空系统默认为，表初始化关键字
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
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageBean.pageSize = val;
        this.getDetailsList();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageBean.page = val;
        this.getDetailsList();
      },
      openView(row) {
        console.log(row);
        this.isShowDetails = true;
        this.$nextTick(() => {
          this.$api.wisdom.partPost({
            url: "realty/payment/detail?community=" + row.community,
            data: {}
          }).then(res => {
            console.log(res);
            this.drawLine(res);
            this.drawLine2(res)
          });
          this.rowData = row;
          this.getDetailsList();
        })
      },
      getDetailsList() {
        let date = new Date();
        let month = date.getMonth() >= 9 ? date.getMonth() + 1 : '0' + date.getMonth() + 1;
        let firstDay = '01';

        var nextMonth = date.getMonth() + 1;
        var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
        var oneDay = 1000 * 60 * 60 * 24;
        let lastDate = new Date(nextMonthFirstDay - oneDay);
        console.log(lastDate, lastDate.getDate());
        let lastDay = lastDate.getDate() > 9 ? lastDate.getDate() : '0' + lastDate.getDate();

        let pay_begin_date = date.getFullYear() + '-' + month + '-' + firstDay;
        let pay_end_date = date.getFullYear() + '-' + month + '-' + lastDay;
        this.detailsLoading = true;
        let filterForm = this.filterForm;
        let query = [];
        if (filterForm.room_num) {
          query.push({
            "hasInitValue": true,
            "operation": "EQUAL",
            "property": "room_num",
            "relation": "AND",
            "value": filterForm.room_num
          })
        }
        if (filterForm.state) {
          query.push({
            "hasInitValue": true,
            "operation": "EQUAL",
            "property": "state",
            "relation": "AND",
            "value": filterForm.state
          })
        }
        this.$api.wisdom.partPost({
          url: "realty/warn?community=" + this.rowData.community,
          data: {
            "pageBean": this.pageBean,
            querys: [
              {
                "hasInitValue": true,
                "operation": "EQUAL",
                "property": "pay_begin_date",
                "relation": "AND",
                "value": pay_begin_date
              },
              {
                "hasInitValue": true,
                "operation": "EQUAL",
                "property": "pay_end_date",
                "relation": "AND",
                "value": pay_end_date
              }
            ].concat(query)
          }
        }).then(res => {
          this.detailsLoading = false;
          console.log(res);
          this.tableData = res.data;
        }).catch(res => {
          this.detailsLoading = false;
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
          {name: "欠费", value: res.data["饼图"].lack},
          {name: "正常", value: res.data["饼图"].common},

        ]

        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '物业缴费状态占比',
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
              name: '物业缴费状态占比',
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
        res.data["柱状图"].forEach((v, i) => {
          if (v) {
            arr1.push(v["欠费"]);
            arr2.push(v["正常"]);
          }
        });
        console.log(arr1, arr2);
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('myChart2'));
        // 绘制图表
        myChart.setOption({
          title: {
            left: "20px",
            top: "10px",
            text: '物业缴费状态分布统计',
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
          textStyle: {
            fontWeight: "normal",
            fontSize: "10px",
          },
          legend: {
            data: ['欠费', '正常']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: res.data["柱状图结果分类"]//['周一','周二','周三','周四','周五','周六','周日']
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '欠费',
              type: 'bar',
              barWidth: '60%',
              stack: '广告',
              data: arr1//[120, 132, 101, 134, 90, 230, 210,300]
            },
            {
              name: '正常',
              type: 'bar',
              barWidth: '60%',
              stack: '广告',
              data: arr2//[220, 182, 191, 234, 290, 330, 310,300]
            },
          ]
        });
      }

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

  .filter-wrapper {

  }
</style>
