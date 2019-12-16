<template>
  <!-- 选项卡-->
  <div>
    <el-tabs type="border-card" v-model="tabName" @tab-click="handleClick">
      <el-tab-pane label="车型预算模板" name="first">
        <template>
          <xyj-table ref="tIcDbTemplate1" id="1" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation1">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
      <el-tab-pane label="冲压预算模板" name="sec">
        <template>
          <xyj-table ref="tIcDbTemplate2" id="2" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation2">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
      <el-tab-pane label="车体预算模板" name="three">
        <template>
          <xyj-table ref="tIcDbTemplate3" id="3" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation3">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
      <el-tab-pane label="涂装预算模板" name="four">
        <template>
          <xyj-table ref="tIcDbTemplate4" id="4" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation4">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
      <el-tab-pane label="树脂预算模板" name="five">
        <template>
          <xyj-table ref="tIcDbTemplate5" id="5" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation5">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
      <el-tab-pane label="总装预算模板" name="six">
        <template>
          <xyj-table ref="tIcDbTemplate6" id="6" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation6">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
      <el-tab-pane label="电池预算模板" name="seven">
        <template>
          <xyj-table ref="tIcDbTemplate7" id="7" v-bind="tIcDbTemplate">
            <template slot="xyjcontainer">
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="reoperation7">{{$t('action.Reoperation')}}</el-button>
              <el-button type="primary" style="padding: 7px;" size="mini"
                         @click="download">{{$t('action.downloadTemplate')}}</el-button>
            </template>
          </xyj-table>
        </template>
      </el-tab-pane>
    </el-tabs>
    <el-dialog
    v-dialogDrag
      :title="title"
      :visible.sync="dialogVisible"
      width="30%"
      >
      <el-form ref="form" :rules="rules" :model="form">
        <el-form-item prop="file">
          <template slot-scope="scope">
            <el-upload
              ref="upload"
              class="upload-demo"
              :multiple=false
              drag
              action="https://jsonplaceholder.typicode.com/posts/"
              :file-list="fileList"
              :on-change="handleChange"
              :on-exceed="handleExceed"
              :http-request="fileUpload">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
          </template>
        </el-form-item>
        <el-form-item prop="memo">
          <el-input v-model="form.memo" clearable placeholder="备注："></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button style="padding: 7px;" size="mini" @click="dialogVisible = false">{{$t('action.cancel')}}</el-button>
        <el-button style="padding: 7px;" size="mini" type="primary" @click="submit">{{$t('action.comfirm')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import axios from "@/http/axios";
    export default {
        name: "ExcelTemplate",
      mounted(){
          this.handleClick();
      },
      data(){
        return{
          tabName:"first",//选项卡默认展示第一个
          dialogVisible: false,
          title:"",
          form:{
            file:'',
            memo:'',
          },
          fileList:[],
          templateNo:'',
          rules:{
            file:{required:true,message:"请选择上传的文件",trigger: 'blur'},
          },
          tIcDbTemplate:{
            hasOperation: false,//操作列是否显示
            hasDelete: false, //删除
            hasNew: true, //新增
            hasView: false, //查看
            hasEdit: false, //编辑
            //工程字段
            formatters: {
              projectType:function (row, column, cellValue) {
                switch (cellValue) {
                  case 1:
                    return "冲压";
                    break;
                  case 2:
                    return "焊装";
                    break;
                  case 3:
                    return "树脂";
                    break;
                  case 4:
                    return "涂装";
                    break;
                  case 5:
                    return "总装";
                    break;
                  case 6:
                    return "电池";
                    break;
                  case 7:
                    return "车型";
                    break;
                  default:
                    return cellValue;
                }
              },
              materialType:function (row, column, cellValue) {
                switch (cellValue) {
                  case 1:
                    return "直材清单";
                    break;
                  case 2:
                    return "直劳清单";
                    break;
                  case 3:
                    return "投资清单";
                    break;
                  case 4:
                    return "PIC劳务清单";
                    break;
                  case 5:
                    return "PIC经费清单";
                    break;
                  case 6:
                    return " 直材推移图";
                    break;
                  case 7:
                    return "直劳推移图";
                    break;
                  case 8:
                    return "投资推移图";
                    break;
                  case 9:
                    return "投资推移图(0基点)";
                    break;
                  case 10:
                    return "PIC推移图";
                    break;
                  case 11:
                    return "PIC推移图(0基点)";
                    break;
                  default:
                    return cellValue;
                }
              }
            },
            //el-table设置
            tableAttrs:{
              //height: "500px",
              width: "800px",
            },
            //el-table事件设置
            tableOns: {
              'row-click': this.rowClick,
            },
            //表格中的操作列设置
            operationAttrs:{
              width: "100px"
            },
            //admin微服务网址
            hostUrl: this.global.lyBaseUrl,
            //表初始化关键字，一般为表名
            tableName: "tIcDbTemplate",
            //查询时的排序字段materialType,
            sorter: [
              {direction: "ASC",property: "materialType"},
              {direction: "ASC",property: "yearType"}
            ],
            paginationSize: 30,
            //查询时固定查询条件，对象下标必须从100开始向上加
            customQuery: {100:{field100:"projectType",gx100:"EQUAL",value100: null, andor100:"AND"}},
            //业务数据微服务,查询与高级查询
            bQuery: {
              hostUrl: this.global.lyBaseUrl,
              url: "tIcDbTemplate/query",
              method: 'post',
              notCondition: false
            },
          },
        }
      },
      methods:{
          rowClick (row, column, event) {
            this.form.memo = row.memo;
            this.templateNo = row.templateNo;
          },
        fileUpload(file){
          if(file.file == undefined){
            this.form.file = file[0];
          }else {
            this.form.file = file.file;
          }
        },
        reoperation1(){
          let select = this.$refs.tIcDbTemplate1.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='车型预算模板';
          }
        },
        reoperation2(){
          let select = this.$refs.tIcDbTemplate2.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='冲压预算模板';
          }
        },
        reoperation3(){
          let select = this.$refs.tIcDbTemplate3.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='车体预算模板';
          }
        },
        reoperation4(){
          let select = this.$refs.tIcDbTemplate4.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='涂装预算模板';
          }
        },
        reoperation5(){
          let select = this.$refs.tIcDbTemplate5.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='树脂预算模板';
          }
        },
        reoperation6(){
          let select = this.$refs.tIcDbTemplate6.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='总装预算模板';
          }
        },
        reoperation7(){
          let select = this.$refs.tIcDbTemplate7.selected;
          if(select <= 0){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行重新操作！'
            });
          }else {
            this.dialogVisible = true;
            this.title='电池预算模板';
          }
        },
        handleChange(file, fileList){
          this.fileList = fileList.slice(-1);
        },
        handleExceed(file, fileList){
         this.fileUpload(file);
        },
        download(){
          let select = this.templateNo;
          if(select == ""){
            this.dialogVisible = false;
            this.$message({
              type: 'info',
              message: '请选择数据进行模板下载！'
            });
          }else {
            let fd = new FormData();
            fd.append("templateNo",this.templateNo);
            axios({
              hostUrl: this.global.lyBaseUrl,
              url: "tIcDbTemplate/downloaddExcel",
              method: "post",
              data: fd
            }).then((res) => {
              const blob = new Blob([res.data],{type: 'application/vnd.ms-excel;charset=utf-8'});
              if ('download' in document.createElement('a')) { // 非IE下载
                const elink = document.createElement('a')
                elink.download = decodeURIComponent(res.filename);
                elink.style.display = 'none'
                elink.href = URL.createObjectURL(blob)
                document.body.appendChild(elink)
                elink.click()
                URL.revokeObjectURL(elink.href) // 释放URL 对象
                document.body.removeChild(elink)
              } else { // IE10+下载
                navigator.msSaveBlob(blob, decodeURIComponent(res.filename));
              }
            }).catch((err) =>{
              this.$message.error(err.message)
            })
          }
        },
        handleClick(){
          if(this.tabName == "first"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:7});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate1.getList();
            });
          }else if(this.tabName == "sec"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:1});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate2.getList();
            });
          }else if(this.tabName == "three"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:2});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate3.getList();
            });
          }else if(this.tabName == "four"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:4});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate4.getList();
            });
          }else if(this.tabName == "five"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:3});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate5.getList();
            });
          }else if(this.tabName == "six"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:5});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate6.getList();
            });
          }else if(this.tabName == "seven"){
            this.tIcDbTemplate.customQuery[100] = Object.assign({}, this.tIcDbTemplate.customQuery[100],{value100:6});
            this.$nextTick(() => {
              this.$refs.tIcDbTemplate7.getList();
            });
          }
        },
        submit(){
          let fd = new FormData();
          fd.append("file",this.form.file);
          fd.append("templateNo",this.templateNo);
          fd.append('memo',this.form.memo);
          axios({
            hostUrl:this.global.lyBaseUrl,
            method:"post",
            url:"/tIcDbTemplate/uploadTemplate",
            data:fd
          }).then((res)=>{
            console.log(res)
            this.$message({
              type: 'success',
              message: '操作成功！'
            });
            this.dialogVisible = false;
            this.handleClick();
          }).catch((err)=>{
            this.$message.error(err.message)
          })
        }
      }
    }
</script>

<style scoped lang="scss">
  .el-input{
    width: 360px;
  }
</style>
