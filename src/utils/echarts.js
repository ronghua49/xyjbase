import axios from "@/http/axios";
import Echarts from 'echarts'
const global = require('@/utils/global.js');
function Drawing(id,_data,Series){
  // 基于准备好的dom，初始化echarts实例
  let moveChart = Echarts.init(document.getElementById(id))
  // 绘制图表
  let option={
    color:function(params){
      return _data[params.dataIndex].color
    },
    title:{
      text: '推移图'
    },
    animation: false,
    tooltip : {
      trigger: 'axis',       // 触发类型
      axisPointer : {            // 坐标轴指示器，坐标轴触发有效
        type : 'cross',        // 默认为直线，可选为：'line' | 'shadow'
      },
      // alwaysShowContent:true,
      // triggerOn:'none',
      formatter: function (params) {  // 设定悬浮提示框数据
        let tar = '';
        if(params[0].data.items){
          params[0].data.items.forEach(x => {
            tar += '<div style="text-align:left">'+x.name+':'+x.money+'</div>'; // 显示多条数据
          });
        }else {
          tar = '<div>'+params[0].data.name+':'+params[0].data.money+'</div>' 
        }
        return tar;
     }
  },
  // legend: { 
  //   itemWidth:15, // 图例宽度
  //   data:(function(){
  //     let arr =  _data.map(x => {
  //       return x.name;
  //     });
  //     arr.splice(10,0,'');
  //     return arr;
  //   })()
  // },
  grid: {
    top:'27%',
    left:'2%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    show : true,
    padding:20,
    feature : {
        mark : {show: true},
        // restore : {show: true},
        saveAsImage : {show: true}
    }
  },
  xAxis: {
    type : 'category',
    splitLine: {show:false},
    axisLabel:{interval:0},
    data : function(){
      return  _data.map(x => {return x.name}) // X轴显示的名字
    }()
  },
  yAxis: {
    axisLabel:{ //y轴数值
      show:false
    },
    axisTick:{ // 刻度线
      show:false
    },
    splitLine:{
      show:false
    },
    show:false,
    type : 'value',
    min:0 // 不显示负轴
  },
  series: (function(){
    let arr = [
      {
        name: '',
        type: 'bar',
        stack: '总量',
        itemStyle: {
          normal: {
            barBorderColor: 'rgba(0,0,0,0)',
            color: 'rgba(0,0,0,0)'
          },
          emphasis: {
            barBorderColor: 'rgba(0,0,0,0)',
            color: 'rgba(0,0,0,0)'
          }
        },
        data:(function(){
          let data = JSON.parse(JSON.stringify(_data)); 
          // 每个柱状图开始的位置数组
          let arr = [];
          for (let i = 0, l = _data.length; i < l; i++) {
            if (i === 0) arr[i] = _data[i].money;
            else arr[i] = arr[i-1] + _data[i].money;
          }
          arr.unshift(0);
          arr.pop();
          // 最后五个数据处理
          // if(arr.length > 0){
          //   _data.length>0 
          //   ? _data[arr.length-3].money > _data[arr.length-5].money //如果申请值小于后边的值 差异的值从申请值顶部开始
          //   ? arr[arr.length-4] = _data[arr.length-5].money 
          //   : arr[arr.length-4] = _data[arr.length-3].money 
          //   :'';
          //   _data.length>0 
          //   ? _data[arr.length-1].money > _data[arr.length-5].money
          //   ? arr[arr.length-2] = _data[arr.length-5].money
          //   : arr[arr.length-2] = _data[arr.length-1].money 
          //   :'';
          //   arr[arr.length-1] = 0;
          //   arr[arr.length-3] = 0;
          //   arr[arr.length-5] = 0;
          // }
          // 把tooltip需要展示的多条数据赋值给data
          let index = 0;
          data = data.map((x,i) => {
            // 当数值为负时，往下显示
            if(x.money < 0){
              // if(i<data.length-5) arr[i] = arr[i] - Math.abs(x.money); 
              if(data[i-1].type == 2) index = i-1;
              data[i-1].type == 0 || data[i-1].type == 2 
              ? arr[i] = Math.abs(data[index].money) - Math.abs(x.money) 
              : arr[i] = arr[i] - Math.abs(x.money);
            }
            x.value = arr[i]; // 必须要有value字段
            if(x.type == 0 || x.type == 2) x.value = 0;
            return x;
          })
          return data
        })()
      }
    ]
    return arr.concat(Series); // 动态添加所有的数据
  })()
  }
  moveChart.setOption(option,true);
  // moveChart.dispatchAction({
  //   type:'showTip',
  //   batch:[{
  //     // type:'showTip',//默认显示江苏的提示框
  //     seriesIndex: 0,//这行不能省
  //     dataIndex:8,
  //   },{
  //     // type:'showTip',//默认显示江苏的提示框
  //     seriesIndex: 0,//这行不能省
  //     dataIndex:1
  //   }]
  // });
}
export function getMessage(tIcBuStampingMaterialDI,val,idName){ // 获取推移图基本信息
  return new Promise((res,rej) => {
    res(
      
      axios({
        hostUrl: global.lyBudgetUrl,
        url: '/'+tIcBuStampingMaterialDI+'/getMaterialTdData?versionId='+val,// 获取基础数据
        method: 'get',
      }).then((res)=>{
        if(res.length>0){
          let data = res;
          let Series = res.map((x,i) => { // 报表series数据
            let obj ={};
            obj.name = x.name;
            obj.type = 'bar';
            obj.stack = '总量';
            obj.barWidth = 30; // 柱宽
            obj.label = {
                normal: {
                  show: true,
                  position: 'top',
                  formatter:function(res){
                    // 格式化上方显示的数值
                    if(x.money<0) res.data = '-'+res.data
                    return res.data
                  }
                }
            };
            let arr = new Array(res.length);
            arr.fill('-');
            obj.data = arr;
            obj.data[i] = Math.abs(x.money); // 柱状图上方显示的数值
            return obj;
          })
          Drawing(idName,data,Series);
        }else {
          let data = [];
          let Series = [];
          Drawing(idName,data,Series);
        }
      }).catch((err) =>{
        this.$message.error(err.message);
      })
    );
  })
}
// 导出echarts图片
export function getImg(data){
  let arr = [];
  return new Promise((res,rej)=>{
    data.forEach(async (x,index) => {
      await getMessage(x.tableName,x.versionId,x.domName).then(() => {
        // let image = Echarts.init(document.getElementById(x.domName)).getDataURL();
        let image = downloadImpByChart(x.domName);
        arr.push(image);
      })
      if(arr.length == data.length) {
        res(arr);
      }
    })
  })
}
function downloadImpByChart(chartId) {
  var myChart = Echarts.getInstanceByDom(document.getElementById(chartId));
  var url = myChart.getConnectedDataURL({
    pixelRatio: 3,　　//导出的图片分辨率比率,默认是1
    backgroundColor: 'transparent',　　//图表背景色
    excludeComponents:[　　//保存图表时忽略的工具组件,默认忽略工具栏
    'toolbox'
    ],
    type:'png'　　//图片类型支持png和jpeg
  });
  return url;
};