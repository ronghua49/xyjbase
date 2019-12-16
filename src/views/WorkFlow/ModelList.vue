<template>
  <div style="height: 100%;">
    <el-row class="m_all" :gutter="15">
      <!--模型类别-->
      <el-col :span="4" class="m_left">
        <div @click.ctrl="showCode" style="width: 278px;">
          <el-tree
            :data="data1"
            show-checkbox
            default-expand-all
            node-key="id"
            ref="tree"
            highlight-current
            :check-strictly="true"
		  			:check-on-click-node="true"
		  			@node-click="change"
		  			@check="changes"
		  			:default-checked-keys="defaultKey"
            :props="defaultProps">
          </el-tree>
        </div>
      </el-col>
      <el-col :span="20" class="m_right">
        <div class="grid-content">
          <!--模型列表-->
          <xyj-table-work-flow ref="reftableworkflow" id="1" v-bind="tableworkflow">
            <template slot="xyjcontainer">
              <Xyj-button  type="primary" :label="$t('workflow.add')" @click="createModel" text="新增"></Xyj-button>
              <XyjUpload v-bind="upload" text="导入模型"></XyjUpload>
              <Xyj-button  type="primary" :label="$t('workflow.deploy')" @click="createModel" text="批量部署"></Xyj-button>
            </template>
          </xyj-table-work-flow>
        </div>
      </el-col>
    </el-row>
    <model-design ref="modeldesing"></model-design>
  </div>
</template>

