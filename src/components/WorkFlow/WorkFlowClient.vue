<template>
  <div>
		<el-tabs id="workflowtab" v-model="activeName" @tab-click="handleClick" style="height: 100%;">
      <!--数据列表Tab页面-->
      <el-tab-pane :label="$t('workflow.BusinessList')" name="BusinessList" style="height: 95%;">
        <!--  数据列表按钮区域-->
        <el-row type="flex" justify="start">
            <slot name="dataButtons" :isDisabled="isBtnDisabled"></slot>
        </el-row>
        <!--业务数据表格-->
        <div style="height: 97%;">
          <xyj-table ref="refdataTable"  id="2" @update="RefDataTableQueryAfter" @error="RefDataTableError" v-bind="dataTable"></xyj-table>
        </div>
      </el-tab-pane>

      <!--业务数据操作页面-->
			<el-tab-pane :label="$t('workflow.Business')" name="Business" style="height: 95%;">
	     <!-- 审批功能按钮-->
        <div id="buttons">
          <el-row type="flex" justify="start">
            <slot name="BusinessButton" :isDisabled="isBtnDisabled"></slot>
              <el-button
                v-for="(btn,i) in xyjButtons"
                :id = "btn.id"
                type="primary"
                size="mini"
                @click="distribute(btn.id,btn.butName)"
                :disabled= "(btn.id ==='termination' || btn.id === 'hangupbut') ? isBtnDisabled : buttonDisable"
                style="padding: 7px;"
                :key="i">
                {{$t('workflow.' + btn.butName)}}
              </el-button>
          </el-row>
        </div>
	      <div style="height: 100%;">
         <!-- 当前流程代办任务-->
          <el-collapse accordion v-model="agencyActiveName">
            <el-collapse-item name="agency" id="agencydown">
              <template slot="title">
                <i class="header-icon el-icon-info">当前流程代办任务({{agencyCount}})</i>
              </template>
              <xyj-table ref="refAgency" id="" @update="refAgencyQueryAfter" v-bind="agencyConfig"></xyj-table>
            </el-collapse-item>
          </el-collapse>
          <!--业务表单插槽-->
	        <slot name="Business" :isDisabled="isBtnDisabled"></slot>
	      </div>
	    </el-tab-pane>
      <!--审批详情tab页面-->
	    <el-tab-pane :label="$t('workflow.ApprovalDetails')" name="Details">
	      <div style="overflow: auto;border: 1px solid #ccc;margin: 0 20px 15px 40px;">
	        <div>流程图</div>
	        <div style="border: #5daf34">
	          <img :src="src" alt="" />
	        </div>
	      </div>
	      <div class="block approvalProcess">
	      	<div style="text-align: left;margin: 0 0 15px 40px;">
        		<span style="margin-right: 20px;">流程名称：{{ prevnodes.length>0?prevnodes[0].processDefinitionName:'' }}</span>
        		<span>业务id：{{prevnodes.length>0?prevnodes[0].businessKey:''}}</span>
        	</div>
	        <el-timeline>
	          <el-timeline-item v-for="(item,index) in prevnodes" :key="index" :timestamp="item.startTime" placement="top">
	            <el-card style="text-align: left;">
	              <el-row :gutter="20" type="flex" class="row-bg" justify="space-between">
	              	<el-col :span="8">
                    <div v-if="item.state == '审批中'">审批动作:<strong style="color: red">{{ item.state }}</strong></div>
                    <div v-else >审批动作:{{ item.state }}</div>
                  </el-col>
								  <el-col :span="8"><div>节点名称:{{ item.activityName }}</div></el-col>
								  <el-col :span="8"><div>处理人id/姓名:{{ item.assignedName || item.assigneeId ? item.assignedName+'('+item.assigneeId+')' :'' }}</div></el-col>
								</el-row>
								<el-row :gutter="20" type="flex" class="row-bg" justify="space-between">
								  <el-col :span="8"><div>执行结束时间:{{ item.endTime }}</div></el-col>
								  <el-col :span="8"><div>审批耗时:{{ item.duration }}</div></el-col>
								  <el-col :span="8">
                    <div class="opinion" :title="item.opinion">审批意见:{{ item.opinion }}</div>
                  </el-col>
								</el-row>
	            </el-card>
	          </el-timeline-item>
	        </el-timeline>
	      </div>
	      <div>
          <!--展开，收起按钮-->
          <el-button type="text" size="mini" @click="onClickApprovalProcess">{{ ApprovalCaption }}</el-button>
        </div>
	    </el-tab-pane>
   </el-tabs>
    
   <!-- 审批同意对话框-->
    <xyj-agree-dialog ref="agreeDialog" v-bind="agreeCallback"></xyj-agree-dialog>
    <!-- 选签审批对话框-->
    <xyj-select-agree-dialog ref="selectAgreeDialog" v-bind="selectAgreeCallback"></xyj-select-agree-dialog>
    <!--转签对话框-->
    <transfer-signature-dialog ref="TSDialog" v-bind="transferSignatureCallBack"></transfer-signature-dialog>
    <!--驳回上一级对话框-->
    <xyj-reject-next-level ref="rejectDialog" v-bind="xyjRejectNextLevelCallBack"></xyj-reject-next-level>
    <!--驳回发起人对话框-->
    <xyj-first-next-level ref="rejectFirstDialog" v-bind="xyjRejectFirstNextLevelCallBack"></xyj-first-next-level>
    <!--选择驳回对话框-->
    <xyj-select-reject-dialog ref="selectRejectDialog" v-bind="xyjSelectRejectCallBack"></xyj-select-reject-dialog>
  </div>
