<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="审批同意对话框"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      center
      width="800px">
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
        <el-button type="text" v-for="(btn,i) in commonlanguage" :key="i" @click="commonlanguageClick(btn.name)">{{btn.name}}</el-button>
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
      name: "xyjAgreeDialog",
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
         *宋建国：2019/9/18 15:55
         *描述：打开对话框
         *版本：1.0
         */
        open(taskId,keyword){
        	this.loading = true;
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
							this.loading = false;
							this.commonlanguage = res.data.rows;
						};
					})
					.catch(err => {
						this.loading = false;
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
          this.agreeLoad = true;
          this.$api.workflow.disposeTask(
            {
              actionName: "agree",
              taskId: this.taskId,
              comment: this.opinion,
              map: {
                aa: "111",
                bb: "222"
              }
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

<style scoped>

</style>
