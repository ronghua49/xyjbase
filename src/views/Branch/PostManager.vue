<template>
	<div>
	  <div style="margin-bottom: 10px; text-align: left;">
	  	<Xyj-button type="primary" size="mini" @click="add" :label="$t('action.add')" text="新增" />
	  	<Xyj-button type="primary" size="mini" @click="edit" :label="$t('action.edit')" text="编辑" />
	  	<Xyj-button type="danger" size="mini" @click="deletePost" :label="$t('action.delete')" text="删除" />
	  	<Xyj-button type="primary" size="mini" @click="seePostUser" :label="$t('action.viewemployees')" text="查看员工" />
	  </div>
    <el-input
		  placeholder="输入关键字进行过滤"
		  v-model="filterText">
		</el-input>
		<el-tree
			:data="data"
			node-key="id"
		  :props="props"
		  ref="tree"
		  :filter-node-method="filterNode"
		  :check-strictly="true"
		  :check-on-click-node="true"
		  @node-click="change"
		  @check="change"
		  show-checkbox>
		</el-tree>
		<el-dialog v-dialogDrag :title="title" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false"
			@close="$refs.dataForm.resetFields()" >
      <el-form :model="formData" :rules="dataRule" ref="dataForm" label-width="80px" size="small" style="text-align:left;">
        <el-form-item label="上级名称" prop="parentName">
          <el-input v-model="formData.parentName" placeholder="名称" disabled></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="编码"></el-input>
        </el-form-item>
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="formData.name" placeholder="岗位名称"></el-input>
        </el-form-item>
        <el-form-item label="权限" prop="queryRole">
          <el-select style="width: 100%;" v-model="formData.queryRole" placeholder="请选择权限">
				    <el-option
				      v-for="item in options"
				      :key="item.value"
				      :label="item.label"
				      :value="item.value">
				    </el-option>
				  </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="formData.remarks" placeholder="备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini"  @click="dialogVisible = false;$refs.dataForm.resetField()">{{$t('action.cancel')}}</el-button>
        <el-button size="mini" :loading="loading" type="primary" @click="confirm">{{$t('action.comfirm')}}</el-button>
      </span>
  </el-dialog>
  <el-dialog v-dialogDrag title="员工信息" width="60%" :visible.sync="dialogVisibleUser" :close-on-click-modal="false" >
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
        min-width="180">
      </el-table-column>
    </el-table>
  </el-dialog>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default{
		name:'PostManager',
		watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
		data(){
			return {
				title:'',
				dialogVisible:false,
				dialogVisibleUser:false,
				loading:false,
				filterText: '',
				props: {
          label: 'name',
          children: 'children'
       },
        data:[],
        formData:{
        	cid:'', // 父级id
        	parentName:'',
        	code:'',
        	name:'',
        	queryRole:'',
        	remarks:'',
        	id:'',
        	branchCode:sessionStorage.getItem('branchCode')
        },
        dataRule:{
					code: [
						{ required: true, message: '请输入编码', trigger: 'change' }
					],
					name: [
						{ required: true, message: '请输入新增名称', trigger: 'change' }
					],
					queryRole: [
						{ required: true, message: '请选择权限', trigger: 'change' }
					]
        },
        options:[],
        tableList:[],
        column:[
        	{label:'用户名',prop:'name'},
        	{label:'机构',prop:'deptName'},
        	{label:'角色',prop:'roleNames'},
        	{label:'邮箱',prop:'email'},
        	{label:'手机',prop:'mobile'},
        	{label:'状态',prop:'status'},
        ]
			}
		},
		methods:{
			deletePost(callback){
				let arr  = this.$refs.tree.getCheckedKeys();
				if(arr.length>0){
					axios({
		        // 用axios发送post请求
		        hostUrl: this.global.baseUrl,
		        method: "post",
		        url:"/sysBranchPost/delete",
		        data:this.$refs.tree.getCheckedKeys()
		      })
					.then(res => {
						if(res.code == 200) this.$message({message: '删除成功',type: 'success'});this.getData();
						callback()
					})
					.catch(err => {
						this.$message.error(err.message);
						callback()
					})
				}else{
    			this.$message({
	          message: '请选择一个岗位',
	          type: 'warning'
	        });
	        callback()
    		}	
			},
			// 筛选时的执行方法
			filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
    	},
    	change(data,isChecked){
      	// 节点选中状态发生变化
      	this.$refs.tree.setCheckedNodes([data])
      },
      getQueryRole(){
      	// 获取权限信息
      	let pageRequest = {
	     		querys: [{
			      hasInitValue: true,
			      operation: "EQUAL",
			      property: "type",
			      relation: "AND",
			      value:"postQueryType"
			    }],
			    sorter: [
				    {
				      direction: "ASC",
				      property: "sort"
				    }
				  ]
	     	}
    		axios({
	        // 用axios发送post请求
	        hostUrl: this.global.baseUrl,
	        method: "post",
	        url:"/dict/query",
	        data:pageRequest
	      })
				.then(res => {
					if(res.code == 200) this.options = res.data.rows
				})
				.catch(err => {
					this.$message.error(err.message);
				})
      },
    	add(callback){
    		let arr  = this.$refs.tree.getCheckedKeys();
    		if(arr.length>0){
    			this.title = "新增";
	    		this.dialogVisible = true;
	    		this.$nextTick(()=>{ 
	    			this.$refs.dataForm.resetFields();
	    			for(let key in this.formData){
	    				if(key === 'parentName') this.formData.parentName = this.$refs.tree.getCheckedNodes()[0].name
		    			else this.formData[key] = ''
		    		}
		     		this.formData.cid = this.$refs.tree.getCheckedNodes()[0].id
	    		})
		     	this.getQueryRole();
		     	callback()
    		}else{
    			this.$message({
	          message: '请选择一个岗位',
	          type: 'warning'
	        });
	        callback()
    		}		
    	},
    	edit(callback){
    		let arr  = this.$refs.tree.getCheckedKeys();
    		this.formData.id = arr;
    		if(arr.length>0){
    			this.title = "修改";
	    		this.dialogVisible = true;
	    		this.getQueryRole();
					// 将需要修改的信息赋值到表单中
	    		let data = this.$refs.tree.getCheckedNodes();
	    		let array = []
					this.fun(data[0].cid,this.data,array)
	    		for(let key in this.formData){	
	    			if(key === 'parentName') this.formData[key] = array.length>0 ? array[0].name : data[0].name.split('|')[0]
	    			else if(key === 'cid') this.formData[key] = array.length>0 ? array[0].id : data[0].id
	    			else if(key === 'parentName') this.formData[key] = data[0].name.split('|')[0]
	    			else this.formData[key] = data[0][key]
	    		}
	    		callback()
    		}else{
    			this.$message({
	          message: '请选择一个岗位',
	          type: 'warning'
	        });
	        callback()
    		}		
    	},
    	seePostUser(callback){
    		let arr  = this.$refs.tree.getCheckedKeys();
    		if(arr.length>0){
    			axios({
		        // 用axios发送post请求
		        hostUrl: this.global.baseUrl,
		        method: "get",
		        url:"/sysBranchPostUser/findUserById?id="+this.$refs.tree.getCheckedKeys()[0],
		      })
					.then(res => {
						if(res.code == 200) this.dialogVisibleUser = true; this.tableList = res.data;
						callback()
					})
					.catch(err => {
						this.$message.error(err.message);
						callback()
					})
    		}else{
    			this.$message({
	          message: '请选择一个岗位',
	          type: 'warning'
	        });
	        callback()
    		}
    	},
    	confirm(){
    		this.$refs.dataForm.validate((valid)=> {
    			if (valid){
    				this.loading = true;
    				let name = this.formData.parentName;
    				delete this.formData.parentName
    				this.formData.branchCode = sessionStorage.getItem('branchCode')
    				axios({
			        // 新增
			        hostUrl: this.global.baseUrl,
			        method: "post",
			        url:"/sysBranchPost/save",
			        data:this.formData
			      })
						.then(res => {
							if(res.code == 200) {
								this.dialogVisible = false;
								this.getData();
								this.$nextTick(()=> {
			    				this.$refs.dataForm.resetFields();
			    			})
								this.$message({
				          message: this.title+'成功',
				          type: 'success'
				        });
				        this.loading = false;
							};
						})
						.catch(err => {
							this.formData.parentName = name;
							this.$message.error(err.message);
							this.loading = false;
						})
    			}
    		})
    	},
    	noRepeat(arr,value) { //id去重
		    for(let i = 0;i < arr.length;i++) {
	        if(arr[i] == value){
	          return false
	        }
	 			}
			  return true
			},
    	fun(value,data,arr) {   //通过查询父节点下是否有子节点 将父节点加入数组中
    		for(let res of data) {
					if(res.children && res.children.length>0) {
						if(res.id == value) {
							if (this.noRepeat(arr, res.id)) {
			 					arr.push(res)
						 	}
						}
						this.fun(value,res.children,arr)
					}
				}
			},
			getData(){
				// 获取数据
				axios({
	        // 用axios发送post请求
	        hostUrl: this.global.baseUrl,
	        method: "get",
	        url:"/sysBranchPost/findTree?branchCode="+sessionStorage.getItem('branchCode')
	      })
				.then(res => {
					if(res.code == 200) {
						this.data = JSON.parse(JSON.stringify(res.data));
						function formatter(data){
							data.forEach(element => {
								if(element.children && element.children.length>0){
									let flag = formatter(element.children);
									if(flag) return false;
								}else{
									element.name = element.name + ' | ' + element.code	
									return true;
								}	
								element.name = element.name + ' | ' + element.code
							})
						}
						formatter(this.data)
					}
				})
				.catch(err => {
					this.$message.error(err.message);
				})
			}
		},
		created(){
			this.getData();
		}
	}
</script>

<style scoped>
	/deep/ .el-dialog__footer {
    text-align: right!important;
  }
</style>