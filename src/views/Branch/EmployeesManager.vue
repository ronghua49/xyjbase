<template>
	<div class="page-container">
		<!--工具栏-->
		<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
			<el-form :inline="true" :model="filters" :size="size">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="公司名称"></el-input>
				</el-form-item>
				<el-form-item>
					<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:role:view" type="primary" @click="search" />
				</el-form-item>
				<el-form-item>
					<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:user:add" type="primary" @click="handleAdd" />
				</el-form-item>
			</el-form>
		</div>
		<!--表格内容栏-->
		<kt-table id="ktable" 
			permsEdit="sys:user:edit" 
			permsDelete="sys:user:delete" 
			:data="pageResult" 
			:columns="filterColumns" 
			:seepost="true"
			@query="findPage" 
			:width="330"
			@handleEdit="handleEdit"
			@handleDelete="handleDelete"
			@seePost="seePost" >
		</kt-table>
		<!--新增编辑界面-->
		<el-dialog v-dialogDrag :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" @close="$refs.dataForm.resetFields()">
			<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size" label-position="right">
				<el-form-item label="ID" prop="id" v-if="false">
					<el-input v-model="dataForm.id" :disabled="true" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="用户名" prop="username">
					<el-input v-model="dataForm.username" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="密码" prop="password">
					<el-input v-model="dataForm.password" type="password" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="机构" prop="deptName">
					<popup-tree-input 
						:data="deptData" 
						:props="deptTreeProps" 
						:prop="dataForm.deptName" 
						:nodeKey="''+dataForm.deptId" 
						:currentChangeHandle="deptTreeCurrentChangeHandle">
					</popup-tree-input>
				</el-form-item>
				<el-form-item label="岗位" prop="sysPostId">
					<el-input v-model="dataForm.sysPostId" auto-complete="off" @click.native="showDialog"></el-input>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">
					<el-input v-model="dataForm.email" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="手机" prop="mobile">
					<el-input v-model="dataForm.mobile" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="角色" prop="userRoles">
					<el-select v-model="dataForm.userRoles" multiple placeholder="请选择" value-key="id"
						 style="width: 100%;">
						<el-option v-for="item in roles" :key="item.id"
							:label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="备注" prop="remark">
					<el-input v-model="dataForm.remark" auto-complete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button :size="size" @click.native="dialogVisible = false;defaultValue = []">{{$t('action.cancel')}}</el-button>
				<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
			</div>
		</el-dialog>
		<Branch-post-dialog ref="dialog" v-bind="query"></Branch-post-dialog>
	</div>
</template>

