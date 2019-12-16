<template>
    <div>
      <el-dialog
        v-dialogDrag
        :title="titleName"
        :close-on-click-modal="false"
        :visible.sync="dialogVisible"
        center
        width="45%"
        append-to-body>
          <div>
              <span>
                驳回步骤：
                <el-select v-model="rejectSelect" placeholder="请选择驳回步骤" style="width: 25%">
                  <el-option
                    v-for="item in reject"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </span>
              <span>
                驳回责任人：
                <el-select
                  v-model="userlist"
                  placeholder="请选择驳回责任人"
                  style="width: 52.5%">
                      <el-option
                        v-for="item in auditors"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                </el-select>
              </span>
          </div>
        <div style="margin-top: 10px">
            <el-input
              style="width: 100%"
              type="textarea"
              :rows="2"
              :placeholder="placeholder"
              :maxlength="50"
              v-model="opinion">
            </el-input>
        </div>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" size="mini" @click="onActionEvent" :loading="buttonLoad"> {{titleName}} </el-button>
          </span>
      </el-dialog>
    </div>
</template>

<script>
    import ErrorInfo from "@/utils/other/mp/errorinfo";
    export default {
      name: "backDialog",
      props:{
      },
      data(){
          return{
            /**
            * 宋建国：2019-06-21 04:50:44
            * 描述：确定按钮执行状态
            */
            buttonLoad: false,
            /**
            * 宋建国：2019-06-21 04:43:40
            * 描述：提示信息
            */
            placeholder: '',
            /**
            * 宋建国：2019-06-19 01:38:50
            * 描述：对话框标题
            */
            titleName: '',
            /**
            * 宋建国：2019-06-09 03:28:45
            * 描述：控制对话是否显示
            */
            dialogVisible: false,
            /**
            * 宋建国：2019-06-09 09:19:20
            * 描述：mp平台返回数据，不知道甚麽意思
            */
            stepid: '',
            /**
            * 宋建国：2019-06-19 04:48:36
            * 描述：当前ID
            */
            currstepid:'',
            /**
            * 宋建国：2019-06-20 10:57:07
            * 描述：对应handleWFAction.do接口的wfid
            */
            nodeguid: '',
            /**
            * 宋建国：2019-06-09 09:22:28
            * 描述：工作流ID
            */
            wfid: '',
            /**
            * 宋建国：2019-06-19 02:15:40
            * 描述：动作类型,发起流程Create(0),
            * 审批Audit(1) ,
             * 重新发起Reset(2),
             * 沟通AssiAssignSubmit(4),
             * 签提交HQSubmit(5),
             * 驳回Back(6),
             * 作废Cancel(7),
             * 转办Revolution(8),
             * 流程结束Close(9),
             * 重设责任人ResetAuditor(10),
             * 发起人指定责任人CreateAuditor(11),
             * 提醒处理Remind(12),
             * 草稿Draft(13),
             * 暂存Hold(14),
             * 已办中追加内容Add(15),
             * 回收Recover(18),
             * 唤醒Wake(19),
             * 删除AdminCancel(20),
             * 已办抄送CC(21),
             * 跳转Jump(22),
             * 带条件审批AuditCondition(24),
             * 查看抄送待办特殊动作Notice(25)
             */
            ationtype: '-1',
            /**
            * 宋建国：2019-06-12 11:17:18
            * 描述：审批商业ID
            */
            business_no: '',
            /**
            * 宋建国：2019-06-20 11:00:56
            * 描述：审批数据
            */
            businessdata: '',
            /**
            * 宋建国：2019-06-19 02:20:57
            * 描述：流程名称
            */
            process_name: '',
            /**
            * 宋建国：2019-06-12 11:40:42
            * 描述：驳回步骤信息
            */
            reject:[],
            /**
            * 宋建国：2019-06-24 03:32:53
            * 描述：所有驳回步骤信息
            */
            allReject:[],
            /**
            * 宋建国：2019-06-12 11:41:20
            * 描述：下一步责任人
            */
            auditors: [],
            /**
            * 宋建国：2019-06-12 02:31:21
            * 描述：驳回选择步骤
            */
            rejectSelect: '',
            /**
            * 宋建国：2019-06-12 02:32:12
            * 描述：下一步
            */
            userlist:'',
            /**
            * 宋建国：2019-06-12 03:37:18
            * 描述：发起意见填写
            */
            opinion: ''
          }
      },
      methods:{
        /**
        * 宋建国：2019-06-09 03:59:28
        * 描述：打开审批对话框
        */
        open: function (rows) {
          let then = this;
          this.titleName = rows.titlename;
          this.stepid = rows.stepid;
          this.wfid = rows.wfid;
          this.business_no = rows.business_no;
          this.ationtype = rows.ationtype;
          this.process_name = rows.process_name;
          this.currstepid = rows.currstepid;
          this.nodeguid = rows.nodeguid;
          this.businessdata = rows.businessdata;
          this.placeholder = rows.placeholder;
          /**
          * 宋建国：2019-06-12 10:22:01
          * 描述：获取下一步信息
          */
          this.$api.expapi.getBackInfo({
            action: this.ationtype,
            wfid: this.currstepid, //流程ID
            currstepid: this.stepid, //当前ID
            stepid: this.stepid
          }).then(res => {
            if(res.result !== '1'){
              let message = ErrorInfo.ERRORINFO[res.msg];
              this.$message.error(message);
              return;
            }
            /**
            * 宋建国：2019-06-12 11:32:14
            * 描述：插入下一步下拉框数据
            */
            if(!res.rows.reject){
              return;
            }
            if(!res.rows.auditors){
              return;
            }
            then.allReject = res.rows.reject;
            for(let i=0; i<res.rows.reject.length; i++){
              then.reject.push({
                value: res.rows.reject[i].ROLLBACK_STEP_GUID,
                label: res.rows.reject[i].STEP_NAME
              });
            }
            if(this.reject.length>0){
              this.rejectSelect = then.reject[0].value;
            }
            for(let i=0; i<res.rows.auditors.length; i++){
              then.auditors.push({
                value: res.rows.auditors[i].user_id,
                label: res.rows.auditors[i].user_name
              });
            }
            if(this.auditors.length>0){
              this.userlist = then.auditors[0].value;
            }
          }).catch(err => {
            this.$message.error(err.message);
          });
          this.dialogVisible = true;
        },
        /**
        * 宋建国：2019-06-12 02:34:15
        * 描述：发起接口调用
        */
        onActionEvent: function(){
           this.buttonLoad = true;
           let tmpreject = this.getReject(this.rejectSelect);
           let sub_json = {
             userinfo: {
               userid: sessionStorage.getItem('userid'),    //用户ID
               username: sessionStorage.getItem('user')  //用户名称
             },
             wfid: this.nodeguid,  //流程ID
             currstepid: this.stepid,//当前流程步骤ID
             actiontype: this.ationtype,  //提交操作类型
             priotiry: '-1', //优先级
             cc: [],  //账号
             handler_text: this.opinion,
             attch: [], //附件信息
             process_name: this.process_name,//流程名称
             business_data: this.businessdata, //审批数据
             business_no: this.business_no,//业务单据号
             back_jump: tmpreject.IS_JUMP,
             back_step: tmpreject.ROLLBACK_STEP_GUID,
             back_auditorsId: this.userlist
           }
           let subjson = encodeURIComponent(JSON.stringify(sub_json));
           this.$api.expapi.handleWFAction({
             subJson: subjson
           }).then(res => {
             if(res.result !== '1'){
               let message = ErrorInfo.ERRORINFO[res.msg]
               this.$message.error(message);
               this.buttonLoad = false;
               return;
             }
             this.$emit("onDialogClose");
             this.close();
           }).catch(err => {
             this.$message.error(err.message);
             this.buttonLoad = false;
           })
        },
        /**
        * 宋建国：2019-06-09 04:00:38
        * 描述：关闭审批对话框
        */
        close:function () {
          this.dialogVisible= false;
        },
        /**
        * 宋建国：2019-06-19 02:52:27
        * 描述：根据用户ID获取用户信息
        */
        getReject(guid){
          let result = {ROLLBACK_STEP_GUID:"",IS_JUMP:0};
          for(let i=0; i<this.allReject.length; i++){
            if(this.allReject[i].ROLLBACK_STEP_GUID === guid){
              result.ROLLBACK_STEP_GUID = this.allReject[i].ROLLBACK_STEP_GUID;
              result.IS_JUMP = this.allReject[i].IS_JUMP;
              break;
            }
          }
          return result;
        }
      }
    }
</script>

<style scoped>
  .el-dialog__footer{
    text-align: right;
  }
</style>
