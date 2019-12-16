<template>
	<div>
		<el-dialog v-dialogDrag title="岗位选择" :close-on-click-modal="false" :visible.sync="dialogVisible" center width="600px" append-to-body>
			<el-row>
				<el-col :span="24" style="color: red;margin-bottom: 10px;">双击岗位名称选择岗位</el-col>
			</el-row>
			<el-row type="flex" justify="space-between">
				<el-col :span="17" style="height: 400px;overflow-y: auto;">
					<el-input size="mini" placeholder="输入关键字进行过滤" v-model="filterText">
					</el-input>
					<el-tree :data="data" node-key="id" :props="props" ref="tree" :filter-node-method="filterNode" :check-on-click-node="true" :check-strictly="true" @check-change="change">
						<span class="custom-tree-node" slot-scope="{ node, data }">
				        <span>{{ node.label }}</span>
						<span>
				          <el-radio v-model="radio" :label="data.id" @change="(val)=>choose(val,data)">是否是主岗位</el-radio>
				        </span>
						</span>
					</el-tree>
				</el-col>
				<el-col :span="6" style="border-left: 1px solid #ccc;box-sizing: border-box;padding-left: 10px;height: 400px;overflow-y: auto;">
					<el-button type="text" @click="chooseList = [];radio = ''">清空</el-button>
					<span style="display: block;">已选岗位</span>
					<el-divider></el-divider>
					<el-tooltip effect="dark" content="双击移除当前已选择的岗位" placement="bottom-start" v-for="(item,index) in chooseList" :key="item.id">
						<span style="display: block;cursor: pointer;" @dblclick="removePost(item)">{{ (index+1)+'、'+item.name }} <span v-html="radio == item.id ? '(主岗位)' : ''"></span></span>
					</el-tooltip>
				</el-col>
			</el-row>
			<span slot="footer" class="dialog-footer">
		    <el-button size="mini" @click="dialogCancel">{{ $t('action.cancel') }}</el-button>
		    <el-button size="mini" type="primary" @click="dialogConfirm">{{ $t('action.comfirm') }}</el-button>
		  </span>
		</el-dialog>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default {
		name: 'BranchPostDialog',
		props: {
			cancel: {
				type: Function,
			},
			confirm: {
				type: Function
			},
			/** 
			 * @date 2019-10-11
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 是否是单选 true为单选，false为多选
			 * @author 张俊娜 
			 */
			isRadio: {
				type: Boolean,
				default: false
			}
		},
		watch: {
			filterText(val) {
				this.$refs.tree.filter(val);
			},
		},
		data() {
			return {
				filterText: '',
				dialogVisible: false,
				props: {
					label: 'name',
					children: 'children'
				},
				data: [],
				radio: '',
				chooseList: [],
				/** 
				 * @date 2019-10-15
				 * @param {Object} str
				 * @return {Boolean} 返回值 
				 * 自定义双击事件需要的时间值
				 * @author 张俊娜 
				 */
				lastSelectTime: null
			}
		},
		methods: {
			// 筛选时的执行方法
			filterNode(value, data) {
				if(!value) return true;
				return data.name.indexOf(value) !== -1;
			},
			/**
			 *宋建国：2019/10/11 10:18
			 *描述：
			 * val:true: 编辑，false：新增，不传入此参数则为新增
			 *版本：1.0
			 */
			formatter(data, res) {
				for(let value of data) {
					if(value.children && value.id) {
						for(let k of res) {
							if(k.id == value.id) {
								this.chooseList.push(value)
							}
						}
						this.formatter(value.children, res)
					}
				}
			},
			open(isReload, res) {
				this.getTreeValue()
				this.chooseList = [];
				this.$nextTick(() => {
					// 新增的时候没有默认数据
					if(res && res.length > 0) {
						this.formatter(this.data, res)
						let hash = {};
						this.chooseList = this.chooseList.reduce((preval, curval) => {
							hash[curval.id] ? '' : hash[curval.id] = true && preval.push(curval);
							return preval
						}, []);
					} else {
						this.$nextTick(()=>{
							this.$refs.tree.setCheckedNodes([]);
							this.chooseList.length = 0;
							this.radio = ''
						})
					}
					this.dialogVisible = true;
				})
			},
			// 选择主岗位
			choose(val, data) {
				this.chooseList.push(data)
				let hash = {};
				this.chooseList = this.chooseList.reduce((preval, curval) => {
					hash[curval.id] ? '' : hash[curval.id] = true && preval.push(curval);
					return preval
				}, []);
			},
			change(data, isChecked) {

				let time = new Date().getTime();
				let t = time - this.lastSelectTime;
				if(t < 500) {
					if(this.isRadio) {
						this.chooseList.length = 0;
						this.chooseList.push(data);
					} else {
						this.chooseList.push(data);
						let hash = {};
						this.chooseList = this.chooseList.reduce((preval, curval) => {
							hash[curval.id] ? '' : hash[curval.id] = true && preval.push(curval);
							return preval
						}, []);
					}
				}

				this.lastSelectTime = new Date().getTime();
			},

			/** 
			 * @date 2019-10-11
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 单击删除已选岗位
			 * @author 张俊娜 
			 */
			removePost(item) {
				this.chooseList = this.chooseList.filter(data => data != item)
				if(!this.chooseList.some(({
						id
					}) => id == this.radio)) this.radio = ''
			},
			// 取消逻辑
			dialogCancel() {
				// 取消时外部定义的函数
				if(this.cancel) {
					this.cancel(this.chooseList)
				}
				this.dialogVisible = false;
			},
			// 确定逻辑
			dialogConfirm() {
				if(!this.radio && this.chooseList.length > 0) {
					this.$message({
						message: '请选择主岗位',
						type: 'warning'
					});
				} else {
					// 使选中的数据在外部可以编辑
					if(this.confirm) {
						this.confirm(this.chooseList, this.radio)
					}
					this.dialogVisible = false;
				}
			},
			getTreeValue() {
				// 获取数据
				axios({
						// 用axios发送post请求
						hostUrl: this.baseUrl,
						method: "get",
						url: "/sysBranchPost/findTree?branchCode="+sessionStorage.getItem('branchCode')
					})
					.then(res => {
						if(res.code == 200) {
							this.data = res.data;
						};
					})
					.catch(err => {
						console.log(err)
					})
			},
		},
	}
</script>

<style scoped>
	/deep/ .el-dialog__footer {
		text-align: right!important;
	}
	
	.custom-tree-node {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 14px;
		padding-right: 8px;
	}
</style>