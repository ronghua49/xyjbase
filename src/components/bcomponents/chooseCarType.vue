<template>
  <div>
    <el-dialog
      v-dialogDrag
      style="text-align: left"
      title="选择车型"
      :visible.sync="dialogVisible"
      width="30%">
      <el-form label-position="right" size="small" class="demo-ruleForm" ref="carForm" :model="carForm" label-width="10px">
        <el-form-item prop="carType">
          <el-select filterable clearable v-model="carForm.carType" placeholder="选择车型" @change="chooseStage2">
            <el-option v-for="item in ctypes"
                       :key="item.ctype"
                       :value="item.ctype"
                       :label="item.ctype"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="stage">
          <el-select filterable clearable v-model="carForm.stage" placeholder="选择阶段" @change="chooseVersion2">
            <el-option v-for="item in stages"
                       :key="item.stage"
                       :value="item.stage"
                       :label="item.stage"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="versionId">
          <el-select filterable clearable v-model="carForm.versionId" placeholder="选择版本" @change="selectedVersion">
            <el-option v-for="item in editions"
                       :key="item.versionId"
                       :value="item.versionId"
                       :label="item.version+'/'+item.factoryName"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button style="padding: 7px;" size="mini" @click="dialogVisible = false">{{$t('action.cancel')}}</el-button>
      <el-button style="padding: 7px;" size="mini" type="primary" @click="carConfirm" :loading="loadState">{{$t('action.comfirm')}}</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
  import axios from "@/http/axios";
  export default {
    name: "chooseCarType",
    //props: ["passparam","paramsTrans"],
    props:{
      paramsTrans:{
        type: Object,
        default(){
          return {}
        }
      },
      onOk: {
        type: Function
      }
    },

    data() {
      return {
        dialogVisible: false,//弹出框显示
        loadState: false,//是否加载
        carForm: {
          carType: "",//车型
          stage: "",//阶段
          versionId:''//版本
        },
        ctypes:"",//车型列表
        stages:"",//阶段列表
        editions:"",//版本列表
      }

    },
    created () {

    },
    methods: {
      open(state){
        this.dialogVisible = state;
        this.chooseCarType();
      },
      //选择车型
      chooseCarType(){
        //接受父组件传过来的请求
        var params = this.paramsTrans;
        axios(params).then((res)=>{
          this.ctypes = res;//选择车型
          //默认值
          this.carForm.carType = res[0].ctype;
          this.carForm.stage = res[0].stages[0].stage;
          //主版本ID
          let version = res[0].stages[0].versions;
          this.carForm.versionId = version[0].versionId;
          //车型
          let basecar = params.confirmParms.basecar;
          //阶段
          let basestage = params.confirmParms.basestage;
          //基点车ID
          let baseCarVersionId = params.confirmParms.baseCarVersionId;

          //默认值是基点车，如果没有基点车默认第一个值
          for(let a=0; a<res.length; a++){
            if(basecar == res[a].ctype){
              this.carForm.carType = res[a].ctype;
              for(let b=0; b<res[a].stages.length; b++){
                if(basestage == res[a].stages[b].stage){
                  this.carForm.stage = res[a].stages[b].stage;
                  for(let c=0; c<res[a].stages[b].versions.length; c++){
                    if(baseCarVersionId == res[a].stages[b].versions[c].versionId){
                      this.carForm.versionId = res[a].stages[b].versions[c].versionId;
                    }
                  }
                }
              }
            }
          }
          this.chooseStage();
        }).catch((err) =>{
          this.$message.error(err.message)
        })

      },
      //自动出现
      //选择阶段
      chooseStage(){
        let carType = this.carForm.carType;
        this.stages = this.ctypes.reduce((final, item, index, arr)=>{
          if(item.ctype == carType){
            final = item.stages;
          }
          return final
        },[]);
        this.chooseVersion();
      },
      //选择版本
      chooseVersion(){
        let stage = this.carForm.stage;
        this.editions = this.stages.reduce((final, item, index, arr)=>{
          if(item.stage == stage){
            final = item.versions;
          }
          return final
        },[]);
      },
      //手动选择
      //选择阶段
      chooseStage2(){
        let carType = this.carForm.carType;
        this.stages = this.ctypes.reduce((final, item, index, arr)=>{
          if(item.ctype == carType){
            this.carForm.stage = item.stages[0].stage;
            this.carForm.versionId = item.stages[0].versions[0].versionId;
            final = item.stages;
          }
          return final
        },[]);
        this.chooseVersion2();
      },
      //选择版本
      chooseVersion2(){
        let stage = this.carForm.stage;
        this.editions = this.stages.reduce((final, item, index, arr)=>{
          if(item.stage == stage){
            this.carForm.versionId = item.versions[0].versionId;
            final = item.versions;
          }
          return final
        },[]);
      },
      //判断是否是基点车
      selectedVersion(){
        //选择的versionId
        let versionId = this.carForm.versionId;
        //判断是否是基点车
        //接受父组件的请求
        var params = this.paramsTrans.confirmParms;
        //基点车ID
        let baseCarVersionId = params.baseCarVersionId;
        //不是基点车
        if(versionId == ""){

        }else if(versionId != baseCarVersionId){
          this.$confirm('你所选车型不是基点车，是否进行?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(()=>{

          }).catch(()=>{
            this.$message({
              type: 'info',
              message: '已取消'
            });
            this.carForm.versionId = "";
          })
        }
      },
      //确定
      carConfirm(){
        //判断下拉框是否选择
        if(this.carForm.carType == ""){
          this.$message.error("请选择车型")
        }else if(this.carForm.stage == ""){
          this.$message.error("请选择阶段")
        }else if(this.carForm.versionId == ""){
          this.$message.error("请选择版本")
        }else {
          //接受父组件的请求
          var params = this.paramsTrans.confirmParms;
          //基点车ID
          let baseCarVersionId = params.baseCarVersionId;
          //主版本ID
          let versionId = params.params.versionId;
          if(versionId.length<=0){
            this.$message.error("主版本ID不能为空");
            return;
          }else if(baseCarVersionId.length<=0){
            this.$message.error("基点车ID不能为空");
            return;
          }else {
            //选择车型的版本ID
            let baseVersionId = this.carForm.versionId;
            if(baseVersionId.length<=0){
              this.$message.error("版本ID不能为空");
              return;
            }else {
              //给请求参数赋值
              params.params.baseVersionId = baseVersionId;
            }
            this.loadState = true;
            axios(params).then((res) =>{
              //传参数
              if(this.onOk){
                this.onOk(baseVersionId);
              }
              this.dialogVisible = false;
              this.loadState = false;
              this.$refs.carForm.resetFields();
            }).catch((err) =>{
              this.$message.error(err.message);
              this.loadState = false;
            }).finally((e) =>{
            });
          }
        }
      },
    }
  }
</script>

<style scoped="scoped" lang="scss">
  .el-select {
    width: 100%;
  }
  .el-form {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    .el-form-item {
      width: 33.3333%;
      /*.el-date-editor.el-input {
        width: 100%;
      }*/
    }
  }
</style>
