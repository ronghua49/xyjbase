<template>
  <div class="zhiwanjia-login">
    <el-row class="login-wrapper">
      <h2>登录授权页面</h2>
      <el-form :model="ruleForm" :rules="rules" size="small" ref="ruleForm" label-width="80px" class="demo-ruleForm">
        <el-form-item label="账户" prop="account">
          <el-input v-model="ruleForm.account"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="passCode">
          <el-row>
            <el-col span="12">
              <el-input v-model="ruleForm.passCode"></el-input>
            </el-col>
            <el-col span="12" style="height: 40px;">
              <img style="height: 40px;" src="../../assets/user.png" alt="">
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%;" type="primary" @click="submitForm('ruleForm')">登录授权</el-button>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        ruleForm: {
          account: "",
          password: "",
          passCode:"",
        },
        rules:{
          account:[
            { required: true, message: '请输入用户名', trigger: 'blur' },
          ],
          password:[
            { required: true, message: '请输入密码', trigger: 'blur' },
          ],
          passCode:[
            { required: true, message: '请输入验证码', trigger: 'blur' },
          ],

        }
      }
    }
  }
</script>

<style scoped lang="scss">
  .zhiwanjia-login {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    .login-wrapper{
      margin-top: -100px;
      width: 500px;
      padding: 20px;
      box-shadow: 0 0 25px #cac6c6;
      border-radius: 10px;
    }
  }
</style>
