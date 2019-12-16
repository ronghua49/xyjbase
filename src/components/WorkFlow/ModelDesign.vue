<template>
  <transition name="fade">
    <el-dialog :title=title :visible.sync="openState" fullscreen v-if="openState" style="background-color: #e8edf1;" >
      <div class="content">
        <iframe id="xyjsoftiframe" ref="iframe" :src="modelpath"></iframe>
      </div>
    </el-dialog>
  </transition>
</template>

<script>
import config from "@/utils/global"
import Cookies from "js-cookie"
  export default {
    name: "ModelDesign",
    props:{
      /**
       *宋建国：2019/8/9 13:09
       *描述：对话框标题
       */
      title: {
        type: String,
        default: '流程模型设计器'
      },
    },
    data() {
      return {
        modelpath: "http://www.baidu.com",
        openState: false,
      }
    },
    /**
     *宋建国：2019/8/9 13:19
     *描述：所有方法
     */
    methods: {
      /**
       *宋建国：2019/8/9 13:21
       *描述：打开对话窗体
       */
      open: function (modelId,row,cab) {
        if(!this.$tu.isBlank(modelId)){
          this.modelpath = this.global.workflow + "/modeler.html?modelId="+modelId+'&baseurl='+config.baseUrl+'&token='+Cookies.get('token')+'&workflow='+config.workflow;
          this.openState = true;
          this.$nextTick(() => {
            document.getElementById('xyjsoftiframe').style.height = (document.documentElement.clientHeight-100)+'px';
          })
        }else{
          /**
           *宋建国：2019/8/9 14:52
           *描述：创建空流程模型
           */
          this.$api.workflow.createModel({code:row[0].code}).then((res) =>{
          	cab()
            if(!res.data){
              this.$message.error("创建空白审批流模型错误");
              return;
            }
            if(!res.data.id){
              this.$message.error("创建空白审批流模型错误");
              return;
            }
            this.modelpath = this.global.workflow + "/modeler.html?modelId="+res.data.id+'&baseurl='+config.baseUrl+'&token='+Cookies.get('token');
            this.openState = true;
            this.$nextTick(() => {
              document.getElementById('xyjsoftiframe').style.height = (document.documentElement.clientHeight-100)+'px';
            })
          }).catch((res) => {
            this.$message.error(res.message);
            cab()
          })
        }
      },
      /**
       *宋建国：2019/8/9 13:21
       *描述：关闭对话窗体
       */
      close: function () {
        this.openState = false;
      }
    }
  }
</script>

<style scoped>
  .content{
    width: 100%;
    height: 100%;
  }
  #xyjsoftiframe{
    width: 100%;
    border: 1px solid #ddd;
    margin-top: -20px;
    height: 600px;
  }
</style>
