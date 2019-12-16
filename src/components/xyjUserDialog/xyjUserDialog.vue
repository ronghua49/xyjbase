<template>
	<div>
		<el-dialog v-dialogDrag title="员工信息" :close-on-click-modal="false" :visible.sync="dialogVisible" center width="1200px">
			<span style="color: red;margin-bottom: 10px;display: inline-block;">双击表格某一行选中该行数据</span>
			<div style="text-align: center;border: 1px solid #ccc;height: 398px;">
				<div class="tree">
					<el-input size="mini" placeholder="输入关键字进行过滤" v-model="filterText">
					</el-input>
					<el-tree :data="data" node-key="id" :props="props" ref="tree" :filter-node-method="filterNode" :check-on-click-node="true" :check-strictly="true" @check="search" @node-click="change" show-checkbox>
					</el-tree>
				</div>
				<i></i>
				<div class="table">
					<xyj-table style="width: 700px;" ref="userTable" id="1" v-bind="tableConfig"></xyj-table>
				</div>
				<i></i>
				<div class="show">
					<el-button type="text" @click="chooseList = []">清空</el-button>
					<span style="display: block;">已选员工</span>
					<el-divider></el-divider>
					<el-tooltip effect="dark" content="双击移除当前已选择的员工" placement="bottom-start" v-for="(item,index) in chooseList" :key="index">
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
	import axios from "@/http/axios";
	export default {
		name: 'xyjUserDialog',
		props: {
			cancel: {
				type: Function,
			},
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
				chooseList: [],
				tableConfig: {
					hasDelete: false, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					hasPagination: true, // 分页是否显示
					hasOperation: false, // 操作列是否显示
					appendToBody: true, 
					//el-table设置
					tableAttrs: {
						height: "300px",
						width: "700px"
					},
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: 'SysUser',
					//查询时的排序字段
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					// 初始化字段
					formatters: {
						status: function(row, column, cellValue) {
							switch(cellValue) {
								case 0:
									return '禁用'
								case 1:
									return '正常'
								default:
									return cellValue
							}
						}
					},
					//el-table事件设置
					tableOns: {
						"row-dblclick": this.dbclick,
					},
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.baseUrl,
						url: "/user/listUserByDeptIds",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
					codeVaule: this.codeVaule, // 查询时添加其它查询条件的处理函数
					sortVaule: this.sortVaule // 排序查询时添加其它查询条件的处理函数
				},
				props: {
					label: 'name',
					children: 'children'
				},
				data: [],
				filterText: '',
				// 筛选时的执行方法
				filterNode(value, data) {
					if(!value) return true;
					return data.name.indexOf(value) !== -1;
				},
			}
		},
		methods: {
			codeVaule(data) {
				this.$nextTick(() => {
					if(this.$refs.tree.getCheckedNodes().length > 0) {
						let id = this.$refs.tree.getCheckedNodes()[0].id
						let obj = {
							hasInitValue: false,
							operation: "EQUAL",
							property: "deptId",
							relation: "AND",
							value: id
						}
						data.querys && data.querys.length > 0 ?
							data.querys.forEach(res => {
								if(res.value != obj.value) {
									data.querys.push(obj)
								}
							}) :
							data.querys.push(obj)
					}
				})
			},
			sortVaule() {
				return {
					0: {
						field0: "deptId",
						gx0: "EQUAL",
						value0: this.$refs.tree.getCheckedNodes()[0].id,
						andor0: "AND"
					}
				}
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
						if(this.$refs.userTable) this.$refs.userTable.getList();
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
								operation: "IN",
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
						axios(Object.assign({}, this.tableConfig.bQuery, {
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
			// 取消
			dialogCancel() {
				if(this.cancel) {
					this.cancel(this.chooseList)
				}
				this.dialogVisible = false;
			},
			// 确定
			dialogConfirm() {
				if(this.confirm) {
					this.confirm(this.chooseList)
				}
				this.dialogVisible = false;
			},
			// 机构树选择时查询相应内容
			search(data) {
				this.$refs.tree.setCheckedNodes([data])
				this.$refs.userTable.getList({
					0: {
						field0: "deptId",
						gx0: "EQUAL",
						value0: data.id,
						andor0: "AND"
					}
				})
			},
			change(data) {
				// 节点选中状态发生变化
				this.$refs.tree.setCheckedNodes([data])
			},
			findTree() {
				// 获取数据
				this.$api.dept.findDeptTree().then((res) => {
					this.data = res.data
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
		padding-left: 20px;
		/*border-left: 1px solid #ccc;*/
	}
	
	.show {
		display: inline-block;
		vertical-align: top;
		width: 150px;
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
