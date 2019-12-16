<template>
	<div style="height: 100%;text-align: left;">
		<el-dialog :title="title" width="635px" :visible.sync="dialogVisible">
			<el-form style="padding-left: 20px;" ref="form" label-position="top" :model="form" label-width="120px" :rules="rules">
				<el-form-item label="纸张规格" prop="papername">
					<el-select v-model="form.papername" placeholder="请选择">
						<el-option v-for="item in papeList" :key="item.id" :label="item.papername" :value="item.papername">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="计费标准">
					<div id="stationPrice">
						<!--<div class="stationPriceTit"><span style="color: red;"></span><span></span></div>-->
						<div class="kindOil">
							<div class="kindOilTit stationPriceTit">
								<div>黑白（单面）</div>
								<div>黑白（双面）</div>
								<div>彩色（单面）</div>
								<div>彩色（双面）</div>
							</div>
							<div class="Oilcontent">
								<div>
									<el-input size="small" maxlength="10" @blur="moneyValidate" v-model="form.blacksingle" placeholder="0.00"></el-input>
								</div>
								<div style="width: 137px;">
									<el-input size="small" maxlength="10" @blur="moneyValidate" v-model="form.blackdouble" placeholder="0.00"></el-input>
								</div>
								<div style="width: 137px;">
									<el-input size="small" maxlength="10" @blur="moneyValidate" v-model="form.colorsingle" placeholder="0.00"></el-input>
								</div>
								<div>
									<el-input size="small" maxlength="10" @blur="moneyValidate" v-model="form.colordouble" placeholder="0.00"></el-input>
								</div>
							</div>
						</div>
					</div>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer" style="margin-right: 20px;">
				<el-button size="mini" @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" :loading="loading" size="mini"  @click="newPaperConfirm">保存</el-button>
			</span>
		</el-dialog>
		<xyj-table ref="deviceTable" id="1" v-bind="tableConfig">
			<template slot="xyjcontainer">
				<Xyj-button type="primary" size="mini" style="padding: 7px;" @click="newPaper" :label="$t('xyfbaseSet.add')" text="新增"></Xyj-button>
				<Xyj-button type="primary" size="mini" @click="editPaper" :disabled="EditPaperDisabled" :label="$t('xyfbaseSet.edit')" text="编辑"></Xyj-button>
				<!--				<Xyj-button type="danger" size="mini" @click="deletePaper" :disabled="DeletePaperDisabled" :label="$t('xyfbaseSet.delete')" text="删除"></Xyj-button>-->
			</template>
		</xyj-table>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	import xyjtools from "@/utils/toolutil.js"
	export default {
		name: 'ChargeStander',
		data() {
			return {
				dialogVisible: false, // 弹框的显示和隐藏
				title: '新增计费标准',
				form: {
					colordouble: "",
					colorsingle: "",
					blackdouble: "",
					blacksingle: "",
					papername: "",
				},
				papeList: [], // 纸张列表
				rules: {
					papername: [{
						required: true,
						trigger: 'change',
						message: '纸张规格不能为空',
					}]
				},
				loading: false, // 弹框的loading
				EditPaperDisabled: true, // 编辑按钮禁用状态
				selectRow: {}, // 选中的那一行数据
				DeletePaperDisabled: true, // 删除按钮的禁用状态
				tableConfig: {
					hasDelete: true, //删除
					hasNew: false, //新增
					hasView: false, //查看
					hasEdit: false, //编辑
					paginationSize: 20,
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					hasPagination: true,
					hostUrl: this.global.baseUrl,
					tableName: 'BasePrice',
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
						url: "/basePrice/query",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
					//业务数据微服务,新增网址
					bNewUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/basePrice/save",
						method: 'post',
						clear: true, //清除新增对话框输入的数据
						close: true, //关闭新增对话框
					},
					//业务数据微服务,修改网址
					bEditUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/basePrice/save",
						method: 'post',
						//clear: true, //清除新增对话框输入的数据
						close: true //关闭修改对话框
					},
					//业务数据微服务,删除网址
					bDeleteUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/basePrice/delete",
						method: 'post'
					},
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.EditPaperDisabled = false;
								this.DeletePaperDisabled = false;
								this.selectRow = selection[0];
							} else {
								this.EditPaperDisabled = true;
								this.DeletePaperDisabled = true;
								this.selectRow = {};
							}
						},
						'select-all': (selection) => {
							this.EditPaperDisabled = true;
						},
					}
				}
			}
		},
		methods: {
			moneyValidate(event) {
				if(!xyjtools.validateMoney(event.target.value)) {
					event.target.value = "";
					return;
				}
			},
			newPaper() { // 新增纸张
				this.dialogVisible = true;
				this.title = "新增计费标准"; 
				if(this.$refs.form) {
					this.$refs.form.resetFields();
				}
				for(let key in this.form) {
					this.form[key] = "";
				}
				delete this.form.id;
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
			},
			newPaperConfirm() { //弹框的保存按钮
				this.$refs.form.validate((valid) => {
					if(!valid) {
						return false;
					}
					for(let key in this.form) {
						if(key != "papername" && key != "id") {
							if(!xyjtools.validateMoney(this.form[key])) {
								return;
							}
						}
					}
					this.loading = true;
					axios({
						hostUrl: this.global.hostUrl85,
						method: 'post',
						url: "/basePrice/save", // 请求地址
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
						this.EditPaperDisabled = true;
						this.DeletePaperDisabled = true;
					}).catch((err) => {
						this.loading = false;
						this.$message.error(err.message);
					});
				})
			},
			editPaper() { // 弹框的编辑按钮事件
				this.dialogVisible = true;
				this.title = "编辑计费标准";
				if(this.$refs.form) {
					this.$refs.form.resetFields();
				}
				for(let key in this.selectRow) {
					if(this.form.hasOwnProperty(key)) {
						this.form[key] = this.selectRow[key];
					}
				}
				this.form.id = this.selectRow.id;
			},
			deletePaper() { // 删除按钮点击
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
						url: "/basePrice/delete", // 请求地址
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
						this.EditPaperDisabled = true;
						this.DeletePaperDisabled = true;
					}).catch((err) => {
						this.$message.error(err.message);
					});
				}).catch(() => {

				});
			}
		},
		mounted() {
			this.getPapers();
		}
	}
</script>

<style scoped="scoped">
	#stationPrice {
		width: 550px;
	}
	.stationPriceTit {
		width: 548px;
		height: 40px;
		text-align: center;
		color: #000000;
		background: #F3F3F3;
		line-height: 40px;
	}
	
	.stationPriceTit>span:nth-of-type(1) {
		color: red;
	}
	
	.kindOil {
		width: 550px;
		height: 83px;
		border: 1px solid #E9E9E9;
		text-align: center;
		box-sizing: border-box;
	}
	
	.kindOilTit {
		display: flex;
		height: 40px;
		color: #404040;
	}
	
	.kindOilTit>div {
		width: 1368px;
		height: 40px;
		line-height: 40px;
		border-right: 1px solid #E9E9E9;
		border-bottom: 1px solid #E9E9E9;
	}
	
	.kindOilTit>div:nth-last-of-type(1) {
		border-right: none!important;
	}
	
	.Oilcontent {
		display: flex;
	}
	
	.Oilcontent>div {
		width: 138px;
		height: 40px;
		border-right: 1px solid #E9E9E9;
	}
	
	.Oilcontent>div:nth-last-of-type(1) {
		border-right: none!important;
	}
	
	.Oilcontent /deep/ input {
		width: 80%;
		margin-top: 5px;
		padding-left: 10px;
		background: none;
		outline: none;
	}
</style>