<template>
    <div>
      <el-dialog
        v-dialogDrag
        title="审批发起"
        :close-on-click-modal="false"
        :visible.sync="dialogVisible"
        center
        width="50%">
          <div>
              <span>
                下一步骤：
                <el-select v-model="setpname" placeholder="请选择下一步骤" @change="onSetpChange"
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
                  style="width: 55%">
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
              placeholder="请输入发起意见"
              :maxlength="50"
              v-model="opinion">
            </el-input>
        </div>
          <div id='graphContainer' ref="xyjsvg" style="width: 100%;margin-top:30px"></div>
          <div style="margin-top: 10px;color: red;font-size: 16px;"><span style="margin-right: 2px;">*</span>
            该清单数据，一旦提交除非上级驳回，否则将不能进行编辑，请确认清单无误后提交。</div>
          <span slot="footer" class="dialog-footer">
            <el-button v-for="actions in actionlist" :key="actions.ID" @click="onActionEvent(actions)" type="primary" size="mini">
              {{actions.NAME}}
            </el-button>
          </span>
      </el-dialog>
    </div>
</template>

<script>
    import mxgraph from "@/components/bcomponents/mxgraph/index.js";
    import ErrorInfo from "@/utils/other/mp/errorinfo";
    const {mxGraph, mxClient, mxCodec, mxUtils, mxConstants,mxGraphModel,mxGraphHandler,mxEdgeStyle} = mxgraph;
    export default {
      name: "approvalDialog",
      props:{
      },
      data(){
          return{
            /**
            * 宋建国：2019-06-09 03:28:45
            * 描述：控制对话是否显示
            */
            dialogVisible: false,
            /**
             * 宋建国：2019-06-09 03:21:18
             * 描述：从MP平台中获取的审批节点设置数据
             * 数组中的对象结构
             *    {
             *      "ID": "E2D4A60D-5A39-40FE-B63E-FD4F423F435E",
             *      "NAME": "起草",
             *      "ACTION_TYPE": 13,
             *      "SHOWATTACH": null,
             *      "SHOWOPTION": null,
             *      "SHOWNEXTSTEP": null,
             *      "SHOWSUBCONFIRM": null,
             *      "SHOWCC": null,
             *      "ACTIONDESC": null,
             *      "QUICKOPTIONLIST": null,
             *      "CONFIRMTEXT": null,
             *      "SHOWACTIONDIALOG": null,
             *      "EXTEND_PROPERTY": null,
             *      "LAN_KEY": "btn_action_draft",
             *      "IS_CAN_CONFIGSTEP": 1,
             *      "IS_NEED_UICONFIG": 0,
             *      "IS_NEED_REMINDCONFIG": 0
             *    }
             */
            actionlist: {
              type: Array,
              default () {
                return []
              }
            },
            /**
            * 宋建国：2019-06-27 03:00:38
            * 描述：发起时回调函数
            */
            callback:{
              type: Function,
            },
            /**
            * 宋建国：2019-06-09 09:19:20
            * 描述：mp平台返回数据，不知道甚麽意思
            */
            stepid: '',
            /**
            * 宋建国：2019-06-09 09:22:28
            * 描述：工作流ID
            */
            wfid: '',
            /**
            * 宋建国：2019-06-12 11:17:18
            * 描述：审批商业ID
            */
            business_no: '',
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
            * 宋建国：2019-06-12 02:32:12
            * 描述：下一步用户列表
            */
            userlist:[],
            /**
            * 宋建国：2019-06-12 03:37:18
            * 描述：发起意见填写
            */
            opinion: '',
            /**
            * 宋建国：2019-06-14 03:43:01
            * 描述：车型基本信息
            */
            carinfo: {},
            base64: null,
          }
      },
      methods:{
        /**
        * 宋建国：2019-06-09 03:59:28
        * 描述：打开审批对话框
        */
        open: function (rows,carinfo,callback) {
          let then = this;
          this.callback = callback;
          this.actionlist = rows.actionlist;
          this.stepid = rows.stepid;
          this.wfid = rows.wfid;
          this.business_no = rows.business_no;
          this.carinfo = carinfo;
          /**
          * 宋建国：2019-06-12 10:22:01
          * 描述：获取下一步信息
          */
          let sub_json = {
            wfid: this.wfid, //流程ID
            currstepid: this.stepid, //当前ID
            business_data: '',  //审批数据
            business_no: this.business_no  //审批单据ID
          };
          let subjson = encodeURIComponent(JSON.stringify(sub_json));
          this.$api.expapi.getNextStepInfo({
            action: 0,
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
              then.setpname = then.choosesteps[0].value;
              then.onSetpChange(then.setpname);
            }
          }).catch(err => {
            this.$message.error(err.message);
          });
          //获取流程图
          this.$api.expapi.getprocessgraph({
            wfid: this.wfid,
            nodeid: ''
          }).then(res =>{
            if(res.result !== '1'){
              let message = ErrorInfo.ERRORINFO[res.msg]
              this.$message.error(message);
              return;
            }
            then.graphContainer(res.rows.graphxml);
          }).catch(err => {
            this.$message.error(err.message);
          })
          this.dialogVisible = true;
        },
        /**
        * 宋建国：2019-06-12 02:34:15
        * 描述：发起接口调用
        */
        onActionEvent: function(actins){
          let businessdata = JSON.stringify(this.carinfo);
          let sub_json = {
            userinfo: {
              userid: sessionStorage.getItem('userid'),    //用户ID
              username: sessionStorage.getItem('user')  //用户名称
            },
            wfid: this.wfid,  //流程ID
            actiontype: actins.ACTION_TYPE,
            priotiry: '-1', //优先级
            cc: [],  //账号
            currstepid: this.stepid,
            handler_text: this.opinion,
            business_data: businessdata,
            business_no: this.business_no,
            process_name: '',
            nextstep: [{
              stepid: this.setpname, //下一步骤流程ID
              auditor: this.getAuditor(this.setpname,this.userlist) //下一步责任人
            }]
          }
          let subjson = encodeURIComponent(JSON.stringify(sub_json));
          this.$api.expapi.handleWFAction({
            subJson: subjson
          }).then(res => {
            if(res.result !== '1'){
              let message = ErrorInfo.ERRORINFO[res.msg]
              this.$message.error(message);
              return;
            }
            this.callback();
            this.close();
          }).catch(err => {
            this.$message.error(err.message);
          })
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
        * 宋建国：2019-06-10 12:12:50
        * 描述：
        */
        graphContainer:function(graphxml){
          if (!mxClient.isBrowserSupported()) {
            // 判断是否支持mxgraph
            mxUtils.error('Browser is not supported!', 200, false);
          } else {
            // 再容器中创建图表
            let container = document.getElementById('graphContainer');
            container.innerHTML = '';
            let model = new mxGraphModel();
            let graph = new mxGraph(container, model);
            graph.getLabel = function(cell) {
              let label = (this.labelsVisible) ? this.convertValueToString(cell) : '';
              let geometry = this.model.getGeometry(cell);

              if (label != null && this.model.isVertex(cell)) {
                if (cell.children != null && cell.children.length > 0) {		//如果是父级vertex则遍历子元素，分别执行getLabel方法
                  for (var i = 0; i < cell.children.length; i++) {
                    graph.getLabel(cell.children[i]);
                    continue;
                  }
                }
                var style = this.getCellStyle(cell);						//底层vertex开始执行处，只有接口图标的label的fontstyle为2，此时就返回空
                var fontStyle = style[mxConstants.STYLE_FONTSTYLE];
                if (2 == fontStyle) {
                  return '';
                }
              }
              if(geometry.isConnector === 'true'){
                label = '';
              }else{
                if(geometry.StepName){
                  if(geometry.StepName.length>0){
                    label = geometry.StepName;
                  }
                }
              }
              return label;											//底层vertex但不是接口图标，此时正常显示label
            };
            graph.setResizeContainer(true);
            graph.setEnabled(true);
            mxGraphHandler.prototype.setMoveEnabled(false);
            graph.setCellsResizable(false);
            graph.setCellsEditable(false);
            graph.setConnectable(false);

            let style = graph.getStylesheet().getDefaultEdgeStyle();
            style[mxConstants.STYLE_ROUNDED] = true;//圆角连线
            style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector; //自己拐弯的连线

            graph.getModel().beginUpdate();
            let doc = mxUtils.parseXml(graphxml);
            let dec = new mxCodec(doc.documentElement);
            dec.decode(doc.documentElement, graph.getModel());
            graph.getModel().endUpdate();

            graph.fit();//自适应
            graph.center(true,true,0.5,0.5);//将画布放到容器中间
            let sc = graph.getView().getScale();//获取当前的缩放比例
            graph.zoomTo(Math.round(sc/2));//在缩放一半，否则是满屏状态，不好看
          }
        },
        /**
        * 宋建国：2019-06-09 04:00:38
        * 描述：关闭审批对话框
        */
        close:function () {
          this.dialogVisible= false;
        }
      },
      created() {
        this.base64 = require('js-base64').Base64;
      }
    }
</script>

<style scoped>
  .el-dialog__footer{
    text-align: right;
  }
</style>
