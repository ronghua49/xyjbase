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
			:width=270
			:stop="true"
			:start="true"
			@findPage="findPage" 
			@handleEdit="handleEdit"
			@handleDelete="handleDelete"
			@Stop="Stop"
			@Start="Start" >
		</kt-table>
		<!--新增编辑界面-->
		<el-dialog v-dialogDrag :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" @close="$refs.dataForm.resetFields()">
			<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size" label-position="right">
				<el-form-item label="ID" prop="id" v-if="false">
					<el-input v-model="dataForm.id" :disabled="true" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="公司名称" prop="name">
					<el-input v-model="dataForm.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="公司邮箱" prop="email">
					<el-input v-model="dataForm.email" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="联系方式" prop="mobile">
					<el-input v-model="dataForm.mobile" auto-complete="off"></el-input>
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
	</div>
</template>

<script>
	import KtTable from "@/views/Core/KtTable"
	import KtButton from "@/views/Core/KtButton"
	import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
	import axios from "@/http/axios";
	import { format } from "@/utils/datetime"
	export default {
		name: 'SubsidiaryOpen',
		components: {
			KtTable,
			KtButton,
			TableColumnFilterDialog,
		},
		data() {
			return {
				size: 'small',
				filters: {
					name: ''
				},
				filterColumns: [{
						prop: "id",
						label: "ID",
						minWidth: 150
					},
					{
						prop: "name",
						label: "公司名称",
						minWidth: 120
					},
					{
						prop: "email",
						label: "公司邮箱",
						minWidth: 120
					},
					{
						prop: "mobile",
						label: "公司联系方式",
						minWidth: 100
					},
					{
						prop: "code",
						label: "公司编号",
						minWidth: 100
					},
					{
						prop: "remark",
						label: "公司备注",
						minWidth: 100
					},
				],
				pageRequest: {
					pageNum: 1,
					pageSize: 10
				},
				pageResult: {},

				operation: false, // true:新增, false:编辑
				dialogVisible: false, // 新增编辑界面是否显示
				editLoading: false,
				dataFormRules: {
					name: [{
						required: true,
						message: '请输入用户名',
						trigger: 'blur'
					}],
				},
				// 新增编辑界面数据
				dataForm: {
					id: 0,
					name: '',
					email: '',
					mobile: '',
					remark:''
				},
			}
		},
		methods: {
			search(){
				let params = {
	      	pageBean: {page: 1, pageSize: 10, showTotal: true, summary: []},
					params: {},
					querys: [{hasInitValue: false, operation: "EQUAL", property: "name", relation: "AND", value: this.filters.name}],
					selfDate: {},
					sorter: []
	      }
				if(!this.filters.name){
					params.querys = []
				}
				this.findPage(params)
			},
			Stop(row){
				console.log(row)
				this.delFlag(row.row.id)
			},
			Start(row){
				this.delFlag(row.row.id)
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
					querys: [],
					selfDate: {},
					sorter: []
	      }
				if(data) {
					params = data
				}
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranch/query',
					method:'post',
					data:params
				}).then((res) => {
					this.pageResult = JSON.parse(JSON.stringify(res.data).replace(/rows/g, 'content'))
					this.pageResult = JSON.parse(JSON.stringify(this.pageResult).replace(/page/g, 'pageNum'))
					this.pageResult = JSON.parse(JSON.stringify(this.pageResult).replace(/total/g, 'totalSize'))
				}).then(data != null ? data.callback : '')
			},
			// 批量删除
			handleDelete(data) {
				let dels = data.params.map(x => {
					return x.id
				})
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranch/delete',
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
						name: '',
						email: '',
						mobile: '',
						remark:''
					}
				})
			},
			// 显示编辑界面
			handleEdit(params) {
				this.dataForm = Object.assign({}, params.row)
				this.dialogVisible = true
				this.operation = false
			},
			// 编辑
			submitForm() {
				this.$refs.dataForm.validate((valid) => {
					if(valid) {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						axios({
							hostUrl:this.baseUrl,
							url:'/sysBranch/save',
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
		}
	}
</script>

<style scoped>
	#ktable /deep/ .el-table__body-wrapper {
		max-height: 420px!important;
	}
</style>