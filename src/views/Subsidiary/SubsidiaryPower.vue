<template>
	<div class="page-container">
		<!--工具栏-->
		<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
			<el-form :inline="true" :model="filters" :size="size">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="角色名"></el-input>
				</el-form-item>
				<el-form-item>
					<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:role:view" type="primary" @click="search" />
				</el-form-item>
				<el-form-item>
					<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:role:add" type="primary" @click="handleAdd" />
				</el-form-item>
			</el-form>
		</div>
		<!--表格内容栏-->
		<kt-table id="ktable" :width=100 :delet="false" permsEdit="sys:role:edit" :highlightCurrentRow="true" :stripe="false" :data="pageResult" :columns="columns" :showBatchDelete="false" @handleCurrentChange="handleRoleSelectChange" @findPage="findPage" @handleEdit="handleEdit">
		</kt-table>
		<!--新增编辑界面-->
		<el-dialog v-dialogDrag :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false">
			<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size">
				<el-form-item label="ID" prop="id" v-if="false">
					<el-input v-model="dataForm.id" :disabled="true" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="角色名" prop="name">
					<el-input v-model="dataForm.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="备注 " prop="remark">
					<el-input v-model="dataForm.remark" auto-complete="off" type="textarea"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
				<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
			</div>
		</el-dialog>
		<!--角色菜单，表格树内容栏-->
		<div class="menu-container" :v-if="true">
			<div class="menu-header">
				<span><B>角色菜单授权</B></span>
			</div>
			<el-tree :data="menuData" size="mini" show-checkbox node-key="id" :props="defaultProps" style="width: 100%;pading-top:20px;" ref="menuTree" :render-content="renderContent" v-loading="menuLoading" element-loading-text="拼命加载中" :check-strictly="true" @check-change="handleMenuCheckChange">
			</el-tree>
			<div style="float:left;padding-left:24px;padding-top:12px;padding-bottom:4px;">
				<el-checkbox v-model="checkAll" @change="handleCheckAll" :disabled="this.selectRole.id == null"><b>全选</b></el-checkbox>
			</div>
			<div style="float:right;padding-right:15px;padding-top:4px;padding-bottom:4px;">
				<kt-button :label="$t('action.reset')" perms="sys:role:edit" type="primary" @click="resetSelection" :disabled="this.selectRole.id == null" />
				<kt-button :label="$t('action.submit')" perms="sys:role:edit" type="primary" @click="submitAuthForm" :disabled="this.selectRole.id == null" :loading="authLoading" />
			</div>
		</div>
	</div>
</template>

