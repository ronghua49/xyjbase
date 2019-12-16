<template>
	<div class="main">
		<div class="top">
			<div class="info">基本信息</div>
		</div>
		<div class="baseInfo">
			<el-form ref="form" :model="infoForm" :rules="rules" label-width="200px">
				<el-form-item label="设备编码" prop="deviceno">
					<el-input v-model="infoForm.deviceno" placeholder="请输入设备编码"></el-input>
				</el-form-item>
				<el-form-item label="出厂日期" prop="pddate">
					<el-date-picker type="date" v-model="infoForm.pddate" placeholder="请选择出厂日期"></el-date-picker>
				</el-form-item>
				<el-form-item label="设备型号" prop="model">
					<el-select v-model="infoForm.model" placeholder="请选择设备型号" @change="selectModel">
						<el-option v-for="item in equipment" :key="item.id" :value="item.model" :label="item.model"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="设备名称" prop="name">
					<el-select v-model="infoForm.name" placeholder="请选择设备名称" @change="selectName">
						<el-option v-for="item in equipment" :key="item.id" :value="item.name" :label="item.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="负责人" prop="fzsysusername">
					<el-select v-model="infoForm.fzsysusername" placeholder="请选择负责人" @change="selectfzphone">
						<el-option v-for="item in users" :key="item.id" :value="item.name" :label="item.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="联系方式1" prop="fzsysuserphone">
					<el-input maxlength="11" v-model="infoForm.fzsysuserphone" placeholder="请输入联系方式"></el-input>
				</el-form-item>
				<el-form-item label="维护人" prop="whsysusername">
					<el-select v-model="infoForm.whsysusername" placeholder="请选择维护人" @change="selectwhphone">
						<el-option v-for="item in users" :key="item.id" :value="item.name" :label="item.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="联系方式2" prop="whsysuserphone">
					<el-input maxlength="11" v-model="infoForm.whsysuserphone" placeholder="请输入联系方式"></el-input>
				</el-form-item>
			</el-form>
		</div>
		<div class="top">
			<div class="info">设备位置</div>
		</div>
		<div class="center">
			<div class="place">
				<el-form ref="DeviceInfoform" :model="placeForm" :rules="rules" label-width="200px">
					<el-form-item label="位置图片" prop="logo">
						<el-upload v-if="!this.placeForm.logo" :multiple=false class="upload-demo" drag accept=".jpg, .png, .gif, .jpeg, .bmp" action="123" :file-list="fileList" :on-change="handleChange" :on-exceed="handleExceed" :http-request="fileUpload">
							<i class="el-icon-upload"></i>
							<div class="el-upload__text">将图片(190*160)拖到此处，或<em>点击上传</em></div>
						</el-upload>
						<div class="logoBox" v-if="this.placeForm.logo">
							<div class="closeImg" @click="deleteUpload">&times;</div>
							<img :src="this.placeForm.logo" class="logo" />
						</div>
					</el-form-item>
					<el-form-item label="门店名称" prop="landmark">
						<el-input maxlength="20" v-model="placeForm.landmark" placeholder="请输入门店名称"></el-input>
					</el-form-item>
					<el-form-item label="门店电话" prop="mdPhone">
						<el-input maxlength="13" v-model="placeForm.mdPhone" placeholder="请输入门店电话"></el-input>
					</el-form-item>
					<el-form-item label="营业时间" required>
						<div style="display:flex;justify-content: space-between; width: 80%;">
							<el-col :span="10">
								<el-form-item prop="yybeginstr">
									<el-time-select :picker-options="{
										      start: '00:00',
										      step: '00:15',
										      end: '23:59'
										    }" style="width: 100%;" placeholder="起始时间" v-model="placeForm.yybeginstr">
									</el-time-select>
								</el-form-item>
							</el-col>
							<el-col :span="10">
								<el-form-item prop="yyendstr">
									<el-time-select :picker-options="{
										      start: '00:00',
										      step: '00:15',
										      end: '24:00',
										      minTime: placeForm.yybeginstr
										    }" style="width: 100%;" placeholder="结束时间" v-model="placeForm.yyendstr">
									</el-time-select>
								</el-form-item>
							</el-col>
						</div>
					</el-form-item>
					<el-form-item label="设备位置" required>
						<el-row type="flex" justify="space-between" style="width: 83%;" :gutter='20'>
							<el-col :span="8" style="flex: 1;">
								<el-form-item prop="province">
									<el-select style="width: 100%;" v-model="placeForm.province" placeholder="省" @change="sprovinceChange">
									    <el-option
									      v-for="item in provinces"
									      :key="item.text"
									      :label="item.text"
									      :value="item.text">
									    </el-option>
									</el-select>
									<!--<el-input style="width: 100%;" v-model="placeForm.province" placeholder="省"></el-input>-->
								</el-form-item>
							</el-col>
							<el-col :span="8" style="flex: 1;">
								<el-form-item prop="city">
									<el-select style="width: 100%;" v-model="placeForm.city" placeholder="市" @change="scityChange">
									    <el-option
									      v-for="item in citys"
									      :key="item.text"
									      :label="item.text"
									      :value="item.text">
									    </el-option>
									</el-select>
									<!--<el-input style="width: 100%;" v-model="placeForm.city" placeholder="市"></el-input>-->
								</el-form-item>
							</el-col>
							<el-col :span="8" style="flex: 1;">
								<el-form-item prop="dist">
									<el-select style="width: 100%;" v-model="placeForm.dist" placeholder="区">
									    <el-option
									      v-for="item in districts"
									      :key="item.value"
									      :label="item.text"
									      :value="item.text">
									    </el-option>
									</el-select>
									<!--<el-input style="width: 100%;" v-model="placeForm.dist" placeholder="区"></el-input>-->
								</el-form-item>
							</el-col>
						</el-row>
					</el-form-item>
					<el-form-item label="具体地址" prop="address">
						<el-input v-model="placeForm.address" placeholder="请输入具体地址">
							<el-button slot="append" icon="el-icon-search" @click="searchAddress"></el-button>
						</el-input>
					</el-form-item>
					<el-form-item label="腾讯经度" prop="qqlongitude">
						<el-input v-model="placeForm.qqlongitude" placeholder="请输入腾讯经度"></el-input>
					</el-form-item>
					<el-form-item label="腾讯纬度" prop="qqlatitude">
						<el-input v-model="placeForm.qqlatitude" placeholder="请输入腾讯纬度"></el-input>
					</el-form-item>
				</el-form>
			</div>
			<div class="map" id="container">
			</div>
		</div>
		<div class="under">
			<div class="info">计费方式</div>
		</div>
		<div style="width: 300px">
			<el-form ref="selectform" :model="selectForm" :rules="rules" label-width="90px">
				<el-form-item label="所属区域">
					<el-select v-model="selectForm.areaid" placeholder="请选择" @change="selectArea">
						<el-option v-for="item in areanames" :key="item.id" :value="item.id" :label="item.areaname"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
		</div>
		<div>
			<el-table :data="tableData" border style="width: 100%;">
				<el-table-column type="index" label="序号" width="150"></el-table-column>
				<el-table-column prop="papername" label="纸张规格" min-width="180"></el-table-column>
				<el-table-column prop="color2side" label="颜色类型" min-width="180"></el-table-column>
				<el-table-column prop="price" label="单价（元/张）" min-width="180"></el-table-column>
				<!--<el-table-column prop="date" label="操作" min-width="180">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="text" size="small">删除</el-button>
            </template>
          </el-table-column>-->
			</el-table>
		</div>
		<div class="footer" style="text-align: center;margin: 20px 0 80px;">
			<el-button size="small" v-if="!idEdit" type="primary" @click="saveMessage">创建</el-button>
			<el-button size="small" v-if="idEdit" type="primary" @click="saveMessage">保存</el-button>
			<el-button size="small" v-if="idEdit" @click="cancel">返回</el-button>
		</div>
	</div>
