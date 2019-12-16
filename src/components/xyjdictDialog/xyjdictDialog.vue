<template>
	<div>
		<el-dialog v-dialogDrag title="字典管理" :close-on-click-modal="false" :visible.sync="dialogVisible" center width="950px">
			<span style="color: red;margin-bottom: 10px;display: inline-block;">双击表格某一行选中该行数据</span>
			<div style="text-align: center;border: 1px solid #ccc;height: 398px;">
				<div class="table">
					<xyj-table ref="userTable" id="1" v-bind="tableConfig"></xyj-table>
				</div>
				<i></i>
				<div class="show">
					<el-button type="text" @click="chooseList = []">清空</el-button>
					<span style="display: block;">已选字典</span>
					<el-divider></el-divider>
					<el-tooltip effect="dark" content="双击移除当前已选字典" placement="bottom-start" v-for="(item,index) in chooseList" :key="index">
						<span style="display: block;cursor: pointer;" @dblclick="removePost(item)">{{ (index+1)+'、'+item.label }}</span>
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
		name: 'xyjdictDialog',
		props: {
			cancel: {
				type: Function,
			},
			confirm: {
				type: Function
			},
			/** 
			 * @date 2019-10-11 
			 * 查询的字段名，默认查询性别
			 * @author 张俊娜 
			 */
			type: {
				type: String,
				default: 'sex'
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
		data() {
			return {
				dialogVisible: false,
				chooseList: [],
				tableConfig: {
					hasDelete: false, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					hasPagination: true,
					hasPermission: true,
					hasOperation: false,
					appendToBody: true,
					//el-table设置
					tableAttrs: {
						height: "300px",
						width: "700px"
					},
					hostUrl: this.global.baseUrl,
					tableName: 'SysDict',
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					formatters: {
						status: function(row, column, cellValue) {
							switch(cellValue) {
								case -1:
									return '已删除'
								case 0:
									return '正常'
								default:
									return cellValue
							}
						}
					},
					tableOns: {
						"row-dblclick": this.dbclick
					},
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.baseUrl,
						url: "/dict/query",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
					InitQuery: {
						0: {
							field0: "type",
							gx0: "EQUAL",
							value0: this.type,
							andor0: "AND"
						}
					}, //表格初始化时查询条件
					codeVaule: this.codeVaule,
				}
			}
		},
		methods: {
			open(isReload, res) {
				/** 
				 * @date 2019-10-12
				 * @return {Boolean} 返回值 
				 * isReload 是否刷新，res 外部传入默认数据
				 * @author 张俊娜 
				 */
				this.$nextTick(() => {
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
			/** 
			 * @date 2019-10-12
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 单击删除已选机构
			 * @author 张俊娜 
			 */
			removePost(item) {
				this.chooseList = this.chooseList.filter(data => data != item)
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
			codeVaule(data) {
				this.$nextTick(() => {
					let obj = {
						hasInitValue: false,
						operation: "EQUAL",
						property: "type",
						relation: "AND",
						value: this.type
					}
					data.querys && data.querys.length > 0 ?
						data.querys.forEach(res => {
							if(res.value != obj.value) {
								data.querys.push(obj)
							}
						}) :
						data.querys.push(obj)
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
		padding-bottom: 3px!important;
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