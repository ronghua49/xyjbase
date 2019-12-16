<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="转签对话框"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      center
      width="800px">
      <div>
        <el-row key="toAssigneeNamekey" :gutter="10">
          <el-col :span="3">
            <div class="node-name bg-purple">
              转签处理人
            </div>
          </el-col>
          <el-col :span="21">
            <div class="grid-content bg-purple">
              <el-input v-model="toAssigneeName" placeholder="单击输入框选择转签处理人" label="asdfasdf" readonly="true"
                        @click.native="showDialog()"></el-input>
            </div>
          </el-col>
        </el-row >
      </div>
      <div>
        意见：
        <el-input
          type="textarea"
          placeholder="请输入转签意见"
          v-model="opinion"
          maxlength="2000"
          show-word-limit
          rows="4"
        >
        </el-input>
      </div>
      <div v-loading="loading" @click.ctrl="showCode">
        常用语：
        <el-button v-for="(btn,i) in commonlanguage" :key="'commonlanguage'+i" type="text" @click="commonlanguageClick(btn.name)">{{btn.name}}</el-button>
      </div>
      <div style="text-align: right">
        <span>
          <el-button type="primary" size="mini" @click="dialogCancel()">{{$t('workflow.AgreeDialog.cancel')}}</el-button>
		      <el-button type="primary" size="mini" @click="dialogConfirm()" :loading="agreeLoad">{{$t('workflow.AgreeDialog.Agree')}}</el-button>
		  </span>
      </div>
    </el-dialog>
    <!--员工信息弹框-->
    <xyj-user-dialog ref="userdialog" v-bind="user"></xyj-user-dialog>
  </div>
</template>

<script>
    import xyjUserDialog from "@/components/xyjUserDialog/xyjUserDialog";
    import axios from "@/http/axios"
    export default {
      name: "transferSignatureDialog",
      components:{
        xyjUserDialog
      },
      props: {
        /**
         *宋建国：2019/9/18 15:58
         *描述：转签成功后事件
         *版本：1.0
         */
        OnTransferSignatureAfter: {
          type: Function,
          default(){
            return true
          }
        },
      },
      data() {
        return {
          user:{
            /**
             *宋建国：2019/10/21 15:46
             *描述：取消事件
             *版本：1.0
             */
            cancel:function(row){
              console.log(row)
            },
            /**
             *宋建国：2019/10/21 15:45
             *描述：确定选择数据事件
             *版本：1.0
             */
            confirm:this.okUserbutton,
            /**
             * @date 2019-10-12
             * @param {Object} str
             * @return {Boolean} 返回值
             * 是否是单选 true为单选，false为多选
             * @author 张俊娜
             */
            isRadio:true
          },
          /**
           *宋建国：2019/10/21 14:37
           *描述：转签处理人姓名
           *版本：1.0
           */
          toAssigneeName:"",
          /**
           *宋建国：2019/10/21 14:38
           *描述：转签负责人ID
           *版本：1.0
           */
          toAssigneeId: "",
          dialogVisible: false, //对话框打开变量
          agreeLoad: false, //同意进度状态
          taskId: "", //任务ID
          opinion: '', //审批意见变量
          commonlanguage: [], //常用语言
          loading:false // 常用语加载状态
        };
      },
      methods: {
        /**
         *宋建国：2019/10/21 15:49
         *描述：用户选择确定对话框
         *版本：1.0
         */
        okUserbutton(row){
          if(row.length>0){
            this.toAssigneeId = row[0].id;
            this.toAssigneeName = row[0].name;
          }
        },
        /**
         *宋建国：2019/10/21 15:47
         *描述：打开用户选择对话框
         *版本：1.0
         */
        showDialog(){
          /**
           *宋建国：2019/10/21 16:03
           *描述：函数参数为true:重新装入员工数据
           *版本：1.0
           */
          if(this.$tu.isBlank(this.toAssigneeId)){
            this.$refs.userdialog.open(true);
          }else{
            this.$refs.userdialog.open(true,[{id:this.toAssigneeId}]);
          }
        },
        /**
         *宋建国：2019/9/18 15:55
         *描述：打开对话框
         *版本：1.0
         */
        open(taskId,keyword){
        	this.loading = true
          this.taskId = taskId;
          this.dialogVisible = true;
          let query = {
						pageBean: {
							page: 1,
							pageSize: 100,
							showTotal: true,
							summary: []
						},
						params: {},
						querys: [{
							hasInitValue: false,
							operation: "EQUAL",
							property: "treecode",
							relation: "AND",
							value: keyword
						}],
						selfDate: {},
						sorter: [{
							direction: "ASC",
							property: "id"
						}]
					};
  				axios({
						hostUrl: this.global.baseUrl,
						url: "/sysKeyword/query",
						method: 'post',
						notCondition: true, //没有查询条件则查询全部数据
						data:query
					})
					.then(res => {
						if(res.code == 200) {
							this.loading = false
							this.commonlanguage = res.data.rows
						};
					})
					.catch(err => {
						this.loading = false
						console.log(err)
					})
        },
        /**
         *宋建国：2019/9/18 15:52
         *描述：常用语单击事件
         *版本：1.0
         */
        commonlanguageClick(strtext){
          this.opinion = strtext;
        },
        /**
         *宋建国：2019/9/18 15:55
         *描述：根据流程图流转,审批事件
         *版本：1.0
         */
        dialogConfirm(){
          let tmpThis = this;
          if(this.$tu.isBlank(this.taskId)){
            this.$message.error("任务ID不能为空");
            return;
          }
          if(this.$tu.isBlank(this.toAssigneeId)){
            this.$message.error("转签处理人ID不能为空");
            return;
          }
          this.agreeLoad = true;
          this.$api.workflow.disposeTask(
            {
              actionName: "transferSignature",
              taskId: this.taskId,
              comment: this.opinion,
              toAssignee: this.toAssigneeId,
              toAssigneeName: this.toAssigneeName
            }
          ).then((res)=>{
            res.data = {taskId: tmpThis.taskId,
                        opinion:tmpThis.opinion};
            let state = this.OnTransferSignatureAfter(res.data);
            if(state) {
              tmpThis.dialogVisible = false;
            }
            tmpThis.agreeLoad = false;
          }).catch((err)=>{
            tmpThis.$message.error(err.message);
            tmpThis.agreeLoad = false;
          })
        },
        /**
         *宋建国：2019/9/18 15:55
         *描述：取消事件
         *版本：1.0
         */
        dialogCancel(){
          this.dialogVisible = false;
        },
        //显示编码
	    	showCode(){
	    		this.$message.info('编码:PassKeyWord')
	    	},
      }
    }
</script>

<style scoped lang="scss">
  .el-row {
    margin-bottom: 10px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .grid-content {
    border-radius: 4px;
    height: 36px;
  }
  .node-name {
    border-radius: 4px;
    height: 36px;
    text-align: justify;
    overflow: hidden; /*防止内容超出容器或者产生自动换行*/
    line-height: 36px;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
