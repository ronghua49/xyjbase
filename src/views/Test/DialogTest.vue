<template>
	<div class="bodyStyle">
		<!--按钮模板-->
    <Xyj-button type="primary" size="mini" :label="$t('action.testbutton')" text="测试按钮" @click="test($event,'a')"></Xyj-button>
    <Xyj-upload v-bind="upload"></Xyj-upload>
		<div style="margin-top: 10px;">
			<el-form label-width="80px" :model="formLabelAlign" size="mini">
			  <el-form-item label="岗位组件">
			    <el-input v-model="formLabelAlign.post" placeholder="点击选择岗位" @click.native="showDialog('post')"></el-input>
			  </el-form-item>
			  <el-form-item label="机构管理">
			    <el-input v-model="formLabelAlign.dept" placeholder="点击选择机构" @click.native="showDialog('dept')"></el-input>
			  </el-form-item>
			  <el-form-item label="流程定义id">
			    <el-input v-model="formLabelAlign.workflow" placeholder="流程定义id" @click.native="showDialog('workflow')"></el-input>
			  </el-form-item>
			  <el-form-item label="流程名称">
			    <el-input v-model="formLabelAlign.workflowName" placeholder="流程名称"></el-input>
			  </el-form-item>
			  <el-form-item label="员工信息">
			    <el-input type="textarea" rows="8" v-model="formLabelAlign.user" placeholder="点击选择员工" @click.native="showDialog('user')"></el-input>
			  </el-form-item>
			  <el-form-item label="字典管理">
			    <el-input type="textarea" rows="8" v-model="formLabelAlign.dict" placeholder="点击选择字典" @click.native="showDialog('dict')"></el-input>
			  </el-form-item>
			  <el-form-item label="子公司管理">
			    <el-input type="textarea" rows="8" v-model="formLabelAlign.branch" placeholder="点击选择子公司" @click.native="showDialog('branch')"></el-input>
			  </el-form-item>
			</el-form>
		</div>
		<!--员工信息弹框-->
		<xyj-user-dialog ref="userdialog" v-bind="user"></xyj-user-dialog>
		<!--机构管理弹框-->
    <xyjdept-dialog ref="deptDialog" v-bind="dept"></xyjdept-dialog>
    <!--字典管理弹框-->
    <xyjdict-dialog ref="dictDialog" v-bind="dict"></xyjdict-dialog>
    <!--子公司管理弹框-->
    <xyjbranch-dialog ref="branchDialog" v-bind="branch"></xyjbranch-dialog>
    <!--岗位选择弹框-->
    <xyjpost-dialog ref="postDialog" v-bind="post"></xyjpost-dialog>
    <!--工作流弹框-->
    <xyjworkflow-dialog ref="workflowDialog" v-bind="workflow"></xyjworkflow-dialog>
	</div>
</template>

