<template>
  <div class="home-wrapper">
    <div class="home-view">
      <div class="home-header">
        <h3>智慧社区数据平台</h3>
      </div>
      <div class="home-item" @click="routeLinkTo('Wisdom/Mjxt/CallThepolice')">
        <p>报警信息统计</p>
        <div class="table-box">
          <home-list :columns="columns1" :tableData="tableData1"/>
        </div>
      </div>
      <div class="home-item" >
        <p style="text-align: center;">设备在线统计</p>
        <div class="table-box-two">
          <div id="homeChart1" :style="{width: '50%', height: '185px',color:'#0092F1'}">
            {{deviceStatNum1}}
            <span class="online-label">主机</span>
          </div>
          <div id="homeChart2" :style="{width: '50%', height: '185px',color:'#1AD96F'}">
            {{deviceStatNum2}}
            <span class="online-label">分机</span>
          </div>
          <div id="homeChart3" :style="{width: '50%', height: '185px'}"></div>
          <div id="homeChart4" :style="{width: '50%', height: '185px'}"></div>
        </div>
      </div>
      <div class="home-item" @click="routeLinkTo('Wisdom/Mjxt/OpendoorHistory')">
        <p>刷卡记录统计</p>
        <div class="table-box">
          <home-list :columns="columns2" :tableData="tableData2"/>
        </div>
      </div>
      <div class="home-item" @click="routeLinkTo('Wisdom/Ajjy/CallerNotes')">
        <p>访客记录</p>
        <div class="table-box">
          <div id="homeChart5" :style="{width: '100%', height: '390px'}"></div>
        </div>
      </div>
      <div class="home-item" @click="routeLinkTo('Wisdom/SecurityManage/CallPoliceHistory')">
        <p>出入口报警</p>
        <div class="table-box">
          <div id="homeChart6" :style="{width: '100%', height: '390px'}"></div>
        </div>
      </div>
      <div class="home-item" @click="routeLinkTo('Wisdom/Mjxt/OpendoorHistory')">
        <p>人脸识别统计</p>
        <div class="table-box">
          <home-list :columns="columns3" :tableData="tableData3"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import homeList from "./common/home-list/home-list";

  export default {
    name: "index",
    components: {
      homeList,
    },
    data() {
      return {
        timer: null,
        columns1:[
          {label:"设备状态",prop:"alertType" , width:"150"},
          {label:"设备位置",prop:"position" , width:"150"},
          {label:"时间",prop:"alertDate" , width:"280"},
        ],
        columns2:[
          {label:"姓名",prop:"userName" , width:"150"},
          {label:"设备位置",prop:"position" , width:"150"},
          {label:"时间",prop:"openTime" , width:"280"},
        ],
        columns3:[
          {label:"姓名",prop:"userName" , width:"150"},
          {label:"设备位置",prop:"position" , width:"150"},
          {label:"时间",prop:"openTime" , width:"280"},
        ],
        // columns1: {
        //   name: "alertType",
        //   position: "position",
        //   date: "alertDate",
        // },
        // columns2: {
        //   name: "userName",
        //   position: "position",
        //   date: "openTime",
        // },
        // columns3: {
        //   name: "userName",
        //   position: "position",
        //   date: "openTime",
        // },
        pageBean: {
          page: 1,
          pageSize: 10,
        },
        tableData1: [],
        tableData2: [],
        tableData3: [],
        tableData4: {},
        deviceStatNum1:"",
        deviceStatNum2:""
      }
    },
    mounted() {
      this.getList1();
      this.getList2();
      this.getList3();
      this.getList4();
      this.getList5();
      this.timer = setInterval(() => {
        this.getList1();
        this.getList2();
        this.getList3();
        this.getList4();
        this.getList5();
      }, 5000)

    },
    beforeDestroy() {
      console.log("'''''''''''''''''''''''''''''''");
      clearTimeout(this.timer);
      this.timer = null;
    },
    methods: {
      routeLinkTo(url){
        this.$router.push('/' + url);
      },
      getList1() {
        this.$api.wisdom.post({
          url: "/dfHousealert/callPolice",
          data: {
            pageBean: this.pageBean
          }
        }).then(res => {
          if (res.code == 200) {
            this.tableData1 = res.data.rows;
          } else {
            this.$message({type: "error", message: res.message});
          }
        })
      },
      getList2() {
        this.$api.wisdom.post({
          url: "/dfDevice/deviceStat",
          data: {
            // pageBean: this.pageBean
          }
        }).then(res => {
          if (res.code == 200) {
            this.deviceStatNum1 = res.data.extDevices;
            this.deviceStatNum2 = res.data.hostDevices;
            // this.drawLine1(res.data);
            // this.drawLine2(res.data);
          } else {
            this.$message({type: "error", message: res.message});
          }
        })
      },
      getList3() {
        this.$api.wisdom.post({
          url: "/dfGateopen2/openDoorStat",
          data: {
            pageBean: this.pageBean
          }
        }).then(res => {
          if (res.code == 200) {
            this.tableData2 = res.data.card_open;
            this.tableData3 = res.data.face_open;
          } else {
            this.$message({type: "error", message: res.message});
          }
        })
      },
      getList4() {
        this.$api.wisdom.post({
          url: "/nestleHomelandController/visitorStat",
          data: {
            // pageBean:this.pageBean
          }
        }).then(res => {
          if (res.code == 200) {
            this.tableData4 = res.data;
            this.drawLine5(res.data);
          } else {
            this.$message({type: "error", message: res.message});
          }
        })
      },
      getList5() {
        this.$api.wisdom.alarmPost({
          url: "/alarmRecord/alarmStat",
          data: {
            // pageBean:this.pageBean
          }
        }).then(res => {
          if (res.code == 200) {
            this.tableData4 = res.data;
            this.drawLine6(res.data);
          } else {
            this.$message({type: "error", message: res.message});
          }
        })
      },
      drawLine1(res) {
        let arr = [
          {value: res.extDevices, name: '门禁'},
        ];
        let option = {
          color: ['#FFBF44'],
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
          },
          // legend: {
          //   orient: 'vertical',
          //   x: 'left',
          //   data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
          // },
          series: [
            {
              name: '门禁',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: false,
                  position: 'center'
                },
                emphasis: {
                  show: true,
                  textStyle: {
                    fontSize: '30',
                    fontWeight: 'bold'
                  }
                }
              },
              labelLine: {
                normal: {
                  show: false
                }
              },
              data: arr
            }
          ]
        };
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('homeChart1'));
        // 绘制图表
        myChart.setOption(option)
      },
      drawLine2(res) {
        let arr = [
          {value: res.hostDevices, name: '单元门主机'},
        ];
        let option = {
          color: ['#0092F1'],
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
          },
          // legend: {
          //   orient: 'vertical',
          //   x: 'left',
          //   data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
          // },
          series: [
            {
              name: '单元门主机',
              type: 'pie',
              radius: ['50%', '70%'],
              // label: {
              //   normal: {
              //     formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
              //     backgroundColor: '#eee',
              //     borderColor: '#aaa',
              //     borderWidth: 1,
              //     borderRadius: 4,
              //     // shadowBlur:3,
              //     // shadowOffsetX: 2,
              //     // shadowOffsetY: 2,
              //     // shadowColor: '#999',
              //     // padding: [0, 7],
              //     rich: {
              //       a: {
              //         color: '#999',
              //         lineHeight: 22,
              //         align: 'center'
              //       },
              //       // abg: {
              //       //     backgroundColor: '#333',
              //       //     width: '100%',
              //       //     align: 'right',
              //       //     height: 22,
              //       //     borderRadius: [4, 4, 0, 0]
              //       // },
              //       hr: {
              //         borderColor: '#aaa',
              //         width: '100%',
              //         borderWidth: 0.5,
              //         height: 0
              //       },
              //       b: {
              //         fontSize: 16,
              //         lineHeight: 33
              //       },
              //       per: {
              //         color: '#eee',
              //         backgroundColor: '#334455',
              //         padding: [2, 4],
              //         borderRadius: 2
              //       }
              //     }
              //   }
              // },
              avoidLabelOverlap: false,
              // label: {
              //   normal: {
              //     show: false,
              //     position: 'center'
              //   },
              //   emphasis: {
              //     show: true,
              //     textStyle: {
              //       fontSize: '30',
              //       fontWeight: 'bold'
              //     }
              //   }
              // },
              // labelLine: {
              //   normal: {
              //     show: false
              //   }
              // },
              data: arr
            }
          ]
        };
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('homeChart2'));
        // 绘制图表
        myChart.setOption(option)
      },
      drawLine5(res) {
        let arr = [
          {value: res.二维码, name: '二维码'},
          {value: res.通行证, name: '通行证'},
          {value: res.云对讲, name: '云对讲'},
        ];
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('homeChart5'));
        // 绘制图表
        myChart.setOption({
          title: {
            text: '访客记录',
            bottom: "0",
            left: "50%",
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
            },
            textAlign: "center",
          },
          color: ['#0263FF', '#8E30FF', '#FF7723'],
          legend: {
            orient: "vertical",
            right: "60px",
            textStyle: {
              color: "#ffffff",
            },
            bottom: "45px",
            data: ['二维码', '通行证', '云对讲']
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          series: [
            {
              name: '访客记录',
              type: 'pie',
              radius: '70%',
              data: arr
            }
          ]
        });
      },
      drawLine6(res) {
        console.log(res);
        let arr1 = [];
        let arr2 = [];
        res.forEach((v, i) => {
          arr1.push(v.bindType);
          arr2.push(v.num);
        });
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('homeChart6'));
        // 绘制图表
        myChart.setOption({
          color: ['#0092F1', '#FF7723', '#8E30FF', '#F9383B'],
          // title: {
          //   left: "20px",
          //   top: "10px",
          //   text: '出入口报警',
          //   textStyle: {
          //     fontWeight: "normal",
          //     fontSize: "14px",
          //   },
          //   color:['#0092F1']
          // },
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow',        // 默认为直线，可选为：'line' | 'shadow'
              lineStyle:{
                color: '#fff'
              }

            },

          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true,
          },
          xAxis: {
            type: 'category',
            data: arr1,
            axisTick: {
              alignWithLabel: true,
              lineStyle:{color:'#fff'}    // x轴刻度的颜色
            },
            textStyle: {
              fontWeight: "normal",
              fontSize: "14px",
              color: "#fff"
            },
            axisLabel:{color:'#fff'},   // x轴字体颜色
            axisLine:{
              lineStyle:{color:'#fff'}    // x轴坐标轴颜色
            },
          },
          yAxis: {
            type: 'value',
            axisLabel : {color:'#fff'},    //y轴字体颜色
            axisLine:{
              lineStyle:{color:'#0C1B2D'}  //y轴坐标轴颜色
            },
            splitArea : {show : false},  //去除网格区域
          },
          series: [{
            name: '出入口报警',
            barWidth: '60%',
            data: arr2,
            type: 'bar'
          }]
        });
      },

    }
  }
