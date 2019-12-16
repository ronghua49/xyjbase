<template>
  <div id="dialog">
    <el-dialog v-dialogDrag title="编辑表头" :visible.sync="dialogVisible" 
    :close-on-click-modal="false" 
    @close="$refs.dialogForm.resetFields()" 
    width="700px" class="newDialog">
      <el-form-renderer class="senior-serach-style" :content="dialogFroms" ref="dialogForm" v-bind="formAttrs">
        <!--@slot 额外的弹窗表单内容, 当form不满足需求时可以使用 -->
        <slot name="form"></slot>
      </el-form-renderer>
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
      dialogVisible:false,
      formAttrs: {
        labelPosition: "right",
        labelWidth: "7em"
      },
      confirmLoading:false,
      dialogFroms:[],
      param:{
        versionId:'',
        tablename:''
      }
    }
  },
  methods: {
    confirm(){
      this.dialogVisible = false;
      let data = Object.assign({},this.$refs.dialogForm.getFormValue());
      let arr = [];
      for(let key in data){
        var obj = {
          prop: key,
          label: data[key],
          versionid: this.param.versionId,
          tablename: this.param.tablename
        }
        arr.push(obj)
      }
      axios({
        hostUrl: this.global.lyBaseUrl,
        method: 'post',
        url: "/sysTableinfo/saveHeaderModify", // 请求地址
        data:arr
      }).then((res)=>{
        if (res.code == 200 && res.data != 0) this.$emit('update');
      }).catch((err)=>{
        this.$message.error(err.message);
      });
    },
    show(allTableColumns,id,name){
      this.param.versionId = id; // 获取版本id
      this.param.tablename = name; // 获取实体类名称
      this.dialogVisible = true;
      // 表单所需数据
      let dialogFrom = allTableColumns.filter(x => x.headermodify == 1);
      let obj = {};
      dialogFrom.forEach(x => {
        obj[x.prop] = x.label 
      })
      this.$nextTick(() => {
        // 填充表单数据
        this.$refs.dialogForm.updateForm(obj)
      })
    },
    convertInput(column){
      return {
        $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
        $type: "input", // 类型，element 提供的所有表单类型，即 el-xxx
        label: column.label, // el-form-item上的属性,
        $el: {
          placeholder: "请输入"+column.label,
          style: 'width: 150px' ,
          disabled: false,
          "show-word-limit": true,
          clearable: true,
        },
        $options: [],
        $default: "", //默认值
        rules: {}
      }
    },
    getdialogFrom(allTableColumns){
      let index = -1;
      // 恢复默认
      this.dialogFroms = [];
      let arr = [];
      // 过滤需要编辑的表头
      let dialogFrom = allTableColumns.filter(x => x.headermodify == 1);
      dialogFrom.forEach((column, idx) => {
        index = index+1;
        if (column.fieldtype === "selection") return;
        let tmpInput = this.convertInput(column);
        tmpInput.$el.disabled =  column.edit == 1 ? false : true;
        if(tmpInput.$el.disabled){
          tmpInput.$el.placeholder = "系统自动生成"
        }
        tmpInput.$el.style = 'width: 100%';
        tmpInput.$editshow = column.editshow;
        let tmpWidth = 0;
        this.dialogFroms.push(tmpInput);
      });
    }
  }
}
</script>
<style lang="scss" scoped>
  #dialog{
  /deep/ .el-form-item{margin-bottom:10px!important;}
    .el-form {
	  display: flex;
    flex-flow: row wrap;
    justify-content:space-around;
	}
  .el-form:after{
    content:'';
    width: 315px;
  }
  .senior-serach-style {
      text-align:left;
      .el-form-item{
        margin-bottom: 0!important;
        display: flex;
        align-items: center;
      }
      &>div>div{
        display: flex;
        &>div{
          display: flex;
          align-items: center;
          label{
            white-space: nowrap;
          }
        }
        &>div:nth-last-of-type(1)>div{
          min-width: 40px!important;
        }
      }
    }
  }
</style>
