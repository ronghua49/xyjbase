<template>
  <div class="personal-panel">
     <div class="personal-desc" :style="{'background':this.$store.state.app.themeColor}"> 
    <!-- <div class="avatar-container">
      <img class="avatar" :src="require('@/assets/user.png')" />
    </div>   -->
     <div class="name-role">
      <span class="sender">{{ user.name }} - {{ user.role }}</span>
    </div>   
     <div class="registe-info">
      <span class="registe-info">
        <li class="fa fa-clock-o"></li>
        {{ user.registeInfo }}
      </span>
    </div>   
     </div> 
     <!--<div class="personal-relation">
        <span class="relation-item">followers</span>
        <span class="relation-item">watches</span>
        <span class="relation-item">friends</span>
    </div> -->
     <!--<div class="main-operation">
        <span class="main-operation-item">
          <el-button size="small" icon="fa fa-male"> 个人中心</el-button>
        </span>
        <span class="main-operation-item">
          <el-button size="small" icon="fa fa-key"> 修改密码</el-button>
        </span>
    </div> -->
     <!--<div class="other-operation">
        <div class="other-operation-item">
          <li class="fa fa-eraser"></li>
          清除缓存
        </div>
        <div class="other-operation-item">
          <li class="fa fa-user"></li>
          在线人数
        </div>
        <div class="other-operation-item">
          <li class="fa fa-bell"></li>
          访问次数
        </div>
        <div class="other-operation-item" @click="showBackupDialog">
          <li class="fa fa-undo"></li>
          {{$t("common.backupRestore")}}
        </div>
    </div> -->
    <div class="personal-footer" @click="logout">
      <li class="fa fa-sign-out"></li>
      {{$t("common.logout")}}
    </div>
    <div class="personal-footer" @click="changePWDShow">
      <li class="fa fa-key"></li>
      {{$t("action.changePassword")}}
    </div>
    <!--备份还原界面-->
    <backup ref="backupDialog" @afterRestore="afterRestore"></backup>
    <div>
    	<el-dialog v-dialogDrag title="修改密码" width="30%" :visible.sync="dialogVisible" :close-on-click-modal="false" :modal-append-to-body='false'>
	      <el-form :model="passform" label-width="100px" :rules="passformRules" ref="passform" size="mini"
	        label-position="right">
	        <el-form-item label="ID" prop="id" v-if="false">
	          <el-input v-model="passform.id" :disabled="true" auto-complete="off"></el-input>
	        </el-form-item>
	        <el-form-item label="原始密码" prop="password">
	          <el-input v-model="passform.password" type="password" auto-complete="off"></el-input>
	        </el-form-item>
	        <el-form-item label="新密码" prop="newPWD">
	          <el-input v-model="passform.newPWD" type="password" auto-complete="off"></el-input>
	        </el-form-item>
	        <el-form-item label="确认新密码" prop="confirmpassword">
	          <el-input v-model="passform.confirmpassword" type="password" auto-complete="off"></el-input>
	        </el-form-item>
	      </el-form>
	      <div slot="footer" class="dialog-footer">
	        <el-button size="mini" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
	        <el-button size="mini" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
	      </div>
	    </el-dialog>
    </div>
  </div>
</template>

