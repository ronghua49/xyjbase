<template>
	<div style="height: 100%;">
		<div v-if="!isShow" style="height: 100%;">
			<xyj-table ref="reftablamain" id="1" v-bind="tablemain">
				<template slot="xyjcontainer">
					<Xyj-button type="primary" :disabled="isDisabledCheck" :label="$t('xyftf.view')" text="查看" @click="infoShow" />
				</template>
			</xyj-table>
		</div>
		<div v-if="isShow" style="height: 100%">
			<div class="top">
				<el-button @click="cancel" style="padding: 7px;float: left; margin-top: 10px; margin-left: 10px" size="mini" type="primary">
					{{$t('xyftf.return')}}</el-button>
				用户基本信息
			</div>
			<div class="info">
				<el-form ref="form" v-model="infoForm" label-width="100px">
					<el-form-item label="注册日期:"><div class="text">{{infoForm.createTime}}</div></el-form-item>
					<el-form-item label="用户状态:"><div class="text">{{infoForm.status==1?"正常":"欠费"}}</div></el-form-item>
					<el-form-item label="总消费金额:"><div class="text">{{infoForm.paidmoney+"元"}}</div></el-form-item>
					<el-form-item label="用户昵称:"><div class="text">{{infoForm.nickname}}</div></el-form-item>
					<el-form-item label="手机号码:"><div class="text">{{infoForm.mobile}}</div></el-form-item>
					<el-form-item label="性别:"><div class="text">{{infoForm.sex}}</div></el-form-item>
				</el-form>
			</div>
			<div style="margin-top: 50px; height: 49%">
				<xyj-table ref="reftablebill" id="2" v-bind="tablebill">
					<template slot="xyjcontainer">
						<Xyj-button type="primary" :disabled="isDisabledCheck1" :label="$t('xyftf.view')" text="查看" @click="billInfoShow" style="margin-left: 10px" />
					</template>
				</xyj-table>
			</div>
		</div>
		<div>
			<el-dialog v-dialogDrag title="账单详情" :visible.sync="dialogVisible" width="40%" :close-on-click-modal="false">
				<div class="bill">
					<el-form ref="bill" v-model="billForm" label-width="90px">
						<el-form-item label="账单状态"><div class="text">{{billForm.status==1?"已支付":"待支付"}}</div></el-form-item>
						<el-form-item label="账单日期"><div class="text">{{billForm.ordertime}}</div></el-form-item>
						<el-form-item label="账单金额"><div class="text">{{billForm.paymoney}}</div></el-form-item>
						<el-form-item label="订单编号"><div class="text">{{billForm.ordercode}}</div></el-form-item>
						<el-form-item label="用户昵称"><div class="text">{{billForm.nickname}}</div></el-form-item>
						<el-form-item label="设备编码"><div class="text">{{billForm.deviceno}}</div></el-form-item>
						<el-form-item label="手机号码"><div class="text">{{billForm.mobile}}</div></el-form-item>
						<el-form-item label="设备位置"><div class="text">{{billForm.address}}</div></el-form-item>
					</el-form>
				</div>
				<div>
					<el-table :data="tableData" border stripe size="mini" style="width: 100%">
					<el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
					<el-table-column prop="papername" label="纸张规格" min-width="120" align="center"></el-table-column>
					<el-table-column prop="color2side" label="类型" min-width="120" align="center"></el-table-column>
					<el-table-column prop="price" label="单价(张)" min-width="120" align="center"></el-table-column>
					<el-table-column prop="papernum" label="数量" min-width="120" align="center"></el-table-column>
				</el-table>
				</div>
				<span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
			</el-dialog>
		</div>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: 'UserList',
		data() {
			return {
				isShow: false,
				isDisabledCheck: true,
				isDisabledCheck1: true,
				selectRow: {
					id: 0
				},
				selectRow1:"",
				infoForm: {},
				dialogVisible: false,
				billForm: {},
				tableData: [],
				tablemain: {
					hasOperation: false,
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					//el-table事件设置
					tableOns: {
						select: function(selection, row) {}
					},
					//表格中的操作列设置
					operationAttrs: {
						width: "100px"
					},
					paginationSize: 20,
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: "AppUser",
					//查询时的排序字段
					sorter: [{
						direction: "DESC",
						property: "lastprinttime"
					}],
					/*//查询时固定查询条件，对象下标必须从100开始向上加
					customQuery: {
					},*/
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl85,
						url: "/appUser/query",
						method: "post",
						notCondition: true //没有查询条件则查询全部数据
					},
					//格式化表格字段显示，按照类属性定义格式化函数
					formatters: {
						status: function(row, column, cellValue) {
							if(cellValue == 1) {
								return "正常";
							} else {
								return "欠费"
							}
						}
					},
					//el-table事件设置
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.isDisabledCheck = false;
								this.selectRow = selection[0];
							} else {
								this.isDisabledCheck = true;
							}
						},
						'select-all': (selection) => {
							this.isDisabledCheck = true;
						},
					},
				},
				tablebill: {
					hasOperation: false,
					//el-table设置
					tableAttrs: {
						height: "100%",
						width: "800px"
					},
					//el-table事件设置
					//表格中的操作列设置
					operationAttrs: {
						width: "100px"
					},
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: "AppOrderMain",
					//查询时的排序字段
					sorter: [{
						direction: "DESC",
						property: "ordertime"
					}],
					//查询时固定查询条件，对象下标必须从100开始向上加
					customQuery: {
						
					},
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl85,
						url: "appOrderMain/query",
						method: "post",
						notCondition: true, //没有查询条件则查询全部数据
					},
					//el-table事件设置
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.isDisabledCheck1 = false;
								this.selectRow1 = selection[0];
							} else {
								this.isDisabledCheck1 = true;
							}
						},
						'select-all': (selection) => {
							this.isDisabledCheck11 = true;
						},
					},
					formatters: {
						status: function(row, column, cellValue) {
							if(cellValue == 1) {
								return "已支付";
							} else {
								return "待支付"
							}
						}
					},
				},
				tableSub: {
					hasOperation: false,
					hasPagination: false, //不显示分页
					//el-table设置
					tableAttrs: {
						// height: "400px",
						width: "800px"
					},
					//el-table事件设置
					tableOns: {
						select: function(selection, row) {}
					},
					//表格中的操作列设置
					operationAttrs: {
						width: "100px"
					},
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: "appUser",
					//查询时的排序字段
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					//查询时固定查询条件，对象下标必须从100开始向上加
//					customQuery: {
//						100:{value100: 10}
//					},
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl85,
						url: "/appUser/query",
						method: "post",
						notCondition: true //没有查询条件则查询全部数据
					},

				},
			}
		},
		mounted(){
			this.selectRow.id = 0;
		},
		watch:{
			selectRow:function(val){
				this.tablebill.customQuery = {
						100:{value100: val.id,gx100:"EQUAL",field100:"appuserid"}
				}
			}
		},
		methods: {
			infoShow() {
				this.isShow = true;
				this.userQuery();
			},
			cancel() {
				this.isShow = false;
			},
			//调用appUser/query接口查询数据，账单查询会用到
			userQuery() {
				axios({
					hostUrl: this.global.hostUrl85,
					url: "/appUser/query",
					method: "post",
					data: {
						"pageBean": {
							"page": 0,
							"pageSize": 0,
							"showTotal": true
						},
						"querys": [{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "id",
							"relation": "AND",
							"value": this.selectRow.id
						}]
					}
				}).then(res => {
					this.infoForm = res.data.rows[0];
				}).catch(err => {
					this.$message.error(err.message)
				})
			},
			billInfoShow() {
				this.dialogVisible = true;
				axios({
					hostUrl: this.global.hostUrl86,
					url: "/appOrderMain/query",
					method: "post",
					data: {
						"pageBean": {
							"page": 0,
							"pageSize": 0,
							"showTotal": true
						},
						"querys": [{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "id",
							"relation": "AND",
							"value": this.selectRow1.id
						}]
					}
				}).then(res => {
					this.billForm = res.data.rows[0];
				}).catch(err => {
					this.$message.error(err.message)
				})
				axios({
					hostUrl: this.global.hostUrl85,
					method: "POST",
					url: "/appOrderSub/query", // 请求地址
					data: {
						"pageBean": {
							"page": 0,
							"pageSize": 0,
							"showTotal": true
						},
						"querys": [{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "ordermainid",
							"relation": "AND",
							"value": this.selectRow1.id
						}]
					}
				}).then(res => {
					this.tableData = res.data.rows;
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.top {
		width: 100%;
		height: 50px;
		line-height: 50px;
		background-color: #f3f3f3;
	}
	
	.info {
		width: 100%;
		height: 150px;
		border: 1px solid #f3f3f3;
	}
	
	.info .el-form {
		margin-top: 20px;
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		width: 60%;
		.el-form-item {
			width: 33.333333333333%;
		}
	}
	
	.bill .el-form {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		.el-form-item {
			width: 45%;
			margin-top: -20px;
		}
	}
	.text{
		width: 240px;
		text-align: left;
	}
</style>