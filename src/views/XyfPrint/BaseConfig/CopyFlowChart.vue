<template>
	<div>
		<el-table :data="tableData" border size="small">
			<el-table-column type="index" label="序号" align="center" header-align="center">

			</el-table-column>
			<el-table-column label="图片" prop="imgsrc" :formatter="imgsrcFn" align="center" header-align="center">
				<template slot-scope="scope">
					<img :src="scope.row.imgsrc" v-if="scope.row.imgsrc"/>
					<el-upload class="upload-demo" v-if="!scope.row.imgsrc" drag action="123" accept="image/*" :show-file-list="false" :http-request="uploadOpen" :on-success="handleAvatarSuccess" :on-error="handlerAvatarError" :before-upload="beforeAvatarUpload">
						<i class="el-icon-upload"></i>
						<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
						<div class="el-upload__tip" slot="tip">为防止图片变形，请上传图片大小为x*x</div>
					</el-upload>
				</template>
			</el-table-column>
			<el-table-column label="操作日期" prop="createTime" align="center" header-align="center">

			</el-table-column>
			<el-table-column label="操作人" prop="createBy" align="center" header-align="center">

			</el-table-column>
		</el-table>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: 'CopyFlowChart',
		data() {
			return {
				tableData: [ // 表格数据
					{
						imgsrc: "http://nd001.oss-cn-beijing.aliyuncs.com/data/62ab25f08ca74e59ad973556cd80e4af.jpg?x-oss-process=image/resize,m_fill,h_128,w_128",
						createTime: "2019-09-01",
						createBy: "admin"
					}
				],
				imgsrcFn: function(column, row, cellValue) { // 图片转换
					return cellValue;
				},
				tableConfig: {
					hasDelete: true, //删除
					hasNew: true, //新增
					hasNew: true, //新增
					hasView: false, //查看
					hasEdit: true, //编辑
					hasPagination: true,
					hostUrl: this.global.baseUrl,
					tableName: 'BaseDvhomeimg',
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					operationAttrs:{
	                  width: "100px"
	                },
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "/baseDvhomeimg/findById?id=1",
						method: 'get',
						notCondition: true //没有查询条件则查询全部数据
					},
				}
			}
		},
		methods: {
			getList(){ // 获取表格数据
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'post',
					data: {},
					url: "/baseDvhomeimg/findById?id=1", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					if ( res.data.rows.length != 0 ) {
						this.tableData = res.data.rows;
					}
				}).catch((err) => {
					this.$message.error(err.message);
				});
			},
			// 上传图片成功
			handleAvatarSuccess(res, file) {
				this.imageUrl = URL.createObjectURL(file.raw);
			},
			// 上传图片失败
			handlerAvatarError(error) {
				this.$alert(error)
			},
			uploadOpen(item) { // 图片上传
				let self = this;
				let formData = new FormData();
				formData.append('file', item.file);
				return;
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'post',
					data: formData,
					url: "/basePapertype/save", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					this.$message({
							type: 'success',
							message: "上传成功"
						});
					this.papeList = res.data.rows;
				}).catch((err) => {
					this.$message.error(err.message);
				});
			},
			//图片上传限制
			beforeAvatarUpload(file) {
				let _this = this
				if(file.type.indexOf('image') < 0) {
					this.$alert({
						message: '图片格式不正确',
						btn: false
					})
					return false
				}
				const isLt2M = file.size / 1024 / 1024 < 2;
				if(!isLt2M) {
					this.$alert({
						message: '图片大小不能超过2M。',
						btn: false
					})
					return false
				}
				const isSize = new Promise(function(resolve, reject) {
					let width = 44;
					let height = 44;
					let _URL = window.URL || window.webkitURL;
					let img = new Image();
					img.onload = function() {
						let valid = img.width == width && img.height == height;
						valid ? resolve() : reject();
					}
					img.src = _URL.createObjectURL(file);
				}).then(() => {
					return file;
				}, () => {
					_this.$alert('上传的图片大小必须是等于44*44!');
					return Promise.reject();
				});
				return isSize
			},
		}
	}
</script>

<style>

</style>