</template>

<script>
	import { TMap } from '../../../components/bcomponents/XyfPrint/map'
	import axios from "@/http/axios";
	import { formatWithSeperator } from "@/utils/datetime.js";
	import citydata from "@/assets/provinceAndCityData/citydata3.js"
	export default {
		name: "NewDelivery",
		props: {
			onOk: {
				type: Function
			}
		},
		mounted() {
			this.queryBaseDevice();
			this.queryUser();
			this.selectbaseArea();
			this.selectbaseDeviceDetail();
			//地图
			this.initMap();
			this.provinces = citydata;
		},
		data() {
			return {
				provinces: [],
				citys: [],
				districts: [],
				sprovinceValue: "",
				scityValue: "",
				sdistrict: "",
				arras: [{
						id: "1",
						name: "wwwd"
					},
					{
						id: "2",
						name: "wwwd2"
					},
					{
						id: "3",
						name: "wwwd3"
					},
					{
						id: "4",
						name: "wwwd4"
					}
				],
				value: "4",
				idEdit: false, // 是否是编辑此页面
				rules: {
					deviceno: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					pddate: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					model: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					name: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					fzsysusername: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					fzsysuserphone: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					whsysusername: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					whsysuserphone: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					logo: [{
						required: true,
						message: '请输入活动名称',
						trigger: 'blur'
					}],
					landmark: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					yybeginstr: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					yyendstr: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}],
					province: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					city: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					dist: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					address: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					qqlongitude: [{
						required: true,
						message: '请输入',
						trigger: 'blur'
					}],
					qqlatitude: [{
						required: true,
						message: '请输入活动名称',
						trigger: 'blur'
					}],
				},
				infoForm: {
					id: "", // 投放id
					deviceid: '', //设备id
					deviceno: '',
					pddate: '',
					model: '',
					name: '',
					whsysuserid: "",
					fzsysuserid: "",
					fzsysusername: '',
					fzsysuserphone: '',
					whsysusername: '',
					whsysuserphone: ''
				},
				placeForm: {
					logo: '',
					landmark: '',
					province: '',
					city: '',
					dist: '',
					address: '',
					yybeginstr: '',
					yyendstr: '',
					qqlongitude: '',
					qqlatitude: '',
					mdPhone: ""
				},
				selectForm: {
					areaname: '基础价格',
					areaid: '0'
				},
				position: [], //搜索后的经纬度
				equipment: [], //设备
				users: [], //员工
				areanames: [], //所属区域
				tableData: [],
				colortypes: [], //颜色类型
				dialogVisible: false,
				fileList: [],
				searchService: {},
				geocoder: {}
			}
		},
		methods: {
			sprovinceChange(val){ // 下拉框省变动的时候
				this.districts = [];
				this.scityValue = this.sdistrict = "";
				let s= citydata.filter(res=>{
					return res.text == val;
				})
				this.citys = s[0].children;
			},
			scityChange(val) { // 市变动的时候
				this.sdistrict = "";
				let s= this.citys.filter(res=>{
					return res.text == val;
				})
				this.districts = s[0].children;
			},
			initMap() {
				var markers = [],
					_this = this,
					marker = {};
				TMap('OJNBZ-W2K34-6PIU4-DEWYG-3MXBE-TBFYK').then(qq => {
					var map = new qq.maps.Map(document.getElementById("container"), {
						// 地图的中心地理坐标。
						center: new qq.maps.LatLng(39.916527, 116.397128),
						zoom: 10
					});
					qq.maps.event.addListener(map, 'click', function(event) {
						_this.placeForm.qqlongitude = event.latLng.getLng();
						_this.placeForm.qqlatitude = event.latLng.getLat();
						_this.geocoder.getAddress(new qq.maps.LatLng(event.latLng.getLat(), event.latLng.getLng()));
					});
					var info = new qq.maps.InfoWindow({
						map: map
					});
					this.geocoder = new qq.maps.Geocoder({
						complete: function(result) {
							map.setCenter(result.detail.location);
							_this.placeForm.province = result.detail.addressComponents.province;
							_this.placeForm.city = result.detail.addressComponents.city;
							_this.placeForm.dist = result.detail.addressComponents.district;
							_this.placeForm.address = result.detail.addressComponents.street + result.detail.addressComponents.town + result.detail.addressComponents.streetNumber;
							_this.$refs.DeviceInfoform.validate(['qqlongitude', 'qqlatitude', 'address', "province", "city", "dist"])
							//							marker.setMap(null); 
							//							marker = new qq.maps.Marker({
							//								map: map,
							//								position: result.detail.location
							//							});
						}
					});
					var latlngBounds = new qq.maps.LatLngBounds();
					//设置Poi检索服务，用于本地检索、周边检索
					this.searchService = new qq.maps.SearchService({
						//设置搜索范围为北京
						location: "",
						//设置搜索页码为1
						pageIndex: 1,
						//设置每页的结果数为5
						pageCapacity: 1,
						//设置展现查询结构到container上
						panel: document.getElementById('container'),
						//设置动扩大检索区域。默认值true，会自动检索指定城市以外区域。
						autoExtend: true,
						//检索成功的回调函数
						complete: function(results) {
							//设置回调函数参数
							var pois = results.detail.pois;
							for(var i = 0, l = pois.length; i < l; i++) {
								var poi = pois[i];
								//扩展边界范围，用来包含搜索到的Poi点
								latlngBounds.extend(poi.latLng);
								var marker = new qq.maps.Marker({
									map: map,
									position: poi.latLng
								});

								_this.position = marker.position;

								marker.setTitle(i + 1);

								//markers.push(marker);

							}
							//调整地图视野
							map.fitBounds(latlngBounds);
							_this.placeForm.qqlongitude = _this.position.lng;
							_this.placeForm.qqlatitude = _this.position.lat;
							_this.$refs.DeviceInfoform.validate(['qqlongitude', 'qqlatitude'])
						},
						//若服务请求失败，则运行以下函数
						error: function() {
							alert("出错了。");
						}
					});
					//设置搜索的范围和关键字等属性
					clearOverlays(markers);
					//清除地图上的marker
					function clearOverlays(overlays) {
						var overlay;
						while(overlay = overlays.pop()) {
							overlay.setMap(null);
						}
					}

				});
			},
			open(state, data) {
				if(state === "add") {
					this.idEdit = false;
				} else if(state == "edit") {
					this.idEdit = true;
					var i = 0;
					for(var key in data) {
						if(this.infoForm.hasOwnProperty(key)) {
							this.infoForm[key] = data[key];
						}
						if(this.placeForm.hasOwnProperty(key)) {
							this.placeForm[key] = data[key];
						}
						if(this.selectForm.hasOwnProperty(key)) {
							this.selectForm[key] = String(data[key]);
						}
						if(this.infoForm.deviceid && this.selectForm.areaid >= 0 && i == 0) {
							i++;
							this.selectbaseDeviceDetail();
						}
					}
				}
			},
			queryBaseDevice() {
				let _this = this;
				axios({
					hostUrl: _this.global.hostUrl86,
					url: "/baseDevice/query",
					method: "post",
					data: {}
				}).then((res) => {
					//设备
					_this.equipment = res.data.rows;
				}).catch((err) => {
					_this.$message.error(err.message)
				})
			},
			selectModel() {
				let _this = this;
				let model = _this.infoForm.model;
				for(let i = 0; i < _this.equipment.length; i++) {
					if(model === _this.equipment[i].model) {
						_this.infoForm.name = _this.equipment[i].name;
						_this.infoForm.deviceid = _this.equipment[i].id;
					}
				}
				this.selectbaseDeviceDetail();
			},
			selectName() {
				let _this = this;
				let name = _this.infoForm.name;
				for(let i = 0; i < _this.equipment.length; i++) {
					if(name === _this.equipment[i].name) {
						_this.infoForm.model = _this.equipment[i].model;
						_this.infoForm.deviceid = _this.equipment[i].id;
					}
				}
				this.selectbaseDeviceDetail();
			},
			//员工选择
			queryUser() {
				let _this = this;
				axios({
					hostUrl: _this.global.baseUrl,
					url: "/user/query",
					method: "post",
					data: {}
				}).then((res) => {
					//设备
					_this.users = res.data.rows;
				}).catch((err) => {
					_this.$message.error(err.message)
				})
			},
			//负责人
			selectfzphone() {
				let _this = this;
				let name = _this.infoForm.fzsysusername;
				for(let i = 0; i < _this.users.length; i++) {
					if(_this.users[i].name == name) {
						_this.infoForm.fzsysuserphone = _this.users[i].mobile;
						_this.infoForm.fzsysuserid = _this.users[i].id;
					}
				}
				this.$refs.form.validateField("fzsysuserphone");
			},
			//维护人
			selectwhphone() {
				let _this = this;
				let name = _this.infoForm.whsysusername;
				for(let i = 0; i < _this.users.length; i++) {
					if(_this.users[i].name == name) {
						_this.infoForm.whsysuserphone = _this.users[i].mobile;
						_this.infoForm.whsysuserid = _this.users[i].id;
					}
				}
				this.$refs.form.validateField("whsysuserphone");
			},
			//搜索地址
			searchAddress() {
				let _this = this;
				if(!_this.placeForm.province && !_this.placeForm.city && !_this.placeForm.dist && !_this.placeForm.address) {
					this.$message.error("请填写地址信息")
					return;
				}
				var address = _this.placeForm.province + _this.placeForm.city + _this.placeForm.dist + _this.placeForm.address;
				//地图
				//根据输入的关键字在搜索范围内检索
				this.searchService.search(address);
			},
			//所属区域获取
			selectbaseArea() {
				let _this = this;
				axios({
					hostUrl: _this.global.hostUrl86,
					url: "/baseAreaMain/query",
					method: "post",
					data: {}
				}).then((res) => {
					_this.areanames = res.data.rows;
					_this.areanames.push({
						areaname: '基础价格',
						id: '0'
					});
				}).catch((err) => {
					_this.$message.error(err.message);
				})
			},
			//所属区域选择
			selectArea(val) {
				let _this = this;
				for(let i = 0; i < _this.areanames.length; i++) {
					if(_this.areanames[i].id == val) {
						_this.selectForm.areaname = _this.areanames[i].areaname;
					}
				}
				_this.selectForm.areaid = val;
				_this.selectbaseDeviceDetail();
			},
			selectbaseDeviceDetail() {
				let _this = this;
				if(!_this.infoForm.deviceid) { // 设备id不存在不请求
					return;
				}
				if(!_this.selectForm.areaid) { // 区域id不存在不请求
					_this.selectForm.areaid = 0;
				}
				axios({
					hostUrl: this.global.hostUrl86,
					url: "/deviceOnline/findPriceByDevice2Area/" + _this.infoForm.deviceid + "/" + _this.selectForm.areaid,
					method: 'get',
				}).then(res => {
					this.tableData = res.data;
				}).catch(err => {
					this.$message.error(err.message);
				});
			},
			fileUpload(item) {
				let self = this;
				let formData = new FormData();
				formData.append('file', item.file);
				axios({
					hostUrl: this.global.hostUrl,
					method: 'post',
					data: formData,
					url: '/FileUploadController/uploadFile', // 请求地址
				}).then((res) => {
					if(typeof res != "string") {
						this.$message.error(res.message);
						return;
					}
					this.$message({
						message: "上传成功",
						duration: 1000,
						type: "success"
					});
					this.placeForm.logo = res;
				})
			},
			handleChange(file, fileList) {
				this.fileList = fileList.slice(-1);
			},
			handleExceed(file, fileList) {
				this.fileUpload(file);
			},
			deleteUpload() { // 删除图片
				axios({
					hostUrl: this.global.hostUrl,
					method: 'post',
					data: {},
					url: "/FileUploadController/deleteFile?url=" + this.placeForm.logo, // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					this.fileList = [];
					this.placeForm.logo = "";
				})
			},
			saveMessage() {
				let _this = this;
				_this.$refs.form.validate((valid) => {
					if(!valid) {
						return;
					}
					_this.$refs.DeviceInfoform.validate((validate) => {
						if(!validate) {
							return;
						}
						var data = {
							"mdPhone": _this.placeForm.mdPhone,
							"address": _this.placeForm.address,
							"areaid": _this.selectForm.areaid,
							"areaname": _this.selectForm.areaname,
							"city": _this.placeForm.city,
							"deviceid": _this.infoForm.deviceid,
							"deviceno": _this.infoForm.deviceno,
							"dist": _this.placeForm.dist,
							"fzsysuserid": _this.infoForm.fzsysuserid,
							"fzsysusername": _this.infoForm.fzsysusername,
							"fzsysuserphone": _this.infoForm.fzsysuserphone,
							"landmark": _this.placeForm.landmark,
							"logo": _this.placeForm.logo,
							"model": _this.infoForm.model,
							"name": _this.infoForm.name,
							"pddate": formatWithSeperator(_this.infoForm.pddate, "-", ":"),
							"province": _this.placeForm.province,
							"qqlatitude": _this.placeForm.qqlatitude,
							"qqlongitude": _this.placeForm.qqlongitude,
							"whsysuserid": _this.infoForm.whsysuserid,
							"whsysusername": _this.infoForm.whsysusername,
							"whsysuserphone": _this.infoForm.whsysuserphone,
							"yybeginstr": _this.placeForm.yybeginstr,
							"yyendstr": _this.placeForm.yyendstr
						}
						if(_this.infoForm.id) {
							data.id = _this.infoForm.id;
						}
						axios({
							hostUrl: this.global.hostUrl85,
							url: "/deviceOnline/save",
							method: 'post',
							data: data
						}).then(res => {
							for(let key in this.placeForm) {
								_this.placeForm[key] = "";
							}
							for(let o in this.infoForm) {
								this.infoForm[o] = "";
							}
							this.tableData = [];
							
							if ( this.idEdit ) {
								this.$message({
									message: "修改成功",
									type: 'success',
									duration: 1000
								});
								this.onOk();
							} else {
								this.$message({
									message: "创建成功",
									type: 'success',
									duration: 1000
								});
							}
						}).catch(err => {
							this.$message.error(err.message);
						})
					})
				})
			},
			cancel() {
				if(this.onOk) {
					this.onOk();
				}
			}
		}
	}
