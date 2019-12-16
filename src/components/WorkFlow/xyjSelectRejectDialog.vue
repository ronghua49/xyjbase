<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="选择驳回对话框"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      center
      width="800px">
      <div id="upperallLoad">
        <el-row key="toupperallLoadkey" :gutter="10">
          <el-col :span="3">
            <div class="node-name bg-purple">
              驳回节点
            </div>
          </el-col>
          <el-col :span="21">
            <div class="grid-content bg-purple">
              <el-select style="width: 100%;text-align: left" v-model="selectNodeId" filterable placeholder="请选择驳回节点">
                <el-option
                	style="text-align: left;"
                  v-for="(item,i) in nodeList"
                  :key="i"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
          </el-col>
        </el-row >
      </div>
      <div>
        意见：
        <el-input
          type="textarea"
          placeholder="请输入驳回意见"
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
          <el-button type="primary" size="mini" @click="dialogCancel()">{{$t('workflow.selectReject.cancel')}}</el-button>
		      <el-button type="primary" size="mini" @click="dialogConfirm()" :loading="rejectLoad">{{$t('workflow.selectReject.reject')}}</el-button>
		  </span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
	import axios from "@/http/axios"
    export default {
      name: "xyjSelectRejectDialog",
      props: {
        /**
         *宋建国：2019/9/18 15:58
         *描述：驳回成功后回调
         *版本：1.0
         */
        OnSelectRejectAfter: {
          type: Function,
          default(){
            return true
          }
        },
      },
      data() {
        return {
          /**
           *宋建国：2019/10/29 15:22
           *描述：被选择节点数据
           *版本：1.0
           */
          nodeList:[],
          /**
           *宋建国：2019/10/29 15:43
           *描述：所用节点数据
           *版本：1.0
           */
          allNodeList: [],
          /**
           *宋建国：2019/10/29 15:42
           *描述：选择节点ID
           *版本：1.0
           */
          selectNodeId: "",
          dialogVisible: false, //对话框打开变量
          rejectLoad: false, //驳回进度状态
          taskId: "", //任务ID
          opinion: '', //审批意见变量
          commonlanguage: [], //常用语言
          loading:false // 常用语加载状态
        };
      },
      methods: {
        /**
         *宋建国：2019/9/18 15:55
         *描述：打开对话框
         * taskId:当前任务ID
         * varmap：流程变量，传入数据，连线表达式使用此变量，判断连线是否有效
         *版本：1.0
         */
        open(taskId,varmap,keyword){
        	this.loading = true
          this.taskId = taskId;
          this.dialogVisible = true;
          let upperallLoading = this.$loading({
            lock: true,
            text: '加载中...',
            spinner: 'el-icon-loading',
            target: document.querySelector('#upperallLoad')
          });
          this.$api.workflow.getUpperAllNode({
            taskId: this.taskId,
            varMap: varmap
          }).then((res) =>{
            /**
             *宋建国：2019/10/26 11:50
             *描述：res.data: [{id:节点定义ID,name:节点定义名称,nodetype: 节点类型(UserTask（）、ExclusiveGateway、ParallelGateway、InclusiveGateway)}]
             *版本：1.0
             */
            this.nodeList.length=0;
            this.allNodeList.length = 0;
            this.selectNodeId = "";
            if(res.data.length<=0){
              this.$message.info("没有上级驳回节点");
              upperallLoading.close();
              this.dialogVisible = false;
              return;
            }
            for(let i=0; i<res.data.length; i++){
              if(res.data[i].nodetype === "UserTask"){
                this.nodeList.push({
                  label:res.data[i].name,
                  value: res.data[i].id
                });
              }
            }
            if(this.nodeList.length>0){
              this.selectNodeId = this.nodeList[0].value;
            }
            //保存所有节点数据
            this.allNodeList = res.data;
            upperallLoading.close();
          }).catch((err)=>{
            upperallLoading.close();
            this.$message.error(err.message);
            this.dialogVisible = false;
          })
          
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
          if(this.$tu.isBlank(this.selectNodeId)){
            this.$message.error("请选择要驳回的节点信息...");
            return;
          }
          let commitNodes = [];
          let selectnode = true;
          for(let i=0; i<this.allNodeList.length; i++){
              if(tmpThis.selectNodeId === tmpThis.allNodeList[i].id){
                commitNodes.push(tmpThis.allNodeList[i]);
                selectnode = false;
                break;
              }
          }
          for(let j=0; j<tmpThis.allNodeList.length; j++){
            //流程中判断是否有并行、包容网关，上传到后台
            if((tmpThis.allNodeList[j].nodetype.toUpperCase() === "PARALLELGATEWAY")
              || (tmpThis.allNodeList[j].nodetype.toUpperCase() === "INCLUSIVEGATEWAY" )){
              commitNodes.push(tmpThis.allNodeList[j]);
              break;
            }
          }
          if(selectnode){
            tmpThis.$message.error("请选择要驳回的节点信息");
            return;
          }
          this.rejectLoad = true;
          this.$api.workflow.disposeTask(
            {
              actionName: "rejectToAssign", //动作类型
              taskId: this.taskId, //当前任务ID
              comment: this.opinion, //批注
              targetActivityIds: commitNodes //提交数据
            }
          ).then((res)=>{
            res.data = {taskId: tmpThis.taskId,
                        opinion:tmpThis.opinion};
            let state = tmpThis.OnSelectRejectAfter(res.data);
            if(state) {
              tmpThis.dialogVisible = false;
            }
            tmpThis.rejectLoad = false;
          }).catch((err)=>{
            tmpThis.$message.error(err.message);
            tmpThis.rejectLoad = false;
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
	    		this.$message.info('编码:RejectKeyWord')
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
