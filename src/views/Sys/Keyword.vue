<template>
  <div style="height: 100%;">
    <el-row class="m_all" :gutter="15">
      <!--模型类别-->
      <el-col :span="6" class="m_left">
        <div class="tree" @click.ctrl="showCode">
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
      <el-col :span="18" class="m_right">
        <div class="grid-content">
          <!--模型列表-->
          <xyj-table ref="reftableitem" id="1" v-bind="tableitem"></xyj-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import axios from "@/http/axios";
  export default {
    name: "Keyword",
    data() {
      return {
        /**
         *宋建国：2019/8/7 15:33
         *描述：审批模型表格
         */
        tableitem: {
          hasDelete: true, //删除
          hasNew: true, //新增
          hasView: false, //查看
          hasEdit: true, //编辑
          //不显示表格操作列
          hasOperation: true,
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
            width: "130px"
          },
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            //state:  utilmp.approvalStateChine
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "SysKeyword",
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.baseUrl,
            url: "sysKeyword/query",
            method: "post",
            notCondition: true //没有查询条件则查询全部数据
          },
          //业务数据微服务,新增网址
	        bNewUrl: {
	          hostUrl: this.global.baseUrl,
	          url: "sysKeyword/save",
	          method: "post",
	          clear: true, //清除新增对话框输入的数据
	          close: true //关闭新增对话框
	        },
	        //业务数据微服务,修改网址
	        bEditUrl: {
	          hostUrl: this.global.baseUrl,
	          url: "sysKeyword/save",
	          method: "post",
	          clear: true, //清除新增对话框输入的数据
	          close: true //关闭修改对话框
	        },
	        //业务数据微服务,删除网址
	        bDeleteUrl: {
	          hostUrl: this.global.baseUrl,
	          url: "/sysKeyword/delete",
	          method: "post"
	        },
          InitQuery: {
              0: { field0: "treecode", gx0: "EQUAL", value0: "KeyWord", andor0: "AND" }
          }, //表格初始化时查询条件
          //点击新增时，处理默认的数据
          onManualNew(row){
          	row.treecode = row.treecode.split('|')[1].trim();
          },
          onManualEdit(row,data){
          	row.id = data.id;
          	row.treecode = row.treecode.split('|')[1].trim();
          },
          codeVaule:this.codeVaule,
          sortVaule:this.sortVaule,
          onDialogDefault:this.dialogDefault,
          onBeforeNew:this.dialogMessage
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
    		this.$message.info('编码:KeyWord')
    	},
    	dialogMessage(row){
    		if(this.data1[0].code == this.$refs.tree.getCheckedNodes()[0].code){
    			this.$message.error('根节点不能新增');
    			this.$refs.reftableitem.dialogVisible = false
    		}
    	},
    	dialogDefault(row){
    		function fun(data){
    			for(let res of data) {
						if(res.childList && res.childList.length>0) {
							if(res.code == row.treecode) {
								row.treecode = res.name
							}	
							fun(res.childList)
						}
						if(res.code == row.treecode) {
							row.treecode = res.name
						}
					}
    		}
    		
    		this.$nextTick(()=>{
      		!row.treecode ? row.treecode = this.$refs.tree.getCheckedNodes()[0].name : fun(this.data1)
      	})
    	},
    	codeVaule(data){
    		this.$nextTick(() => {
    			let code = this.$refs.tree.getCheckedNodes()[0].code
    			let obj = {hasInitValue: false, operation: "EQUAL", property: "treecode", relation: "AND", value:code }
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
    		return {0: { field0: "treecode", gx0: "EQUAL", value0:this.$refs.tree.getCheckedNodes()[0].code , andor0: "AND" }}
    	},
    	change(data,isChecked){
    		// 节点选中状态发生变化
      	this.$refs.tree.setCheckedNodes([data])
      	
    	},
    	changes(data){
    		this.$refs.tree.setCheckedNodes([data])
    		this.$refs.reftableitem.getList({0: { field0: "treecode", gx0: "EQUAL", value0: data.code, andor0: "AND" }})
    	},
    },
    created(){
    	axios({
        // 用axios发送post请求
        hostUrl: this.global.baseUrl,
        method: "get",
        url:"/sysTree/fiandByCode?code=KeyWord",
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
  .tree{
  	width: 500px;
  	height: 660px;
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
