<template>
  <div>
    <el-form ref="form" :rules="rules" size="small" :model="form" label-width="140px">
      <el-form-item label="APP名称：" prop="appName">
        <el-input v-model="form.appName"></el-input>
      </el-form-item>
      <el-form-item label="项目访问路径：" prop="notifyUrl">
        <el-input v-model="form.notifyUrl"></el-input>
      </el-form-item>
      <el-form-item label="微信appid：" prop="wxappid">
        <el-input v-model="form.wxappid"></el-input>
      </el-form-item>
      <el-form-item label="微信appSecret：" prop="wxappsecret">
        <el-input v-model="form.wxappsecret"></el-input>
      </el-form-item>
      <el-form-item label="微信使用商户号：" prop="mchid">
        <el-input v-model="form.mchid"></el-input>
      </el-form-item>
      <el-form-item label="服务器ip：" prop="ipspbillcreateip">
        <el-input v-model="form.ipspbillcreateip"></el-input>
      </el-form-item>
      <el-form-item label="微信KpiKey：" prop="wxKpikey">
        <el-input v-model="form.wxKpikey"></el-input>
      </el-form-item>
      <el-form-item label="支付宝pld：" prop="zfbPid">
        <el-input v-model="form.zfbPid"></el-input>
      </el-form-item>
      <el-form-item label="支付宝appid：" prop="zfbAppid">
        <el-input v-model="form.zfbAppid"></el-input>
      </el-form-item>
      <el-form-item label="支付宝应用私钥：" prop="privateKey">
        <el-input :rows="5" type="textarea" v-model="form.privateKey"></el-input>
      </el-form-item>
      <el-form-item label="支付宝应用公钥：" prop="publicKey">
        <el-input :rows="5" type="textarea" v-model="form.publicKey"></el-input>
      </el-form-item>
      <el-form-item  label="支付宝公钥：" prop="alipayPublicKey">
        <el-input :rows="5" type="textarea" v-model="form.alipayPublicKey"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="open2">{{$t('action.submit')}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: "pay",
    data() {
      return {
        form: {
          id:'',
          appName:'',
          wxappid:'',
          wxappsecret:"",
          mchid:'',
          ipspbillcreateip:'',//微信使用终端###服务器IP
          notifyUrl:'',//回调地址##项目访问路径
          wxKpikey:'',
          zfbPid:'',
          zfbAppid:'',
          privateKey:'',
          publicKey:'',
          alipayPublicKey:'',
        },
        rules: {
          appName: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          wxappid: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          wxappsecret: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          mchid: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          ipspbillcreateip: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          notifyUrl: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          wxKpikey: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          zfbPid: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          zfbAppid: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          privateKey: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          publicKey: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
          alipayPublicKey: [
            {required: true, message: '请输入活动名称', trigger: 'blur'},
          ],
        }



      }
    },
    mounted(){
      this.getFormList();
    },
    methods: {

      onSubmit(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$api.common.xyjPostPay({
              url:'/sysPay/save',
              data:this.form
            }).then(res=>{
              if (res.code == 200) {
                this.$message({
                  message: '设置更新成功！',
                  type: 'success'
                });
              }
            })
          } else {
            this.$message({
              message: '系统错误！',
              type: 'error'
            });
            return false;
          }
        });

      },
      open2() {
        this.$confirm('此操作将更新支付设置信息, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.onSubmit('form');
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消提交'
          });
        });
      },
      getFormList() {
        let _this = this;
        this.$api.common.xyjGetPay({
          url:'/sysPay/get',
        }).then(res=>{
          if (res.data.length!==0) {
            _this.form = res.data;
          }
        })
      }
    }
  }
</script>

<style scoped lang="scss">
.el-form{
  width: 70%;
  margin: 0 auto;
}
</style>
