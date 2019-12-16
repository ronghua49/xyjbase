<template>
  <div style="height: 100%;">
    <el-row class="m_all" :gutter="15">
      <!--模型类别-->
      <el-col :span="4" class="m_left">
        <div @click.ctrl="showCode" style="width: 278px;">
          <el-tree
            :data="data1"
            show-checkbox
            default-expand-all
            node-key="id"
            ref="tree"
            highlight-current
            :check-strictly="true"
		  			:check-on-click-node="true"
		  			@node-click="change"
		  			@check="changes"
		  			:default-checked-keys="defaultKey"
            :props="defaultProps">
          </el-tree>
        </div>
      </el-col>
      <el-col :span="20" class="m_right">
        <div class="grid-content">
          <!--模型列表-->
          <xyj-table-work-flow ref="reftableworkflow" id="1" v-bind="tableworkflow">
          </xyj-table-work-flow>
        </div>
      </el-col>
    </el-row>
    <model-design ref="modeldesing"></model-design>
    <el-dialog
		  title="流程图"
		  :visible.sync="dialogVisible"
		  width="796px">
		  <div style="width: 690px;overflow: auto;">
		  	<img :src="src" alt="" />
		  </div>
		  <span slot="footer" class="dialog-footer">
		    <el-button @click="dialogVisible = false" size="mini">{{ $t('action.cancel') }}</el-button>
		    <el-button type="primary" @click="dialogVisible = false" size="mini">{{ $t('action.comfirm') }}</el-button>
		  </span>
		</el-dialog>
  </div>
</template>

<script>
  import ModelDesign from "@/components/WorkFlow/ModelDesign";
  import axios from "@/http/axios";
  export default {
    name: "Example",
    components: {ModelDesign},
    data() {
      return {
        /**
         *宋建国：2019/8/7 15:33
         *描述：审批模型表格
         */
        tableworkflow: {
          hasDelete: false, //删除
          hasNew: false, //新增
          hasView: false, //查看
          hasEdit: false, //编辑
          //不显示表格操作列
          hasOperation: true,
          /**
           *宋建国：2019/8/7 17:39
           *描述：自定义按钮
           */
          extraButtons:[
            {
              label: 'workflow.Viewflowchart',
              type: "primary",
              text: "查看流程图",
              atClick: this.Viewflowchart
            },
            {
              label: 'workflow.hangup',
              type: "warning",
              text: "挂起",
              atClick: this.hangup,
              flag:true
            },
            {
              label: 'workflow.delete',
              type: "danger",
              text: "删除",
              atClick: this.remove
            },
          ],
          changeBtns:{
          	label: 'workflow.activation',
            type: "primary",
            text: "激活",
            atClick: this.hangup
          },
          //el-table设置
          tableAttrs: {
            height: "89%",
            width: "90%"
          },
          //el-table事件设置
          tableOns: {

          },
          //表格中的操作列设置
          operationAttrs: {
            width: "210px"
          },
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            //state:  utilmp.approvalStateChine
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "ProcessInstanceVo",
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.workflow,
            url: "/processInstance/getAllProcessDefinitionList",
            method: "post",
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {
              0: { field0: "category", gx0: "EQUAL", value0: "BpmTree", andor0: "AND" }
          }, //表格初始化时查询条件
          codeVaule:this.codeVaule,
          sortVaule:this.sortVaule
        },
        data1: [],
        defaultProps: {
          children: 'childList',
          label: 'name'
        },
        defaultKey:[],
        src:'',
        dialogVisible:false
      };
    },
    /**
     *宋建国：2019/8/7 17:47
     *描述：方法
     */
    methods:{
    	//显示编码
    	showCode(){
    		this.$message.info('编码:BpmTree')
    	},
    	codeVaule(data){
    		this.$nextTick(() => {
    			let code = this.$refs.tree.getCheckedNodes()[0].code
    			let obj = {hasInitValue: false, operation: "EQUAL", property: "category", relation: "AND", value:code }
    			data.querys && data.querys.length>0 
    			? data.querys.forEach(res => {
			    	if(res.value != obj.value){
			    		data.querys.push(obj)
			    	}
			    })
    			: data.querys.push(obj)
    		})
    	},
    	sortVaule(){
    		return {0: { field0: "category", gx0: "EQUAL", value0:this.$refs.tree.getCheckedNodes()[0].code , andor0: "AND" }}
    	},
    	change(data,isChecked){
    		// 节点选中状态发生变化
      	this.$refs.tree.setCheckedNodes([data])
      	
    	},
    	changes(data){
    		this.$refs.tree.setCheckedNodes([data])
    		this.$refs.reftableworkflow.getList({0: { field0: "category", gx0: "EQUAL", value0: data.code, andor0: "AND" }})
    	},
      /**
       *宋建国：2019/8/7 17:50
       *描述：查看流程图
       */
      Viewflowchart: function (row) {
      	let _this = this;
      	return new Promise((res,rej)=>{
      		_this.$api.workflow.seeWorkflow(row.id).then((res) => {
            _this.src = 'data:image/png;base64,' + btoa(new Uint8Array(res).reduce((data, byte) => data + String.fromCharCode(byte), ''))
            _this.dialogVisible = true
          }).catch((err) => {
            _this.$message.error(err.message);
          })
          res(false)
      	})  
      },
      /**
       *宋建国：2019/8/7 17:50
       *描述：删除
       */
      remove:function(row){
      	let _this = this;
      	return new Promise((res,rej)=>{
      		_this.$api.workflow.deleteProessdefInstance(row.id).then((res) => {
            if(res.code == 200) {
            	_this.$message({
            		type:'success',
            		message:res.msg
            	})
            	_this.$refs.reftableworkflow.getList({0: { field0: "category", gx0: "EQUAL", value0: _this.$refs.tree.getCheckedNodes()[0].code, andor0: "AND" }})
            }
          }).catch((err) => {
            _this.$message.error(err.message);
          })
          res(false)
      	}) 
      },
      /**
       *宋建国：2019/8/7 17:50
       *描述：挂起/激活
       */
      hangup:function(row){
      	let _this = this;
      	return new Promise((res,rej)=>{
      		_this.$api.workflow.suspendProcessInstanceById(row.id).then((res) => {
            if(res.code == 200){
            	_this.$message({
            		type:'success',
            		message:res.msg
            	})
	            _this.$refs.reftableworkflow.getList({0: { field0: "category", gx0: "EQUAL", value0: _this.$refs.tree.getCheckedNodes()[0].code, andor0: "AND" }})
            }
          }).catch((err) => {
            _this.$message.error(err.message);
          })
          res(false)
      	}) 
      }
    },
    created(){
    	axios({
        // 用axios发送post请求
        hostUrl: this.global.baseUrl,
        method: "get",
        url:"/sysTree/fiandByCode?code=BpmTree",
      })
			.then(res => {
				if(res.code == 200) {
					this.data1 = JSON.parse(JSON.stringify([res.data]));
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
					formatter(this.data1)
				}
				this.defaultKey = [this.data1[0].id];
			})
			.catch(err => {
				this.$message.error(err.message);
			})
    }
  }
</script>

<style scoped>
  .m_all{
    height: 100%;
  }
  .m_left{
    overflow: auto;
    height: 100%;
  }
  .m_right:before{
    content: '';
    position: absolute;
    top: -10px;
    bottom: -10px;
    left: 0;
    width: 1px;
    background: #E4E7ED;
  }
  .m_left>div{
    height: 100%;
    overflow: auto;
  }
  .grid-content,.m_right{
  	position: relative;
    height: 100%;
  }
</style>
