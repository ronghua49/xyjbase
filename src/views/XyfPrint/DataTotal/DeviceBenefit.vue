<template>
	<div>
		<div id="currentBox"></div>
		<div class="searchBox">
			<el-input v-model="keys" size="small" clearable placeholder="请输入查询内容" prefix-icon="el-icon-search"></el-input>
			<el-button class="searach" type="primary" size="mini" @click="search">查询</el-button>
		</div>
		<div>
			<el-table height="400" v-loading="loading" element-loading-text="加载中" :data="tableData.rows" size="mini" border style="width: 100%;">
				<el-table-column type="index" label="序号" width="100" align="center">
				</el-table-column>
				<el-table-column v-for="item in columns" sortable :key="item.prop" :prop="item.prop" :label="item.label" :formatter="item.formatter" :min-width="item.width" :align="item.align">
				</el-table-column>
			</el-table>
			<div class="pagination" style="margin-bottom: 20px;">
				<el-pagination @current-change="refreshPageRequest" @size-change="refreshPageSizeRequest" background :current-page="pageRequest.page" :page-sizes="[10, 20, 30, 40, 50]" :page-size="pageRequest.pageSize" layout="sizes, total, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	let echarts = require('echarts');
	require('echarts/map/js/china');
	require('echarts/lib/chart/pie');
	require('echarts/lib/chart/line');
	require('echarts/lib/chart/bar');
	require('echarts/lib/component/tooltip');
	require('echarts/lib/component/legend');
	export default {
		name: 'DeviceBenefit',
		data() {
			return {
				myChart: {},
				allDevices: [],
				allDevicesData: [],
				tableData: {
					rows: [],
					total: 0,
				},
				loading: false,
				keys: "",
				pageRequest: {
					page:1,
					pageSize: 10
				},
				columns: [{
						prop: 'deviceno',
						label: '设备编码',
						width: "140",
						align: "center",
					}, {
						prop: 'model',
						label: '设备型号',
						width: "140",
						align: "center"
					}, {
						prop: 'NAME',
						label: '设备名称',
						width: "140",
						align: "center"
					}, {
						prop: 'paymoney',
						label: '已支付金额',
						width: "140",
						align: "center",
						formatter: function(a,b,c){
							if ( c ) {
								return c;
							}
							return "0";
						}
					}, {
						prop: 'nopaymoney',
						label: '待支付金额',
						width: "140",
						align: "center",
						formatter: function(a,b,c){
							if ( c ) {
								return c;
							}
							return "0";
						}
					}, {
						prop: 'ordernum',
						label: '使用次数',
						width: "140",
						align: "center",
						formatter: function(a,b,c){
							if ( c ) {
								return c;
							}
							return "0";
						}
					}]
			}
		},
		methods: {
			refreshPageRequest: function(val) {
				this.pageRequest.page = val;
				this.getDataQuery();
			},
			refreshPageSizeRequest: function(val) {
				this.pageRequest.pageSize = val;
				this.getDataQuery();
			},
			getData() { // 获取统计图数据
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'POST',
					data: {},
					url: '/reportController/sbxyPhbquery'
				}).then(res => {
					if(res.hasOwnProperty('error')) {
						this.$message({
							type: "danner",
							message: res.message ? res.message : res.msg
						})
						return;
					}
					this.allDevices = res.data.map((item) => {
						return item.NAME;
					});
					this.allDevicesData = res.data.map((item) => {
						return item.paymoney;
					});
					this.initCharts();
				})
			},
			initCharts() {
				let self = this;
				this.myChart.setOption({
					title: {
						text: '设备累计收益 TOP 10',
						subtext: ''
					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: {
						name: "单位/元",
						type: 'value',
						boundaryGap: [0, 0.01]
					},
					yAxis: {
						name: "设备名称",
						type: 'category',
						data: self.allDevices
					},
					series: [{
						type: 'bar',
						data: self.allDevicesData
					}]
				})
			},
			getDataQuery() { // 获取表格数据
				let data = {};
				this.loading = true;
				if(this.keys) {
					data = {
						"param": this.keys
					}
				} else {
					data = {
						"param": ""
					}
				}
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'POST',
					data: {
						"pageBean": {
							"page": this.pageRequest.page,
							"pageSize": this.pageRequest.pageSize,
							"showTotal": true,
						},
						"params": {},
						"querys": [],
						"selfDate": data,
					},
					url: '/reportController/sbxyquery'
				}).then(res => {
					this.loading = false;
					if(res.hasOwnProperty('error')) {
						this.$message({
							type: "danner",
							message: res.message ? res.message : res.msg
						})
						return;
					}
					res.data.total = Number(res.data.total);
					this.tableData = res.data;
				})
			},
			search() {
				this.getDataQuery();
			}
		},
		mounted() {
			this.myChart = echarts.init(document.getElementById('currentBox'));
			this.getData();
			this.getDataQuery();
		}
	}
</script>

<style scoped="scoped">
	#currentBox {
		width: 100%;
		height: 400px;
	}
	.searchBox{
		display: flex;
		align-items: center;
		justify-content: space-between;
		width: 250px;
		margin: 30px 0 30px 30px;
	}
	.searach{
		margin-left: 20px;
	}
	.pagination{
		margin-top: 20px;
		text-align: right;
	}
</style>