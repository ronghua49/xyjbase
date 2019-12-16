<template>
	<div>
		<el-dialog
			v-dialogDrag
			title="机构选择"
			:close-on-click-modal="false"
        :visible.sync="dialogVisible"
        center
        width="600px"
        append-to-body>
        <el-row>
        	<el-col :span="24" style="color: red;margin-bottom: 10px;">双击机构名称选择机构</el-col>
        </el-row>
        <el-row type="flex" justify="space-between">
        	<el-col :span="16" style="height: 400px;overflow-y: auto;">
        		<el-input
						  placeholder="输入关键字进行过滤"
						  v-model="filterText">
						</el-input>
						<el-tree
							:data="data"
							node-key="id"
						  :props="props"
						  ref="tree"
						  :filter-node-method="filterNode"
						  :check-on-click-node="true"
						  :check-strictly="true"
						  @check-change="change">
						</el-tree>
        	</el-col>
        	<el-col :span="7" style="border-left: 1px solid #ccc;box-sizing: border-box;padding-left: 10px;height: 400px;overflow-y: auto;">
        		<el-button type="text" @click="chooseList = []">清空</el-button>
        		<span style="display: block;">已选机构</span>
        		<el-divider></el-divider>
        		<el-tooltip effect="dark" content="双击移除当前已选择的机构" placement="bottom-start" v-for="(item,index) in chooseList" :key="item.id">
        			<span style="display: block;cursor: pointer;" @dblclick="removePost(item)">{{ (index+1)+'、'+item.name }}</span>
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
	export default{
		name:'xyjdeptDialog',
		props:{
			cancel:{
				type:Function,
			},
			confirm:{
				type:Function
			},
			/** 
			 * @date 2019-10-12
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 是否是单选 true为单选，false为多选
			 * @author 张俊娜 
			 */
			isRadio:{
				type:Boolean,
				default:false
			}
		},
		watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      },
    },
		data(){
			return {
				filterText: '',
				dialogVisible:false,
				props: {
          label: 'name',
          children: 'children'
       },
        data:[],
        chooseList:[],
        /** 
				 * @date 2019-10-15
				 * @param {Object} str
				 * @return {Boolean} 返回值 
				 * 自定义双击事件需要的时间值
				 * @author 张俊娜 
				 */
        lastSelectTime:null
			}
		},
		methods:{
			// 筛选时的执行方法
			filterNode(value, data) {
					if(!value) return true;
					return data.name.indexOf(value) !== -1;
				},
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
					this.getTreeValue();
					this.chooseList = [];
					this.$nextTick(() => {
								let that = this;
								if(res && res.length > 0) {
									this.formatter(this.data, res)
									let hash = {};
									this.chooseList = this.chooseList.reduce((preval, curval) => {
												hash[curval.id] ?'' : hash[curval.id] = true && preval.push(curval);return preval},[]);
      		}else{
      			this.$refs.tree.setCheckedNodes([])
      		}
      	})
      	this.dialogVisible = true;
      },
      change(data,isChecked){
      	let time = new Date().getTime();
				let t = time - this.lastSelectTime;
      	// 节点选中状态发生变化
      	if (t < 500) {
      		if(this.isRadio){
	      		this.chooseList.length = 0;
	      		this.chooseList.push(data);
	      	}else{
	      		this.chooseList.push(data);
	    			let hash = {};
						this.chooseList = this.chooseList.reduce((preval,curval) => {hash[curval.id] ? '' : hash[curval.id] = true && preval.push(curval);return preval},[]);
	      	}
      	}
      	this.lastSelectTime = new Date().getTime();
      },
      /** 
			 * @date 2019-10-12
			 * @param {Object} str
			 * @return {Boolean} 返回值 
			 * 单击删除已选机构
			 * @author 张俊娜 
			 */
      removePost(item){
      	this.chooseList = this.chooseList.filter(data => data != item)
      },
      // 取消逻辑
			dialogCancel(){
				// 取消时外部定义的函数
				if(this.cancel){
					this.cancel(this.chooseList)
				}
				this.dialogVisible = false;
			},
			// 确定逻辑
			dialogConfirm(){
			  // 使选中的数据在外部可以编辑
				if(this.confirm){
					this.confirm(this.chooseList)
				}
				this.dialogVisible = false;
			},
			getTreeValue(){
				// 获取数据
				this.$api.dept.findDeptTree().then((res) => {
					this.data = res.data
	      })
			}
		},
	}
</script>

<style scoped>
	/deep/ .el-dialog__footer {
    text-align: right!important;
  }
  /deep/ .el-tree{
  	width: 366px;
  	overflow: auto;
  }
</style>