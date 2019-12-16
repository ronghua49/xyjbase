<template>
  <el-form :model="loginForm" :rules="fieldRules" ref="loginForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <span class="tool-bar">
      <!-- 主题切换 -->
      <theme-picker style="float:right;" class="theme-picker" :default="themeColor" @onThemeChange="onThemeChange"></theme-picker>
    </span>
    <h2 class="title" style="padding-left:22px;" >讯宜捷中台</h2>
    <el-form-item prop="account">
      <el-input ref='account' type="text" v-model="loginForm.account"
                auto-complete="off"
                placeholder="账号"
                @keyup.enter.native="accountEnter"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input ref="password" type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"
                @keyup.enter.native="login"></el-input>
    </el-form-item>
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:48%;" @click.native.prevent="reset">重 置</el-button>
      <el-button type="primary" style="width:48%;" @click.native.prevent="login" :loading="loading">登 录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapState } from 'vuex'
import Cookies from "js-cookie"
import ThemePicker from "@/components/ThemePicker"
import LangSelector from "@/components/LangSelector"
export default {
  name: 'SubsidiaryLogin',
  components:{
    ThemePicker,
    LangSelector
  },
  data() {
    return {
      loading: false,
      loginForm: {
        account: '',
        password: '',
        usertype:'1'
      },
      fieldRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      checked: true
    }
  },
  methods: {
    /**
     *宋建国：2019/7/30 9:25
     *描述：当输入账号回车时光标移动到密码位置
     */
    accountEnter(){
      if(this.loginForm.account.length>0) {
        this.$refs.password.focus();
      }
    },
    login() {
      this.loading = true
      let userInfo = {account:this.loginForm.account, password:this.loginForm.password,usertype:'1'}
      this.$api.login.login(userInfo).then((res) => {
          Cookies.set('token', res.data.token.token) // 放置token到Cookie
          sessionStorage.setItem('user', userInfo.account) // 保存用户到本地会话
          sessionStorage.setItem('branchCode', res.data.branchCode) // 保存用户到本地会话
          this.$store.commit('menuRouteLoaded', false) // 要求重新加载导航菜单
          this.$router.push('/')  // 登录成功，跳转到主页
          this.loading = false
        }).catch((res) => {
          this.$message({
          message: res.message,
          type: 'error'
          })
        this.loading = false
        });
    },
    reset() {
      this.$refs.loginForm.resetFields()
    },
    // 切换主题
    onThemeChange: function(themeColor) {
      this.$store.commit('setThemeColor', themeColor)
    }
  },
  mounted() {
    this.$refs.account.focus();
  },
  computed:{
    ...mapState({
      themeColor: state=>state.app.themeColor
    })
  }
}
</script>

<style lang="scss" scoped>
  .login-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 100px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 30px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>
