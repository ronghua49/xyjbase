<template>
  <div style="height: 100%;">
    <div class="grid-content">
      <!--模型列表-->
      <xyj-table-work-flow ref="reftableworkflow" id="1" v-bind="tableworkflow">
      </xyj-table-work-flow>
      <el-dialog
			  title="流程图"
			  :visible.sync="dialogVisible"
			  width="796px">
			  <div style="width: 690px;overflow: auto;">
			  	<img :src="src" alt="" />
			  </div>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="dialogVisible = false" size="mini">取 消</el-button>
			    <el-button type="primary" @click="dialogVisible = false" size="mini">确 定</el-button>
			  </span>
			</el-dialog>
    </div>
  </div>
</template>

<script>
  import ModelDesign from "@/components/WorkFlow/ModelDesign";
  import axios from "@/http/axios";
  export default {
    name: "Task",
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
          ],
          //el-table设置
          tableAttrs: {
            height: "89%",
            width: "90%"
          },
          //el-table事件设置
          tableOns: {
						"row-dblclick": this.dbclick
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
          tableName: "ProcessTaskVo",
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.workflow,
            url: "/task/getAllTask",
            method: "post",
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {
//            0: { field0: "category", gx0: "EQUAL", value0: "BpmTree", andor0: "AND" }
          }, //表格初始化时查询条件
        },
        src:'',
        dialogVisible:false
      };
    },
    /**
     *宋建国：2019/8/7 17:47
     *描述：方法
     */
    methods:{
    	/**
       *宋建国：2019/8/7 17:50
       *描述：查看流程图
       */
      Viewflowchart: function (row) {
      	let _this = this;
      	return new Promise((res,rej)=>{
      		_this.$api.workflow.seeWorkflowTask(row.id).then((res) => {
             _this.src = 'data:image/png;base64,' + btoa(new Uint8Array(res).reduce((data, byte) => data + String.fromCharCode(byte), ''))
            _this.dialogVisible = true
          }).catch((err) => {
            _this.$message.error(err.message);
          })
          res(false)
      	})  
      },
      dbclick(row,column){
      	let url = ''
      	if(row.formRout.search("/") != -1){
      		url = row.formRout.substr(1)
      	}
      	let flag = true
      	this.$router.options.routes[0].children.forEach(x => {
      		if(x.path == url){
      			flag = false
      		}
      	})
      	if(flag){
      		this.$router.options.routes[0].children.push({name: row.name,path: row.formRout})
      		this.$router.addRoutes(this.$router.options.routes)
      	}
      	row.formRout ? this.$router.push({ path: row.formRout,query:{id:row.processInstanceId} }) : this.$message.error('该条数据没有配置表单路由，请配置！')
      }
    }
  }
</script>

<style scoped>
  .m_all{
    height: 100%;
  }
  .m_left{
    position: relative;
    height: 100%;
  }
  .m_left:after{
    content: '';
    position: absolute;
    top: -10px;
    bottom: -10px;
    right: 0;
    width: 1px;
    background: #E4E7ED;
  }
  .m_left>div{
    height: 100%;
    overflow: auto;
  }
  .grid-content,.m_right{
    height: 100%;
  }
</style>
