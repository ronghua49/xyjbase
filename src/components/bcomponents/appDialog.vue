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
          <div style="white-space:nowrap">
              <span>
                下一步骤：
                <el-select v-model="setpid" placeholder="请选择下一步骤" @change="onSetpChange"
                style="width: 25%">
                  <el-option
                    v-for="item in choosesteps"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </span>
              <span>
                下一步责任人：
                <el-select
                  v-model="userlist"
                  placeholder="下一步责任人"
                  multiple
                  collapse-tags
                  style="width: 52.3%">
                      <el-option
                        v-for="item in stepUser"
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
            <el-button type="primary" size="mini" @click="onActionEvent" :loading="buttonLoad" :disabled="butState"> {{titleName}} </el-button>
          </span>
      </el-dialog>
    </div>
</template>

<script>
    import ErrorInfo from "@/utils/other/mp/errorinfo";
    export default {
      name: "appDialog",
      props:{
      },
      data(){
          return{
            /**
            * 宋建国：2019-07-17 07:50:00
            * 描述：el-button 按钮状态
            */
            butState: true,
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
            * 宋建国：2019-06-12 11:52:37
            * 描述：下一步所有信息
            */
            steps:[],
            /**
            * 宋建国：2019-06-12 11:40:42
            * 描述：下一步信息
            */
            choosesteps:[],
            /**
            * 宋建国：2019-06-12 11:41:20
            * 描述：下一步责任人
            */
            stepUser: [],
            /**
            * 宋建国：2019-06-12 02:31:21
            * 描述：下一步
            */
            setpname: '',
            /**
            * 宋建国：2019-07-17 08:05:16
            * 描述：下一步ID
            */
            setpid: '',
            /**
            * 宋建国：2019-06-12 02:32:12
            * 描述：下一步用户列表
            */
            userlist:[],
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
          this.butState = true;
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
          let sub_json = {
            wfid: this.currstepid, //流程ID
            currstepid: this.stepid, //当前ID
            business_data: this.businessdata,  //审批数据
            business_no: this.business_no  //审批单据ID
          };
          let subjson = encodeURIComponent(JSON.stringify(sub_json));
          this.$api.expapi.getNextStepInfo({
            action: this.ationtype,
            showd: false,
            nextJson: subjson
          }).then(res => {
            if(res.result !== '1'){
              let message = ErrorInfo.ERRORINFO[res.msg]
              this.$message.error(message);
              return;
            }
            if(!res.rows.steps){
              this.$message.error("获取工作流下一步错误！！！");
              return;
            }
            /**
            * 宋建国：2019-06-12 11:32:14
            * 描述：插入下一步下拉框数据
            */
            then.steps.length = 0;
            then.steps = res.rows.steps[0].choosesteps;
            then.choosesteps.length = 0;
            for(let i=0; i<then.steps.length; i++){
              then.choosesteps.push({
                value: then.steps[i].id,
                label: then.steps[i].name
              });
            }
            if(then.choosesteps.length>0){
              then.setpname = then.choosesteps[0].label;
              then.setpid = then.choosesteps[0].value;
              then.onSetpChange(then.setpid);
            }
            then.butState = false;
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
          // let tbusinessdata = JSON.stringify(this.businessdata);
           let sub_json = {
             userinfo: {
               userid: sessionStorage.getItem('userid'),    //用户ID
               username: sessionStorage.getItem('user')  //用户名称
             },
             wfid: this.nodeguid,  //流程ID
             currstepid: this.currstepid,//当前流程步骤ID
             actiontype: this.ationtype,  //提交操作类型
             priotiry: '-1', //优先级
             cc: [],  //账号
             handler_text: this.opinion,
             attch: [], //附件信息
             process_name: this.process_name,//流程名称
             business_data: this.businessdata, //审批数据
             business_no: this.business_no,//业务单据号
             nextstep: [{
               stepid: this.setpid, //下一步骤流程ID
               auditor: this.getAuditor(this.setpid,this.userlist) //下一步责任人
             }]
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
        * 宋建国：2019-06-12 11:57:32
        * 描述：下一步下拉框change事件
        */
        onSetpChange: function(e){
          for(let i=0; i<this.steps.length; i++){
            if(this.steps[i].id === e){
              this.stepUser.length = 0;
              for(let j=0; j<this.steps[i].user.userlist.length; j++){
                this.stepUser.push({
                  value: this.steps[i].user.userlist[j].user_id,
                  label: this.steps[i].user.userlist[j].user_name
                });
              }
            }
          }
          this.userlist.length = 0;
          if(this.stepUser.length>0){
            this.userlist.push(this.stepUser[0].value);
          }
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
        getAuditor(setpid,userids){
          let result = [];
          for(let i=0; i<this.steps.length; i++){
            if(this.steps[i].id === setpid){
              for(let j=0; j<userids.length; j++){
                let userinfo = this.steps[i].user.userlist.filter(function (item) {
                  return item.user_id === userids[j];
                });
                if(userinfo.length>0){
                  result.push({
                    userid: userinfo[0].user_id,
                    username: userinfo[0].user_name
                  });
                }
              }
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
