<template>
  <div style="height: 100%;">
  	<el-dialog title="菜单路由" :visible.sync="menuRouteVisible" width="1200px" :close-on-click-modal="false">
    	<el-row class="m_all" :gutter="15" style="height: 400px;">
	      <el-col :span="5" class="m_left">
	      	<el-input size="mini" placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
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
	            :props="defaultProps"
	            :filter-node-method="filterNode">
	          </el-tree>
	        </div>
	      </el-col>
	      <el-col :span="16" class="m_right">
	        <div class="grid-content">
	          <!--模型列表-->
	          <xyj-table ref="reftableitem" id="1" v-bind="tableitem"></xyj-table>
	        </div>
	      </el-col>
	      <el-col :span="3" style="text-align: left;">
	      	<el-button type="text" @click="chooseList = []">清空</el-button>
					<span style="display: block;">已选菜单</span>
					<el-divider></el-divider>
					<el-tooltip effect="dark" content="双击移除当前已选择的菜单路由" placement="bottom-start" v-for="(item,index) in chooseList" :key="index">
						<span style="display: block;cursor: pointer;" @dblclick="removePost(item)">{{ (index+1)+'、'+item.menuName }}</span>
					</el-tooltip>
	      </el-col>
	    </el-row>
    	<span slot="footer" class="dialog-footer">
        <el-button size="mini"  @click="dialogCancel">{{$t('action.cancel')}}</el-button>
        <el-button size="mini"  type="primary" @click="dialogConfirm">{{$t('action.comfirm')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import axios from "@/http/axios";
  export default {
    name: "xyjMenuRouteDialog",
    props:{
    	cancel: {
				type: Function,
			},
			confirm: {
				type: Function
			}
    },
    watch: {
			filterText(val) {
				this.$refs.tree.filter(val);
			}
		},
    data() {
      return {
        tableitem: {
          hasDelete: true, //删除
          hasNew:  false, //新增
          hasView: false, //查看
          hasEdit: true, //编辑
          //不显示表格操作列
          hasOperation: false,
          appendToBody: true,
          //el-table设置
          tableAttrs: {
            height: "89%",
            width: "90%"
          },
          //el-table事件设置
          tableOns: {
						"row-dblclick": this.dbclick
          },
          //表格中的操作列设置
          operationAttrs: {
            width: "130px"
          },
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "SysMenuRoute",
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.baseUrl,
            url: "sysMenuRoute/query",
            method: "post",
            notCondition: true //没有查询条件则查询全部数据
          },
          //业务数据微服务,新增网址
	        bNewUrl: {
	          hostUrl: this.global.baseUrl,
	          url: "sysMenuRoute/save",
	          method: "post",
	          clear: true, //清除新增对话框输入的数据
	          close: true //关闭新增对话框
	        },
	        //业务数据微服务,修改网址
	        bEditUrl: {
	          hostUrl: this.global.baseUrl,
	          url: "sysMenuRoute/save",
	          method: "post",
	          clear: true, //清除新增对话框输入的数据
	          close: true //关闭修改对话框
	        },
	        //业务数据微服务,删除网址
	        bDeleteUrl: {
	          hostUrl: this.global.baseUrl,
	          url: "/sysMenuRoute/delete",
	          method: "post"
	        },
          InitQuery: {
              0: { field0: "treeCode", gx0: "EQUAL", value0: "MenuRoute", andor0: "AND" }
          }, //表格初始化时查询条件
          /**
			     *张俊娜：2019/8/7 17:47
			     *查询时需要添加其它查询条件的函数
			     */
          codeVaule:this.codeVaule,
          /**
			     *张俊娜：2019/8/7 17:47
			     *排序时需要添加其它查询条件的函数
			     */
          sortVaule:this.sortVaule,
        },
        data1: [],
        defaultProps: {
          children: 'childList',
          label: 'name'
        },
        defaultKey:[],
        chooseList:[], // 选择的数据
        menuRouteVisible:false,
        filterText: '',
				// 筛选时的执行方法
				filterNode(value, data) {
					if(!value) return true;
					return data.name.indexOf(value) !== -1;
				},
      };
    },
    /**
     *张俊娜：2019/8/7 17:47
     *描述：方法
     */
    methods:{
    	/**
     *张俊娜：2019/8/7 17:47
     * 第一个参数是否刷新数据，true刷新，false不刷新
     * 第二个参数是否有默认值，用于编辑的时候，传入路由地址如[{url:'/Test/DialogTest'}]
     */
    	open(isReload,value){
    		this.findTree();
    		if(isReload) {
					if(this.$refs.reftableitem) this.$refs.reftableitem.getList();
				}
    		if(value && value.length > 0){
					let query = {
						pageBean: {
							page: 1,
							pageSize: 100,
							showTotal: true,
							summary: []
						},
						params: {},
						querys: [],
						selfDate: {},
						sorter: [{
							direction: "ASC",
							property: "id"
						}]
					};
					axios(Object.assign({}, this.tableitem.bQuery, {
							data: query
						}))
						.then(res => {
							this.chooseList = res.data.rows.filter(x => {
								return x.menuRoute == value[0].url 
							})
						})
						.catch(err => {
							this.$message.error(err)
						})
    		}else {
					this.chooseList = []
				}
    		this.menuRouteVisible = true
    	},
    	//显示编码
    	showCode(){
    		this.$message.info('编码:MenuRoute')
    	},
    	codeVaule(data){
    		this.$nextTick(() => {
    			let code = this.$refs.tree.getCheckedNodes()[0].code
    			let obj = {hasInitValue: false, operation: "EQUAL", property: "treeCode", relation: "AND", value:code }
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
    		return {0: { field0: "treeCode", gx0: "EQUAL", value0:this.$refs.tree.getCheckedNodes()[0].code , andor0: "AND" }}
    	},
    	change(data,isChecked){
    		// 节点选中状态发生变化
      	this.$refs.tree.setCheckedNodes([data])
      	
    	},
    	changes(data){
    		this.$refs.tree.setCheckedNodes([data])
    		this.$refs.reftableitem.getList({0: { field0: "treeCode", gx0: "EQUAL", value0: data.code, andor0: "AND" }})
    	},
    	removePost(item){
    		this.chooseList = this.chooseList.filter(data => data != item)
    	},
    	// 双击表格选择数据
			dbclick(row, column, event) {
				this.chooseList.length = 0;
				this.chooseList.push(row);
			},
			// 取消
			dialogCancel() {
				if(this.cancel) {
					this.cancel(this.chooseList)
				}
				this.menuRouteVisible = false;
			},
			// 确定
			dialogConfirm() {
				if(this.confirm) {
					this.confirm(this.chooseList)
				}
				this.menuRouteVisible = false;
			},
			findTree(){
				axios({
	        // 用axios发送post请求
	        hostUrl: this.global.baseUrl,
	        method: "get",
	        url:"/sysTree/fiandByCode?code=MenuRoute",
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
   },
    mounted() {
			document.getElementsByTagName("body")[0].setAttribute('style', "text-align:center")
		},
		beforeDestroy() {
			document.body.removeAttribute('style', "text-align:center")
		}
  }
</script>

<style scoped>
  .m_all{
    height: 100%;
  }
  .m_left{
    height: 400px;
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
  .m_right:after{
    content: '';
    position: absolute;
    top: -10px;
    bottom: -10px;
    right: 0;
    width: 1px;
    background: #E4E7ED;
  }
  .grid-content,.m_right{
  	position: relative;
    height: 100%;
  }
</style>
