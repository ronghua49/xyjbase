<template>
	<div class="main">
		<xyj-table ref="reftablamain" id="2" v-bind="tablemain">
		</xyj-table>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: "EquipmentStatus",
		mounted() {
			//this.selectAll();
		},
		data() {
			return {
				tablemain: {
					hasDelete: false, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					hasPagination: true,
					hasPermission: true,
					hasOperation: false,
					hostUrl: this.global.hostUrl86,
					tableName: 'deviceOnlineStatus',
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "/deviceOnlineStatus/query",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
				},
				page: 1,
				size: 10,
				total: null,
				paginationSizes: [10, 20, 30],
				paginationLayout: 'total,sizes,prev,pager,next,jumper',
				tableData: [],
			}
		},
		methods: {
			
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