<template>
	<div class="home">
		<div>
			<div class="headtitle">平台汇总</div>
			<el-row :gutter="10" type="flex" justify="start">
				<el-col class="l">
					<div class="grid-content bg-purple l l-1">
						<div class="title">
							<span>今日收益(元)</span>
						</div>
						<div class="money">{{Number(totalData.todaymoney).toFixed(2)}} <span style="font-size: 14px"></span></div>
						<div class="bottom">
							<div class="left">累计收益</div>
							<div class="right">{{Number(totalData.allmoney).toFixed(2)}}</div>
						</div>
					</div>
				</el-col>
				<el-col class="l">
					<div class="grid-content bg-purple l l-1">
						<div class="title">
							<span>今日新增用户(人)</span>
						</div>
						<div class="money">{{totalData.tRegCount}} <span style="font-size: 14px"></span></div>
						<div class="bottom">
							<div class="left">累计用户</div>
							<div class="right">{{totalData.allRegCount}}</div>
						</div>
					</div>
				</el-col>
				<el-col class="l">
					<div class="grid-content bg-purple l l-1">
						<div class="title">
							<span>运行设备(台)</span>
						</div>
						<div class="money">{{totalData.runDevice}} <span style="font-size: 14px"></span></div>
						<div class="bottom">
							<div class="left">设备数量</div>
							<div class="right">{{totalData.allDevice}}</div>
						</div>
					</div>
				</el-col>
			</el-row>
		</div>
		<div class="head">
			<div class="headleft">
				<span>平台总体收入趋势</span>
			</div>
			<div class="headright">
				<div class="navdiv" v-for="(item,index) in usermoney" :key="item.type" :class="{Color:timeColor==index}" @click="timeMes(index,item)">
					<span v-if='item.type!=3'>{{item.name}}</span>
					<el-date-picker v-if='item.type==3' v-model="value2" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change='changeTime' style='height: 30px;padding: 0;padding-left: 10px;font-size: 14px;border:none;box-sizing: border-box;color: #14889A !important;'>
					</el-date-picker>
				</div>
			</div>
		</div>
		<div style="display: flex;min-width: 1300px;">
			<div id="chart" class="chart"></div>
			<div id="piePay"></div>
			<div class="pieTradeBox">
				<div class="pieTradeBoxInfo">
					<div>用户总数</div>
					<div>{{allCOUNT}}</div>
				</div>
				<div id="pieTrade"></div>
			</div>
		</div>
		<div id="userPayChart"></div>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	import { formatWithSeperator } from "@/utils/datetime.js"
	let echarts = require('echarts/lib/echarts');
	require('echarts/map/js/china');
	require('echarts/lib/chart/pie');
	require('echarts/lib/chart/line');
	require('echarts/lib/chart/bar');
	require('echarts/lib/component/tooltip');
	require('echarts/lib/component/legend');
	export default {
		name: 'Home',
		data() {
			return {
				usermoney: [{
						name: '近7天',
						type: 1
					},
					{
						name: '近30天',
						type: 2
					},
					{
						name: '自定义',
						type: 3
					},
				],
				timeColor: 1,
				value2: "",
				totalData: {
					allmoney: 0,
					allRegCount: 0,
					allDevice: 0,
					todaymoney: 0,
					tRegCount: 0,
					runDevice: 0
				},
				earnX: [],
				earnY: [],
				deviceY: [],
				deviceX: [],
				myChart: {},
				userPayChart: {},
				piePay: {},
				pieTrade: {},
				nopaymoney: 0,
				nopaymoney: 0,
				yxUser: 0,
				wxUser: 0,
				allCOUNT: 0
			}
		},
		created() {

		},
		mounted() {
			this.myChart = echarts.init(document.getElementById('chart'));
			this.userPayChart = echarts.init(document.getElementById('userPayChart'));
			this.piePay = echarts.init(document.getElementById('piePay'));
			this.pieTrade = echarts.init(document.getElementById('pieTrade'));
			this.getAllData();
			this.getTrend(30);
		},
		methods: {
			changeTime() {
				let _this = this;
				if(_this.value2) {
					_this.getTrend(_this.value2); //自定义获取每日营业总额和实收总额
				} else {
					_this.timeColor = 1;
					_this.getTrend(30); //每日营业额
				}
			},
			getAllData() {
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'POST',
					data: {},
					url: "/reportController/firstPage", // 请求地址
				}).then(res => {
					if(res.hasOwnProperty('error')) {
						this.$message({
							type: "danner",
							message: res.message ? res.message : res.msg
						})
						return;
					}
					this.totalData = res.data;
				});
			},
			getTrend(day) {
				let _this = this,
					nowTime, beforeTime;
				if(day instanceof Array) {
					beforeTime = formatWithSeperator(day[0], "-", ":").split(" ")[0]
					nowTime = formatWithSeperator(day[1], "-", ":").split(" ")[0]
				} else {
					let s = new Date().getTime();
					let time = (s - day * 24 * 60 * 60 * 1000);
					beforeTime = formatWithSeperator(time, "-", ":").split(" ")[0];
					nowTime = formatWithSeperator(s, "-", ":").split(" ")[0];
				}
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'POST',
					data: {
						"selfDate": {
							"stop": nowTime,
							"star": beforeTime
						},
					},
					url: "/reportController/firstPageImg", // 请求地址
				}).then(res => {
					if(res.hasOwnProperty('error')) {
						this.$message({
							type: "danner",
							message: res.message ? res.message : res.msg
						})
						return;
					}
					this.earnX = res.data.earnImg.map(res => {
						return res.thday.slice(5).replace("-", "/");
					});
					this.earnY = res.data.earnImg.map(res => {
						return res.paymoney;
					});
					this.deviceX = res.data.xbsyPhb.map(res => {
						return res.paymoney;
					});
					this.deviceY = res.data.xbsyPhb.map(res => {
						return res.NAME;
					});
					this.nopaymoney = res.data.userPay[0].nopaymoney;
					this.paymoney = res.data.userPay[0].paymoney;

					this.yxUser = res.data.userCount.yxUser;
					this.wxUser = res.data.userCount.wxuser;

					this.allCOUNT = (res.data.userCount.yxUser + res.data.userCount.wxuser);
					this.drawChart();

					//this.totalData = res.data;
				});
			},
			timeMes(index, item) { // 筛选日期
				let _this = this;
				_this.timeColor = index;
				if(item.type == 1) {
					_this.value2 = '';
					_this.getTrend(7); //每日营业额
				}
				if(item.type == 2) {
					_this.value2 = '';
					_this.getTrend(30); //每日营业额

				}
			},
			drawChart() {
				// 设备收益排行
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
						name: "元",
						type: 'value',
						nameGap: 5,
						boundaryGap: [0, 0.01]
					},
					yAxis: {
						name: "设备名称",
						type: 'category',
						data: this.deviceY
					},
					series: [{
						type: 'bar',
						data: this.deviceX
					}]
				})
				// 用户支付占比
				this.piePay.setOption({
					title: {
						text: "用户支付占比"
					},
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						left: 'right',
						right: 20,
						top: 40,
						data: ['已支付金额', "待支付金额"]
					},
					series: [{
						name: '用户支付',
						type: 'pie',
						radius: '65%',
						center: ['50%', '60%'],
						data: [{
								value: this.paymoney,
								name: '已支付金额'
							},
							{
								value: this.nopaymoney,
								name: '待支付金额'
							},
						],
						itemStyle: {
							emphasis: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}]
				});
				// 用户交易占比
				this.pieTrade.setOption({
					title: {
						text: "用户交易占比"
					},
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b}: {c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						left: 'right',
						right: 20,
						top: 40,
						data: ['有交易', '从未交易']
					},
					series: [{
						name: '用户交易',
						type: 'pie',
						radius: ['50%', '70%'],
						avoidLabelOverlap: false,
						label: {
							normal: {
								show: false,
								position: 'center'
							},
							emphasis: {
								show: true,
								textStyle: {
									fontSize: '0',
									fontWeight: 'bold'
								}
							},
							fontSize: 20
						},
						labelLine: {
							normal: {
								show: false
							}
						},
						data: [{
								value: this.yxUser,
								name: '有交易'
							},
							{
								value: this.wxUser,
								name: '从未交易'
							},
						]
					}]
				});

				//近期收益趋势
				this.userPayChart.setOption({
					title: {
						text: "近期收益趋势"
					},
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['平台收入']
					},
					toolbox: {
						show: true,
					},
					calculable: true,
					xAxis: [{
						name: "日",
						type: 'category',
						data: this.earnX,
					}],
					yAxis: [{
						name: "金额/元",
						type: 'value'
					}],
					series: [{
						name: '平台收入',
						type: 'bar',
						data: this.earnY
					}]
				})
			},
		},
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	#chart,
	#piePay,
	#userPayChart {
		width: 30%;
		height: 380px;
		margin-top: 20px;
		margin-bottom: 50px;
	}
	#chart{
		width: 40%;
	}
	#userPayChart,
	#pieTrade {
		width: 100%;
	}
	
	#pieTrade {
		height: 100%;
	}
	
	.pieTradeBox {
		position: relative;
		width: 33%;
		height: 380px;
		margin-top: 20px;
		margin-bottom: 50px;
	}
	
	.pieTradeBoxInfo {
		position: absolute;
		width: 42%;
		height: 10%;
		top: 50%;
		left: 8rem;
		background: transparent;
		transform:  translateY(-50%);
		text-align: center;
		line-height: 30px;
		font-size: 14px;
		color: #333;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		z-index: 999;
	}
	
	.pieTradeBoxInfo>div {
		white-space: nowrap;
	}
	
	.Color {
		color: rgba(254, 254, 254, 1);
		background: rgba(20, 136, 154, 1);
		color: #FEFEFE !important;
	}
	/* left*/
	
	.wd_left {
		display: flex;
		flex-wrap: wrap;
	}
	
	.wd_title {
		width: 100%;
		display: flex;
		height: 40px;
		justify-content: flex-start;
		align-items: center;
		box-sizing: border-box;
		font-size: 15px;
		padding-left: 20px;
		font-weight: bold;
	}
	
	.wd_center {
		width: 100%;
	}
	
	.wdbox {
		display: flex;
	}
	
	.wd_xu {
		width: 20%;
		height: 57.2px;
		font-size: 14px;
		border: 1px solid #DDDDDD;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.wd_oilname {
		width: 40%;
		height: 57.2px;
		border: 1px solid #DDDDDD;
		display: flex;
		font-size: 14px;
		justify-content: center;
		align-items: center;
		border-left: none;
	}
	
	.wd_oilmoney {
		width: 40%;
		height: 57.2px;
		border: 1px solid #DDDDDD;
		display: flex;
		font-size: 14px;
		justify-content: center;
		align-items: center;
		border-left: none;
	}
	
	.wd_xu1 {
		width: 20%;
		height: 35.4px;
		font-size: 14px;
		border: 1px solid #DDDDDD;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.wd_oilname1 {
		width: 40%;
		height: 35.4px;
		border: 1px solid #DDDDDD;
		display: flex;
		font-size: 14px;
		justify-content: center;
		align-items: center;
		border-left: none;
	}
	
	.wd_oilmoney1 {
		width: 40%;
		height: 35.4px;
		border: 1px solid #DDDDDD;
		display: flex;
		font-size: 14px;
		justify-content: center;
		align-items: center;
		border-left: none;
	}
	
	.wdbox:not(:first-child) .wd_xu1 {
		border-top: none;
	}
	
	.wdbox:not(:first-child) .wd_oilname1 {
		border-top: none;
	}
	
	.wdbox:not(:first-child) .wd_oilmoney1 {
		border-top: none;
	}
	
	.wdbox:not(:first-child) .wd_xu {
		border-top: none;
	}
	
	.wdbox:not(:first-child) .wd_oilname {
		border-top: none;
	}
	
	.wdbox:not(:first-child) .wd_oilmoney {
		border-top: none;
	}
	
	.headright {
		display: flex;
	}
	
	.navdiv {
		color: #14889A;
		width: 60px;
		height: 28px;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 1px solid #14889A;
		box-sizing: border-box;
		overflow: hidden;
		cursor: pointer;
	}
	
	.navdiv:not(:first-child) {
		border-left: none;
	}
	
	.navdiv:first-child {
		border-radius: 3px 0px 0px 3px;
	}
	
	.navdiv:last-child {
		border-radius: 0px 3px 3px 0px;
		width: 250px;
		height: 28px;
	}
	
	.home {
		font-family: "微软雅黑";
		overflow: hidden;
	}
	
	#myBing {
		width: 380px;
		height: 350px;
	}
	
	.bag {
		background: #DDDDDD;
		font-size: 15px !important;
		height: 50px !important;
	}
	
	.head {
		height: 38px;
		font-size: 14px;
		background: #F3F3F3;
		box-sizing: border-box;
		display: flex;
		padding-left: 20px;
		padding-right: 20px;
		justify-content: space-between;
		align-items: center;
		min-width: 800px;
	}
	
	input {
		background: #F3F3F3 !important;
	}
	
	.title {
		width: 100%;
		min-height: 50px;
		display: flex;
		align-items: center;
		box-sizing: border-box;
		padding-left: 10px;
		font-size: 14px;
	}
	
	.money {
		width: 100%;
		min-height: 30px;
		box-sizing: border-box;
		padding-left: 10px;
		padding-right: 10px;
		padding-bottom: 5px;
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
		font-size: 20px;
		color: #666666;
	}
	
	.bottom {
		border-top: 1px solid #DDDDDD;
		width: 100%;
		box-sizing: border-box;
		padding-left: 10px;
		padding-right: 10px;
		min-height: 40px;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 14px;
	}
	
	h1,
	h2 {
		font-weight: normal;
	}
	
	ul {
		list-style-type: none;
		padding: 0;
	}
	
	li {
		display: inline-block;
		margin: 0 10px;
	}
	
	a {
		color: #42b983;
	}
	
	.el-row {
		margin-bottom: 20px;
	}
	
	.el-col {
		/*width: 338px;
		border-radius: 4px;
		flex: 1;*/
	}
	
	.bg-purple-dark {}
	
	.bg-purple {
		border: 1px solid #DDDDDD;
		box-sizing: border-box;
	}
	
	.bg-purple-light {
		background: #e5e9f2;
	}
	
	.grid-content {
		border-radius: 4px;
		height: 120px;
	}
	
	.row-bg {
		padding: 10px 0;
	}
	
	.acitive {
		width: 100%;
		padding-bottom: 30px;
		display: none;
	}
	
	.oright {
		display: flex;
		justify-content: space-between;
	}
	
	.oilTitle {
		width: 50%;
		display: flex;
		justify-content: flex-start;
		height: 60px;
		align-items: center;
		padding-left: 20px;
		font-weight: bold;
		font-size: 15px;
	}
	
	.leftbox {
		display: flex;
	}
	
	.oilnum {
		font-size: 16px;
		font-weight: bold;
		color: rgba(102, 102, 102, 1);
	}
	
	.oilcode {
		margin-top: 5px;
		font-size: 14px;
		font-weight: 400;
		color: rgba(102, 102, 102, 1);
	}
	
	.oilbox:not(:first-child) {
		border-left: none;
	}
	
	.oilbox {
		display: flex;
		min-width: 100px;
		min-height: 60px;
		border: 1px solid #DDDDDD;
		justify-content: center;
		flex-direction: column;
		align-items: center;
	}
	
	.bleft {
		border: 1px solid #CCCCCC;
		min-height: 30px;
		border-right: none;
	}
	
	.phbox {
		width: 100%;
		display: flex;
		flex-wrap: wrap;
	}
	
	.nameBox {
		width: 50%;
		display: flex;
		box-sizing: border-box;
		border: 1px solid #CCCCCC;
	}
	
	.nameBox:nth-child(2) .num {
		border-left: none;
		border-bottom: none;
	}
	
	.oilName {
		width: 70%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.num {
		width: 30%;
		height: 30px;
		display: flex;
		align-items: center;
		justify-content: center;
		border-right: 1px solid #CCCCCC;
	}
	
	.bright {
		border: 1px solid #CCCCCC;
		min-height: 30px;
	}
	
	.pagination {
		display: flex;
		align-items: flex-end;
		justify-content: flex-end;
		margin-top: 25px;
	}
	
	.el-pagination {
		padding-right: 0px !important;
	}
	
	.headtitle {
		padding-left: 20px;
		width: 100%;
		height: 30px;
		display: flex;
		justify-content: flex-start;
		box-sizing: border-box;
		font-weight: bold;
		font-size: 15px;
	}
	
	.boxKindOil {}
	
	.boxKindOil>div {}
	
	.l {
		width: 275px;
		margin-right: 20px;
	}
	
	.l .title,
	.l .money {
		justify-content: center;
	}
	
	.l-2 {
		align-items: center;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}
	
	.l-2 .title {
		min-height: 35px;
		line-height: 35px;
	}
	
	.l-3 .bottom {
		align-items: initial;
	}
	
	.l-3 .bottom .left {
		width: 50%;
		height: 36px;
		line-height: 36px;
		border-right: 1px solid #DDDDDD;
	}
	
	.l-3 .bottom .right {
		width: 50%;
		height: 36px;
		line-height: 36px;
	}
	
	.tableOilnum {
		width: 500px;
	}
	
	.tableOilnum>div {
		display: flex;
	}
	
	.tableOilnum>div>div {
		min-width: 50px;
		flex: 1;
		min-height: 40px;
		line-height: 40px;
		border: 1px solid #f5f5f5;
		text-align: center;
	}
	
	.tableOilnum>div>div:nth-of-type(n - 15) {
		border-bottom: none;
	}
	
	.borderBottom {
		border-bottom: 1px solid #f5f5f5!important;
	}
	
	.bgF5 {
		background: #F5F5F5;
	}
</style>