<script>
	import axios from "@/http/axios"
	import KtTable from "@/views/Core/KtTable"
	import KtButton from "@/views/Core/KtButton"
	import { format } from "@/utils/datetime"
	export default {
		name: 'SubsidiaryPower',
		components: {
			KtTable,
			KtButton
		},
		data() {
			return {
				size: 'small',
				filters: {
					name: ''
				},
				columns: [{
						prop: "id",
						label: "ID",
						minWidth: 120
					},
					{
						prop: "name",
						label: "角色名",
						minWidth: 120
					},
					{
						prop: "remark",
						label: "备注",
						minWidth: 120
					},
					{
						prop: "createBy",
						label: "创建人",
						minWidth: 120
					},
					{
						prop: "createTime",
						label: "创建时间",
						minWidth: 120,
						formatter: this.dateFormat
					}
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
						message: '请输入角色名',
						trigger: 'blur'
					}]
				},
				// 新增编辑界面数据
				dataForm: {
					id: 0,
					name: '',
					remark: ''
				},
				selectRole: {},
				menuData: [],
				menuSelections: [],
				menuLoading: false,
				authLoading: false,
				checkAll: false,
				currentRoleMenus: [],
				defaultProps: {
					children: 'children',
					label: 'name'
				}
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
					hostUrl: this.baseUrl,
	        url: '/sysBranchRole/query',
	        method: 'post',
					data:params
				}).then((res) => {
					this.pageResult = JSON.parse(JSON.stringify(res.data).replace(/rows/g, 'content'))
					this.pageResult = JSON.parse(JSON.stringify(this.pageResult).replace(/page/g, 'pageNum'))
					this.pageResult = JSON.parse(JSON.stringify(this.pageResult).replace(/total/g, 'totalSize'))
					this.findTreeData()
				}).catch(err => {
					this.$error(err.message)
				}).then(data!=null?data.callback:'')
			},
			// 批量删除
			handleDelete(data) {
				let dels = data.params.map(x => {
					return x.id
				})
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchRole/delete',
					method:'post',
					data:dels
				}).then(data.callback)
			},
			// 显示新增界面
			handleAdd() {
				this.editLoading = false
				this.dialogVisible = true
				this.operation = true
				this.dataForm = {
					id: 0,
					name: '',
					remark: ''
				}
			},
			// 显示编辑界面
			handleEdit(params) {
				this.editLoading = false
				this.dialogVisible = true
				this.operation = false
				this.dataForm = Object.assign({}, params.row)
			},
			// 编辑
			submitForm() {
				this.$refs.dataForm.validate((valid) => {
					if(valid) {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						axios({
							hostUrl:this.baseUrl,
							url:'/sysBranchRole/save',
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
					}
				})
			},
			// 获取数据
			findTreeData() {
				this.menuLoading = true
				this.pageRequest.columnFilters = {
					additionalProperties1: {
						name: 'name',
						value: this.filters.name
					}
				}
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchMenu/findMenuTree',
					method:'get',
				}).then((res) => {
					this.menuData = res.data
					this.menuLoading = false
				})
			},
			// 角色选择改变监听
			handleRoleSelectChange(val) {
				if(val == null || val.val == null) {
					return
				}
				this.selectRole = val.val
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchRole/findRoleMenus?roleId='+val.val.id,
					method:'get',
				}).then((res) => {
					this.currentRoleMenus = res.data
					this.$refs.menuTree.setCheckedNodes(res.data)
				})
			},
			// 树节点选择监听
			handleMenuCheckChange(data, check, subCheck) {
				if(check) {
					// 节点选中时同步选中父节点
					let parentId = data.parentId
					// this.$refs.menuTree.setChecked(parentId, true, false)
					//选中父节点，全选子节点
					if(data.children != null) {
						data.children.forEach(element => {
							this.$refs.menuTree.setChecked(element.id, true, true)
						});
					}
				} else {
					// 节点取消选中时同步取消选中子节点
					if(data.children != null) {
						data.children.forEach(element => {
							this.$refs.menuTree.setChecked(element.id, false, false)
						});
					}
				}
			},
			// 重置选择
			resetSelection() {
				this.checkAll = false
				this.$refs.menuTree.setCheckedNodes(this.currentRoleMenus)
			},
			// 全选操作
			handleCheckAll() {
				if(this.checkAll) {
					let allMenus = []
					this.checkAllMenu(this.menuData, allMenus)
					this.$refs.menuTree.setCheckedNodes(allMenus)
				} else {
					this.$refs.menuTree.setCheckedNodes([])
				}
			},
			// 递归全选
			checkAllMenu(menuData, allMenus) {
				menuData.forEach(menu => {
					allMenus.push(menu)
					if(menu.children) {
						this.checkAllMenu(menu.children, allMenus)
					}
				});
			},
			// 角色菜单授权提交
			submitAuthForm() {
				let roleId = this.selectRole.id
				if('admin' == this.selectRole.name) {
					this.$message({
						message: '超级管理员拥有所有菜单权限，不允许修改！',
						type: 'error'
					})
					return
				}
				this.authLoading = true
				let checkedNodes = this.$refs.menuTree.getCheckedNodes(false, true)
				let roleMenus = []
				for(let i = 0, len = checkedNodes.length; i < len; i++) {
					let roleMenu = {
						roleId: roleId,
						menuId: checkedNodes[i].id
					}
					roleMenus.push(roleMenu)
				}
				axios({
					hostUrl:this.baseUrl,
					url:'/sysBranchRole/saveRoleMenus',
					method:'post',
					data:roleMenus
				}).then((res) => {
					if(res.code == 200) {
						this.$message({
							message: '操作成功',
							type: 'success'
						})
					} else {
						this.$message({
							message: '操作失败, ' + res.msg,
							type: 'error'
						})
					}
					this.authLoading = false
				})
			},
			renderContent(h, {
				node,
				data,
				store
			}) {
				return(
					<div class="column-container" style="width:100%;display:flex;justify-content:space-between;">
				<span style="display:inline-block;width:21%;margin-right:5%;text-align:left">{data.name}</span>
				<span style="margin-right:5%;text-align:left">
					<el-tag type={data.type === 0?'':data.type === 1?'success':'info'} size="small">
						{data.type === 0?'目录':data.type === 1?'菜单':'按钮'}
					</el-tag>
				</span>
				<span style="text-algin:center;"> <i class={data.icon}></i></span>
				<span style="text-algin:center;">{data.parentName?data.parentName:'顶级菜单'}</span>
				{/*<span style="text-algin:center;">{data.url?data.url:'\t'}</span>*/}
			</div>);
			},
			// 时间格式化
			      dateFormat: function(row, column, cellValue, index) {          
				return format(row[column.property])      
			}

		},
	}
</script>
<style scoped>
	.menu-container {
		margin-top: 10px;
	}
	
	.menu-header {
		padding-left: 8px;
		padding-bottom: 5px;
		text-align: left;
		font-size: 16px;
		color: rgb(20, 89, 121);
	}
	
	#ktable /deep/ .el-table__body-wrapper {
		max-height: 420px!important;
	}
</style>