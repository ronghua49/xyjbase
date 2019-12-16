<template>
	<div>
		<el-dialog :title="title" width="500px" :visible.sync="dialogVisible">
			<div style="width: 360px;margin: 0 auto;">
				<el-upload class="dialog-lunbo-uploader" drag action="123" accept="image/*"
					 :show-file-list="false"
					 :http-request="uploadOpen" 
					 :data="uploadData" 
					 :on-success="handleAvatarSuccess" 
					 :on-error="handlerAvatarError" 
					 :before-upload="beforeAvatarUpload">
				 <i class="el-icon-upload"></i>
				  <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
				</el-upload>
			</div>
			<div class="tip"><span style="color: red;">*</span>上传图片（为防止变形，请上传600*{{size}}尺寸的图片）</div>
			<div slot="footer" class="dialog-footer">
				<!--<el-button size="mini" @click="dialogVisible = false">取 消</el-button>
				<el-button size="mini" @click="saveHandler" type="primary">确 定</el-button>-->
			</div>
		</el-dialog>
		<el-table :data="tableData" border size="small">
			<el-table-column type="index" label="序号" align="center" header-align="center">

			</el-table-column>
			<el-table-column label="操作" prop="imgsrc" :formatter="imgsrcFn" align="center" header-align="center">
				<template slot-scope="scope">
					<el-button type="primary" @click="editColumn(scope.rows,scope.$index)" size="mini">编辑</el-button>
				</template>
			</el-table-column>
			<el-table-column label="图片名称" prop="imgName" :formatter="imgNameFn" align="center" header-align="center">
				
			</el-table-column>
			<el-table-column label="图片" prop="imgsrc" :formatter="imgsrcFn" align="center" header-align="center">
				<template slot-scope="scope">
					<img :src="scope.row.imgsrc" class="tableImg"/>
				</template>
			</el-table-column>
			<!--<el-table-column label="操作日期" prop="createTime" align="center" header-align="center">

			</el-table-column>-->
			<el-table-column label="操作人" prop="createBy" align="center" header-align="center">

			</el-table-column>
		</el-table>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: 'IPCPicture',
		data() {
			return {
				dialogVisible: false,
				tableData: [ // 表格数据
					{
						imgsrc: "http://nd001.oss-cn-beijing.aliyuncs.com/data/62ab25f08ca74e59ad973556cd80e4af.jpg?x-oss-process=image/resize,m_fill,h_128,w_128",
						createTime: "2019-09-01",
						createBy: "admin"
					},
					{
						imgsrc: "http://nd001.oss-cn-beijing.aliyuncs.com/data/62ab25f08ca74e59ad973556cd80e4af.jpg?x-oss-process=image/resize,m_fill,h_128,w_128",
						createTime: "2019-09-01",
						createBy: "admin"
					}
				],
				title: "",
				size: "",
				isAgainChange: false,
				willDeleteImg: "",
				imgsrcFn: function(column, row, cellValue,index) { // 图片转换
					return cellValue;
				},
				imgNameFn: function(column, row, cellValue,index) { // 图片名称
					if ( index == 0 ) {
						return "工控机图片";
					}
					return "复印流程图";
				},
				uploadData: {},
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
		mounted(){
			this.getList();
		},
		methods: {
			editColumn(row,index){
				if ( index == 0 ) {
					this.title = "更新工控机图片";
					this.size = "1024";
				} else {
					this.title = "更新复印机流程图片";
					this.size = "630";
				}
				this.dialogVisible = true;
			},
			getList(){ // 获取工控机图片数据
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'post',
					data: {},
					url: "/baseDvhomeimg/findPage", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					if ( res.data.content.length != 0 ) {
						this.tableData[0]["imgsrc"] = res.data.content[0]["imgsrc"];
						this.tableData[0]["id"] = res.data.content[0]["id"];
					}
				}).catch((err) => {
					this.$message.error(err.message);
				});
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'post',
					data: {},
					url: "/baseDvfyimg/findPage", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					if ( res.data.content.length != 0 ) {
						this.tableData[1]["imgsrc"] = res.data.content[0]["imgsrc"];
						this.tableData[1]["id"] = res.data.content[0]["id"];
					}
				}).catch((err) => {
					this.$message.error(err.message);
				});
			},
			deleteUpload(img){ // 删除图片
				axios({
					hostUrl: this.global.hostUrl,
					method: 'post',
					data: {},
					url: "/FileUploadController/deleteFile?url="+img, // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					this.$message({
							type: 'success',
							message: "删除成功"
						});
				})
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
				axios({
					hostUrl: this.global.hostUrl,
					method: 'post',
					data: formData,
					url: '/FileUploadController/uploadFile', // 请求地址
				}).then((res) => {
					if( typeof res != "string" ) {
						this.$message.error(res.message);
						return;
					}
					this.$message({
						message: "上传成功",
						duration: 1000,
						type: "success"
					});
					this.dialogVisible = false;
					this.isAgainChange = true; // 当前上传状态
					if ( this.size == "1024" ) {
						this.willDeleteImg = this.tableData[0]["imgsrc"];
						this.tableData[0]["imgsrc"] = res;
					} else {
						this.willDeleteImg = this.tableData[1]["imgsrc"];
						this.tableData[1]["imgsrc"] = res;
					}
					this.saveHandler();
				})
			},
			//图片上传限制
			beforeAvatarUpload(file) {
				let _this = this
				if(file.type.indexOf('image') < 0) {
					this.$alert({
						message: '图片格式不正确',
						btn: false
					})
					return false;
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
					let width = 600;
					let height = _this.size;
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
					_this.$alert('上传的图片大小必须是等于600*'+ _this.size +'!');
					return Promise.reject();
				});
				return isSize
			},
			saveHandler(){ // 点击上传图片的确定按钮
				this.deleteUpload(this.willDeleteImg);
				if ( this.size == "1024" ) {
					this.newDeviceImg(this.tableData[0]);
				} else {
					this.newFyImg(this.tableData[1]);
				}
			},
			newDeviceImg(obj){ // 新增保存工控机图片
				axios({
					hostUrl: this.global.hostUrl85,
					method: 'post',
					data: {
						id: obj.id,
						imgsrc: obj.imgsrc
					},
					url: "/baseDvhomeimg/save", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
				})
			},
			newFyImg(obj){ // 复印流程图的图片
				axios({
					hostUrl: this.global.hostUrl85,
					method: 'post',
					data: {
						id: obj.id,
						imgsrc: obj.imgsrc
					},
					url: "/baseDvfyimg/save", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
				})
			}
		}
	}
</script>

<style scoped="scoped" lang="scss">
.tableImg{
	width: 80px;
}
</style>
