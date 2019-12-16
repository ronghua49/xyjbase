<template>
	<div @click.ctrl="showCode">
		<el-dialog v-dialogDrag title="流程表" :close-on-click-modal="false" :visible.sync="dialogVisible" center width="1150px">
			<span style="color: red;margin-bottom: 10px;display: inline-block;">双击表格某一行选中该行数据</span>
			<div style="text-align: center;border: 1px solid #ccc;height: 398px;">
				<div class="tree">
					<el-input size="mini" placeholder="输入关键字进行过滤" v-model="filterText">
					</el-input>
					<el-tree :data="data1" show-checkbox node-key="id" ref="tree" highlight-current :check-strictly="true" :check-on-click-node="true" @node-click="change" @check="changes" :default-checked-keys="defaultKey" :props="defaultProps">
					</el-tree>
				</div>
				<i></i>
				<div class="table">
					<xyj-table-work-flow style="width: 700px;" ref="reftableworkflow" id="1" v-bind="tableworkflow"></xyj-table-work-flow>
				</div>
				<i></i>
				<div class="show">
					<el-button type="text" @click="chooseList = []">清空</el-button>
					<span style="display: block;">已选流程定义</span>
					<el-divider></el-divider>
					<el-tooltip effect="dark" content="双击移除当前已选择的流程定义" placement="bottom-start" v-for="(item,index) in chooseList" :key="index">
						<span style="display: block;cursor: pointer;" @dblclick="removePost(item)">{{ (index+1)+'、'+item.name }}</span>
					</el-tooltip>
				</div>
			</div>
			<span slot="footer">
		    <el-button size="mini" @click="dialogCancel">{{ $t('action.cancel') }}</el-button>
		    <el-button size="mini" type="primary" @click="dialogConfirm">{{ $t('action.comfirm') }}</el-button>
		  </span>
		</el-dialog>
	</div>
</template>

