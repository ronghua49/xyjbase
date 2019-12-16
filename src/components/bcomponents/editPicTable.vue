<template>
  <div id="dialog">
    <el-dialog v-dialogDrag :title="title" :visible.sync="dialogVisible"
    :close-on-click-modal="false"
    width="1000px" class="newDialog">
      <template>
        <el-form :rules="yearFormRules" ref="yearForm1" :model="yearForm">
          <el-form-item label="一级" prop="class1">
            <el-select v-model="yearForm.class1" @change="getClass2" clearable>
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="二级" prop="class2">
            <el-select v-model="yearForm.class2" @visible-change="getValue" clearable>
              <el-option
                v-for="item in select2"
                :key="item.value"
                :label="item.label"
                :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="三级" prop="class3">
            <el-input v-model="yearForm.class3" clearable></el-input>
          </el-form-item>
        </el-form>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane
            v-for="item in taps"
            :key="item.id"
            :label="item.label"
            :name="item.label"
            v-if="(item.label != '类别')&&(item.label != 'Total[KCNY]')&&(item.label != 'Total(KCNY)')
            &&(item.label != 'Total(Man-month)')&&(item.label != '选择')">
            <el-form ref="yearForm2" :model="yearForm" >
              <el-form-item
                v-for="items in item.children"
                :key="items.id"
                :label="items.label"
                :prop="items.prop"
                v-if="items.label != '小计'">
                <el-input v-model="yearForm[items.prop]" clearable oninput ="value=value.replace(/[^\d^\.]+/g,'')">
                </el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </template>
      <div slot="footer">
        <el-button @click="dialogVisible = false" size="small">{{$t('action.cancel')}}</el-button>
        <el-button type="primary" @click="confirm" :loading="confirmLoading" size="small">{{$t('action.comfirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import ElFormRenderer from '../../components/XyjRenderer'
import axios from '@/http/axios';
export default {
  name: 'editTable',
  components:{
    ElFormRenderer
  },
  data(){
    return {
      title:"",
      dialogVisible:false,
      activeName:"",
      taps:[],
      options:[
        {value:"劳务费",label:"劳务费"},
        {value:"JLAT",label:"JLAT"},
        {value:"海外出差",label:"海外出差"},
        {value:"国内出差",label:"国内出差"},
      ],
      yearForm:{
        class1:"劳务费",
        class2:"直劳"
      },
      select2:[],
      formAttrs: {
        labelPosition: "right",
        labelWidth: "7em"
      },
      confirmLoading:false,
      dialogFroms:[],
      param:{
        versionId:'',
        row:'',
        url:''
      },
      yearFormRules:{
        class1:[{
          required: true, message: '请选择一级', trigger: 'blur'
        }],
        class2:[
          {required:true, message:'请选择二级', trigger: 'blur'}
        ],
        class3:[
          {required:true, message:'请输入三级', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    confirm(){
      this.$refs.yearForm1.validate((valid) =>{
        if(valid){
          this.loading = true;
          let url = this.param.url;
          let map = this.yearForm;//往后台传参
          map.versionId = this.param.versionId;//需要版本id
          /*if(this.title == "新增"){
            map.picId = this.param.picId;
          }*/
          axios({
            hostUrl: this.global.lyBudgetUrl,
            method: 'post',
            url: url, // 请求地址
            data:map
          }).then((res)=>{
            this.$message({
              type: 'success',
              message: '保存成功！'
            });
            this.loading = false;
            this.dialogVisible = false;
            this.$emit('update');//保存成功后刷新数据
            this.$refs.yearForm1.resetFields();
          }).catch((err)=>{
            this.$message.error(err.message);
          });
        }
      });
    },
    handleClick(){

    },
    show(type,allTableColumns,versionId,row,url){
      this.param.versionId = versionId; // 获取版本id
      this.param.url = url;
      this.dialogVisible = true;
      this.taps = allTableColumns;//表头数据
      let arr =[];
      for (let i=0; i<allTableColumns.length; i++){
        if(allTableColumns[i].children != null){
          if(allTableColumns[i].label != "类别"){
            arr.push(allTableColumns[i].label);
          }
        }
      }
      this.activeName = arr[0];//taps选项卡定义在第一个上
      //是新增（add）还是编辑（edit）
      if(type == "add"){
        this.title = "新增";
        this.yearForm = {};
        //新增需要把所有数据都传过去，没有填写的传默认值("")
        for (let i=0; i<allTableColumns.length; i++){
          if(allTableColumns[i].children != null){
            for (let j=0; j<allTableColumns[i].children.length; j++){
              let value = allTableColumns[i].children[j].prop;//通过prop得到实体类字段名
              this.$set(this.yearForm,value,"");//对象赋值（对象名，键，默认值）
            }
          }
        }
      }else if(type == "edit"){
        this.title = "编辑";
        this.yearForm = row;
      }
    },
    getClass2(value){
      //二级下拉框默认值
      if(value == "劳务费"){
        this.yearForm.class2 = "直劳";
      }else{
        this.yearForm.class2 = "人月";
      }
      //一级劳务费下内容
      let one = [
        {label:"直劳",value:"直劳"},
        {label:"准直",value:"准直"},
        {label:"间接",value:"间接"},
      ];
      //一级JLAT下内容(冲压)
      let other = [
        {label:"人月",value:"人月"},
        {label:"人月-渡航回数",value:"人月-渡航回数"},
        {label:"S/V",value:"S/V"},
        {label:"S/V-渡航回数",value:"S/V-渡航回数"},
      ];
      //其他科室一级JLAT
      let two = [
        {label:"人月",value:"人月"},
        {label:"人月-渡航回数",value:"人月-渡航回数"},
      ];
      //海外出差，国内出差下内容
      let three = [
        {label:"人月",value:"人月"},
        {label:"渡航回数",value:"渡航回数"},
      ];

      if(this.param.url == "/tIcBuStampingPic/save"){//冲压
        if (value == "劳务费") {
          this.select2 = one;
        }else if(value == "JLAT"){
          this.select2 = other;
        }else if(value == "海外出差" || value == "国内出差") {
          this.select2 = three;
        }
      }else {
        if (value == "劳务费") {
          this.select2 = one;
        }else if(value == "JLAT"){
          this.select2 = two;
        }else if(value == "海外出差" || value == "国内出差"){
          this.select2 = three;
        }
      }

    },
    getValue(state){
      //一级劳务费下内容
      let one = [
        {label:"直劳",value:"直劳"},
        {label:"准直",value:"准直"},
        {label:"间接",value:"间接"},
      ];
      //一级JLAT下内容(冲压)
      let other = [
        {label:"人月",value:"人月"},
        {label:"人月-渡航回数",value:"人月-渡航回数"},
        {label:"S/V",value:"S/V"},
        {label:"S/V-渡航回数",value:"S/V-渡航回数"},
      ];
      //其他科室一级JLAT
      let two = [
        {label:"人月",value:"人月"},
        {label:"人月-渡航回数",value:"人月-渡航回数"},
      ];
      //海外出差，国内出差下内容
      let three = [
        {label:"人月",value:"人月"},
        {label:"渡航回数",value:"渡航回数"},
      ];
      let value = this.yearForm.class1;
      if(state){
        if(this.param.url == "/tIcBuStampingPic/save"){
          if (value == "劳务费") {
            this.select2 = one;
          }else if(value == "JLAT"){
            this.select2 = other;
          }else if(value == "海外出差" || value == "国内出差") {
            this.select2 = three;
          }
        }else {
          if (value == "劳务费") {
            this.select2 = one;
          }else if(value == "JLAT"){
            this.select2 = two;
          }else if(value == "海外出差" || value == "国内出差"){
            this.select2 = three;
          }
        }

      }
    }
  }
}
</script>
<style lang="scss" scoped>
  #dialog {
    .el-form {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;

      .el-form-item {
        width: 25%;
        margin-bottom: 20px !important;
        .el-input{
          width: 185px;
        }
        .el-select{
          width: 185px;
        }
      }
    }
    .el-form:after {
      content: '';
      width: 315px;
    }
  }
</style>

