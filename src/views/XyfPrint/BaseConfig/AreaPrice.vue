<template>
	<div style="height: 100%;text-align: left;">
		<el-dialog :title="title" width="635px" :visible.sync="dialogVisible">
			<el-form ref="form" style="margin-left: 20px;" label-position="top" :model="form" label-width="120px" :rules="rules">
				<el-form-item label="区域名称" prop="areaname">
					<div style="width: 50%;display: flex;">
						<el-input :disabled="isEdit" v-model="form.areaname" placeholder="请输入区域名称"></el-input>
					</div>
				</el-form-item>
				<el-form-item label="纸张规格" prop="papername">
					<div style="width: 50%;display: flex;">
						<el-select :disabled="isEdit" style="width:100%;" @change="pageChange" v-model="form.papername" placeholder="请选择">
							<el-option v-for="item in papeList" :key="item.id" :label="item.papername" :value="item.papername">
							</el-option>
						</el-select>
					</div>
				</el-form-item>
				<el-form-item label="计费标准（区域价格 = 标准价 + 系数）">
					<div id="stationPrice">
						<!--<div class="stationPriceTit"><span style="color: red;"></span><span>计费标准</span></div>-->
						<div class="kindOil">
							<div class="kindOilTit stationPriceTit">
								<div>颜色类型</div>
								<div>黑白（单面）</div>
								<div>黑白（双面）</div>
								<div>彩色（单面）</div>
								<div>彩色（双面）</div>
							</div>
							<div class="kindOilTit standerPrice">
								<div>标准价(元)</div>
								<div>{{selectItem.blacksingle}}</div>
								<div>{{selectItem.blackdouble}}</div>
								<div>{{selectItem.colorsingle}}</div>
								<div>{{selectItem.colordouble}}</div>
							</div>
							<div class="Oilcontent">
								<div>系数(元)</div>
								<div>
									<el-input size="small" @blur="moneyValidate" v-model="form.blacksingle" placeholder="0.00"></el-input>
								</div>
								<div style="width: 137px;">
									<el-input size="small" @blur="moneyValidate" v-model="form.blackdouble" placeholder="0.00"></el-input>
								</div>
								<div style="width: 137px;">
									<el-input size="small" @blur="moneyValidate" v-model="form.colorsingle" placeholder="0.00"></el-input>
								</div>
								<div>
									<el-input size="small" @blur="moneyValidate" v-model="form.colordouble" placeholder="0.00"></el-input>
								</div>
							</div>
						</div>
					</div>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer" style="margin-right: 20px;">
				<el-button size="mini" @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" :loading="loading" size="mini"  @click="newPapersConfirm">保存</el-button>
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
	import xyjtools from "@/utils/toolutil.js"
	export default {
		name: 'AreaPrice',
		data() {
			return {
				dialogVisible: false, // 弹出框的显示隐藏
				EditBtnDisabled: true,
				deleteBtnDisabled: false,
				title: '新增区域计费标准',
				papeList: [], // 纸张规格
				form: { // 弹框当中的表单数据
					papername: "",
					areaname: "",
					blacksingle: "",
					blackdouble: "",
					colorsingle: "",
					colordouble: "",
				},
				rules: { // 弹框当中的表单规则
					papername: [{
						required: true,
						message: "请输入纸张规格",
						trigger: 'blur'
					}],
					areaname: [{
						required: true,
						message: "请输入纸张规格",
						trigger: 'change'
					}]
				},
				isEdit: false, // 当前是否是编辑状态
				selectRow: [],
				selectItem: {
					blacksingle: "0.00",
					blackdouble: "0.00",
					colorsingle: "0.00",
					colordouble: "0.00",
				},
				loading: false, // 按钮的弹出框
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
					hasPermission: true,
					hostUrl: this.global.baseUrl,
					tableName: 'BaseAreaSubt',
					sorter: [{
						direction: "ASC",
						property: "id"
					}],
					 //表格中的操作列设置
	                operationAttrs:{
	                  width: "100px"
	                },
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "/baseAreaSubt/query",
						method: 'post',
						notCondition: true //没有查询条件则查询全部数据
					},
					//业务数据微服务,新增网址
					bNewUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/baseAreaSubt/save",
						method: 'post',
						clear: true, //清除新增对话框输入的数据
						close: true, //关闭新增对话框
					},
					//业务数据微服务,修改网址
					bEditUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/baseAreaSubt/save",
						method: 'post',
						//clear: true, //清除新增对话框输入的数据
						close: true //关闭修改对话框
					},
					//业务数据微服务,删除网址
					bDeleteUrl: {
						hostUrl: this.global.hostUrl85,
						url: "/baseAreaSubt/delete",
						method: 'post'
					},
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.EditBtnDisabled = false;
								this.DeletePaperDisabled = false;
								this.selectRow = selection[0];
							} else {
								this.EditBtnDisabled = true;
								this.DeletePaperDisabled = true;
								this.selectRow = {};
							}
						},
						'select-all': (selection) => {
							this.EditBtnDisabled = true;
						},

					}
				}
			}
		},
		methods: {
			moneyValidate(event) {
				if(!xyjtools.validateMoney(event.target.value)) {
					return;
				}
			},
			newPapers() { // 新增按钮点击
				for(let key in this.form) {
					this.form[key] = "";
					if ( this.selectItem[key] ) {
						this.selectItem[key] = "0.00";
					}
				}
				this.title = "新增区域计费标准";
				if(this.$refs.form) {
					this.$refs.form.resetFields();
				}
				this.formpapername = "";
				this.isEdit = false;
				delete this.form['id'];
				this.form.papername = "";
				this.form.areaname = "";
				this.dialogVisible = true;
			},
			editPapers() { // 编辑按钮点击
				this.dialogVisible = true;
				this.title = "编辑区域计费标准";
				this.isEdit = true;
				for(let key in this.selectRow) {
					if(this.form.hasOwnProperty(key)) {
						this.form[key] = this.selectRow[key];
					}
				}
				this.pageChange(this.form.papername);
				this.form.id = this.selectRow.id;
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
						url: "/baseAreaMain/delete", // 请求地址
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
					url: "/basePrice/query", // 请求地址
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
			newPapersConfirm() { // 弹框确定按钮
				this.$refs.form.validate((valid) => {
					if(!valid) {
						return false;
					}
					for(let key in this.form) {
						if( key == "colordouble" || key == "colorsingle" || key == "blackdouble" || key == "blacksingle" ) {
							if(!xyjtools.validateMoney(this.form[key])) {
								return;
							}
						}
					}
					console.log( this.form);
					this.loading = true;
					axios({
						hostUrl: this.global.hostUrl85,
						method: 'post',
						url: "/baseAreaSubt/save", // 请求地址
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
			pageChange(item){ // 纸张规格改变的时候
				for ( let i = 0; i < this.papeList.length; i++ ) {
					if ( this.papeList[i].papername == item ) {
						var data = this.papeList[i];
						for ( let key in data ) {
							if(key == "colordouble" || key == "colorsingle" || key == "blackdouble" || key == "blacksingle" ) {
								this.selectItem[key] = data[key];
							}
						}
						return;
					}
				}
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
		height: 123px;
		border: 1px solid #E9E9E9;
		text-align: center;
		box-sizing: border-box;
		border-bottom: 0;
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
		border: 1px solid #E9E9E9;
		border-top: 0;
		border-right: none;
	}
	
	.Oilcontent>div:nth-of-type(1) {
		border-left: none;
	}
	
	.Oilcontent /deep/ input {
		width: 80%;
		margin-top: 5px;
		padding-left: 10px;
		background: none;
		outline: none;
	}
</style>