<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.name" placeholder="用户名"></el-input>
			</el-form-item>
			<el-form-item>
				<kt-button  icon="fa fa-search" :label="$t('action.search')" perms="sys:role:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:user:add" type="primary" @click="handleAdd" />
			</el-form-item>
		</el-form>
	</div>
	<div class="toolbar" style="float:right;padding-top:10px;padding-right:15px;">
		<!--<el-form :inline="true" :size="size">
			<el-form-item>
				<el-button-group>
				<el-tooltip content="刷新" placement="top">
					<el-button icon="fa fa-refresh" @click="findPage(null)"></el-button>
				</el-tooltip>
				<el-tooltip content="列显示" placement="top">
					<el-button icon="fa fa-filter" @click="displayFilterColumnsDialog"></el-button>
				</el-tooltip>
				<el-tooltip content="导出" placement="top">
					<el-button icon="fa fa-file-excel-o"></el-button>
				</el-tooltip>
				</el-button-group>
			</el-form-item>
		</el-form>-->
		<!--表格显示列界面-->
		<table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns" 
			@handleFilterColumns="handleFilterColumns"> 
		</table-column-filter-dialog>
	</div>
	<!--表格内容栏-->
	<kt-table id="ktable" permsEdit="sys:user:edit" permsDelete="sys:user:delete"
		:data="pageResult" :columns="filterColumns" :seepost="true" :width="330"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" @seePost="seePost">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog v-dialogDrag :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false"
		@close="$refs.dataForm.resetFields()">
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="ID" prop="id" v-if="false">
				<el-input v-model="dataForm.id" :disabled="true" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="用户名" prop="name">
				<el-input v-model="dataForm.name" auto-complete="off"></el-input>
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
						:label="item.remark" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false;defaultValue = []">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
	<xyjpost-dialog ref="dialog" v-bind="query"></xyjpost-dialog>
	<el-dialog v-dialogDrag title="岗位信息" width="50%" :visible.sync="dialogVisibleUser" :close-on-click-modal="false" >
  	<el-table
      :data="tableList"
      border
      size="mini"
      style="width: 100%">
      <el-table-column
      	v-for="(item,index) in column"
      	:key="index"
        :prop="item.prop"
        :label="item.label"
        :formatter="item.formatter"
        min-width="80">
      </el-table-column>
    </el-table>
  </el-dialog>
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import xyjpostDialog from "@/components/xyjpostDialog/xyjpostDialog";
import axios from "@/http/axios";
import { format } from "@/utils/datetime"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
		xyjpostDialog
	},
	data() {
		return {
			flag:true,
			defaultValue:[],
			size: 'small',
			filters: {
				name: ''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 10 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			dialogVisibleUser:false,
			editLoading: false,
			dataFormRules: {
				name: [
					{ required: true, message: '请输入用户名', trigger: 'blur' }
				],
				deptName: [
					{ required: true, message: '请选择机构', trigger: 'change' }
				],
				sysPostId: [
					{ required: true, message: '请选择岗位', trigger: 'change' }
				]
			},
			// 新增编辑界面数据
			dataForm: {
				id: 0,
				name: '',
				password: '123456',
				deptId: 1,
				deptName: '',
				sysPostId:'ceshi',
				email: '',
				mobile: '',
				status: 1,
				userRoles: [],
				postUsers:[]
			},
			deptData: [],
			deptTreeProps: {
				label: 'name',
				children: 'children'
			},
			roles: [],
			query:{
    		cancel:function(row){console.log(row)},
    		confirm:this.postdialog,
    	},
    	tableList:[],
    	column:[
    		{label:'岗位名称',prop:'postName'},
    		{label:'编码',prop:'code'},
    		{label:'权限',prop:'queryRole',formatter:function(row,column,cellValue){
    			if(cellValue == 0){
    				return '全部'
    			}else if(cellValue == 1){
    				return '下级岗位'
    			}else if(cellValue == 2){
    				return '自己'
    			}
    		}},
    		{label:'是否是主岗位',prop:'isMain',formatter:function(row,column,cellValue){
    			return cellValue == 1 ? '是' : cellValue == 0 ? '否' : cellValue
    		}},
    		{label:'创建人',prop:'createBy'},
    		{label:'创建时间',prop:'createTime'},
    	]
		}
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}
			this.pageRequest.columnFilters = {name: {name:'name', value:this.filters.name}}
			this.$api.user.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
				this.findUserRoles()
			}).then(data!=null?data.callback:'')
		},
		// 加载用户角色信息
		findUserRoles: function () {
			this.$api.role.findAll().then((res) => {
				// 加载角色集合
				this.roles = res.data	
			})
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.user.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.editLoading = false
			this.dialogVisible = true
			this.operation = true
			this.dataForm = {
				id: 0,
				name: '',
				password: '',
				deptId: 1,
				deptName: '',
				sysPostId:'',
				email: '',
				mobile: '',
				status: 1,
				userRoles: [],
				postUsers:[]
			}
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.getPostMsg(params.row).then(res => {
				if(!res){
					this.dataForm = Object.assign({}, params.row)
//					for(let key in this.dataForm){
//						this.dataForm[key] = params.row[key]
//					}
					return
				}
				for(let key in this.dataForm){
					this.dataForm[key] = params.row[key]
				}
				// 显示岗位信息
				let html = '';
				this.dataForm.postUsers = res.data.map(data => {
					html += data.postName+'、';
					let obj = {};
					obj.sysPostId = data.id;
					obj.isMain = data.isMain;
					return obj;
				})
				this.dataForm.sysPostId = html.substr(0,html.length-1);
				this.defaultValue = res.data;
				let userRoles = []
				for(let i=0,len=params.row.userRoles.length; i<len; i++) {
					userRoles.push(params.row.userRoles[i].roleId)
				}
				this.dataForm.userRoles = userRoles
			})
			this.editLoading = false
			this.dialogVisible = true
			this.operation = false
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
//						let userRoles = []
//						for(let i=0,len=params.userRoles.length; i<len; i++) {
//							let userRole = {
//								userId: params.id,
//								roleId: params.userRoles[i]
//							}
//							userRoles.push(userRole)
//						}
						params.userRoles = params.userRoles.map(data => {
							let obj = {}
							obj.roleId = data
							return obj
						})
						delete params.sysPostId
						this.$api.user.save(params).then((res) => {
							this.editLoading = false
							if(res.code == 200) {
								this.$message({ message: '操作成功', type: 'success' })
								this.dialogVisible = false
								this.$refs['dataForm'].resetFields()
							} else {
								this.$message({message: '操作失败, ' + res.msg, type: 'error'})
							}
							this.findPage(null)
						})
						.catch(err=> {
								this.editLoading = false
							this.$message.error(err.message);
						})
				}
			})
		},
		getPostMsg(row){
			return new Promise((res,reg) => {
				axios({
	        // 用axios发送post请求
	        hostUrl: this.global.baseUrl,
	        method: "get",
	        url:"/sysPostUser/findPostByUserId?id="+row.id
	      })
				.then(data => {
					if(data.code == 200) res(data)
				})
				.catch(err => {
					res(false)
					this.$message.error(err.message);
				})
			})
		},
		seePost(row){
			// 查看岗位
			this.getPostMsg(row.row).then(res => {
				if(res){
					this.tableList = res.data;this.dialogVisibleUser = true
				}
			})
		},
		// 选择岗位信息
		showDialog(){
			this.operation ? this.$refs.dialog.open(true) : this.$refs.dialog.open(true,this.defaultValue)	
		},
		postdialog(row,isMain){
			this.defaultValue = row
			this.dataForm.sysPostId = '';
			// isMain  0--否   1--是
			this.dataForm.postUsers = row.map(data => {
				let text = data.postName;
				data.id == isMain ? text = text+'(主岗位)' : text
				this.dataForm.sysPostId += text+'、';
				let obj = {};
				obj.sysPostId = data.id;
				obj.isMain = data.id == isMain ? 1 : 0;
				return obj;
			})
			this.dataForm.sysPostId = this.dataForm.sysPostId.substring(0,this.dataForm.sysPostId.length-1)
		},
		// 获取部门列表
		findDeptTree: function () {
			this.$api.dept.findDeptTree().then((res) => {
				this.deptData = res.data
			})
		},
		// 菜单树选中
      	deptTreeCurrentChangeHandle(data, node) {
      		this.dataForm.deptId = data.id
      		this.dataForm.deptName = data.name
      	},// 时间格式化
      	dateFormat: function (row, column, cellValue, index){
          	return format(row[column.property])
      	},
		// 处理表格列过滤显示
      	displayFilterColumnsDialog: function () {
			this.$refs.tableColumnFilterDialog.setDialogVisible(true)
      	},
		// 处理表格列过滤显示
      	handleFilterColumns: function (data) {
			this.filterColumns = data.filterColumns
			this.$refs.tableColumnFilterDialog.setDialogVisible(false)
      	},
		// 处理表格列过滤显示
      	initColumns: function () {
			this.columns = [
				{prop:"id", label:"ID", minWidth:50},
				{prop:"name", label:"用户名", minWidth:120},
				{prop:"deptName", label:"机构", minWidth:120},
				{prop:"roleNames", label:"角色", minWidth:100},
				{prop:"email", label:"邮箱", minWidth:120},
				{prop:"mobile", label:"手机", minWidth:100},
				{prop:"status", label:"状态", minWidth:70},
				// {prop:"createBy", label:"创建人", minWidth:120},
				// {prop:"createTime", label:"创建时间", minWidth:120, formatter:this.dateFormat}
				// {prop:"lastUpdateBy", label:"更新人", minWidth:100},
				// {prop:"lastUpdateTime", label:"更新时间", minWidth:120, formatter:this.dateFormat}
			]
			this.filterColumns = JSON.parse(JSON.stringify(this.columns));
      	}
	},
	mounted() {
		this.findDeptTree()
		this.initColumns()
	}
}
</script>

<style scoped>
#ktable /deep/ .el-table__body-wrapper{max-height:420px!important;}
</style>