<script>
	import axios from '@/http/axios'
	export default {
		name: 'xyjworkflowDialog',
		props: {
			/** 
			 * @date 2019-10-11
			 * 取消事件
			 * @author 张俊娜 
			 */
			cancel: {
				type: Function,
			},
			/** 
			 * @date 2019-10-11
			 * 确认事件
			 * @author 张俊娜 
			 */
			confirm: {
				type: Function
			},
			/** 
			 * @date 2019-10-11
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 是否是单选 true为单选，false为多选
			 * @author 张俊娜 
			 */
			isRadio: {
				type: Boolean,
				default: false
			}
		},
		watch: {
			filterText(val) {
				this.$refs.tree.filter(val);
			}
		},
		data() {
			return {
				dialogVisible: false,
				filterText: '',
				/** 
				 * @date 2019-10-11
				 * 选中的数据
				 * @author 张俊娜 
				 */
				chooseList: [],
				tableworkflow: {
					hasDelete: false, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					appendToBody: true,
					//不显示表格操作列
					hasOperation: false,
					//el-table设置
					tableAttrs: {
						height: "300px",
						width: "700px"
					},
					//el-table事件设置
					tableOns: {
						"row-dblclick": this.dbclick,
					},
					//格式化表格字段显示，按照类属性定义格式化函数
					formatters: {
						suspensionState: function(row, column, cellValue) {
							switch(cellValue) {
								case 1:
									return "正常";
									break;
								case 2:
									return "挂起";
									break;
							}
						}
					},
					//single: false //单选
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: "XyjProcessDefinition",
					//查询时的排序字段
					//sorter: [{ direction: "ASC", property: "versionId" }],
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.workflow,
						url: "/process/getAllProcessDefinitionList",
						method: "post",
						notCondition: true //没有查询条件则查询全部数据
					},
					InitQuery: {
						0: {
							field0: "category",
							gx0: "EQUAL",
							value0: "BpmTree",
							andor0: "AND"
						}
					}, //表格初始化时查询条件
					codeVaule: this.codeVaule, // 查询时增加的其它查询条件处理
					sortVaule: this.sortVaule, // 排序查询时需要增加的其它查询条件处理
					// 字段查询需要显示的字段值
					searchList: ['key', 'name', 'id', 'version', 'deploymentId', 'resourceName', 'tenantId', 'description']
				},
				data1: [],
				defaultProps: {
					children: 'childList',
					label: 'name'
				},
				defaultKey: [],
			}
		},
		methods: {
			change(data, isChecked) {
				// 节点选中状态发生变化
				this.$refs.tree.setCheckedNodes([data])
			},
			changes(data) {
				this.$refs.tree.setCheckedNodes([data])
				this.$refs.reftableworkflow.getList({
					0: {
						field0: "category",
						gx0: "EQUAL",
						value0: data.code,
						andor0: "AND"
					}
				})
			},
			codeVaule(data) {
				this.$nextTick(() => {
					let code = this.$refs.tree.getCheckedNodes()[0].code
					let obj = {
						hasInitValue: false,
						operation: "EQUAL",
						property: "category",
						relation: "AND",
						value: code
					}
					data.querys && data.querys.length > 0 ?
						data.querys.forEach(res => {
							if(res.value != obj.value) {
								data.querys.push(obj)
							}
						}) :
						data.querys.push(obj)
				})
			},
			sortVaule() {
				return {
					0: {
						field0: "category",
						gx0: "EQUAL",
						value0: this.$refs.tree.getCheckedNodes()[0].code,
						andor0: "AND"
					}
				}
			},
			/** 
			 * @date 2019-10-11
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 单击删除已选岗位
			 * @author 张俊娜 
			 */
			removePost(item) {
				this.chooseList = this.chooseList.filter(data => data != item)
			},
			// 确定
			dialogConfirm() {
				if(this.confirm) {
					this.confirm(this.chooseList)
				}
				this.dialogVisible = false;
			},
			// 取消
			dialogCancel() {
				if(this.cancel) {
					this.cancel(this.chooseList)
				}
				this.dialogVisible = false;
			},
			open(isReload, res) {
				/** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * isReload 是否刷新，res 外部传入默认数据
				 * @author 张俊娜 
				 */
				this.$nextTick(() => {
					this.findTree();
					if(isReload) {
						if(this.$refs.reftableworkflow) this.$refs.reftableworkflow.getList();
					}
					if(res && res.length > 0) {
						let queryValue = '';
						res.forEach(data => {
							queryValue += data.id + ',';
						})
						queryValue = queryValue.substr(0, queryValue.length - 1)
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
								property: "id",
								relation: "AND",
								value: queryValue
							}],
							selfDate: {},
							sorter: [{
								direction: "ASC",
								property: "id"
							}]
						};
						axios(Object.assign({}, this.tableworkflow.bQuery, {
								data: query
							}))
							.then(res => {
								if(res.code == 200) {
									this.chooseList = res.data.rows
								};
							})
							.catch(err => {
								console.log(err)
							})
					} else {
						this.chooseList = []
					}

				})
				this.dialogVisible = true;
			},
			// 双击表格选择数据
			dbclick(row, column, event) {
				if(this.isRadio) {
					this.chooseList.length = 0;
					this.chooseList.push(row);
				} else {
					this.chooseList.push(row);
					let hash = {};
					this.chooseList = this.chooseList.reduce((preval, curval) => {
						hash[curval.id] ? '' : hash[curval.id] = true && preval.push(curval);
						return preval
					}, []);
				}
			},
			findTree() {
				axios({
						// 用axios发送post请求
						hostUrl: this.global.baseUrl,
						method: "get",
						url: "/sysTree/fiandByCode?code=BpmTree",
					})
					.then(res => {
						if(res.code == 200) {
							this.data1 = JSON.parse(JSON.stringify([res.data]));

							function formatter(data) {
								data.forEach(element => {
									if(element.childList && element.childList.length > 0) {
										let flag = formatter(element.childList);
										if(flag) return false;
									} else {
										element.name = element.name + ' | ' + element.code
										return true;
									}
									element.name = element.name + ' | ' + element.code
								})
							}
							formatter(this.data1)
							this.defaultKey = [this.data1[0].id]
						}
					})
					.catch(err => {
						this.$message.error(err.message);
					})
			},
			//显示编码
    	showCode(){
    		this.$message.info('编码:BpmTree')
    	},
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
	/deep/ .el-dialog__footer {
		text-align: right!important;
	}
	
	/deep/ .el-dialog__body {
		padding-bottom: 0!important;
	}
	
	/deep/ .el-tree {
		width: 380px;
		overflow: auto;
	}
	
	.tree {
		display: inline-block;
		width: 180px;
		vertical-align: top;
		margin: 10px 20px 10px 0;
		height: 384px;
		overflow: auto;
	}
	
	.table {
		vertical-align: top;
		display: inline-block;
		box-sizing: border-box;
		padding: 0 20px;
		/*border-left: 1px solid #ccc;*/
	}
	
	.show {
		display: inline-block;
		vertical-align: top;
		width: 100px;
		margin: 10px 0 10px 20px;
		height: 384px;
		overflow-y: auto;
		text-align: left;
	}
	
	i {
		display: inline-block;
		width: 1px;
		height: 398px;
		vertical-align: top;
		background-color: #ccc;
	}
</style>