<script>
  import ModelDesign from "@/components/WorkFlow/ModelDesign";
  import axios from "@/http/axios";
  export default {
    name: "ModelList",
    components: {ModelDesign},
    data() {
      return {
        /**
         *宋建国：2019/8/14 10:55
         *描述：导入流程模型
         *版本：1.0
         */
        upload:{
        	uploadUrl:{
	          hostUrl: this.global.workflow,
	          url: '/model/importXml',
	          /*上传参数整理*/
	          upParam: function (upFile) {
	            let fd = new FormData();
	            fd.append('file',upFile.file);
	            return fd;
	          },
	          /*上传成功回调*/
	          upSuccess : function (res) {
	            this.$message.success("流程名称：‘" + res.data.name+"’导入成功");
	          }
	        },
	        label:'action.Import'
        },
        /**
         *宋建国：2019/8/7 15:33
         *描述：审批模型表格
         */
        tableworkflow: {
          hasDelete: false, //删除
          hasNew: false, //新增
          hasView: false, //查看
          hasEdit: false, //编辑
          //不显示表格操作列
          hasOperation: true,
          /**
           *宋建国：2019/8/7 17:39
           *描述：自定义按钮
           */
          extraButtons:[
            {
              label: 'workflow.modify',
              type: "primary",
              text: "修改",
              atClick: this.butModify
            },
            {
              label: 'workflow.sdeploy',
              type: "primary",
              text: "部署",
              atClick: this.butDeploy
            },
            {
              label: 'workflow.delete',
              type: "danger",
              text: "删除",
              atClick: this.butDelete
            },
            {
              label: 'workflow.export',
              type: "primary",
              text: "导出",
              atClick: this.exportXml
            }
          ],
          //el-table设置
          tableAttrs: {
            height: "89%",
            width: "90%"
          },
          //el-table事件设置
          tableOns: {

          },
          //表格中的操作列设置
          operationAttrs: {
            width: "210px"
          },
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            //state:  utilmp.approvalStateChine
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "WorkflowModel",
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.workflow,
            url: "model/AllModelList",
            method: "post",
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {
              0: { field0: "category", gx0: "EQUAL", value0: "BpmTree", andor0: "AND" }
          }, //表格初始化时查询条件
          codeVaule:this.codeVaule,
          sortVaule:this.sortVaule,
          searchList:['key','name']
        },
        data1: [],
        defaultProps: {
          children: 'childList',
          label: 'name'
        },
        defaultKey:[],
      };
    },
    /**
     *宋建国：2019/8/7 17:47
     *描述：方法
     */
    methods:{
    	//显示编码
    	showCode(){
    		this.$message.info('编码:BpmTree')
    	},
    	codeVaule(data){
    		this.$nextTick(() => {
    			let code = this.$refs.tree.getCheckedNodes()[0].code
    			let obj = {hasInitValue: false, operation: "EQUAL", property: "category", relation: "AND", value:code }
    			data.querys && data.querys.length>0 
    			? data.querys.forEach(res => {
			    	if(res.value != obj.value){
			    		data.querys.push(obj)
			    	}
			    })
    			: data.querys.push(obj)
    		})
    	},
    	sortVaule(){
    		return {0: { field0: "category", gx0: "EQUAL", value0:this.$refs.tree.getCheckedNodes()[0].code , andor0: "AND" }}
    	},
    	change(data,isChecked){
    		// 节点选中状态发生变化
      	this.$refs.tree.setCheckedNodes([data])
      	
    	},
    	changes(data){
    		this.$refs.tree.setCheckedNodes([data])
    		this.$refs.reftableworkflow.getList({0: { field0: "category", gx0: "EQUAL", value0: data.code, andor0: "AND" }})
    	},
      /**
       *宋建国：2019/8/7 17:50
       *描述：编辑流程模型
       */
      butModify: function (row) {
        let _this = this;
        return new Promise(function (resolve,reject){
          _this.$refs.modeldesing.open(row.id);
          resolve(false);
        });
      },
      /**
       *宋建国：2019/8/7 17:50
       *描述：部署流程模型
       */
      butDeploy: function (row) {
        let _this = this;
        return new Promise(function (resolve,reject){
          _this.$confirm(_this.$TINFO.TIPSINFO.modelDeploy,{
            type: 'warning',
            beforeClose: (action,instance,done) => {
              if(action == 'confirm'){
                instance.confirmButtonLoading = true;
                _this.$api.workflow.deployModel({
                  modelId: row.id
                }).then((res) => {
                  instance.confirmButtonLoading = false;
                  done();
                  _this.$message.success(_this.$TINFO.TIPSINFO.modelDeploySuccess);
                }).catch((err) => {
                  _this.$message.error(err.message);
                  instance.confirmButtonLoading = false;
                  done();
                });

              }else{
                done();
              }
            }
          }).catch((err) => {

          })
          resolve(false);
        });
      },
      /**
       *宋建国：2019/8/9 17:28
       *描述：删除流程模型
       */
      butDelete: function(row){
        let _this = this;
        return new Promise(function (resolve,reject){
          _this.$confirm(_this.$TINFO.TIPSINFO.deleteTipsInfo,{
            type: 'warning',
            beforeClose: (action,instance,done) => {
              if(action == 'confirm'){
                instance.confirmButtonLoading = true;
                _this.$api.workflow.deleteModel({
                  modelId: row.id
                }).then((res) => {
                  instance.confirmButtonLoading = false;
                  done();
                  _this.$refs.reftableworkflow.getList()
                  _this.$message.success(_this.$TINFO.TIPSINFO.deleleInfo);
                }).catch((err) => {
                  _this.$message.error(err.message);
                  instance.confirmButtonLoading = false;
                  done();
                });

              }else{
                done();
              }
            }
          }).catch((err) => {

          })
          resolve(false)
        })
      },
      /**
       *宋建国：2019/8/9 12:59
       *描述：创建空流程模型
       */
      createModel: function (cab) {
        this.$refs.modeldesing.open(null,this.$refs.tree.getCheckedNodes(),cab);
      },
      /**
       *宋建国：2019/8/11 23:14
       *描述：导出流程模型XML文件
       */
      exportXml: function (row) {
        let _this = this;
        return new Promise(function (resolve,reject){
          _this.$api.workflow.exportModelXml({
            modelId: row.id
          }).then((res) => {
            //处理下载文件
            _this.$tu.downloadfile(res);
          }).catch((err) => {
            _this.$message.error(err.message);
          })
          resolve(false)
        });
      }
    },
    created(){
    	axios({
        // 用axios发送post请求
        hostUrl: this.global.baseUrl,
        method: "get",
        url:"/sysTree/fiandByCode?code=BpmTree",
      })
			.then(res => {
				if(res.code == 200) {
					this.data1 = JSON.parse(JSON.stringify([res.data]));
					function formatter(data){
						data.forEach(element => {
							if(element.childList && element.childList.length>0){
								let flag = formatter(element.childList);
								if(flag) return false;
							}else{
								element.name = element.name + ' | ' + element.code	
								return true;
							}	
							element.name = element.name + ' | ' + element.code
						})
					}
					formatter(this.data1)
				}
				this.defaultKey = [this.data1[0].id];
			})
			.catch(err => {
				this.$message.error(err.message);
			})
    }
  }
</script>

<style scoped>
  .m_all{
    height: 100%;
  }
  .m_left{
    height: 100%;
    overflow: auto;
  }
  .m_right:before{
    content: '';
    position: absolute;
    top: -10px;
    bottom: -10px;
    left: 0;
    width: 1px;
    background: #E4E7ED;
  }
  .m_left>div{
    height: 100%;
    overflow: auto;
  }
  .grid-content,.m_right{
  	position: relative;
    height: 100%;
  }
</style>