<script>
	import XyjUpload from "@/components/XyjUpload/XyjUpload";
	import xyjUserDialog from "@/components/xyjUserDialog/xyjUserDialog";
	import xyjdeptDialog from "@/components/xyjdeptDialog/xyjdeptDialog";
	import xyjdictDialog from "@/components/xyjdictDialog/xyjdictDialog";
	import xyjbranchDialog from "@/components/xyjbranchDialog/xyjbranchDialog";
	import xyjpostDialog from "@/components/xyjpostDialog/xyjpostDialog";
	import xyjworkflowDialog from "@/components/xyjworkflowDialog/xyjworkflowDialog";
	export default{
		name:'DialogTest',
		components: { 
			XyjUpload,
			xyjUserDialog,
			xyjdeptDialog,
			xyjdictDialog,
			xyjworkflowDialog,
			xyjpostDialog,
			xyjbranchDialog
		},
		data(){
			return {
				formLabelAlign: {
          post: '',
          dept:'',
          user:'',
          dict:'',
          workflowName:'',
          workflow:'',
          branch:''
        },
        /** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * 岗位默认数据
				 * @author 张俊娜 
				 */
        defaultValue:[{id:"1"}],
        /** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * 机构默认数据
				 * @author 张俊娜 
				 */
        deptValue:[{id:"44"}],
        /** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * 员工默认数据
				 * @author 张俊娜 
				 */
        userValue:[],
        /** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * 字典默认数据
				 * @author 张俊娜 
				 */
        dictValue:[{id:"8"},{id:"9"}],
        /** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * 流程标识默认数据
				 * @author 张俊娜 
				 */
        workflowVaule:[{id:"MD20190924105107788000001:1:197504"},{id:"MD20190924105107788000001:2:210021"}],
				post:{
					/** 
					 * @date 2019-10-12
					 * 岗位弹框取消事件回调函数
					 * 参数row为选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		cancel:function(row){console.log(row);},
	    		/** 
					 * @date 2019-10-12
					 * 岗位弹框确认事件回调函数
					 * 有一个参数为选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		confirm:this.postdialog,
	    		/** 
					 * @date 2019-10-12
					 * @return {Boolean} 返回值 
					 * 是否是单选 true为单选，false为多选
					 * @author 张俊娜 
					 */
					isRadio:false
	    	},
	    	dict:{
	    		/** 
					 * @date 2019-10-12
					 * 字典弹框取消事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		cancel:function(row){console.log(row)},
	    		/** 
					 * @date 2019-10-12
					 * 字典弹框确认事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		confirm:this.dictdialog,
	    		/** 
					 * @date 2019-10-12
					 *  需要查询的类型，传入字段名
					 * @author 张俊娜 
					 */
	    		type:'counttype',
	    		/** 
					 * @date 2019-10-12
					 * @param {Object} str
					 * @return {Boolean} 返回值 
					 * 是否是单选 true为单选，false为多选
					 * @author 张俊娜 
					 */
					isRadio:false
	    	},
	    	dept:{
	    		/** 
					 * @date 2019-10-12
					 * 机构管理弹框取消事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		cancel:function(row){console.log(row);this.deptValue = []},
	    		/** 
					 * @date 2019-10-12
					 * 机构管理弹框确认事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		confirm:this.deptdialog,
	    		/** 
					 * @date 2019-10-12
					 * @param {Object} str
					 * @return {Boolean} 返回值 
					 * 是否是单选 true为单选，false为多选
					 * @author 张俊娜 
					 */
					isRadio:false
	    	},
	    	user:{
	    		/** 
					 * @date 2019-10-12
					 * 员工弹框取消事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		cancel:function(row){console.log(row)},
	    		/** 
					 * @date 2019-10-12
					 * 员工弹框确认事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		confirm:this.userdialog,
	    		/** 
					 * @date 2019-10-12
					 * @param {Object} str
					 * @return {Boolean} 返回值 
					 * 是否是单选 true为单选，false为多选
					 * @author 张俊娜 
					 */
					isRadio:false
	    	},
	    	workflow:{
	    		/** 
					 * @date 2019-10-12
					 * 流程标识弹框取消事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		cancel:function(row){console.log(row)},
	    		/** 
					 * @date 2019-10-12
					 * 流程标识弹框确认事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		confirm:this.workflowdialog,
	    		/** 
					 * @date 2019-10-12
					 * @param {Object} str
					 * @return {Boolean} 返回值 
					 * 是否是单选 true为单选，false为多选
					 * @author 张俊娜 
					 */
					isRadio:true
	    	},
	    	branch:{
	    		/** 
					 * @date 2019-10-12
					 * 字典弹框取消事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		cancel:function(row){console.log(row)},
	    		/** 
					 * @date 2019-10-12
					 * 字典弹框确认事件回调函数
					 * 一个参数，表示选中的数据，类型是数组
					 * @author 张俊娜 
					 */
	    		confirm:this.branchdialog,
	    		/** 
					 * @date 2019-10-12
					 * @param {Object} str
					 * @return {Boolean} 返回值 
					 * 是否是单选 true为单选，false为多选
					 * @author 张俊娜 
					 */
					isRadio:false
	    	},
	    	upload:{
	    		uploadUrl:{
		    		hostUrl:this.global.baseUrl,
			      url:'',
			      upParam:function(data){  // 上传需要的参数
			      	// data --> 选择的上传文件
			      	console.log(data)
			      	let fd = new FormData();
              fd.append('file',data.file);
              return fd;
			      },
			      upSuccess:function(_this,res){
			      	// 上传成功时的逻辑  res --> 上传成功接口返回数据
			      }
		    	},
		    	label:'action.Import'
	    	}
			}
		},
		methods:{
	  	showDialog(name){ // 岗位选择弹框弹出
	  		switch(name){
	  			case 'post':
	  			/** 
					 * @date 2019-10-12
					 * 第一个参数是否刷新，为true时刷新，false不刷新，第二个参数传入的是默认值 目前传的类型是数组 必须有id字段 例如[{id:'1'}]
					 * @author 张俊娜 
					 */
	  				this.$refs.postDialog.open(true,this.defaultValue);
	  				break;
	  			case 'dept':
	  			/** 
					 * @date 2019-10-12
					 * 第一个参数是否刷新，为true时刷新，false不刷新，第二个参数传入的是默认值 目前传的类型是数组 必须有id字段 例如[{id:'1'}]
					 * @author 张俊娜 
					 */
	  				this.$refs.deptDialog.open(true,this.deptValue);
	  				break;
	  			case 'user':
	  			/** 
					 * @date 2019-10-12
					 * @param {Boolean} 
					 * 第一个参数是否刷新，为true时刷新，false不刷新，第二个参数传入的是默认值 目前传的类型是数组 必须有id字段 例如[{id:'1'}]
					 * @author 张俊娜 
					 */
	  				this.$refs.userdialog.open(false,this.userValue);
	  				break;
	  			case 'dict':
	  			/** 
					 * @date 2019-10-12
					 * @param {Boolean} 
					 * 第一个参数是否刷新，为true时刷新，false不刷新，第二个参数传入的是默认值 目前传的类型是数组 必须有id字段 例如[{id:'1'}]
					 * @author 张俊娜 
					 */
	  				this.$refs.dictDialog.open(true,this.dictValue);
	  				break;
	  			case 'workflow':
	  			/** 
					 * @date 2019-10-12
					 * @param {Boolean} 
					 * 第一个参数是否刷新，为true时刷新，false不刷新，第二个参数传入的是默认值 目前传的类型是数组 必须有id字段 例如[{id:'1'}]
					 * @author 张俊娜 
					 */
	  				this.$refs.workflowDialog.open(true,this.workflowVaule);
	  				break;
	  			case 'branch':
	  			/** 
					 * @date 2019-10-12
					 * @param {Boolean} 
					 * 第一个参数是否刷新，为true时刷新，false不刷新，第二个参数传入的是默认值 目前传的类型是数组 必须有id字段 例如[{id:'1'}]
					 * @author 张俊娜 
					 */
	  				this.$refs.branchDialog.open(true);
	  				break;
	  		}
	  	},
	  	postdialog(row,isMain){ // 选择岗位后的操作
				// 是否是主岗位 isMain  0--否   1--是
				this.defaultValue = row
				let list = JSON.parse(JSON.stringify(row));
				this.formLabelAlign.post=''
				list.forEach(element => {
					let text = element.postName
					element.id == isMain ? text = text+'(主岗位)' : text
					this.formLabelAlign.post += text+'、'
				})
				this.formLabelAlign.post = this.formLabelAlign.post.substr(0,this.formLabelAlign.post.length-1)
			},
			dictdialog(row){
				// 字典管理弹框  row --> 选中的数据是一个数组
				let dictList = JSON.parse(JSON.stringify(row));
				let html = '';
				dictList.forEach(element => {
					html += JSON.stringify(element)+'\n';
				})
				this.formLabelAlign.dict = html.substr(0,html.length-1)
				
			},
			branchdialog(row){
				this.dictValue = row
				// 字典管理弹框  row --> 选中的数据是一个数组
				let branchList = JSON.parse(JSON.stringify(row));
				let html = '';
				branchList.forEach(element => {
					html += JSON.stringify(element)+'\n';
				})
				this.formLabelAlign.branch = html.substr(0,html.length-1)
			},
			deptdialog(row){
				this.deptValue = row;
				// 机构管理弹框 row --> 选中的数据是数组
				let deptList = JSON.parse(JSON.stringify(row));
				let html = '';
				deptList.forEach(element => {
					html += element.name + '、';
				})
				this.formLabelAlign.dept = html.substr(0,html.length-1);
				
			},
			userdialog(row){
				this.userValue = row;
				// 员工信息弹框 row --> 选中的数据是一个数组
				let userList = JSON.parse(JSON.stringify(row));
				let html = '';
				userList.forEach(element => {
					html += JSON.stringify(element)+'\n';
				})
				this.formLabelAlign.user = html.substr(0,html.length-1)
			},
			workflowdialog(row){
				this.workflowVaule = row;
				// 工作流信息弹框 row --> 选中的数据是一个数组
				if(row.length>0){
					this.formLabelAlign.workflow = row[0].key
					this.formLabelAlign.workflowName = row[0].name
				}else{
					this.formLabelAlign.workflow = ''
					this.formLabelAlign.workflowName = ''
				}
			},
			// 测试按钮事件
			test(cab){
				let msg = '回调测试';
				cab()
			}
		}
	}
</script>

<style scoped>
</style>