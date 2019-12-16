<template>
	<div>
		<div style="margin-bottom: 10px; text-align: left;">
	  	<Xyj-button type="primary" size="mini" @click="add" :label="$t('action.add')" text="新增"></Xyj-button>
	  	<Xyj-button type="primary" size="mini" @click="edit" :label="$t('action.edit')" text="编辑"></Xyj-button>
	  	<Xyj-button type="danger" size="mini" @click="deletePost" :label="$t('action.delete')" text="删除"></Xyj-button>
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
		<el-dialog v-dialogDrag :title="title" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" @close="$refs.dataForm.resetFields()">
      <el-form :model="formData" :rules="dataRule" ref="dataForm" label-width="80px" size="small" style="text-align:left;">
        <el-form-item label="上级名称" prop="postName">
          <el-input v-model="formData.postName" placeholder="名称" disabled></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="编码"></el-input>
        </el-form-item>
        <el-form-item label="新增名称" prop="name">
          <el-input v-model="formData.name" placeholder="新增名称"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" placeholder="备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini"  @click="dialogVisible = false;$refs.dataForm.resetFields()">{{$t('action.cancel')}}</el-button>
        <el-button size="mini" :loading="loading" type="primary" @click="confirm">{{$t('action.comfirm')}}</el-button>
      </span>
   	</el-dialog>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default{
		name:'classification',
		watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
		data(){
			return {
				title:'',
				dialogVisible:false,
				loading:false,
				filterText: '',
				props: {
          label: 'name',
          children: 'childList'
       },
        data:[],
        formData:{
        	cid:'', // 父级id
        	name:'',
        	code:'',
        	postName:'',
        	remark:'',
        },
        dataRule:{
        	name: [
						{ required: true, message: '请输入上级名称', trigger: 'change' }
					],
					code: [
						{ required: true, message: '请输入编码', trigger: 'change' }
					],
					postName: [
						{ required: true, message: '请输入新增名称', trigger: 'change' }
					]
        }
			}
		},
		methods:{
			deletePost(callback){
				axios({
	        // 用axios发送post请求
	        hostUrl: this.global.baseUrl,
	        method: "post",
	        url:"/sysTree/delete",
	        data:this.$refs.tree.getCheckedKeys()
	      })
				.then(res => {
					if(res.code == 200) this.$message({message: '删除成功',type: 'success'});this.getList();
					callback();
				})
				.catch(err => {
					this.$message.error(err.message);
					callback();
				})
			},
			change(data,isChecked){
      	// 节点选中状态发生变化
      	this.$refs.tree.setCheckedNodes([data])
      },
			// 筛选时的执行方法
			filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
    	},
    	add(callback){
    		let arr  = this.$refs.tree.getCheckedKeys();
    		if(arr.length>0){
    			for(let key in this.formData){
    				this.formData[key] = ''
    			}
    			this.title = "新增";
    			this.dialogVisible = true;
    			this.formData.postName = this.$refs.tree.getCheckedNodes()[0].name
    			this.formData.cid = this.$refs.tree.getCheckedNodes()[0].id
    		}else{
    			this.$message({
	          message: '请选择一个节点',
	          type: 'warning'
	        });
    		}
    		callback()
    	},
    	edit(callback){
    		let arr  = this.$refs.tree.getCheckedKeys();
    		callback()
    		if(arr.length>0){
    			axios({
		        // 新增
		        hostUrl: this.global.baseUrl,
		        method: "get",
		        url:"/sysTree/findById?id="+this.$refs.tree.getCheckedNodes()[0].id
		      })
					.then(res => {
						if(res.code == 200){
							this.title = "修改";
		    			this.dialogVisible = true;
		    			for(let key in this.formData){
								this.formData[key] = res.data[key]
							}
		    			let array = []
							for(let i = 0;i < arr.length;i++) {
							  this.fun(arr[i],this.data,array)
							}
		    			array.length>0? this.formData.postName = array[0].name : this.formData.postName = this.$refs.tree.getCheckedNodes()[0].name
		    			array.length>0? this.formData.cid = array[0].id : this.formData.cid = this.$refs.tree.getCheckedNodes()[0].id
		    			this.formData.id = this.$refs.tree.getCheckedNodes()[0].id
						}
					})
					.catch(err => {
						this.$message.error(err.message);
					})
    		}else{
    			this.$message({
	          message: '请选择一个节点',
	          type: 'warning'
	        });
    		}
    	},
    	confirm(){
    		this.loading = true;
    		let name = this.formData.postName;
    		delete this.formData.postName
    		axios({
	        // 新增
	        hostUrl: this.global.baseUrl,
	        method: "post",
	        url:"/sysTree/save",
	        data:this.formData
	      })
				.then(res => {
					if(res.code == 200) {
						this.dialogVisible = false;
						this.getList();
						this.$message({
		          message: this.title+'成功',
		          type: 'success'
		        });
		        this.loading = false;
					}
				})
				.catch(err => {
					this.formData.postName = name;
					this.$message.error(err.message);
					this.loading = false;
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
			 	for( let i  = 0;i < data.length;i++){
				 	let mod = data[i]
				 	if(mod.id == value) {
				 		return true;
				 	}else {
				 		if(mod.childList) {
					 		let bool = this.fun(value, mod.childList, arr)
				 			if(bool) {
				 				if (this.noRepeat(arr, mod.id)) {
				 					arr.push(mod)
							 	}
				 				return false
				 			}
				 		}
				 	}
				}
			},
			getList(){
				// 获取分类管理数据
				axios({
	        // 用axios发送post请求
	        hostUrl: this.global.baseUrl,
	        method: "get",
	        url:"/sysTree/list"
	      })
				.then(res => {
					if(res.code == 200) {
						this.data = JSON.parse(JSON.stringify(res.data));
						function formatter(data){
							data.forEach(element => {
								if(element.childList && element.childList.length>0){
									let flag = formatter(element.childList);
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
			// 获取数据
			this.getList()
		}
	}
</script>

<style scoped>
</style>