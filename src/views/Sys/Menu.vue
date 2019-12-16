<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<kt-button ref="kButton" icon="fa fa-refresh" :label="$t('action.refresh')" perms="sys:menu:refresh" type="primary" @click="findTreeData(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button ref="kButton" icon="fa fa-plus" :label="$t('action.add')" perms="sys:menu:add" type="primary" @click="handleAdd"/>
			</el-form-item>
		</el-form>
	</div>
	<!--表格树内容栏-->
    <el-table :data="tableTreeDdata" stripe size="mini" style="width: 100%;"
      v-loading="loading" element-loading-text="拼命加载中" row-key="id">
      <el-table-column
        prop="name" header-align="center" align="left" width="250" label="ID">
      </el-table-column>
       <!--<table-tree-column 
        prop="name" header-align="center" treeKey="id" width="150" label="名称">
      </table-tree-column> -->
      <el-table-column header-align="center" align="center" label="图标">
        <template slot-scope="scope">
          <i :class="scope.row.icon || ''"></i>
        </template>
      </el-table-column>
      <el-table-column prop="type" header-align="center" align="center" label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" size="small">目录</el-tag>
          <el-tag v-else-if="scope.row.type === 1" size="small" type="success">菜单</el-tag>
          <el-tag v-else-if="scope.row.type === 2" size="small" type="info">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column 
        prop="parentName" header-align="center" align="center" width="120" label="上级菜单">
      </el-table-column>
      <el-table-column
        prop="url" header-align="center" align="center" width="150" 
        :show-overflow-tooltip="true" label="菜单URL">
      </el-table-column>
      <el-table-column
        prop="perms" header-align="center" align="center" width="150" 
        :show-overflow-tooltip="true" label="授权标识">
      </el-table-column>
      <el-table-column
        prop="orderNum" header-align="center" align="center" label="排序">
      </el-table-column>
      <el-table-column
        fixed="right" header-align="center" align="center" width="185" :label="$t('action.operation')">
        <template slot-scope="scope">
          <kt-button ref="kButton" icon="fa fa-edit" :label="$t('action.edit')" perms="sys:menu:edit" @click="handleEdit(scope.row)"/>
          <kt-button ref="kButton" icon="fa fa-trash" :label="$t('action.delete')" perms="sys:menu:delete" type="danger" @click="handleDelete(scope.row)"/>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增修改界面 -->
    <el-dialog v-dialogDrag :title="!dataForm.id ? '新增' : '修改'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :before-close="handleClose">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="submitForm()" 
        label-width="80px" :size="size" style="text-align:left;">
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="dataForm.type">
            <el-radio v-for="(type, index) in menuTypeList" :label="index" :key="index">{{ type }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="menuTypeList[dataForm.type] + '名称'" prop="name">
          <el-input v-model="dataForm.name" :placeholder="menuTypeList[dataForm.type] + '名称'"></el-input>
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentName">
            <popup-tree-input 
              :data="popupTreeData" :props="popupTreeProps" :prop="dataForm.parentName==null||dataForm.parentName==''?'顶级菜单':dataForm.parentName" 
              :nodeKey="''+dataForm.parentId" :currentChangeHandle="handleTreeSelectChange">
            </popup-tree-input>
        </el-form-item>
        <el-form-item v-if="dataForm.type == 2" label="授权标识" prop="perms">
          <el-input v-if="dataForm.id" v-model="dataForm.perms" placeholder="如: sys:user:add, sys:user:edit, sys:user:delete"></el-input>
          <div v-else>
            <el-select v-loading="loading" element-loading-spinner="el-icon-loading" style="width:100%;" 
            	v-model="dataForm.perms" value-key="label" 
            	clearable placeholder="请选择"
            	@change="defaultName">
              <el-option
                v-for="(item,index) in data"
                :key="index"
                :label="item.label+'('+item.value+')'"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item v-if="dataForm.type === 1" label="菜单路由" prop="url">
          <el-input v-model="dataForm.url" @click.native="showMenuRoute" placeholder="菜单路由"></el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.type === 1" label="流程ID" prop="cid">
          <el-input v-model="dataForm.cid" placeholder="流程ID" @click.native="open"></el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.type === 1" label="流程名称" prop="processName">
          <el-input v-model="dataForm.processName" placeholder="流程名称"></el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.type !== 2" label="排序编号" prop="orderNum">
          <el-input-number v-model="dataForm.orderNum" controls-position="right" :min="0" label="排序编号"></el-input-number>
        </el-form-item>
        <el-form-item v-if="dataForm.type !== 2" label="菜单图标" prop="icon">
          <el-row>
            <el-col :span="22">
              <!-- <el-popover
                ref="iconListPopover"
                placement="bottom-start"
                trigger="click"
                popper-class="mod-menu__icon-popover">
                <div class="mod-menu__icon-list">
                  <el-button
                    v-for="(item, index) in dataForm.iconList"
                    :key="index"
                    @click="iconActiveHandle(item)"
                    :class="{ 'is-active': item === dataForm.icon }">
                    <icon-svg :name="item"></icon-svg>
                  </el-button>
                </div>
              </el-popover> -->
              <el-input v-model="dataForm.icon" v-popover:iconListPopover :readonly="false" placeholder="菜单图标名称（如：fa fa-home fa-lg）" class="icon-list__input"></el-input>
            </el-col>
            <el-col :span="2" class="icon-list__tips">
              <fa-icon-tooltip />
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button :size="size"  @click="handleClose">{{$t('action.cancel')}}</el-button>
        <el-button :size="size"  type="primary" @click="submitForm()" :loading="editLoading">{{$t('action.comfirm')}}</el-button>
      </span>
    </el-dialog>
    <div>
      <compoent v-show="false" :is="comp" ></compoent>
      <!--工作流弹框-->
      <xyjworkflow-dialog ref="workflowDialog" v-bind="workflow"></xyjworkflow-dialog>
    </div>
    <menu-route ref="menuRoute" v-bind="menuRoutes"></menu-route>
  </div>
</template>

<script>
import KtButton from "@/views/Core/KtButton";
import PopupTreeInput from "@/components/PopupTreeInput";
import FaIconTooltip from "@/components/FaIconTooltip";
import xyjworkflowDialog from "@/components/xyjworkflowDialog/xyjworkflowDialog";
import menuRoute from "@/components/xyjMenuRouteDialog/xyjMenuRouteDialog";
export default {
  components: {
    PopupTreeInput,
    KtButton,
    FaIconTooltip,
    xyjworkflowDialog,
    menuRoute
  },
  computed: {
    comp: function () {
      if(this.url) return() => import(`@/views${this.url}`)
    }
  },
  data() {
    return {
      /**
       *宋建国：2019/8/1 9:06
       *描述：编辑确定时变量
       */
      editLoading: false,
      url:'', // 选择上级菜单后渲染选择的组件地址
      data:[], // 按钮下拉框数据
      size: "small",
      loading: false,
      filters: {
        name: ""
      },
      tableTreeDdata: [],
      dialogVisible: false,
      menuTypeList: ["目录", "菜单", "按钮"],
      dataForm: {
        id: 0,
        type: 1,
        name: "",
        parentId: 0,
        parentName: "",
        url: "",
        perms: "",
        orderNum: 0,
        icon: "",
        iconList: [],
        cid:'',
        processName:'',
        processId:''
      },
      dataRule: {
        name: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }]
      },
      popupTreeData: [],
      popupTreeProps: {
        label: "name",
        children: "children"
      },
      workflow:{
    		cancel:function(row){console.log(row)},
    		confirm:this.workflowdialog,
    		isRadio:true
    	},
    	menuRoutes:{
    		/** 
				 * @date 2019-10-12
				 * 员工弹框取消事件回调函数
				 * 一个参数，表示选中的数据，类型是数组
				 * @author 张俊娜 
				 */
    		cancel:function(row){console.log(row)},
    		/** 
				 * @date 2019-10-12
				 * 员工弹框确认事件回调函数
				 * 一个参数，表示选中的数据，类型是数组
				 * @author 张俊娜 
				 */
    		confirm:this.menuRouteConfirm,
    	},
    };
  },
  methods: {
    /**
     *宋建国：2019/8/1 10:42
     *描述：关闭新增与修改对话框事件
     */
    handleClose(){
      this.editLoading = false;
      this.dialogVisible = false
    },
  	defaultName(val){
  		console.log(val.split(':')[val.split(':').length-1])
  		this.dataForm.name = val.split(':')[val.split(':').length-1];
  	},
    // 获取数据
    findTreeData: function() {
      this.loading = true;
      this.$api.menu.findMenuTree().then(res => {
        this.tableTreeDdata = res.data;
        this.popupTreeData = this.getParentMenuTree(res.data);
        this.loading = false;
      });
    },
    // 获取上级菜单树
    getParentMenuTree: function(tableTreeDdata) {
      let parent = {
        parentId: 0,
        name: "顶级菜单",
        children: tableTreeDdata
      };
      return [parent];
    },
    // 显示新增界面
    handleAdd: function() {
    	this.editLoading = false;
      this.dialogVisible = true;
      this.data = [];
      this.dataForm = {
        id: 0,
        type: 1,
        typeList: ["目录", "菜单", "按钮"],
        name: "",
        parentId: 0,
        parentName: "",
        url: "",
        perms: "",
        orderNum: 0,
        icon: "",
        iconList: [],
        cid:'',
        processName:'',
        processId:''
      };
    },
    // 显示编辑界面
    handleEdit: function(row) {
    	this.editLoading = false;
      this.dialogVisible = true;
      Object.assign(this.dataForm, row);
    },
    // 删除
    handleDelete(row) {
      this.$confirm("确认删除选中记录吗？", "提示", {
        type: "warning"
      }).then(() => {
        let params = this.getDeleteIds([], row);
        this.$api.menu.batchDelete(params).then(res => {
          this.findTreeData();
          this.$message({ message: "删除成功", type: "success" });
        });
      });
    },
    // 获取删除的包含子菜单的id列表
    getDeleteIds(ids, row) {
      ids.push({ id: row.id });
      if (row.children != null) {
        for (let i = 0, len = row.children.length; i < len; i++) {
          this.getDeleteIds(ids, row.children[i]);
        }
      }
      return ids;
    },
    test(){
      let timer = null;
      function check (){
        // 获取表格里边的所有按钮
        let res = Array.from(document.querySelectorAll('.xyj-table .tableMain .XyjBtn'));
        // 如果有数据
        if ( res.length != 0  ) {
          // 清除定时器
          if ( timer ) {
            clearTimeout(timer);
          }
          // 返回表格的按钮
          return res;
        } else {
          // 如果没有拿到表格按钮继续
          timer = setTimeout(check,1);
        }
      }
      return check();
    },
    // 菜单树选中
    handleTreeSelectChange(data, node) {
      this.loading = true;
      let timer;
      this.url = data.url;
      this.dataForm.parentId = data.id;
      this.dataForm.parentName = data.name;
      clearTimeout(timer);
      timer = setTimeout(() => {
        // 获取按钮
        let btns = Array.from(document.querySelectorAll('.XyjBtn'));
        if( this.test() ) btns = btns.concat(this.test(data));
        // 拼接按钮授权规则
        let reg = /[\u4e00-\u9fa5]/g;
        btns = btns.map(x => {
          let obj = {};
          obj.label = x.dataset.perms.match(reg)?x.dataset.perms.match(reg).join(""):x.dataset.perms;
          obj.value = data.url.replace(/\//g,":").substr(1)+":"+x.dataset.perms;
          return obj;
        })
        // 去除多余的按钮
        var hash = {};
        btns = btns.reduce(function (item, next) {
          hash[next.value] ? '' : hash[next.value] = true && item.push(next);
          return item;
        }, []);
        this.data = btns;
        this.loading = false;
      },2000) 
      
    },
    // 图标选中
    iconActiveHandle(iconName) {
      this.dataForm.icon = iconName;
    },
    // 表单提交
    submitForm() {
    	let then = this
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
            this.editLoading = true;
            let params = Object.assign({}, this.dataForm);
            if(!this.dataForm.id && this.dataForm.url){
	          	this.url = this.dataForm.url
	          	let timer = null;
	          	clearTimeout(timer);
	          	timer = setTimeout(()=> {
	          		let btns = Array.from(document.querySelectorAll('.XyjBtn'));
	          		if( then.test() ) btns = btns.concat(then.test());
		          	params.buttons = btns.map(x => {
		          		let obj = {};
		          		obj.name = x.dataset.perms;
				          obj.perms = this.url.replace(/\//g,":").substr(1)+":"+x.dataset.perms;
				          return obj;
				        })
		          	var hash = {};
				        params.buttons = params.buttons.reduce(function (item, next) {
				          hash[next.perms] ? '' : hash[next.perms] = true && item.push(next);
				          return item;
				        }, []);
		          	this.submitSave(params)
	          	},2000)
	          }else { this.submitSave(params) }
        }
      });
    },
    submitSave(params){
    	this.$api.menu.save(params).then(res => {
	      if (res.code == 200) {
	        this.$message({ message: "操作成功", type: "success" });
	        this.$refs["dataForm"].resetFields();
	        this.dialogVisible = false;
	      } else {
	        this.$message({
	          message: "操作失败, " + res.msg,
	          type: "error"
	        });
	      }
	      this.editLoading = false;
	      this.data.length = 0;
	      this.findTreeData();
	    }).catch(err => {
	    	this.$message.error(err)
	    	this.editLoading = false;
	    });
    },
    workflowdialog(row){
			// 工作流信息弹框 row --> 选中的数据是一个数组
			this.dataForm.cid = row && row.length>0 ? row[0].key : '';
			this.dataForm.processName = row && row.length>0 ? row[0].name : '';
			this.dataForm.processId = row && row.length>0 ? row[0].id : '';
		},
		open(){
			this.dataForm.processId ? this.$refs.workflowDialog.open(true,[{id:this.dataForm.processId}]) : this.$refs.workflowDialog.open()
		},
		showMenuRoute(){
			!this.dataForm.url ? this.$refs.menuRoute.open(false) : this.$refs.menuRoute.open(false,[{"url":this.dataForm.url}])
		},
		menuRouteConfirm(data){
			this.dataForm.url = data.length>0 ? data[0].menuRoute :''
			this.dataForm.name = data.length > 0? data[0].menuName :''
		}
  },
  mounted() {
    this.findTreeData();
  }
};
</script>

<style scoped>
	
</style>