</script>

<style scoped lang="scss">
  .home-wrapper {
    height: 100%;
  }

  .home-view {
    height: 100%;
    overflow: auto;
    background-color: #0C1B2D;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    box-sizing: border-box;

    .home-header {
      width: 100%;
      height: 100px;
      font-size: 45px;
      color: #ffffff;

      h3 {
        height: 100%;
        line-height: 100px;
        margin: 0;
      }
    }

    .home-item {
      width: 582px;
      box-sizing: border-box;
      padding-left: 20px;

      p {
        color: #1296db;
        text-align: left;
        padding-left: 10px;
        font-weight: bold;
      }

      .table-box {
        border: 2px solid #666AFF;
        height: 370px;
        box-shadow: 1px 1px 3px #17abe3;
      }

      .table-box-two {
        border: none !important;
        height: 370px;
        display: flex;
        flex-wrap: wrap;
      }
      .table-box-two > div{
        text-align: center;
        line-height: 185px;
        color: #ffffff;
        font-size: 46px;
        position: relative;
        font-weight: bold;
      }
      .online-label{
        font-weight: normal;
        position: absolute;
        left: 0;
        right: 0;
        bottom: 20px;
        font-size: 20px;
        color: #ffffff;
        height: 20px;
        line-height: 20px;
        width: 100%;
        text-align: center;
      }
    }

  }
</style>
