<template>
	<div style="height: 100%">
		<el-dialog width="650px" title="设备信息" :visible.sync="dialogVisible">
			<el-form ref="form" :model="form" label-width="120px" :rules="rules">
				<el-form-item label="设备型号" prop="model">
					<div style="width: 47%;">
						<el-input size="small" maxlength="20" v-model="form.model" placeholder="请输入设备型号"></el-input>
					</div>
				</el-form-item>
				<el-form-item label="设备名称" prop="name">
					<div style="width: 47%;">
						<el-input size="small" maxlength="20" v-model="form.name" placeholder="请输入设备名称"></el-input>
					</div>
				</el-form-item>
				<el-form-item label="颜色类型" prop="colortype">
					<div style="display: flex;justify-content: flex-start;">
						<el-radio-group v-model="form.colortype" size="small">
							<el-radio style="width: 100px;" label="黑白" border>黑白</el-radio>
							<el-radio style="width: 100px;margin-left: 0;" label="黑白/彩色" border>黑白/彩色</el-radio>
						</el-radio-group>
					</div>
				</el-form-item>
				<el-form-item label="是否支持双面" prop="doublesidetype">
					<div style="display: flex;justify-content: flex-start;">
						<el-radio-group v-model="form.doublesidetype" size="small">
							<el-radio style="width: 100px;" :label="1" border>是</el-radio>
							<el-radio style="width: 100px;margin-left: 0;" :label="0" border>否</el-radio>
						</el-radio-group>
					</div>
				</el-form-item>
				<el-form-item label="支持打印幅面" prop="papertype">
					<div style="display: flex;justify-content: flex-start;">
						<el-checkbox-group v-model="papertype" size="small" style="display: flex;justify-content: flex-start;flex-wrap: wrap;">
							<el-checkbox @change="papeChange" style="width: 100px;margin-bottom: 10px;margin-left: 0;" :label="item.papername" border :key="item.id" v-for="(item) in papeList" :disabled="item.papername == 'A4'"></el-checkbox>
						</el-checkbox-group>
					</div>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
			    <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
			    <el-button type="primary" :loading="loading" size="mini"  @click="newDeviceConfirm">确 定</el-button>
			  </span>
		</el-dialog>
		<xyj-table ref="deviceTable" id="1" v-bind="tableConfig">
			<template slot="xyjcontainer">
				<Xyj-button type="primary" size="mini" @click="newDevice" :label="$t('xyfbaseSet.add')" text="新增"></Xyj-button>
				<Xyj-button type="primary" size="mini" @click="editDevice" :disabled="EditBtnDisabled" :label="$t('xyfbaseSet.edit')" text="编辑"></Xyj-button>
				<!--<Xyj-button type="danger" size="mini" @click="deleteDevice" :disabled="deleteBtnDisabled" :label="$t('xyfbaseSet.delete')" text="删除"></Xyj-button>-->
			</template>
		</xyj-table>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: 'DeviceDict',
		data() {
			return {
				tableConfig: {
					hasDelete: true, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					hasPagination: true,
					hasPermission: true,
					paginationSize: 20,
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					operationAttrs:{
	                  width: "100px"
	                },
					hostUrl: this.global.baseUrl,
					tableName: 'BaseDevice',
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					formatters: {
						lastUpdateBy: function(row, column, cellValue) {
							if(!cellValue) {
								return row['createBy'];
							} else {
								return cellValue;
							}
						},
						lastUpdateTime: function(row, column, cellValue) {
							if(!cellValue) {
								return row['createTime'];
							} else {
								return cellValue;
							}
						},
						doublesidetype: function(row, column, cellValue) {
							if(cellValue == "1") {
								return "支持";
							} else {
								return "不支持";
							}
						},
						colortype: function(row, colum, cellValue) {
							return cellValue;
						}
					},
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "/baseDevice/query",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
					//业务数据微服务,新增网址
					bNewUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/baseDevice/save",
						method: 'post',
						clear: true, //清除新增对话框输入的数据
						close: true, //关闭新增对话框
					},
					//业务数据微服务,修改网址
					bEditUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/baseDevice/save",
						method: 'post',
						//clear: true, //清除新增对话框输入的数据
						close: true //关闭修改对话框
					},
					//业务数据微服务,删除网址
					bDeleteUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/baseDevice/delete",
						method: 'post'
					},
					//当点击修改时，默认修改调用前处理上传数据时
					onManualEdit: this.reftableEdit,
					//当点击新增时，默认新增调用前处理上传数据时
					onManualNew: this.reftableNew,
					//点击删除的时候，需要处理数据
					onManualDelete: this.reftableDelete,
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.EditBtnDisabled = false;
								this.deleteBtnDisabled = false;
								this.selectRow = selection[0];
							} else {
								this.EditBtnDisabled = true;
								this.deleteBtnDisabled = true;
								this.selectRow = {};
							}
						},
						'select-all': (selection) => {
							this.EditBtnDisabled = true;
						},
					}
				},
				EditBtnDisabled: true, //编辑按钮是否禁用
				dialogVisible: false, // 弹出框的显示或者隐藏
				deleteBtnDisabled: false, // 删除按钮是否禁用
				form: { // dialog当中的form表单
					model: "",
					name: "",
					colortype: '黑白',
					doublesidetype: 1,
					papertype: ""
				},
				papertype: ['A4'],
				papeList: [],
				loading: false, //弹框按钮的加载状态
				selectRow: {}, // 当前勾选的数据
				rules: {
					model: [{
						required: true,
						message: '请输入设备型号',
						trigger: 'blur'
					}, ],
					name: [{
						required: true,
						message: '请输入设备名称',
						trigger: 'blur'
					}, ],
					colortype: [{
						required: true,
						message: '请选择颜色类型',
						trigger: 'change'
					}],
					doublesidetype: [{
						required: true,
						message: '请选择单/双面',
						trigger: 'change'
					}],
					papertype: [{
						required: true,
						message: '请选择支持的打印幅面',
						trigger: 'change'
					}]
				}
			}
		},
		methods: {
			newDevice() { // 新增按钮
				this.dialogVisible = true;
				if(this.$refs.form) {
					this.$refs.form.resetFields();
				}
				
				for(let key in this.form) {
					if(key == 'colortype') {
						this.form[key] == "黑白";
						continue;
					}
					if(key == "doublesidetype") {
						this.form[key] == 1;
						continue;
					}
					if(this.form.hasOwnProperty(key)) {
						this.form[key] = "";
					}
				}
				this.papertype = ["A4"];
				delete this.form.id;
			},
			newDeviceConfirm() { // 弹框确定按钮
				this.$refs.form.validate((valid) => {
					if(!valid) {
						return false;
					}
					this.loading = true;
					axios({
						hostUrl: this.global.hostUrl85,
						method: 'post',
						url: "/baseDevice/save", // 请求地址
						data: this.form
					}).then((res) => {
						this.loading = false;
						if(res.code != 200) {
							this.$message.error(res.message);
							return;
						}
						this.$message({
							type: 'success',
							message: "操作成功"
						});
						this.dialogVisible = false;
						this.$refs.deviceTable.getList();
						this.EditBtnDisabled = true;
						this.deleteBtnDisabled = true;
					}).catch((err) => {
						this.loading = false;
						this.$message.error(err.message);
					});
				})
			},
			papeChange(val) { // 支持打印幅面的change事件
				if ( val ) {
					
				}
				this.form.papertype = this.papertype.join(",");
			},
			reftableEdit(data, row) { // 点击编辑之后处理的数据
				if(data) {
					data.id = row.id;
				}
			},
			reftableNew() { // 新增时候处理的数据

			},
			reftableDelete(data) { // 删除的时候处理数据
				//let deleteId = data;
				//				deleteId = data.map(function(id){
				//					return {'id': id};
				//				});
				//				data = deleteId;
				//				data = [];
				//				data.push({
				//					'id': deleteId[0]
				//				});
			},
			editDevice() { // 编辑设备
				this.dialogVisible = true;
				if(this.$refs.form) {
					this.$refs.form.resetFields();
				}
				for(let key in this.selectRow) {
					if(key == 'papertype') {
						this.form[key] = this.selectRow[key];
						if(this.form[key]) {
							this.papertype = this.selectRow[key].split(",");
						}
						continue;
					}
					if(this.form.hasOwnProperty(key)) {
						this.form[key] = this.selectRow[key];
					}
				}
				this.form.id = this.selectRow.id;
			},
			deleteDevice() {
				this.$confirm('确定删除此设备？', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					let deleteArr = [];
					deleteArr.push({
						id: this.selectRow.id
					})
					axios({
						hostUrl: this.global.hostUrl85,
						method: 'post',
						data: deleteArr,
						url: "/baseDevice/delete", // 请求地址
					}).then((res) => {
						if(res.code != 200) {
							this.$message.error(res.message);
							return;
						}
						this.$message({
							type: 'success',
							message: "删除成功"
						});
						this.$refs.deviceTable.getList();
						this.EditBtnDisabled = true;
						this.deleteBtnDisabled = true;
					}).catch((err) => {
						this.$message.error(err.message);
					});
				}).catch(() => {

				});
			},
			getPapers() { // 弹框当中的纸张规格
				axios({
					hostUrl: this.global.hostUrl86,
					method: 'post',
					data: {},
					url: "/basePapertype/query", // 请求地址
				}).then((res) => {
					if(res.code != 200) {
						this.$message.error(res.message);
						return;
					}
					this.papeList = res.data.rows;
				}).catch((err) => {
					this.$message.error(err.message);
				});
			}
		},
		mounted() {
			this.getPapers();
		}
	}
</script>

<style scoped="scoped">
	.dialog>div {
		width: 670px;
	}
	
	.el-checkbox-group>label:nth-of-type(4n) {
		margin-right: 0;
	}
</style>