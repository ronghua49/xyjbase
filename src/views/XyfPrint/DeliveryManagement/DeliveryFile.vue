<template>
	<div class="main">
		<div v-show="!isShow" style="height: 100%">
			<xyj-table ref="reftablamain" id="2" v-bind="tablemain">
				<template slot="xyjcontainer">
					<!--<Xyj-button style="padding: 7px;" type="primary" :label="$t('xyftf.add')" text="新增" @click="add" />-->
					<Xyj-button style="padding: 7px;" type="primary" @click="add" text="新增" :label="$t('xyfbaseSet.add')"></Xyj-button>
					<Xyj-button style="padding: 7px;" type="primary" :disabled="EditBtnDisabled" @click="edit" text="编辑" :label="$t('xyfbaseSet.edit')"></Xyj-button>
				</template>
			</xyj-table>
		</div>
		<!--新增编辑页面-->
		<div v-show="isShow">
			<new-delivery ref="delivery" :onOk="onOk"></new-delivery>
		</div>
	</div>
</template>

<script>
	/*import NewDelivery from "NewDelivery";*/
	import NewDelivery from "../DeliveryManagement/NewDelivery";
	export default {
		name: "DeliveryFile",
		components: {
			NewDelivery
		},
		mounted() {},
		data() {
			return {
				EditBtnDisabled: true,
				selectRow: {},
				isShow: false,
				tablemain: {
					hasDelete: true, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					disabledNew: false, // 新增按钮是否禁用
					paginationSize: 20,
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					//el-table事件设置
					tableOns: {
						select: function(selection, row) {}
					},
					formatters: {
						onlinestatus: function(row, column, cellValue) {
							if( cellValue == 1 ) {
								return "已投放";
							} else {
								return "未投放";
							}
						},
					},
					//表格中的操作列设置
					operationAttrs: {
						width: "100px"
					},
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: "DeviceOnline",
					//查询时的排序字段
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					/*//查询时固定查询条件，对象下标必须从100开始向上加
					customQuery: {
					},*/
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "deviceOnline/query",
						method: "post",
						notCondition: true //没有查询条件则查询全部数据
					},
					//业务数据微服务,修改网址
					bEditUrl: {
						hostUrl: this.global.hostUrl86,
						url: "deviceOnline/save",
						method: "post",
						clear: true, //清除新增对话框输入的数据
						close: true //关闭修改对话框
					},
					//业务数据微服务,删除网址
					bDeleteUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/deviceOnline/delete",
						method: "post"
					},
					//当点击修改时，默认修改调用前处理上传数据时
					onManualEdit: this.reftableitemManualEdit,
					//当点击新增时，默认新增调用前处理上传数据时
					onManualNew: this.reftableitemManualNew,
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.EditBtnDisabled = false;
								this.selectRow = selection[0];
							} else {
								this.EditBtnDisabled = true;
								this.selectRow = {};
							}
						},
						'select-all': (selection) => {
							this.EditBtnDisabled = true;
						},
					}
				}
			}
		},
		methods: {
			onOk() {
				this.isShow = false;
				this.EditBtnDisabled = true;
				this.$refs.reftablamain.getList();
			},
			add() {
				this.isShow = true;
				this.$router.push("/XyfPrint/DeliveryManagement/NewDelivery")
				//this.$refs.delivery.open("add");
			},
			edit(){
				this.isShow = true;
				this.$refs.delivery.open("edit",this.selectRow);
			}
		}
	}
</script>

<style scoped lang="scss">
	.main {
		height: 100%;
	}
	
	.top-upper {
		height: 30px;
		line-height: 30px;
	}
</style>