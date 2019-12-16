<template>
	<div style="height: 100%;">
		<el-row class="m_all" :gutter="15">
			<el-col :span="5" class="m_left">
				<div @click.ctrl="showCode" style="width: 278px;">
					<el-input size="mini" placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
					<el-tree :data="data1" show-checkbox default-expand-all node-key="id" ref="tree" highlight-current :check-strictly="true" :check-on-click-node="true" @node-click="change" @check="changes" :default-checked-keys="defaultKey" :props="defaultProps" :filter-node-method="filterNode">
					</el-tree>
				</div>
			</el-col>
			<el-col :span="19" class="m_right">
				<div class="grid-content">
					<!--模型列表-->
					<xyj-table ref="reftableitem" id="1" v-bind="tableitem">
						<template slot="form">
							<el-form-item label="菜单路由" prop="menuRoute">
								<el-row>
									<el-col :span="22">
										<el-input v-model="Route" auto-complete="off" size="small"></el-input>
									</el-col>
									<el-col :span="2" class="icon-list__tips">
										<el-tooltip placement="top" effect="light" style="padding: 10px;">
											<div slot="content">
												<p>URL格式：</p>
												<p>1.常规业务开发的功能URL，如用户管理，Views目录下页面路径为 /Sys/User, 此处填写 /sys/user。</p>
												<p>2.嵌套外部网页，如通过菜单打开百度网页，此处填写 http://www.baidu.com，http:// 不可省略。</p>
												<p>示例：用户管理：/sys/user 嵌套百度：http://www.baidu.com 嵌套网页：http://127.0.0.1:8000</p>
											</div>
											<i class="el-icon-warning"></i>
										</el-tooltip>
									</el-col>
								</el-row>
							</el-form-item>
						</template>
					</xyj-table>
				</div>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: "MenuRoute",
		watch: {
			filterText(val) {
				this.$refs.tree.filter(val);
			}
		},
		data() {
			return {
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
						0: {
							field0: "treeCode",
							gx0: "EQUAL",
							value0: "MenuRoute",
							andor0: "AND"
						}
					}, //表格初始化时查询条件
					onManualNew: this.onManualNew,
					onManualEdit: this.manualEdit,
					codeVaule: this.codeVaule,
					sortVaule: this.sortVaule,
					onBeforeNew: this.dialogMessage,
					onDialogDefault: this.dialogDefault,
				},
				data1: [],
				defaultProps: {
					children: 'childList',
					label: 'name'
				},
				defaultKey: [],
				filterText: '',
				// 筛选时的执行方法
				filterNode(value, data) {
					if(!value) return true;
					return data.name.indexOf(value) !== -1;
				},
				Route: ''
			};
		},
		/**
		 *张俊娜：2019/8/7 17:47
		 *描述：方法
		 */
		methods: {
			onManualNew(row) {
				row.treeCode = row.treeCode.split('|')[1].trim();
				row.menuRoute = this.Route
			},
			manualEdit(row, data) {
				row.id = data.id;
				row.treeCode = row.treeCode.split('|')[1].trim();
				row.menuRoute = this.Route
			},
			dialogDefault(row) {
				console.log(row, this.$refs.tree.getCheckedNodes()[0])
				this.Route = row.menuRoute

				function fun(data) {
					for(let res of data) {
						if(res.childList && res.childList.length > 0) {
							if(res.code == row.treeCode) {
								row.treeCode = res.name
							}
							fun(res.childList)
						}
						if(res.code == row.treeCode) {
							row.treeCode = res.name
						}
					}
				}

				this.$nextTick(() => {
					!row.treeCode ? row.treeCode = this.$refs.tree.getCheckedNodes()[0].name : fun(this.data1)
				})
			},
			dialogMessage(row) {
				if(this.data1[0].code == this.$refs.tree.getCheckedNodes()[0].code) {
					this.$message.error('根节点不能新增');
					this.$refs.reftableitem.dialogVisible = false
				}
			},
			//显示编码
			showCode() {
				this.$message.info('编码:MenuRoute')
			},
			codeVaule(data) {
				this.$nextTick(() => {
					let code = this.$refs.tree.getCheckedNodes()[0].code
					let obj = {
						hasInitValue: false,
						operation: "EQUAL",
						property: "treeCode",
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
						field0: "treeCode",
						gx0: "EQUAL",
						value0: this.$refs.tree.getCheckedNodes()[0].code,
						andor0: "AND"
					}
				}
			},
			change(data, isChecked) {
				// 节点选中状态发生变化
				this.$refs.tree.setCheckedNodes([data])

			},
			changes(data) {
				this.$refs.tree.setCheckedNodes([data])
				this.$refs.reftableitem.getList({
					0: {
						field0: "treeCode",
						gx0: "EQUAL",
						value0: data.code,
						andor0: "AND"
					}
				})
			},
		},
		created() {
			axios({
					// 用axios发送post请求
					hostUrl: this.global.baseUrl,
					method: "get",
					url: "/sysTree/fiandByCode?code=MenuRoute",
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
	.m_all {
		height: 100%;
	}
	
	.m_left {
		height: 100%;
		overflow: auto;
	}
	
	.m_right:before {
		content: '';
		position: absolute;
		top: -10px;
		bottom: -10px;
		left: 0;
		width: 1px;
		background: #E4E7ED;
	}
	
	.m_left>div {
		height: 100%;
		overflow: auto;
	}
	
	.grid-content,
	.m_right {
		position: relative;
		height: 100%;
	}
</style>