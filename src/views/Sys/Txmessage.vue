<template>
  <div>
    <div class="content">
      <el-form
        class="txmessage-form"
        :model="wrapForm"
        :rules="wraprules"
        ref="wrapForm1"
        :label-position="labelPosition"
        size="small"
      >
        <el-form-item prop="name" label="用户名">
          <el-input :disabled="true" v-model="wrapForm.name"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="wrapForm.email"></el-input>
        </el-form-item>
        <el-form-item prop="mobile" label="手机号">
          <el-input v-model="wrapForm.mobile"></el-input>
        </el-form-item>
        <el-form-item prop="realname" label="真实姓名">
          <el-input v-model="wrapForm.realname"></el-input>
        </el-form-item>
        <el-form-item prop="createTime" label="创建时间">
          <el-input :disabled="true" v-model="wrapForm.createTime"></el-input>
        </el-form-item>
        <el-form-item style="text-align:center;">
          <el-button type="primary" :loading="loading" @click="save">{{$t('action.submit')}}</el-button>
          <el-button type="primary" @click="dialogVisible = true;editLoading = false">{{$t('action.changePassword')}}</el-button>

        </el-form-item>
      </el-form>
       <el-dialog v-dialogDrag title="修改密码" width="30%" :visible.sync="dialogVisible" :close-on-click-modal="false">
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
import Cookies from "js-cookie"
export default {
  name: "txmessage",
  data() {
    return {
    	loading:false,
      labelPosition: "left",
      ownerId: "",
      wrapForm: {
        id:"",
        name: "",
        email: "",
        mobile: "",
        createTime: "",
        realname: "",
      },
      wraprules: {

      },
       dialogVisible: false,
      editLoading: false,
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
      passform:{
        name:'',
        password: '',
        newPWD: '',
        confirmpassword: ''
      }
    };
  },
  mounted() {
    let userName = sessionStorage.getItem('user');
    this.$api.common
              .xyjGet({
                url: "/user/findByName",
                data: {
									name: userName
								}
              })
              .then(res => {
                if (res.code == 200) {
                  for(var i in this.wrapForm){
                    this.wrapForm[i] = res.data[i]
                  }
                  this.passform.name = res.data['name']
                  return ;
                } else {
                  this.$message({
                    message: "操作失败, " + res.message,
                    type: "error"
                  });
                }
                
              });
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    save() {
    	this.loading = true;
      var searchData = this.wrapForm;
      this.$refs.wrapForm1.validate(valid => {
        if (valid) {
         this.$api.common
              .xyjPost({
              url: "/user/save",
              data: searchData
            })
            .then(res => {
              if (res.code) {
                this.$message({
                  message: "操作成功",
                  type: "success",
                  duration: 800
                });
                this.searchData = {};
              } else {
                this.$message({
                  message: "操作失败, " + res.message,
                  type: "error",
                  duration: 800
                });
              }
              this.loading = false;
            });
        }
      });
    },
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
           }).catch(err => {
            });

						// this.$api.user.updatePwd(params).then((res) => {
            //   this.editLoading = false
						// 	if(res.code == 200) {
            //     if(res.msg == '密码校验失败' || res.msg == '旧密码与新密码相同，无需修改' || res.msg == '账号不存在'){
            //       this.$message({message: '操作失败, ' + res.msg, type: 'error'})                  
            //       return;
            //     }
						// 		this.$message({ message: '操作成功', type: 'success' })
						// 		this.dialogVisible = false
						// 		this.$refs['passform'].resetFields()
						// 	} else {
						// 		this.$message({message: '操作失败, ' + res.msg, type: 'error'})
						// 	}
						// })
					})
				}
			})
		},
  }
};
</script>

<style scoped="scoped">
.content {
  width: 60%;
  margin: 0 auto;
  margin-top: 30px;
}
</style>
