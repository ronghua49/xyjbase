<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="驳回发起节点对话框"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      center
      width="800px">
      <div id="upperFirstLevelLoad">
        <el-row v-for="(nodeItme,i) in firstNodes"
                :key="i"
                :gutter="10">
          <el-col :span="4"><div class="node-name bg-purple">发起点名称</div></el-col>
          <el-col :span="20"><div class="node-name bg-purple">{{nodeItme.name}}</div></el-col>
        </el-row>
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
          <el-button type="primary" size="mini" @click="dialogCancel()">{{$t('workflow.rejectFirstTask.cancel')}}</el-button>
		      <el-button type="primary" size="mini" @click="dialogConfirm()" :loading="okLoad">{{$t('workflow.rejectFirstTask.reject')}}</el-button>
		  </span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
	import axios from "@/http/axios"
    export default {
      name: "xyjFirstNextLevel",
      props: {
        /**
         *宋建国：2019/9/18 15:58
         *描述：驳回后事件
         *版本：1.0
         */
        OnRejectFirstAfter: {
          type: Function,
          default(rData){
            return true
          }
        },
      },
      data() {
        return {
          /**
           *宋建国：2019/10/26 1:50
           *描述：为了显示发起节点
           *版本：1.0
           */
          firstNodes:[],
          /**
           *宋建国：2019/10/26 12:42
           *描述：提交发起节点数据
           *版本：1.0
           */
          commitNodes:[],
          dialogVisible: false, //对话框打开变量
          okLoad: false, //驳回进度状态
          taskId: "", //当前任务ID
          /**
           *宋建国：2019/10/25 12:11
           *描述：驳回节点ID
           *版本：1.0
           */
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
         *版本：1.0
         */
        open(taskId,keyword){
        	this.loading = true
          this.upperNodeName = "";
          this.targetActivityId = "";
          this.taskId = taskId;
          this.dialogVisible = true;
          let firstLoading = this.$loading({
              lock: true,
              text: '加载中...',
              spinner: 'el-icon-loading',
              target: document.querySelector('#upperFirstLevelLoad')
            });
          this.$api.workflow.getFirstNode({
            taskId: this.taskId //任务Id

          }).then((res) =>{
            /**
             *宋建国：2019/10/26 11:50
             *描述：res.data: [{id:节点定义ID,name:节点定义名称,nodetype: 节点类型(UserTask（）、ExclusiveGateway、ParallelGateway、InclusiveGateway)}]
             *版本：1.0
             */
            this.firstNodes.length=0;
            if(res.data.length<=0){
              this.$message.info("没有找到发起节点");
              firstLoading.close();
              this.dialogVisible = false;
              return;
            }
            for(let i=0; i<res.data.length; i++){
              if(res.data[i].nodetype === "UserTask"){
                this.firstNodes.push({name:res.data[i].name});
              }
            }
            if(this.firstNodes.length>0){
              this.commitNodes = res.data;
            }
            firstLoading.close();
          }).catch((err)=>{
            firstLoading.close();
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
          if(this.commitNodes.length<=0){
            this.$message.error("发起节点ID不能为空");
            return;
          }
          this.okLoad = true;
          this.$api.workflow.disposeTask(
            {
              actionName: "rejectToStart",
              taskId: this.taskId,
              comment: this.opinion,
              targetActivityIds: this.commitNodes
            }
          ).then((res)=>{
            res.data = {taskId: tmpThis.taskId,
                        opinion:tmpThis.opinion};
            let state = this.OnRejectFirstAfter(res.data);
            if(state) {
              tmpThis.dialogVisible = false;
            }
            tmpThis.okLoad = false;
          }).catch((err)=>{
            tmpThis.$message.error(err.message);
            tmpThis.okLoad = false;
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
  .el-col {
    border-radius: 4px;
  }
  .bg-purple {
    background: #d3dce6;
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
</style>
