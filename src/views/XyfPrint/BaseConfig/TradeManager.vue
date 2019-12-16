<template>
	<div>
		<el-form :model="form" label-width="180px" ref="ruleForm" style="width: 50%;">
			<el-form-item label="新用户首次授信金额" prop="money">
				<div style="display: flex;width: 100%;">
					<el-input size="small" style="width: 50%;margin-right: 15px;" v-model="form.newusermoney" placeholder="请输入首次授信金额"></el-input>
					<span>元</span>
				</div>
			</el-form-item>
			<el-form-item label="单次账单最大金额" prop="money">
				<div style="display: flex;width: 100%;">
					<el-input size="small" style="width: 50%;margin-right: 15px;" v-model="form.singlemaxmoney" placeholder="请输入首次授信金额"></el-input>
					<span>元</span>
				</div>
			</el-form-item>
			<el-form-item label="设备投放注册密码" prop="money">
				<div style="display: flex;width: 100%;">
					<el-input size="small" maxlength="10" style="width: 50%;margin-right: 15px;" v-model="form.registpsw" placeholder="设备投放注册密码"></el-input>
					<span></span>
				</div>
			</el-form-item>
			<el-form-item label="客服电话" prop="phone">
				<div style="display: flex;width: 100%;">
					<el-input size="small"  style="width: 50%;margin-right: 15px;" maxlength="13" v-model="form.kfphone" placeholder="请输入首次授信金额"></el-input>
				</div>
			</el-form-item>
			<el-button :loading="loading" type="primary" @click="confirm" size="mini" style="margin-left: -200px;">确定</el-button>
		</el-form>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	import xyjtools from "@/utils/toolutil.js"
	export default{
		name: 'TradeManager',
		data(){
			return {
				form: {
					newusermoney: "",
					kfphone: "",
					singlemaxmoney: "",
					registpsw: ""
				},
				loading: false
			}
		},
		methods:{
			getInfo(){
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'get',
					data: {},
					url: "/sysInfoset/findById?id=1", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					let rows = res.data;
					this.form = rows;
				}).catch((err) => {
					this.$message.error(err.message);
				});
			},
			confirm(){
				if ( !xyjtools.validateMoney(this.form.newusermoney) ) {
					return;
				}
				if ( !xyjtools.validateMoney(this.form.singlemaxmoney) ) {
					return;
				}
				if ( this.form.kfphone.indexOf("-") == -1 ) {
					if ( !xyjtools.validatePhone(this.form.kfphone) ) {
						return;
					}
				} else {
					if ( !/^[0-9]+$/.test(this.form.kfphone.split("-").join("")) ) {
						this.$message.error("手机号码格式不正确");
						return;
					}
				}
				
				if ( this.form.registpsw == "" ) {
					this.$message.error("设备投放注册密码不能为空")
					return;
				}
				if ( !/^[0-9a-zA-Z]+$/.test(this.form.registpsw) ) {
					this.$message.error("设备投放注册密码格式不正确");
					return;
				}
				this.loading = true;
				axios({
					hostUrl: this.global.hostUrl85,
					method: 'post',
					data: this.form,
					url: "/sysInfoset/save", // 请求地址
				}).then((res) => {
					this.loading = false;
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					this.$message({
						type: 'success',
						message: "保存成功"
					})
				}).catch((err) => {
					this.$message.error(err.message);
				});
			}
		},
		mounted(){
			this.getInfo();
		}
	}
</script>

<style>
</style>