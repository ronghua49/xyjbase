<template>
	<div class="home">
		<div>
			<div id="chart" class="chart"></div>
			<div class="block">
				<span class="demonstration">年份</span>
				<el-date-picker v-model="value" type="year" @change="yearC" placeholder="选择年">
				</el-date-picker>
			</div>
			<div class="payBox">
				<div class="header">
					<div v-for="item in headerData">{{item}}</div>
				</div>
				<div class="hasPay">
					<div v-for="item in hasPayData">{{item}}</div>
				</div>
				<div class="unPay">
					<div v-for="ky in unPayData">{{ky}}</div>
				</div>
				<div class="allData">
					<div v-for="item in allData">{{item}}</div>
				</div>
				<div class="noneMoney" v-if="noneMoney">
					暂无收益
				</div>
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
		name: 'Home',
		data() {
			return {
				unPay: [], // 待支付金额
				hasPay: [], // 已支付金额
				value: "",
				year: new Date().getFullYear(),
				tableData: [],
				headerData: ['指标',new Date().getFullYear()+"年01月",new Date().getFullYear()+"年02月",new Date().getFullYear()+"年03月",new Date().getFullYear()+"年04月",new Date().getFullYear()+"年05月",new Date().getFullYear()+"年06月"
				,new Date().getFullYear()+"年07月",new Date().getFullYear()+"年08月",new Date().getFullYear()+"年09月",new Date().getFullYear()+"年10月",new Date().getFullYear()+"年11月",new Date().getFullYear()+"年12月"
				,"合计"],
				unPayData: [],
				hasPayData: [],
				allData: [],
				noneMoney: false
			}
		},
		mounted() {
			let _this = this;
			_this.myChart = echarts.init(document.getElementById('chart'));
			_this.getTrend();
		},
		methods: {
			getTrend() { // 获取统计图数据
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'POST',
					data: {},
					url: "/reportController/sytjquery?year=" + this.year, // 请求地址
				}).then(res => {
					if(res.hasOwnProperty('error')) {
						this.$message({
							type: "danner",
							message: res.message ? res.message : res.msg
						})
						return;
					}
					let data = res.data[0];
					for ( let key in data ) {
						if (data[key] == 'null' || data[key] == null) ""
					}
					this.hasPay = [data["january"], data["february"], data["march"], data["april"], data["may"], data["june"], data["july"],
						data["august"], data["september"], data["october"], data["november"], data["december"]
					];
					this.unPay = [data["nojanuary"], data["nofebruary"], data["nomarch"], data["noapril"], data["nomay"],
						data["nojune"], data["nojuly"], data["noaugust"], data["noseptember"], data["nooctober"], data["novemberNo"], data["nodecember"]
					];
					let allHasPayData = this.hasPay.reduce((total,num)=>{
						return (total + num);
					});
					let allUnPayData = this.unPay.reduce((total,num)=>{
						return (total + num);
					})
					this.hasPay.push(allHasPayData.toFixed(2));
					this.unPay.push(allUnPayData.toFixed(2));
					this.hasPayData = ['已支付金额'].concat(this.hasPay);
					this.unPayData = ['待支付金额'].concat(this.unPay);
					for ( let i = 0; i < this.unPayData.length; i++ ) {
						if ( i == 0 ) {
							this.allData.push("合计");
						} else {
							this.allData.push((Number(this.unPayData[i])+Number(this.hasPayData[i])).toFixed(2));
						}
					}
					this.hasPay.pop();
					this.unPay.pop();
					this.drawChart();
				})
			},
			drawChart() {
				let _this = this;
				let option = {
					title: {},
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['待支付金额', '已支付金额']
					},
					toolbox: {
						show: true,
					},
					calculable: true,
					xAxis: [{
						type: 'category',
						data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
					}],
					yAxis: [{
						type: 'value'
					}],
					series: [{
						name: '待支付金额',
						type: 'bar',
						data: _this.unPay
					}, {
						name: '已支付金额',
						type: 'bar',
						data: _this.hasPay
					}]
				};
				_this.myChart.setOption(option);
			},
			yearC(val){
				this.year = val.getFullYear();
				this.hasPay = [];
				this.unPay = [];
				this.hasPayData = [];
				this.unPayData = [];
				this.allData = [];
				this.getTrend();	
			}
		},
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	#chart {
		width: 100%;
		height: 380px;
		margin-top: 20px;
		margin-bottom: 50px;
	}
	.block{
		text-align: left;
		margin-left: 50px;
	}
	.payBox{
		width: 100%;
		border: 1px solid #E4E4E4;
		border-bottom: 0;
		border-right: 0;
		box-sizing: border-box;
		margin-top: 30px;
	}
	.payBox>div>div{
		text-align: center;
		line-height: 50px;
		color: #333;
		flex: 1;
		font-size: 12px;
		border-right: 1px solid #E4E4E4;
		border-bottom: 1px solid #E4E4E4;
	}

	.payBox>div{
		display: flex;
	}
	.header{
		background: #F2F2F2;
		
	}
	.noneMoney{
		text-align: center;
		line-height: 30px;
		border-bottom: 1px solid #E4E4E4;
		font-size: 12px;
	}
	.demonstration{
		font-size: 14px;
	}
</style>