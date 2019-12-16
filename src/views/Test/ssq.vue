<template>
	<!--调用此组件时 ，外部需要接收selectCallback方法-->
	<div>
		<div class="cascader" v-if="kinds == 'cascader'">
			
		</div>
		<div class="selectBox" v-else-if="kinds=='select'">
			<el-select v-model="sprovinceValue" placeholder="请选择省" @change="sprovinceChange">
			    <el-option
			      v-for="item in provinces"
			      :key="item.value"
			      :label="item.text"
			      :value="item.text">
			    </el-option>
			</el-select>
			<el-select v-model="scityValue" placeholder="请选择市" @change="scityChange">
			    <el-option
			      v-for="item in citys"
			      :key="item.value"
			      :label="item.text"
			      :value="item.text">
			    </el-option>
			</el-select>
			<el-select v-model="sdistrict" placeholder="请选择区" @change="sdistChange">
			    <el-option
			      v-for="item in districts"
			      :key="item.value"
			      :label="item.text"
			      :value="item.text">
			    </el-option>
			</el-select>
		</div>
	</div>
</template>

<script>
	import data from "@/assets/provinceAndCityData/citydata3.js"
	export default {
		name: 'selectTools',
		props:{
			kinds:{
				type: String,
				default: "select"
			}
		},
		data(){
			return {
				provinces: [],
				citys: [],
				districts: [],
				sprovinceValue: "",
				scityValue: "",
				sdistrict: ""
			}
		},
		methods: {
			sprovinceChange(val){ // 下拉框省变动的时候
				this.districts = [];
				this.scityValue = this.sdistrict = "";
				let s= data.filter(res=>{
					return res.text == val;
				})
				this.citys = s[0].children;
				this.$emit("selectCallback",this.sprovinceValue + this.scityValue + this.sdistrict);
			},
			scityChange(val) { // 市变动的时候
				this.sdistrict = "";
				let s= this.citys.filter(res=>{
					return res.text == val;
				})
				this.districts = s[0].children;
				this.$emit("selectCallback",this.sprovinceValue + this.scityValue + this.sdistrict);
			},
			sdistChange(val){ // 区变动的时候
				this.$emit("selectCallback",this.sprovinceValue + this.scityValue + this.sdistrict);
			}
		},
		mounted(){
			if ( !data ) {
				this.$message.error("没有省市区js插件,无法加载数据!");
				return;
			}
			this.provinces = data;
		}
	}
</script>

<style>
</style>