</script>

<style scoped lang="scss">
	.main {
		width: 94%;
		min-width: 1000px;
		margin-left: 3%;
		text-align: left;
	}
	
	.top-upper {
		height: 30px;
		line-height: 30px;
	}
	
	.top {
		width: 100%;
		height: 40px;
		line-height: 40px;
		background-color: #f3f3f3;
		margin-top: 10px;
	}
	
	.under {
		width: 100%;
		height: 40px;
		line-height: 40px;
		background-color: #f3f3f3;
		margin-top: 685px;
		margin-bottom: 20px;
	}
	
	.info {
		text-align: left;
		padding-left: 30px;
	}
	
	.baseInfo {
		margin-top: 20px;
	}
	
	.baseInfo .el-form {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		.el-form-item {
			width: 40%;
			.el-date-editor,
			.el-input,
			.el-select {
				width: 100%;
			}
		}
	}
	
	.center {
		width: 94%;
		min-width: 1000px;
		position: absolute;
		margin-top: 20px;
	}
	
	.place {
		width: 50%;
		float: left;
		.el-form {
			display: inline;
			flex-direction: row;
			flex-wrap: nowrap;
			.el-form-item {
				width: 100%;
				.el-input,
				.el-select {
					width: 80%;
				}
			}
		}
	}
	
	.map {
		width: 40%;
		height: 350px;
		margin-top: 200px;
		border: 1px solid blanchedalmond;
		float: left;
	}
	
	.footer {
		width: 100%;
		margin-top: 10px;
		margin-bottom: 20px;
	}
	.logoBox{
		position: relative;
		width: 360px;
	}
	.logo {
		height: 180px;
	}
	
	.closeImg {
		position: absolute;
		top: 0;
		left: 330px;
		z-index: 999;
		width: 30px;
		height: 30px;
		background: #F5F5F5;
		text-align: center;
		line-height: 30px;
		font-size: 22px;
		color: #333333;
		display: none;
	}
	
	.logoBox:hover {
		.closeImg {
			display: block;
			background: #E9E9E9;
			color: #666;
		}
	}
	.closeImg:hover {
		cursor: pointer;
		background: #E9E9E9;
		color: #666;
	}
</style>