<script>
  import Backup from "@/views/Backup/Backup"
  import store from '@/store'
  import Cookies from "js-cookie";
  export default {
    name: 'PersonalPanel',
    components:{
      Backup
    },
    props: {
      user: {
        type: Object,
        default: {
          name: "admin",
          avatar: "@/assets/user.png",
          role: "超级管理员",
          registeInfo: "注册时间：2018-12-25 "
        }
      }
    },
    data() {
      return {
      	dialogVisible:false,
      	editLoading:false,
      	passform:{
	        name:'',
	        password: '',
	        newPWD: '',
	        confirmpassword: ''
	      },
	      passformRules: {
	        password: [
						{ required: true, message: '请输入原始密码', trigger: 'blur' }
					],
	        newPWD: [
						{ required: true, message: '请输入新密码', trigger: 'blur' }
					],
	        confirmpassword: [
						{ required: true, message: '请再次确认密码', trigger: 'blur' }
					],
	      },
      }
    },
    methods: {
      // 退出登录
      logout: function() {
        this.$confirm("确认退出吗?", "提示", {
          type: "warning"
        })
          .then(() => {

            sessionStorage.removeItem("user")
            sessionStorage.removeItem("userid");
            Cookies.remove('token');
            // 清除tab标签页
            this.$store.commit('updateMainTabs', []);
            sessionStorage.getItem('branchCode') ? this.$router.push("/SubsidiaryLogin") : this.$router.push("/Login")
            this.$api.login.logout().then((res) => {
            	sessionStorage.getItem('branchCode') ? this.$router.push("/SubsidiaryLogin") : this.$router.push("/Login")
            }).catch(function(res) {
            })
          })
          .catch(() => {})
      },
      // 打开备份还原界面
      showBackupDialog: function() {
        this.$refs.backupDialog.setBackupVisible(true)
      },
      // 成功还原之后，重新登录
      afterRestore: function() {
        this.$refs.backupDialog.setBackupVisible(false)
        sessionStorage.removeItem("user")
//      this.$router.push("/login")
        this.$api.login.logout().then((res) => {
        }).catch(function(res) {
        })
      },
      changePWDShow:function(){
      	this.dialogVisible = true;
      	this.editLoading = false;
      },
      // 提交
      submitForm: function () {
				this.$refs.passform.validate((valid) => {
					if (valid) {
						this.$confirm('确认修改吗？', '提示', {}).then(() => {
							this.editLoading = true
	            
	            if(this.passform.newPWD !== this.passform.confirmpassword){
	              this.$message({ message: '第二次输入的密码与第一次输入的不一致！', type: 'warning' })
	              this.editLoading = false
	              return;
	            }
	            let params = JSON.parse(JSON.stringify(this.passform));
	            delete params.confirmpassword;
	            this.$api.common
	              .xyjGet({
	              url: "/user/updatePwd",
	              data: params
	            })
	            .then(res => {
	              this.editLoading = false
								if(res.code == 200) {
	                if(res.msg == '密码校验失败' || res.msg == '旧密码与新密码相同，无需修改' || res.msg == '账号不存在'){
	                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})                  
	                  return;
	                }
									this.$message({ message: '操作成功', type: 'success' })
									this.dialogVisible = false
									this.$refs['passform'].resetFields()
								} else {
									this.$message({message: '操作失败, ' + res.msg, type: 'error'})
								}
	           });
						})
					}
				})
			},
    }
  }
</script>

<style scoped>
  .personal-panel {
    font-size: 14px;
    width: 280px;
    text-align: center;
    border-color: rgba(180, 190, 190, 0.2);
    border-width: 1px;
    border-style: solid;
    background: rgba(182, 172, 172, 0.1);
    margin: -14px;
  }
  .personal-desc {
    padding: 15px;
    color: #fff;
  }
  .avatar {
    width: 80px;
    height: 80px;
    border-radius: 90px;
  }
  .name-role {
    font-size: 16px;
    padding: 5px;
  }
  .personal-relation {
    font-size: 16px;
    padding: 12px;
    margin-right: 1px;
    background: rgba(200, 209, 204, 0.3);
  }
  .relation-item {
    padding: 12px;
  }
  .relation-item:hover {
    cursor: pointer;
    color: rgb(19, 138, 156);
  }
  .main-operation {
    padding: 8px;
    margin-right: 1px;
    /* background: rgba(175, 182, 179, 0.3); */
    border-color: rgba(201, 206, 206, 0.2);
    border-top-width: 1px;
    border-top-style: solid;
  }
  .main-operation-item {
    margin: 15px;
  }
  .other-operation {
    padding: 15px;
    margin-right: 1px;
    text-align: left;
    border-color: rgba(180, 190, 190, 0.2);
    border-top-width: 1px;
    border-top-style: solid;
  }
  .other-operation-item {
    padding: 12px;
  }
  .other-operation-item:hover {
    cursor: pointer;
    background: #9e94941e;
    color: rgb(19, 138, 156);
  }
  .personal-footer {
    margin-right: 1px;
    font-size: 14px;
    text-align: center;
    padding-top: 10px;
    padding-bottom: 10px;
    border-color: rgba(180, 190, 190, 0.2);
    border-top-width: 1px;
    border-top-style: solid;
  }
  .personal-footer:hover {
    cursor: pointer;
    color: rgb(19, 138, 156);
    background: #b1a6a61e;
  }
</style>
