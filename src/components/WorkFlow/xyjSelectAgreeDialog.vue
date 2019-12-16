<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="选签同意对话框"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      center
      width="800px">
      <div>
        <el-row v-for="(nodeItme,i) in nodes"
                :key="i"
                :gutter="10">
          <el-col :span="6"><div :title="nodeItme.name" class="node-name bg-purple">{{nodeItme.name}}</div></el-col>
          <el-col :span="18"><div class="grid-content bg-purple">
            <el-select style="width: 100%;" v-model="nodeItme.selectData[nodeItme.id]" filterable multiple placeholder="请指定审批人" :loading = selectLoad>
              <el-option
              	style="text-align: left;"
                v-for="(item,i) in nodeItme.taskRoleList"
                :key="i"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </div></el-col>
        </el-row>
      </div>
      <div>
        意见：
        <el-input
          type="textarea"
          placeholder="请输入审批意见"
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
  </div>
</template>

<script>
	import axios from "@/http/axios"
    export default {
      name: "xyjSelectAgreeDialog",
      props: {
        /**
         *宋建国：2019/9/18 15:58
         *描述：审批后事件
         *版本：1.0
         */
        OnAgreeAfter: {
          type: Function,
          default(){
            return true
          }
        },
      },
      data() {
        return {
          /**
           *宋建国：2019/10/19 14:31
           *描述：选择框装载数据状态
           *版本：1.0
           */
          selectLoad: false,
          dialogVisible: false, //对话框打开变量
          agreeLoad: false, //同意进度状态
          taskId: "", //任务ID
          opinion: '', //审批意见变量
          commonlanguage: [],
          /**
           *宋建国：2019/10/19 16:18
           *描述：任务节点候选人信息
           *版本：1.0
           */
          nodes:[],
          loading:false // 常用语加载状态
        };
      },
      methods: {
        /**
         *宋建国：2019/9/18 15:55
         *描述：打开对话框
         *版本：1.0
         */
        open(taskId,keyword){
        	this.loading = true
          this.taskId = taskId;
          this.dialogVisible = true;
          this.nodes = [];
          this.getApprovalInfo();
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
         *宋建国：2019/10/19 13:12
         *描述：获取下一步多个任务节点获选人信息
         *版本：1.0
         */
        getApprovalInfo: function () {
          let tmpThis = this;
          if (this.$tu.isBlank(this.taskId)) {
            this.$message.error("任务ID不能为空");
            return;
          }
          this.selectLoad = true;
          this.$api.workflow.getApprovalInfo({
            taskId: tmpThis.taskId
          }).then((res) => {
            let tmpNodes = res.data;

            for (let i = 0; i < tmpNodes.length; i++) {
              if (tmpNodes[i].taskRoleList.length > 0) {
                tmpNodes[i]["selectData"] = {};
                tmpNodes[i]["selectData"][tmpNodes[i].id] = [tmpNodes[i].taskRoleList[0].value];
              } else {
                tmpNodes[i]["selectData"] = {};
                tmpNodes[i]["selectData"][tmpNodes[i].id] = [];
              }
            }
            tmpThis.nodes = tmpNodes;
            tmpThis.selectLoad = false;
          }).catch((err) => {
            tmpThis.$message.error(err.message);
            tmpThis.selectLoad = false;
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
         *描述：根据流程图流转,允许从节点的多个候选人中选择候选人审批
         *版本：1.0
         */
        dialogConfirm(){
          let tmpThis = this;
          if(this.$tu.isBlank(this.taskId)){
            this.$message.error("任务ID不能为空");
            return;
          }
          /**
           *宋建国：2019/10/19 15:02
           *描述：检验下一步任务节点候选人
           *版本：1.0
           */
          for(let key in this.userInfos){
            if(this.userInfos[key].length<=0){
              this.$message.error("最少选择一个任务节点候选人");
              return;
            }
          }
          this.agreeLoad = true;
          let sData = {};
          for(let i=0; i<this.nodes.length; i++){
            Object.assign(sData,this.nodes[i].selectData)
          }
          this.$api.workflow.disposeTask(
            {
              actionName: "selectAgree",
              taskId: this.taskId,
              comment: this.opinion,
              map: {
                aa: "111",
                bb: "222"
              },
              mapCandidate: sData
            }
          ).then((res)=>{
            res.data = {taskId: tmpThis.taskId,
                        opinion:tmpThis.opinion};
            let state = this.OnAgreeAfter(res.data);
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
