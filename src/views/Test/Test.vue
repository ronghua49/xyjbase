<template>
  <div>
    <el-row>
      <el-button type="primary" @click="but1">加法</el-button>
      <el-button type="primary" @click="but2">减法</el-button>
      <el-button type="primary" @click="but3">乘法</el-button>
      <el-button type="primary" @click="but4">除法</el-button>
      <el-button type="primary" @click="getPrintData">getPrintData</el-button>
      <el-button type="primary" @click="printOn">开机</el-button>
      <el-button type="primary" @click="printOff">关机</el-button>
      <el-button type="primary" @click="setKey">存储Key</el-button>
      <el-button type="primary" @click="getKey">获取Key值</el-button>
      <el-button type="primary" @click="clearPrintData">clearPrintData</el-button>
      <el-button type="primary" @click="setCharging">setCharging(计费标准接口)</el-button>
      <el-button type="primary" @click="printPDF">打印</el-button>
      <el-button type="primary" @click="deleteFiles">删除目录下所有文件</el-button>
    </el-row>
    <div>
      <el-input v-model="input" placeholder="加法"></el-input>
      <el-input v-model="valuesub" placeholder="减法"></el-input>
      <el-input v-model="valuemul" placeholder="加法"></el-input>
      <el-input v-model="valuediv" placeholder="减法"></el-input>
      <el-input v-model="PrintData" placeholder="获取打印数据"></el-input>
      <el-input v-model="num" placeholder="触发次数"></el-input>
      <el-input v-model="onvalue" placeholder="开机返回值"></el-input>
      <el-input v-model="offvalue" placeholder="关机返回值"></el-input>
      <el-input v-model="pdata" placeholder="打印触发数据"></el-input>
      <el-input v-model="setKeyvalue" placeholder="存储Key"></el-input>
      <el-input v-model="getKeyvalue" placeholder="获取Key值"></el-input>
      <el-input v-model="clearPrintDatavalue" placeholder="clearPrintData"></el-input>
      <el-input v-model="setChargingValue" placeholder="计费标准"></el-input>
      <el-input v-model="folder" placeholder="要删除的文件夹路径，相对路径"></el-input>
      <div>
        <el-row :gutter="20">
          <!--Copies,Orientation,Duplex,Color,PaperSize,Scale,DefaultSource,PrintQuality,FileUrl-->
          <el-col :span="6">打印份数<el-input v-model="Copies" placeholder="打印份数"></el-input></el-col>
          <el-col :span="6">纸张方向;1(纵向)2(横向)<el-input v-model="Orientation" placeholder="纸张方向;1(纵向)2(横向)"></el-input></el-col>
          <el-col :span="6">纸张是否为双面打印输出;1-单面 2-水平 3-垂直(需打印支持)<el-input v-model="Duplex" placeholder="纸张是否为双面打印输出;1-单面 2-水平 3-垂直(需打印支持)："></el-input></el-col>
          <el-col :span="6">1-彩色 2-单色<el-input v-model="Color" placeholder="1-彩色 2-单色"></el-input></el-col>
          <el-col :span="6">纸张大小(A3:8,A4:9,A5:11,B4:12,B5:13 <el-input v-model="PaperSize" placeholder="纸张大小(A3:8,A4:9,A5:11,B4:12,B5:13)"></el-input></el-col>
          <el-col :span="6">打印输出的缩放系数<el-input v-model="Scale" placeholder="打印输出的缩放系数"></el-input></el-col>
          <el-col :span="6">送纸的默认纸盒7：自动选择 / 261：手动纸盒 / 257：纸盒1 / 258：纸盒2<el-input v-model="DefaultSource" placeholder="送纸的默认纸盒7：自动选择 / 261：手动纸盒 / 257：纸盒1 / 258：纸盒2"></el-input></el-col>
          <el-col :span="6">打印机的分辨率;4-高品质 3-中品质 2-低品质 1-草稿<el-input v-model="PrintQuality" placeholder="打印机的分辨率;4-高品质 3-中品质 2-低品质 1-草稿"></el-input></el-col>
        </el-row>
      </div>
      <div>
        下载文件URL
        <el-input v-model="FileUrl" placeholder="下载文件URL"></el-input>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "test",
    data() {
      return {
        setChargingValue: '[{"papername":"A3","color2side":"彩色单面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"01","color2sideDexvalue":"03"},{"papername":"A3","color2side":"彩色双面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"01","color2sideDexvalue":"04"},{"papername":"A3","color2side":"黑白单面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"01","color2sideDexvalue":"01"},{"papername":"A3","color2side":"黑白双面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"01","color2sideDexvalue":"02"},{"papername":"A4","color2side":"彩色单面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"02","color2sideDexvalue":"03"},{"papername":"A4","color2side":"彩色双面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"02","color2sideDexvalue":"04"},{"papername":"A4","color2side":"黑白单面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"02","color2sideDexvalue":"01"},{"papername":"A4","color2side":"黑白双面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"02","color2sideDexvalue":"02"},{"papername":"A5","color2side":"彩色单面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"10","color2sideDexvalue":"03"},{"papername":"A5","color2side":"彩色双面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"10","color2sideDexvalue":"04"},{"papername":"A5","color2side":"黑白单面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"10","color2sideDexvalue":"01"},{"papername":"A5","color2side":"黑白双面","ysprice":0.02,"areaprice":0.0,"price":0.02,"paperDexvalue":"10","color2sideDexvalue":"02"},{"papername":"B4","color2side":"彩色单面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"04","color2sideDexvalue":"03"},{"papername":"B4","color2side":"彩色双面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"04","color2sideDexvalue":"04"},{"papername":"B4","color2side":"黑白单面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"04","color2sideDexvalue":"01"},{"papername":"B4","color2side":"黑白双面","ysprice":0.01,"areaprice":0.0,"price":0.01,"paperDexvalue":"04","color2sideDexvalue":"02"},{"papername":"B5","color2side":"彩色单面","ysprice":0.12,"areaprice":0.0,"price":0.12,"paperDexvalue":"08","color2sideDexvalue":"03"},{"papername":"B5","color2side":"彩色双面","ysprice":0.4,"areaprice":0.0,"price":0.4,"paperDexvalue":"08","color2sideDexvalue":"04"},{"papername":"B5","color2side":"黑白单面","ysprice":12.0,"areaprice":0.0,"price":12.0,"paperDexvalue":"08","color2sideDexvalue":"01"},{"papername":"B5","color2side":"黑白双面","ysprice":1.0,"areaprice":0.0,"price":1.0,"paperDexvalue":"08","color2sideDexvalue":"02"}]',
        clearPrintDatavalue: '',
        setKeyvalue: '{"name":"BeJson","url":"http://www.bejson.com","page":88,"isNonProfit":true,"address":{"street":"科技园路.","city":"江苏苏州","country":"中国"},"links":[{"name":"Google","url":"http://www.google.com"},{"name":"Baidu","url":"http://www.baidu.com"},{"name":"SoSo","url":"http://www.SoSo.com"}]}',
        getKeyvalue: '',
        input: '',
        valuesub: '',
        valuemul: '',
        valuediv: '0',
        PrintData: 'admin',
        num: '0',
        onvalue: '',
        offvalue: '',
        pdata: '',
        Copies: '1',
        Orientation:'1',
        Duplex:'1',
        Color:'2',
        PaperSize:'9',
        Scale:'100',
        DefaultSource:'7',
        PrintQuality:'-1',
        FileUrl:'http://vip.ow365.cn/?i=19469&info=2&furl=http://119.3.190.205:8000/group1/M00/00/08/wKgBDV3Lu6GAdw0fAAGzE6_jgSs695.doc',
        folder:''
      }
    },
    methods:{
      but1: function () {
        this.input = window.AppInterace.add(1.01,2.0555);
      },
      but2: function () {
        this.valuesub = window.AppInterace.sub(10,2.0555);
      },
      but3: function () {
        this.valuemul = window.AppInterace.mul(10,2.0555);
      },
      but4: function () {
        this.valuediv = window.AppInterace.div(10,2.0555);
      },
      getPrintData: function () {
        let value = window.AppInterace.getPrintData(this.PrintData);
        let bbb = JSON.parse(value);
        this.PrintData = value;
      },
      printOn: function () {
        window.AppInterace.printOn('admin');
      },
      printOff: function () {
        window.AppInterace.printOff();
      },
      setKey: function () {
        this.setKeyvalue = window.AppInterace.setKey('test',this.setKeyvalue);
      },
      getKey: function () {
        this.getKeyvalue =  window.AppInterace.getKey('test');
      },
      clearPrintData: function () {
        //打印数据ID
        this.clearPrintDatavalue = window.AppInterace.clearPrintData('["admin"]');
      },
      setCharging: function () {
        this.setChargingValue = window.AppInterace.setCharging(this.setChargingValue);
      },
      //PDF打印 Copies,Orientation,Duplex,Color,PaperSize,Scale,DefaultSource,PrintQuality,FileUrl
      printPDF: function () {
        let filetype = this.FileUrl.substr(-4);
        if(filetype !== '.jpg'){
          filetype= '.pdf';
        }
        window.AppInterace.printPDF(this.Copies,this.Orientation,this.Duplex,this.Color,this.PaperSize,this.Scale,this.DefaultSource,this.PrintQuality,this.FileUrl,filetype);
      },
      /**
       *宋建国：2019/11/15 11:25
       *描述：
       *版本：1.0
       */
      deleteFiles: function () {
        if(!this.$tu.isBlank(this.folder)){
          window.AppInterace.deleteFiles(this.folder);
        }
      }
    },
    mounted(){
      //心跳触发事件
      window['printHandle'] = data => {
        let bbb = JSON.parse(data);
        this.pdata = bbb.seq;
      }
      //心跳触发事件
      window['heartbeat'] = () => {
        this.valuediv = this.valuediv + 1;
      }
      //打印机开机回调函数
      window['recOn'] = data => {
        this.onvalue = data;
      }
      //打印机关机回调函数
      window['recOff'] = data => {
        this.offvalue = data;
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
