<template>
	<div style="height: 100%">
		<el-dialog width="20%" title="纸张标签" :visible.sync="dialogVisible">
			<el-form ref="form" :model="form" label-width="100px" :rules="rules">
				<el-form-item label="纸张规格" prop="papername" style="margin-bottom: 0;">
					<div style="width: 100%;">
						<el-select style="width: 100%;" size="small" v-model="form.papername" placeholder="请输入纸张规格">
						    <el-option
						      v-for="item in paperList"
						      :key="item.id"
						      :label="item.papername"
						      :value="item.papername">
						    </el-option>
						</el-select>
						<!--<el-input size="small" maxlength='10' v-model="form.papername" placeholder="请输入纸张规格"></el-input>-->
					</div>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
			    <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
			    <el-button type="primary" :loading="loading" size="mini" @click="newPaperConfirm">确 定</el-button>
		    </span>
		</el-dialog>
		<xyj-table ref="deviceTable" id="1" v-bind="tableConfig">
			<template slot="xyjcontainer">
				<Xyj-button type="primary" size="mini" @click="newPapers" :label="$t('xyfbaseSet.add')" text="新增"></Xyj-button>
				<Xyj-button type="primary" size="mini" @click="editPapers" :disabled="EditBtnDisabled" :label="$t('xyfbaseSet.edit')" text="编辑"></Xyj-button>
				<!--<Xyj-button type="danger" size="mini" @click="deletePapers" :disabled="deleteBtnDisabled" :label="$t('xyfbaseSet.delete')" text="删除"></Xyj-button>-->
			</template>
		</xyj-table>
	</div>
</template>

<script>
	import axios from "@/http/axios";

	export default {
		name: 'PaperLabel',
		data() {
			return {
				paperList:[],
				loading: false,
				dialogVisible: false, //弹框显示隐藏
				EditBtnDisabled: true, // 编辑按钮禁用
				deleteBtnDisabled: true, // 删除按钮禁用
				form: {
					papername: ""
				},
				rules: {
					papername: [{
						required: true,
						message: "请输入纸张规格",
						trigger: 'blur'
					}]
				},
				tableConfig: {
					hasDelete: true, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					hasPagination: true,
					hasPermission: true,
					hasOperation: true,
					paginationSize: 20,
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					hostUrl: this.global.baseUrl,
					tableName: 'BasePapertype',
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
						}
					},
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
					},
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "/basePapertype/query",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
					operationAttrs: {
						width: "100px"
					},
					//业务数据微服务,新增网址
					bNewUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/basePapertype/save",
						method: 'post',
						clear: true, //清除新增对话框输入的数据
						close: true, //关闭新增对话框
					},
					//业务数据微服务,修改网址
					bEditUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/basePapertype/save",
						method: 'post',
						//clear: true, //清除新增对话框输入的数据
						close: true //关闭修改对话框
					},
					//业务数据微服务,删除网址
					bDeleteUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/basePapertype/delete",
						method: 'post'
					},
					//当点击修改时，默认修改调用前处理上传数据时
					onManualEdit: this.reftableEdit,
					//当点击新增时，默认新增调用前处理上传数据时
					onManualNew: this.reftableNew
				}
			}
		},
		mounted() {
			this.getPpaer();
		},
		methods: {
			reftableEdit(data, row) { // 点击编辑之后处理的数据
				if(data) {
					data.id = row.id;
				}
			},
			reftableNew() { // 新增时候处理的数据

			},
			getPpaer() {
				axios({
					hostUrl: this.global.hostUrl86,
					url: '/baseConfigPapername/query',
					method: 'post',
					data: {
						"pageBean": {
							"page": 0,
							"pageSize": 0,
							"showTotal": true,
						}
					}
				}).then((res) => {
					this.paperList = res.data.rows;
				}).catch(e=>{
					this.$message.error(e.message);
				})
			},
			newPaperConfirm() { // 弹框当中的新增
				let self = this;
				this.$refs.form.validate((valid) => {
					if(!valid) {
						return false;
					}
					this.loading = true;
					axios({
						hostUrl: this.global.hostUrl85,
						method: 'post',
						url: "/basePapertype/save", // 请求地址
						data: this.form
					}).then((res) => {
						self.loading = false;
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
						self.loading = false;
						this.$message.error(err.message);
					});
				})
			},
			newPapers() { // 新增按钮点击
				this.dialogVisible = true;
				if(this.$refs.form) {
					this.$refs.form.resetFields();
				}
				this.form.papername = "";
				delete this.form.id;
			},
			editPapers() { // 编辑按钮点击
				this.dialogVisible = true;
				this.form.id = this.selectRow.id;
				this.form.papername = this.selectRow.papername;
			},
			deletePapers() { // 删除按钮点击
				this.$confirm('确定删除吗？', {
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
						url: "/basePapertype/delete", // 请求地址
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
			}
		}
	}
</script>

<style>

</style>