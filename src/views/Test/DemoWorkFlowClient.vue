<template style="height: 100%;">
      <!--
        OnAgreeAfter：审批流成功提交后外部接口
        OnSelectAgreeAfter：选签成功提交后外部接口
        OnTransferSignatureAfter：转签成功后事件
        OnRejectNextLevelAfter：驳回上一级成功后,事件
        OnRejectFirstLevelAfter：驳回发起人成功后,事件，方便业务开发执行代码
        OnSelectRejectlAfter：选择驳回成功后事件，方便业务开发执行代码
        OnRowDoubleClickAgency: 双击待办数据后回调事件
        configDataTable：业务数据表格设置
      -->
		  <work-flow-client @OnAgreeAfter=""
                        @OnSelectAgreeAfter=""
                        @OnRowDoubleClickAgency = "OnRowDoubleClickAgency"
                        :configDataTable = "bDataTable"
                        :id="routeId"
                        ref="wfc" style="height: 90%;">
       <!-- 数据列表按钮区域-->
        <template slot="dataButtons" slot-scope="btn">
          数据列表按钮区域
          <Xyj-button @click="editTableClick" type="primary" size="mini" label="表格编辑" text="表格编辑" :disabled="btn.isDisabled"></Xyj-button>
        </template>

        <!--业务表单按钮区域-->
        <template slot="BusinessButton" slot-scope="btn">
          <Xyj-button @click="addClick" type="primary" size="mini" label="自定义增单" text="增单" :disabled="btn.isDisabled"></Xyj-button>
          <Xyj-button @click="addTableClick" type="primary" size="mini" label="表格增单" text="表格增单" :disabled="btn.isDisabled"></Xyj-button>
          <Xyj-button @click="EditTableClick" type="primary" size="mini" label="表格编辑" text="表格编辑" :disabled="btn.isDisabled"></Xyj-button>
          <Xyj-button @click="deleteTableClick" type="primary" size="mini" label="表格删除" text="表格删除" :disabled="btn.isDisabled"></Xyj-button>
        </template>

        <!--业务表单自定义区域-->
		    <template slot="Business" style="height: 99%;">
		      <div>
            <el-input v-model="tmpleaveName" placeholder="请输入请假类型"></el-input>
            <el-input v-model="tmpleaveCode" placeholder="请输入请假编号"></el-input>
            <el-input v-model="tmpleavePersion" placeholder="请输入请假人"></el-input>
            <el-input v-model="tmpleavePersionId" placeholder="请输入请假人ID"></el-input>
            <el-input v-model="tmpleaveMemo" placeholder="请输入请假备注"></el-input>
		      </div>
		    </template>

		  </work-flow-client>
 
</template>

<script>
    export default {
        name: "DemoWorkFlowClient",
        data(){
          return {
          	routeId:'',
            tmpleaveName: "请假测试",
            tmpleaveCode: "请假编号1",
            tmpleavePersion: "宋建国",
            tmpleavePersionId: "12334",
            tmpleaveMemo: "测试工作流",
            /*业务数据表格设置*/
            bDataTable:{
              /**
               *宋建国：2019/11/1 11:23
               *描述：表格扩展按钮
               *版本：1.0
               */
/*              extraButtons:[
                {
                  label: this.$i18n.t('workflow.Viewflowchart'),
                  type: "primary",
                  text: "查看流程图",
                  atClick: this.Viewflowchart
                }
              ],
              //字段显示转换
              formatters: {
                processDefinitionName: this.formatProcessDefinitionName
              },*/
              //要查询的类名
              tableName: "XyjLeave",
              //业务数据微服务,新增网址
              bNewUrl: {
                hostUrl: this.leave,
                url: "/xyjLeave/save",
                method: 'post',
                // clear: true, //清除新增对话框输入的数据
                close: true, //关闭新增对话框
              },
              //微服务查询接口
              bQuery:{
                hostUrl: this.leave,
                url: "/xyjLeave/query",
                method: "post",
                notCondition: false //没有查询条件则查询全部数据
              },
              //业务数据微服务,修改网址
              bEditUrl: {
                hostUrl: this.leave,
                url: "xyjLeave/save",
                method: 'post',
                // clear: true, //清除新增对话框输入的数据
                close: true  //关闭修改对话框
              },
              //业务数据微服务,删除网址
			        bDeleteUrl: {
			          hostUrl: this.leave,
			          url: "xyjLeave/delete",
			          method: "post"
			        },
            }
          }
        },
        methods: {
          /**
           *宋建国：2019/11/1 11:27
           *描述：双击待办数据后回调事件
           *版本：1.0
           */
          OnRowDoubleClickAgency(row, column, event){
            this.test = row.businessId;
          },
          /**
           *宋建国：2019/11/1 11:28
           *描述：字段显示转换
           *版本：1.0
           */
          formatProcessDefinitionName(row, column, cellValue, index){
            /*return "aaaa";*/
          },
          /**
           *宋建国：2019/11/7 13:55
           *描述：借用表格新增弹框
           *版本：1.0
           */
          addTableClick(roolback){
            this.$refs.wfc.addTableTicket();
            roolback();
          },
          /**
           *张俊娜：2019/11/8 
           *描述：借用表格编辑弹框
           *版本：1.0
           */
          EditTableClick(roolback){
          	// 传的true用于导航页切换
          	this.$refs.wfc.editTableTicket(true);
            roolback();
          },
          /**
           *张俊娜：2019/11/8 
           *描述：借用表格删除弹框
           *版本：1.0
           */
          deleteTableClick(roolback){
          	this.$refs.wfc.deleteTableTicket();
            roolback();
          },
          /**
           *宋建国：2019/11/7 16:15
           *描述：表格单据修改
           *版本：1.0
           */
          editTableClick(roolback){
            this.$refs.wfc.editTableTicket();
            roolback();
          },
          /**
           *宋建国：2019/9/25 13:36
           *描述：新增单据例子
           *版本：1.0
           */
          addClick(rollback){
            let then = this;
   /*         this.$api.common.createRuleNo(
              {id: '489043634973310976'}
            ).then((res) =>{*/

            then.$api.leave.save({
              leaveName: this.tmpleaveName,
              leaveCode: this.tmpleaveCode,
              leavePersion: this.tmpleavePersion,
              leavePersionId: this.tmpleavePersionId,
              leaveMemo: this.tmpleaveMemo
            },then).then((res) => {
              this.$refs.wfc.startWorkFlow(
                {
                businessKey: res.data.id, //与工作流关联ID
                variables: {}  //工作流程执行时的流程变量
                }
                ,function (resultData) { //成功回調
                  rollback();
                }
                ,function (err) {  //失敗回調
                  then.$message.info(res.message);
                  rollback();
                }
              )

            }).catch((err) =>{
              this.$message.info(err.message);
              rollback();
            });



/*
              //发起审批接口
              //businessKey : 业务ID
              //variables：审批流，流程变量
              this.$refs.wfc.startWorkFlow({
                businessKey: res.msg,
                variables: {}
              },function (resultData) {
                rollback();
              },function (err) {
                then.$message.info(res.message);
                rollback();
              })
            }).catch((err) =>{
              then.$message.error(err.message);
            });*/
          }
        },
        created(){
        	if(this.$route.query.id){
        		localStorage.setItem('routeId',this.$route.query.id)
        		this.routeId = localStorage.getItem('routeId')
        	}
        	if(localStorage.getItem('routeId')){
        		this.routeId = localStorage.getItem('routeId')
        	}
        },
        beforeDestroy(){
        	localStorage.removeItem('routeId')
        }
    }
</script>

<style scoped>

</style>
