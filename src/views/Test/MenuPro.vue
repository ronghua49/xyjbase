<template>
  <div>
    <el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.name" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item>
        <kt-button ref="kButton" icon="fa fa-search" :label="$t('action.search')" perms="sys:menu:view" type="primary"
                   @click="findTreeData(null)"/>
      </el-form-item>
      <el-form-item>
        <kt-button ref="kButton" icon="fa fa-plus" :label="$t('action.add')" perms="sys:menu:add" type="primary"
                   @click="handleAdd"/>
      </el-form-item>
    </el-form>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <span>这是一段信息</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

    <div class="menu-wrapper">
      <div style="width: 140px;" class="column-item">ID</div>
      <div style="width: 80px;" class="column-item">图标</div>
      <div style="width: 100px;" class="column-item">
        类型
      </div>
      <div style="width: 140px;" class="column-item">上级菜单</div>
      <div style="width: 200px;" class="column-item">菜单URL</div>
      <div style="width: 140px;" class="column-item">授权标识</div>
      <div style="width: 50px;" class="column-item">排序</div>
      <el-row type="flex" style="width: 230px;z-index: 100" @click.stop="">
        操作
      </el-row>
    </div>

    <collapse-list :rootData="tableTreeDdata" :tableTreeDdata="tableTreeDdata">

    </collapse-list>


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
          <el-row>
            <el-col :span="22">
              <el-input v-model="dataForm.url" placeholder="菜单路由"></el-input>
            </el-col>
            <el-col :span="2" class="icon-list__tips">
              <el-tooltip placement="top" effect="light" style="padding: 10px;">
                <div slot="content">
                  <p>URL格式：</p>
                  <p>1.常规业务开发的功能URL，如用户管理，Views目录下页面路径为 /Sys/User, 此处填写 /sys/user。</p>
                  <p>2.嵌套外部网页，如通过菜单打开百度网页，此处填写 http://www.baidu.com，http:// 不可省略。</p>
                  <p>示例：用户管理：/sys/user 嵌套百度：http://www.baidu.com 嵌套网页：http://127.0.0.1:8000</p></div>
                <i class="el-icon-warning"></i>
              </el-tooltip>
            </el-col>
          </el-row>
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


  </div>
</template>

<script>
  import KtButton from "@/views/Core/KtButton";
  import PopupTreeInput from "@/components/PopupTreeInput";
  import FaIconTooltip from "@/components/FaIconTooltip";
  import xyjworkflowDialog from "@/components/xyjworkflowDialog/xyjworkflowDialog";
  import menuItem from "./menu-item"
  import collapseList from "./collapse-item"

  export default {
    name: "MenuPro",
    components: {
      PopupTreeInput,
      KtButton,
      FaIconTooltip,
      xyjworkflowDialog,
      menuItem,
      collapseList,
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
          processName:''
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
          dbClick:this.workflowDbClick
        },
      }
    },
    mounted() {
      this.$bus.$on("add-Btns",this.handleAddBtns);
      this.$bus.$on("handleCurrAdd",this.handleCurrAdd);
      this.$bus.$on("handleEdit",this.handleEdit);
      this.$bus.$on("handleDelete",this.handleDelete);
      this.findTreeData();
    },
    methods: {
      handleCurrAdd(val){
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
          processName:''
        };
        if (val.type == 0 && val.children.length > 0 && val.children[0].url) {
          this.dataForm.url = val.children[0].url.split("/").slice(0,3).join("/")
        }
        if (val.type == 1){
          this.dataForm.type = 2;
        }
        if (val.children){
          this.dataForm.orderNum = val.children.length;
        }
        this.handleTreeSelectChange(val);
        console.log(val);
      },
      handleAddBtns(val){
        console.log(val);
        this.dialogVisible = true;
      },
      change() {
        console.log("']]]]]]]]]]]]]]]]]");
        this.popoverVisible = true;
      },
      handleChange(val) {
        console.log(val);
      },
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
          processName:''
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
            this.$confirm("确认提交吗？", "提示", {}).then(() => {
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
            });
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
        });
      },
      workflowdialog(row){
        // 工作流信息弹框 row --> 选中的数据是一个数组
        this.dataForm.cid = row[0].key;
        this.dataForm.processName = row[0].name;
      },
      workflowDbClick(row){
        // 工作流组件双击选择事件 row --> 双击选中的数据 是一个对象
        this.dataForm.cid = row.key;
        this.dataForm.processName = row.name;
      },
      open(){
        this.$refs.workflowDialog.open()
      }


    }
  }
</script>

<style scoped lang="scss">
  .menu-wrapper {
    /*padding: 0 12px;*/
    font-weight: bold;
    font-size: 12px;
    display: flex;
    /*background-color: #f0f0f0;*/
    width: 100%;
    align-items: center;
    z-index: 99;
  }

  .column-item {
    padding: 0 10px;
    height: 40px !important;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: flex;
    align-items: center;
  }
</style>