</template>

<script>
    import XyjAgreeDialog from "@/components/WorkFlow/xyjAgreeDialog";
    import XyjSelectAgreeDialog from "@/components/WorkFlow/xyjSelectAgreeDialog";
    import TransferSignatureDialog from "@/components/WorkFlow/transferSignatureDialog";
    import XyjRejectNextLevel from "@/components/WorkFlow/xyjRejectnextlevel";
    import XyjFirstNextLevel from "@/components/WorkFlow/xyjFirstNextLevel";
    import xyjSelectRejectDialog from "@/components/WorkFlow/xyjSelectRejectDialog";
    export default {
      name: "WorkFlowClient",
      components: {
        XyjAgreeDialog,
        XyjSelectAgreeDialog,
        TransferSignatureDialog,
        XyjRejectNextLevel,
        XyjFirstNextLevel,
        xyjSelectRejectDialog},
      props: {
          /**
           *宋建国：2019/9/18 12:28
           *描述：审批流成功提交后外部接口
           *版本：1.0
           */
          OnAgreeAfter: {
            type: Function,
            default(rData){
              return true;
            }
          },
        /**
         *宋建国：2019/10/18 22:51
         *描述：选签成功提交后外部接口
         *版本：1.0
         */
          OnSelectAgreeAfter:{
            type: Function,
            default(rData){
              return true;
            }
          },
        /**
         *宋建国：2019/10/21 14:49
         *描述：转签成功后事件
         *版本：1.0
         */
          OnTransferSignatureAfter:{
            type: Function,
            default(rData){
              return true;
            }
          },
        /**
         *宋建国：2019/10/24 18:32
         *描述：驳回上一级成功后,事件
         *版本：1.0
         */
          OnRejectNextLevelAfter:{
            type: Function,
            default(rData){
              return true;
            }
          },
          /**
           *宋建国：2019/10/24 18:32
           *描述：驳回发起人成功后,事件，方便业务开发执行代码
           *版本：1.0
           */
          OnRejectFirstLevelAfter:{
            type: Function,
            default(rData){
              return true;
            }
          },
         /**
         *宋建国：2019/10/29 16:14
         *描述：选择驳回成功后事件，方便业务开发执行代码
         *版本：1.0
         */
          OnSelectRejectlAfter:{
             type: Function,
             default(rData){
               return true;
             }
          },
        /**
         *宋建国：2019/11/1 10:56
         *描述：业务数据表格设置
         *版本：1.0
         */
          configDataTable:{
            type: Object,
            default(){
              return {}
            }
          },
          id:{
          	type:String,
          }
        },
        data() {
          return{
            /**
             *宋建国：2019/11/7 15:52
             *描述：流程发起进度状态
             *版本：1.0
             */
            workflowLoading: null,
            /**
             *宋建国：2019/11/1 18:17
             *描述：代办数量
             *版本：1.0
             */
            agencyCount: 0,
            /**
             *宋建国：2019/10/31 14:15
             *描述：按钮禁用状态 false:不禁用，true:禁用
             *版本：1.0
             */
            buttonDisable: false,
            isBtnDisabled:false,
            /**
             *宋建国：2019/10/31 12:23
             *描述：代办折叠变量
             *版本：1.0
             */
            agencyActiveName: "",
            /**
             *宋建国：2019/9/26 15:08
             *描述：业务单号(KEY)
             *版本：1.0
             */
            businessKey: "",
            /**
             *宋建国：2019/10/11 12:08
             *描述：流程实例ID
             *版本：1.0
             */
            InstanceId: "",
            /**
             *宋建国：2019/10/12 15:25
             *描述：任务ID
             *版本：1.0
             */
            taskId: "",
             /**
             *宋建国：2019/9/18 16:16
             *描述：审批通过后回调函数
             *版本：1.0
             */
            agreeCallback:{
               OnAgreeAfter: this.OnAgreeClick
            },
            /**
             *宋建国：2019/10/18 22:47
             *描述：选签通过后回调函数
             *版本：1.0
             */
            selectAgreeCallback:{
              OnAgreeAfter: this.OnselectAgreeClick
            },
            /**
             *宋建国：2019/10/21 14:45
             *描述：转签通过后回调函数
             *版本：1.0
             */
            transferSignatureCallBack:{
              OnTransferSignatureAfter: this.OnTransferSignatureAfterClick
            },
            /**
             *宋建国：2019/10/24 18:29
             *描述：驳回上一级回调函数
             *版本：1.0
             */
            xyjRejectNextLevelCallBack:{
              OnRejectAfter: this.OnRejectAfterClick
            },
            /**
             *宋建国：2019/10/29 12:07
             *描述：驳回发起人成功后回调函数
             *版本：1.0
             */
            xyjRejectFirstNextLevelCallBack:{
              OnRejectFirstAfter: this.OnRejectFirstAfterClick
            },
            /**
             *宋建国：2019/10/29 16:10
             *描述：选择驳回成功后回调函数
             *版本：1.0
             */
            xyjSelectRejectCallBack:{
              OnSelectRejectAfter: this.OnSelectRejectAfterClick
            },
            /**
             *宋建国：2019/10/17 13:13
             *描述：流程图路径
             *版本：1.0
             */
            src: '',
            /**
             *宋建国：2019/10/17 13:14
             *描述：激活页面
             *版本：1.0
             */
            activeName: 'Business',
            /**
            * 宋建国：2019-06-20 06:14:12
            * 描述：审批过程时间线展开与收起
            */
            ApprovalCaption: '展开',
            isOpen: false,
            /**
             *宋建国：2019/10/17 10:11
             *描述：审批流程数据变量
             *版本：1.0
             */
            prevnodes:[],
            /**
             *宋建国：2019/10/11 12:13
             *描述：审批按钮
             *版本：1.0
             */
            xyjButtons:[],
            /**
             *宋建国：2019/11/1 16:19
             *描述：表单是否能编辑：false:不能编辑，true:允许编辑
             *版本：1.0
             */
            isEdit: false,
            /**
             *宋建国：2019/11/1 17:18
             *描述：当前节点表单路由
             *版本：1.0
             */
            formkeydefinition: "",
            /**
             *张俊娜：2019/11/8
             *描述：用于编辑是导航切换
             *版本：1.0
             */
            tab:false,
            /**
             *宋建国：2019/10/31 10:40
             *描述：当前流程代办任务列表
             *版本：1.0
             */
            agencyConfig: {
              hasDelete: false, //删除
              hasNew: false, //新增
              hasView: false, //查看
              hasEdit: false, //编辑
              hasSearch: false, //隐藏查询条件
              //不显示表格操作列
              hasOperation: false,
              /**
               *宋建国：2019/8/7 17:39
               *描述：自定义按钮
               */
/*              extraButtons:[
                {
                  label: this.$i18n.t('workflow.Viewflowchart'),
                  type: "primary",
                  text: "查看流程图",
                  atClick: this.Viewflowchart
                },
              ],*/
              //el-table设置
              tableAttrs: {
                height: "150px",
                width: "90%"
              },
              //el-table事件设置
              tableOns: {
                "row-dblclick": this.doubleAgencyClick
              },
              //表格中的操作列设置
              operationAttrs: {
                width: "120px"
              },
              //格式化表格字段显示，按照类属性定义格式化函数
              formatters: {
               // isSuspended: this.formatSuspended
              },
              //single: false //单选
              //admin微服务网址
              hostUrl: this.baseUrl,
              //表初始化关键字，一般为表名
              tableName: "ProcessTaskVo",
              //业务数据微服务,查询与高级查询
              bQuery: {
                hostUrl: this.workflow,
                url: "/task/getMyTask",
                method: "post",
                notCondition: false //没有查询条件则查询全部数据
              },
              InitQuery: {
              //0: { field0: "processInstanceId", gx0: "EQUAL", value0: "BpmTree", andor0: "AND" }
              }, //表格初始化时查询条件
            },
            /**
             *宋建国：2019/10/16 13:52
             *描述：业务数据表格
             *版本：1.0
             */
            dataTable: {
              hasDelete: true, //删除
              hasNew: false, //新增
              hasView: false, //查看
              hasEdit: true, //编辑
              hasExport: true, //导出
              //不显示表格操作列
              hasOperation: true,
              /**
               *宋建国：2019/8/7 17:39
               *描述：自定义按钮
               */
              extraButtons:[],
                //el-table设置
              tableAttrs: {
                height: "95%",
                width: "90%"
              },
              //el-table事件设置
              tableOns: {
                "row-dblclick": this.dataTableBrowdblclick,
                "select":function(selection, row){
                	for(var i=selection.length-1;i>=0;i--){
                		if(selection[i].isShow){
                			selection.splice(i,1)
                		}
                	}
                },
                "select-all":function(selection){
                	for(var i=selection.length-1;i>=0;i--){
                		if(selection[i].isShow){
                			selection.splice(i,1)
                		}
                	}
                }
              },
              //表格中的操作列设置
              operationAttrs: {
                width: "120px"
              },
              //格式化表格字段显示，按照类属性定义格式化函数
              formatters: {
                /**
                 *宋建国：2019/11/7 11:45
                 *描述：审批状态转换中文
                 *版本：1.0
                 */
                applyState: this.$tu.applyStateChine
              },
              //single: false //单选
              //admin微服务网址
                hostUrl: this.global.baseUrl,
                //表初始化关键字，一般为表名
              tableName: "ProcessTaskVo",
              hostUrl:this.baseUrl,
                //业务数据微服务,查询与高级查询
              bQuery: {
                  hostUrl: this.workflow,
                  url: "/task/getMyTask",
                  method: "post",
                  notCondition: false //没有查询条件则查询全部数据*/
              },
              //业务数据微服务,导出
              bExport: {
                // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
                tableName: "",//如果为空系统默认为，表初始化关键字
                fileName: ""//自定义文件名,如果为空则根据实体类名称定义下载文件名
              },
              //业务数据微服务,新增网址
              bNewUrl: {
                method: "post",
                close: true, //关闭新增对话框
              },
              //业务数据微服务,修改网址
              bEditUrl: {
                method: "post",
                close: true  //关闭修改对话框
              },
              //业务数据微服务,删除网址
			        bDeleteUrl: {
			          method: "post",
			          close: true  //关闭删除对话框
			        },
              /**
               *宋建国：2019/11/7 15:01
               *描述：表格新增成功后事件
               *版本：1.0
               */
              onAfterSuccessNew: this.bsinessListAfterSuccessNew,
              /**
               *宋建国：2019/11/7 17:20
               *描述：表格修改成功后事件
               *版本：1.0
               */
              onAfterSuccessEdit: this.bsinessListAfterSuccessEdit,
              onAfterSuccessDelete:this.bsinessListAfterSuccessDelete,
              InitQuery: {
  //               0: { field0: "category", gx0: "EQUAL", value0: "BpmTree", andor0: "AND" }
              } //表格初始化时查询条件
            }
          };
        },
        methods: {
          /**
           *宋建国：2019/11/7 15:43
           *描述：业务数据列表查询成功后事件
           *版本：1.0
           */
          RefDataTableQueryAfter(data, res){
          	data.forEach(x => {
          		if(x.applyState != 0){
          			x.isShow = true;
          		}
          	})
            if(this.workflowLoading != null){
              this.workflowLoading.close();
            }
          },
          /**
           *宋建国：2019/11/7 15:59
           *描述：业务数据列表查询失败后事件
           *版本：1.0
           */
          RefDataTableError(err){
            if(this.workflowLoading != null){
              this.workflowLoading.close();
            }
          },
          /**
           *宋建国：2019/11/7 14:54
           *描述：表格新增单据
           *版本：1.0
           */
          addTableTicket(){
            //切换到数据列表
            this.activeName = 'BusinessList';
            this.$refs.refdataTable.onDefaultNew(()=>{},{});
          },
          /**
           *宋建国：2019/11/7 16:02
           *描述：表格单据修改数据
           *版本：1.0
           */
          editTableTicket(flag){
          	if(flag) this.tab = flag
            let row = this.$refs.refdataTable.selected[0];
            if(!row) this.$message.info('请到数据列表选择需要编辑的流程')
            if(!this.$tu.isObjectBlank(row)){
              this.activeName = 'BusinessList';
              this.$refs.refdataTable.onDefaultEdit(()=>{},row);
            }
          },
          /**
           *张俊娜：2019/11/8 
           *描述：表格单据删除数据
           *版本：1.0
           */
          deleteTableTicket(){
          	let row = this.$refs.refdataTable.selected[0];
            if(!row) this.$message.info('请到数据列表选择需要删除的流程')
            if(!this.$tu.isObjectBlank(row)){
              this.activeName = 'BusinessList';
              this.$refs.refdataTable.onDefaultDelete(()=>{},row,'single');
            }
          },
          bsinessListAfterSuccessDelete(selected){
          	this.activeName = 'Business';
          },
          /**
           *宋建国：2019/11/7 17:20
           *描述：表格修改新增成功后事件
           *版本：1.0
           */
          bsinessListAfterSuccessEdit(data,row){
            //设置修改行选中
            this.$refs.refdataTable.setCurrentRow(data[0]);
            if(this.tab) this.activeName = 'Business';
          },
          /**
           *宋建国：2019/11/7 15:02
           *描述：表格单据新增成功后事件
           *版本：1.0
           */
          bsinessListAfterSuccessNew(data,row){
            //新增成功后不刷新数据
            this.$refs.refdataTable.setNotCondition(false);
            let then = this;
            this.workflowLoading = this.$loading({
              lock: true,
              text: '流程发起中...',
              spinner: 'el-icon-loading',
              target: document.querySelector('#workflowtab')
            });
            this.startWorkFlow(
              {
                businessKey: data.id, //与工作流关联ID
                variables: {}  //工作流程执行时的流程变量
              }
              ,function (resultData) { //成功回調
                //审批发起后回复空条件查询
                then.$refs.refdataTable.setNotCondition(true);
                then.$refs.refdataTable.getList();
                then.activeName = 'Business';
              }
              ,function (err) {  //失敗回調
                //审批发起后回复空条件查询
                then.$refs.refdataTable.setNotCondition(true);
                then.$message.info(err.message);
                if(then.workflowLoading != null){
                  then.workflowLoading.close();
                }
                then.activeName = 'Business';
              }
            )
          },
          /**
           *宋建国：2019/10/16 14:33
           *描述：双击业务数据行
           *版本：1.0
           */
          dataTableBrowdblclick(row, column, event){
          	let then = this
            if(!row.hasOwnProperty("applyId")){
              this.$message.info("业务数据中不存在审批实例ID属性");
              return;
            }
            if(this.$tu.isBlank(row.applyId)){
            	this.workflowLoading = this.$loading({
	              lock: true,
	              text: '流程发起中...',
	              spinner: 'el-icon-loading',
	              target: document.querySelector('#workflowtab')
	            });
	            this.startWorkFlow(
	              {
	                businessKey: row.id, //与工作流关联ID
	                variables: {}  //工作流程执行时的流程变量
	              }
	              ,function (resultData) { //成功回調
	                //审批发起后回复空条件查询
	                then.$refs.refdataTable.setNotCondition(true);
	                then.$refs.refdataTable.getList();
	                then.activeName = 'Business';
	              }
	              ,function (err) {  //失敗回調
	                //审批发起后回复空条件查询
	                then.$refs.refdataTable.setNotCondition(true);
	                then.$message.info(err.message);
	                if(then.workflowLoading != null){
	                  then.workflowLoading.close();
	                }
	              }
	            )
//            this.$message.info("审批实例ID不能为空");
//            return;
            }else{
            	this.setInstanceId(row.applyId);
            	this.setTabs("Business");
            }
          },
          /**
           *宋建国：2019/10/31 13:43
           *描述：代办表格双击事件
           *版本：1.0
           */
           doubleAgencyClick(row, column, event){
           	// 表单路由跳转
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
              if(row.id === this.taskId){
                return;
              }
              this.setInstanceId(row.processInstanceId);
              //流程挂起时禁用按钮
              if(row.suspended === "挂起"){
                this.buttonDisable = true;
                this.setTaskId(row.id,{hangupbut:"clientactivation"});
              }else{
                this.buttonDisable = false;
                this.setTaskId(row.id);
              }
              this.$emit('OnRowDoubleClickAgency',row, column, event);
           },
          /**
           *宋建国：2019/10/31 11:50
           *描述：代办查询数据后回调事件
           *版本：1.0
           */
          refAgencyQueryAfter(data, res){
            this.agencyCount = data.length;
            //删除审批相关按钮
            let len = this.xyjButtons.length;
            this.xyjButtons.splice(0,len);
            if(data.length>0){
              //展开代办折叠
              this.agencyActiveName = "agency";
              this.$refs.refAgency.setCurrentRow(data[0]);
              this.setInstanceId(data[0].processInstanceId);
              //流程挂起时禁用按钮
              if(data[0].suspended === "挂起"){
                this.buttonDisable = true;
                //传入这个参数{hangupbut:"clientactivation"} 代表修改挂起按钮标题
                this.setTaskId(data[0].id,{hangupbut:"clientactivation"});
              }else{
                this.buttonDisable = false;
                this.setTaskId(data[0].id);
              }
              this.$emit('OnRowDoubleClickAgency',data[0],null, null);
            }
          },
          /**
           *宋建国：2019/9/25 15:23
           *描述：切换Tab页
           *版本：1.0
           */
          setTabs(TabName){
            if(TabName === 'BusinessList'){
              this.activeName = 'BusinessList';
              this.handleClick({name:this.activeName}, null);
            }else if(TabName === 'Business'){
              this.activeName = 'Business';
              this.handleClick({name:this.activeName}, null);
            }else if(TabName === 'Details'){
              this.activeName = 'Details';
              this.handleClick({name:this.activeName}, null);
            }
          },
          /**
           *宋建国：2019/9/18 16:20
           *描述：同意确定钩子事件
           * rData： 审批业务返回数据
           * 如果返回true则关闭“审批页面”否则不关
           * 如果代码错误则抛出错误 例如 new Error();
           *版本：1.0
           */
          OnAgreeClick(rData){
            //回调业务方法
            if(this.OnAgreeAfter(rData)){
              this.agencyRefresh();
              this.$message.success("业务处理成功");
              return true;
            }else{
              return false
            }
          },
          /**
           *宋建国：2019/10/18 22:49
           *描述：同意确定钩子事件
           * rData： 审批业务返回数据
           * 如果返回true则关闭“审批页面”否则不关
           * 如果代码错误则抛出错误 例如 new Error();
           *版本：1.0
           */
          OnselectAgreeClick(rData){
            //回调业务方法
            if(this.OnSelectAgreeAfter(rData)){
              this.agencyRefresh();
              this.$message.success("选签业务处理成功");
              return true;
            }else{
              return false;
            }
          },
          /**
           *宋建国：2019/10/21 14:51
           *描述：转签确定钩子事件
           * rData： 转签业务返回数据
           * 如果返回true则关闭“审批页面”否则不关
           * 如果代码错误则可以抛出错误 例如 new Error();
           *版本：1.0
           */
          OnTransferSignatureAfterClick(rData){
            if(this.OnTransferSignatureAfter(rData)){
              this.$message.success("转签业务处理成功");
              return true;
            }else{
              return false;
            }
          },
          /**
           *宋建国：2019/10/24 18:30
           *描述：驳回上一级回调函数
           * rData: 驳回义务返回数据
           * 如果返回true则关闭“审批页面”否则不关
           * 如果代码错误则可以抛出错误 例如 new Error();
           *版本：1.0
           */
          OnRejectAfterClick(rData){
            if(this.OnRejectNextLevelAfter(rData)) {
              this.agencyRefresh();
              this.$message.success("驳回上一级处理成功");
              return true;
            }else{
              return false;
            }
          },
          /**
           *宋建国：2019/10/24 18:30
           *描述：驳回发起人成功后回调行数
           * rData: 驳回成功返回数据
           * 如果返回true则关闭“驳回发起人弹框”否则不关
           * 如果代码错误则可以抛出错误 例如 new Error();
           *版本：1.0
           */
          OnRejectFirstAfterClick(rData){
            if(this.OnRejectFirstLevelAfter(rData)) {
              this.agencyRefresh();
              this.$message.success("驳回发起人处理成功");
              return true;
            }else{
              return false;
            }
          },
          /**
           *宋建国：2019/10/29 16:12
           * 描述：选择驳回成功后回调函数
           * rData: 驳回成功返回数据
           * 如果返回true则关闭“驳回发起人弹框”否则不关
           * 如果代码错误则可以抛出错误 例如 new Error();
           *版本：1.0
           */
          OnSelectRejectAfterClick(rData){
            if(this.OnSelectRejectlAfter(rData)) {
              this.agencyRefresh();
              this.$message.success("选择驳回处理成功");
              return true;
            }else{
              return false;
            }
          },
          /**
           *宋建国：2019/9/18 16:17
           *描述：Tabs页切换事件
           *版本：1.0
           */
          handleClick(tab, event) {
            if (tab.name ==='Business'){
              this.agencyRefresh();
            }else if (tab.name ==='Details'){
              if(!this.$tu.isBlank(this.InstanceId)){
                this.getImageByInstance(this.InstanceId);
                this.getProcessHistory(this.InstanceId);
              }
            }else if (tab.name == "BusinessList"){
              //空条件查询设置
              this.$refs.refdataTable.setNotCondition(true);
              //查询数据
              this.$refs.refdataTable.getList();
            }
          },
          /**
           *宋建国：2019/11/1 19:21
           *描述：刷新代办任务
           *版本：1.0
           */
          agencyRefresh(){
            if(this.$tu.isBlank(this.InstanceId)){
              //this.$message.info("流程实例ID为空，请在数据列表中重新双击业务数据");
              return false;
            }
            let findWhere = {
              0:{
                field0: "processInstanceId",
                gx0: "EQUAL",
                value0: this.InstanceId,
                andor0: "AND"
              }
            };
            this.$refs.refAgency.getList(findWhere);
            return true;
          },

          /**
           *宋建国：2019/9/27 14:57
           *描述：查看流程实例ID查询审批记录
           *版本：1.0
           */
          getProcessHistory(instanceId){
            if(this.$tu.isBlank(instanceId)){
              this.$message.error("流程实例ID为空无法获取流程图");
              return;
            }
            this.$api.workflow.processHistory(
              {processId: instanceId}
            ).then((res) =>{
            	if(res.code == 200){
            		this.prevnodes = res.data
            	}
            }).catch((err) => {
              this.$message.error("获取审批数据错误：" + err.message);
            })
          },
          /**
           *宋建国：2019/9/26 15:14
           *描述：根据流程实例ID获取流程图，高亮当前任务节点
           *版本：1.0
           */
          getImageByInstance(instanceId){
            if(this.$tu.isBlank(instanceId)){
              this.$message.error("流程实例ID为空无法获取流程图");
              return;
            }
            this.$api.workflow.getImageByInstance(
              instanceId
            ).then((res) => {
              this.src = 'data:image/png;base64,' + btoa(new Uint8Array(res).reduce((data, byte) => data + String.fromCharCode(byte), ''))
            }).catch((err) => {
              this.$message.error("获取流程图错误：" + err.message);
            })
            document.getElementsByClassName('el-tabs__content')[1].style.height = document.getElementsByClassName('main-content')[0].clientHeight-document.getElementsByClassName('el-tabs__header')[1].clientHeight-15+'px'
          },
          /**
           *宋建国：2019/9/24 16:03
           *描述：审批流发起
           *版本：1.0
           */
          launchClick(){
            this.$message.info('launchClick');
          },
          /**
           *宋建国：2019/9/17 11:54
           *描述：审批单击
           *版本：1.0
           */
          agree(eventName,butName){
            this.$refs.agreeDialog.open(this.taskId,'PassKeyWord');
          },
          /**
           *宋建国：2019/9/17 11:57
           *描述：会签单击事件
           *版本：1.0
           */
          countersign(eventName,butName){
            this.$message.info('countersign');
          },
          /**
           *宋建国：2019/10/11 13:41
           *描述：选签单击事件
           *版本：1.0
           */
          selectsign(eventName,butName){
            this.$refs.selectAgreeDialog.open(this.taskId,'PassKeyWord');
          },
          /**
           *宋建国：2019/9/19 14:03
           *描述：转办
           *版本：1.0
           */
          agency(eventName,butName){
            this.$refs.TSDialog.open(this.taskId,'PassKeyWord');
          },
          /**
           *宋建国：2019/10/11 13:42
           *描述：驳回上一级
           *版本：1.0
           */
          reject(eventName,butName){
            this.$refs.rejectDialog.open(this.taskId,'','RejectKeyWord');
          },
          /**
           *宋建国：2019/10/11 13:43
           *描述：驳回发起人
           *版本：1.0
           */
          rejecttostart(eventName,butName){
            this.$refs.rejectFirstDialog.open(this.taskId,'RejectKeyWord');
          },
          /**
           *宋建国：2019/10/11 13:44
           *描述：选择驳回
           *版本：1.0
           */
          rejecttoassign(eventName,butName){
            this.$refs.selectRejectDialog.open(this.taskId,'','RejectKeyWord');
          },
          /**
           *宋建国：2019/10/11 13:44
           *描述：终止
           *版本：1.0
           */
          termination(eventName,butName){
            if(this.$tu.isBlank(this.InstanceId)){
              this.$message.info("没有流程实例ID无法终止")
              return;
            }
            //流程删除

            this.$confirm("流程终止后将无法回复，请谨慎！！！",{
              type: 'warning',
              beforeClose: (action,instance,done) => {
                if(action == 'confirm'){
                  instance.confirmButtonLoading = true;

                  this.$api.workflow.deleteProessdefInstance(
                    this.InstanceId
                  ).then((res) =>{
                    this.$message.info(res.msg);
                    let findWhere = {
                      0:{
                        field0: "processInstanceId",
                        gx0: "EQUAL",
                        value0: this.InstanceId,
                        andor0: "AND"
                      }
                    };
                    this.$refs.refAgency.getList(findWhere);
                    instance.confirmButtonLoading = false;
                    //删除审批相关按钮
                    let len = this.xyjButtons.length;
                    this.xyjButtons.splice(0,len);
                    done();
                  }).catch((err)=>{
                    this.$message.info(res.msg);
                    instance.confirmButtonLoading = false;
                    done();
                  });

                }else{
                  done();
                }
              }
            }).catch((err) => {

            })
          },
          /**
           *宋建国：2019/10/11 13:45
           *描述：挂起
           *版本：1.0
           */
          hangupbut(eventName,butName){

            if(this.$tu.isBlank(this.InstanceId)){
              this.$message.info("没有流程实例ID无法操作")
              return;
            }
            //流程挂起与激活
            let tipsStr = "流程挂起后此流程实例的所有任务将无法操作，请谨慎！！！";
            if(butName != "hangupbut"){
              tipsStr = "流程激活后，此流程实例的所有任务将允许操作，请谨慎！！！";
            }
            this.$confirm(tipsStr,{
              type: 'warning',
              beforeClose: (action,instance,done) => {
                if(action == 'confirm'){

                  instance.confirmButtonLoading = true;
                  this.$api.workflow.suspendProcessInstanceById(
                    this.InstanceId
                  ).then((res) =>{
                    this.$message.info(res.msg);
                    let findWhere = {
                      0:{
                        field0: "processInstanceId",
                        gx0: "EQUAL",
                        value0: this.InstanceId,
                        andor0: "AND"
                      }
                    };
                    this.$refs.refAgency.getList(findWhere);
                    instance.confirmButtonLoading = false;
                    done();
                  }).catch((err)=>{
                    this.$message.info(res.msg);
                    instance.confirmButtonLoading = false;
                    done();
                  });

                }else{
                  done();
                }
              }
            }).catch((err) => {

            })
          },
          /**
           *宋建国：2019/10/11 13:37
           *描述：审批按钮单击事件分发
           *版本：1.0
           */
          distribute(eventName,butName){
            this[eventName](eventName,butName);
          },
          onClickApprovalProcess(){
            this.isOpen = !this.isOpen;
            let ele = document.querySelector(".block");
            let sele = document.querySelectorAll(".el-timeline>.el-timeline-item")[0];
            if ( this.isOpen ) {
              ele.style.height = sele.clientHeight * this.prevnodes.length + 'px';
              this.ApprovalCaption = '收起';
              this.isOpen = true
            } else {
              this.ApprovalCaption = '展开';
              ele.style.height = sele.clientHeight+20 + 'px';
            }
          },
          /**
           *宋建国：2019/9/26 14:23
           *描述：流程发起对外接口
           *版本：1.0
           */
          startWorkFlow(data,rollback,erroback){
            if (this.$tu.isObjectBlank(data)){
              this.$message.error("流程发起参数不能为空");
              return;
            }
            if(this.$tu.isBlank(data.businessKey)){
              this.$message.error("业务数据Key不能为空");
              return;
            }
            if(this.$tu.isBlank(data.variables)){
              this.$message.error("流程变量参数不能为空");
              return;
            }
            this.$api.workflow.startWorkFlow({
              businessKey: data.businessKey,
              key: this.$route.meta.processKey,
              map: data.variables
            }).then((res) => {
              //保存业务单号
              this.setInstanceId(res.data.InstanceId);
              /**
               *宋建国：2019/11/1 15:38
               *描述：查询代办数据
               *版本：1.0
               */
              let findWhere = {
                0:{
                  field0: "processInstanceId",
                  gx0: "EQUAL",
                  value0: this.InstanceId,
                  andor0: "AND"
                }
              };
              this.$refs.refAgency.getList(findWhere);
              //回调流程发起成功函数
              rollback(res);
            }).catch((err) => {
              this.$message.error(err.message);
              erroback(err);
            });
          },
          /**
           *宋建国：2019/9/26 15:12
           *描述：设置业务单号（key）接口
           *版本：1.0
           */
          setBusinessKey(bKey){
            this.businessKey = bKey;
          },
          /**
           *宋建国：2019/9/26 16:28
           *描述：设置流程实例ID接口
           *版本：1.0
           */
          setInstanceId(instanceId){
            this.InstanceId = instanceId;
          },
          /**
           *宋建国：2019/10/12 15:26
           *描述：设置任务ID
           *版本：1.0
           */
          setTaskId(taskId,butTitle){
            this.taskId = taskId;
            this.initButton(butTitle);
          },
          /**
           *宋建国：2019/10/11 12:07
           *描述：初始化按钮权限
           * instanceId: 流程实例Id
           *版本：1.0
           */
          initButton(butTitle){
            let then = this;
            let buttonLoading = this.$loading({
              lock: true,
              text: '加载中...',
              spinner: 'el-icon-loading',
              target: document.querySelector('#buttons')
            });
            this.$api.workflow.getTaskIdButtonRole({
              taskId:this.taskId
            }).then((res)=>{
              then.xyjButtons.length = 0;
              for (let i=0; i<res.data.buttons.length; i++){
                res.data.buttons[i]['butName'] = res.data.buttons[i].id;
                //修改按钮标题
                if(!then.$tu.isObjectBlank(butTitle)){
                  if(butTitle.hasOwnProperty(res.data.buttons[i].id)){
                    res.data.buttons[i]['butName'] = butTitle[res.data.buttons[i].id];
                  }
                }
                then.xyjButtons.push(res.data.buttons[i]);
              }
              //表单编辑权限
              if(then.$tu.isBlank(res.data.isedit)){
                then.isEdit = false;
              }else{
                if(res.data.isedit === 'true'){
                  then.isEdit = true;
                }else{
                  then.isEdit = false;
                  then.isBtnDisabled = true;
                  then.buttonDisable = true;
                  this.$refs.refdataTable.disabledNew = true
                }
              }
              buttonLoading.close();
            }).catch((err) =>{
              then.$message.error(err.message);
              buttonLoading.close();
            })
          }
        },
        mounted() {
        	this.$nextTick(() => {
        		document.getElementsByClassName('xyj-table')[1].parentNode.style.height="97%"
	          if (this.$tu.isBlank(this.$route.meta.processKey)){
	            this.$message.error('流程Key没有设置，请到“集团菜单”中设置流程Key');
	            return;
	          }
	          if(this.id){
	          	this.setInstanceId(this.id);
	            this.setTabs("Business");
	          }
        	})
        },
        created(){
          /**
           *宋建国：2019/11/1 11:07
           *描述：数据列表对象深度合并，实现外部设置表格属性
           *版本：1.0
           */
          Object.assign(this.dataTable,this.configDataTable);
        },
        watch:{
        	prevnodes:function(val){
        		this.$nextTick(()=>{
        			if(this.isOpen){
        				let ele = document.querySelector(".block");
		            let sele = document.querySelectorAll(".el-timeline>.el-timeline-item")[0];
		            ele.style.height = sele.clientHeight * val.length + 'px';
        			}
        		})
        	}
        }
    }
</script>

<style scoped>
/deep/ .el-timeline-item__timestamp.is-top{
	text-align: left;
}
/deep/ .el-tabs__content{
	height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}
/deep/ .el-table__fixed-footer-wrapper{
	display: none;
}
/deep/ .el-card .is-always-shadow{
	height: 88px;
}
h4{
	margin: 9px 0;
}
p{
	margin: 7px 0;
}
.block{
	box-sizing: border-box;
	margin-right: 20px;
	overflow: hidden;
	height: 150px;
  transition: height .5s ease;
  -webkit-transition: height .5s ease;
}
.opinion{
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