<script>
	import PopupTreeInput from "@/components/PopupTreeInput"
	import KtTable from "@/views/Core/KtTable"
	import KtButton from "@/views/Core/KtButton"
	import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
	import BranchPostDialog from "@/components/BranchPostDialog/BranchPostDialog";
	import axios from "@/http/axios";
	import { format } from "@/utils/datetime"
	export default {
		name: 'EmployeesManager',
		components: {
			KtTable,
			KtButton,
			TableColumnFilterDialog,
			PopupTreeInput,
			BranchPostDialog
		},
		data() {
			return {
				defaultValue:[],
				size: 'small',
				filters: {
					name: ''
				},
				filterColumns: [
					{prop: "id",label: "ID",minWidth: 150},
					{prop: "username",label: "用户名",minWidth: 120},
					{prop:"deptName", label:"机构", minWidth:120},
					{prop: "email",label: "邮箱",minWidth: 120},
					{prop: "mobile",label: "手机",minWidth: 100},
					{prop:"status", label:"状态", minWidth:70},
				],
				pageRequest: {
					pageNum: 1,
					pageSize: 10
				},
				pageResult: {},
				deptData: [],
				deptTreeProps: {
					label: 'name',
					children: 'children'
				},
				operation: false, // true:新增, false:编辑
				dialogVisible: false, // 新增编辑界面是否显示
				editLoading: false,
				dataFormRules: {
					username: [{
						required: true,
						message: '请输入用户名',
						trigger: 'blur'
					}],
				},
				roles:[],
				// 新增编辑界面数据
				dataForm: {
					id: 0,
					username: '',
					email: '',
					mobile: '',
					remark:'',
					deptName:'',
					deptId:'',
					userRoles: [],
					postUsers:[],
					sysPostId:'',
					branchCode:sessionStorage.getItem('branchCode')
				},
				query:{
	    		cancel:function(row){console.log(row)},
	    		confirm:this.branchpostdialog,
	    	},
			}
		},
		methods: {
			search(){
				let params = {
	      	pageBean: {page: 1, pageSize: 10, showTotal: true, summary: []},
					params: {},
					querys: [{hasInitValue: false, operation: "EQUAL", property: "username", relation: "AND", value: this.filters.username},
					{hasInitValue: false, operation: "EQUAL", property: "branchCode", relation: "AND", value: sessionStorage.getItem('branchCode')}],
					selfDate: {},
					sorter: []
	      }
				if(!this.filters.username){
					params.querys = [{hasInitValue: false, operation: "EQUAL", property: "branchCode", relation: "AND", value: sessionStorage.getItem('branchCode')}]
				}
				this.findPage(params)
			},
			delFlag(id){
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranch/StartOrStop?id='+id,
					method:'get'
				}).then(res => {
					this.$message({
						type:'success',
						message:'操作成功'
					})
					this.findPage()
				}).catch(err => {
					this.$error(res.message)
				})
			},
			// 获取分页数据
			findPage(data) {
				let params = {
	      	pageBean: {page: 1, pageSize: 10, showTotal: true, summary: []},
					params: {},
					querys: [{hasInitValue: false, operation: "EQUAL", property: "branchCode", relation: "AND", value: sessionStorage.getItem('branchCode')}],
					selfDate: {},
					sorter: []
	      }
				if(data && data.querys) {
					params = data
				}
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchUser/query',
					method:'post',
					data:params
				}).then((res) => {
					this.pageResult = JSON.parse(JSON.stringify(res.data).replace(/rows/g, 'content'))
					this.pageResult = JSON.parse(JSON.stringify(this.pageResult).replace(/page/g, 'pageNum'))
					this.pageResult = JSON.parse(JSON.stringify(this.pageResult).replace(/total/g, 'totalSize'))
					this.findUserRoles()
				}).then(data != null ? data.callback : '')
			},
			// 加载用户角色信息
			findUserRoles() {
				let params = {
	      	pageBean: {page: 1, pageSize: 10, showTotal: true, summary: []},
					params: {},
					querys: [{hasInitValue: false, operation: "EQUAL", property: "branchCode", relation: "AND", value: sessionStorage.getItem('branchCode')}],
					selfDate: {},
					sorter: []
	      }
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchRole/query',
					method:'post',
					data:params
				}).then(res => {
					this.roles = res.data.rows
				})
			},
			// 批量删除
			handleDelete(data) {
				let dels = data.params.map(x => {
					return x.id
				})
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchUser/delete',
					method:'post',
					data:dels
				}).then(data != null ? data.callback : '')
			},
			// 显示新增界面
			handleAdd() {
				this.dialogVisible = true
				this.operation = true
				this.$nextTick(() => {
					this.$refs.dataForm.resetFields()
					this.dataForm = {
						id: 0,
						username: '',
						email: '',
						mobile: '',
						remark:'',
						deptName:'',
						deptId:'',
						userRoles: [],
						postUsers:[],
						sysPostId:'',
						branchCode:sessionStorage.getItem('branchCode')
					}
				})
			},
			// 显示编辑界面
			handleEdit(params) {
				this.dataForm = Object.assign({}, params.row)
				this.dialogVisible = true
				this.operation = false
				this.defaultValue = res.data;
			},
			// 编辑
			submitForm() {
				this.$refs.dataForm.validate((valid) => {
					if(valid) {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						params.userRoles = params.userRoles.map(data => {
							let obj = {};
							obj.roleId = data;
							return obj
						})
						axios({
							hostUrl:this.baseUrl,
							url:'/sysBranchUser/save',
							method:'post',
							data:params
						}).then((res) => {
								this.editLoading = false
								if(res.code == 200) {
									this.$message({
										message: '操作成功',
										type: 'success'
									})
									this.dialogVisible = false
									this.$refs['dataForm'].resetFields()
								} else {
									this.$message({
										message: '操作失败, ' + res.msg,
										type: 'error'
									})
								}
								this.findPage()
							})
							.catch(err => {
								this.editLoading = false
								this.$message.error(err.message);
							})
					}
				})
			},
			deptTreeCurrentChangeHandle(data, node) {
    		this.dataForm.deptId = data.id
    		this.dataForm.deptName = data.name
    	},
    	// 获取部门列表
			findDeptTree() {
				axios({
        	hostUrl:this.baseUrl,
        	url:'/sysBranchDept/findTree?branchCode='+sessionStorage.getItem('branchCode'),
        	method:'get',
        }).then((res) => {
        	this.deptData = res.data
        }).catch(err => {
        	this.$message.error(err.message)
        })
			},
			// 选择岗位信息
			showDialog(){
				this.operation ? this.$refs.dialog.open(true) : this.$refs.dialog.open(true,this.defaultValue)	
			},
			branchpostdialog(row,isMain){
				this.defaultValue = row
				this.dataForm.sysPostId = '';
				// isMain  0--否   1--是
				this.dataForm.postUsers = row.map(data => {
					let text = data.name;
					data.id == isMain ? text = text+'(主岗位)' : text
					this.dataForm.sysPostId += text+'、';
					let obj = {};
					obj.sysPostId = data.id;
					obj.isMain = data.id == isMain ? 1 : 0;
					return obj;
				})
				this.dataForm.sysPostId = this.dataForm.sysPostId.substring(0,this.dataForm.sysPostId.length-1)
			},
			seePost(row){
				// 查看岗位
//				this.getPostMsg(row.row).then(res => {
//					if(res){
//						this.tableList = res.data;this.dialogVisibleUser = true
//					}
//				})
			},
		},
		mounted(){
			this.findDeptTree()
		}
	}
</script>

<style scoped>
	#ktable /deep/ .el-table__body-wrapper {
		max-height: 420px!important;
	}
</style>