<template>
  <div style="height: 100%;">
    <div style="height: 100%;">
      <xyj-table ref="otable"  id="1" v-bind="tableConfig">
        <template slot="xyjcontainer">
          <el-button type="primary" size="small" @click="selectEnClick" :loading="selectload">{{$t('action.selectEntityClasses')}}</el-button>
          <el-button type="primary" size="small" @click="moreClick">{{$t('action.newMultilevelHeader')}}</el-button>
          <el-button type="primary" size="small" @click="entityClick" :loading="allEntityLoading">{{$t('action.allEntityClassInitialize')}}</el-button>
        </template>
      </xyj-table>
    </div>

    <el-dialog v-dialogDrag title="选择实体类" :visible.sync="addDialogVisible">
      <el-table ref="classinfo" :data="gridData" height="400" size="mini" highlight-current-row  @current-change="getRowDatas">
        <el-table-column property="serviceId" label="微服务ID" width="100"></el-table-column>
        <el-table-column property="httpUrl" label="微服务网址"></el-table-column>
        <el-table-column property="entityName" label="包类名"></el-table-column>
        <el-table-column property="className" label="类名" width="200"></el-table-column>
      </el-table>
      <div>
        <el-button style="padding: 7px;" size="mini" @click="addDialogVisible = false">{{$t('action.cancel')}}</el-button>
        <el-button style="padding: 7px;" size="mini" type="primary" @click="addData" :loading="addDialogLoading">{{$t('action.comfirm')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog title="新增多级表头" :visible.sync="addDialogMany" width="30%">
      <el-form label-width="100px" label-position="right" ref="fromMany" :model="editForm">
        <el-row>
          <el-form-item label="表头标题:" prop="checkMustFill">
            <el-input placeholder="请输入多级表头标题"
                      v-model="manylable"
                      clearable
                      size="medium"
                      maxlength="18"></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button style="padding: 7px;" @click="addDialogMany = false" size="mini">{{$t('action.cancel')}}</el-button>
        <el-button style="padding: 7px;" type="primary" @click="addManyHeader" size="mini" :loading="addDialogManyLoading">{{$t('action.comfirm')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import XyjTable from '../../components/xyjtable/xyjTable';
  import { baseUrl } from '@/utils/global.js';
  import jqueryAjax from "@/http/jqueryAjax";
  import axios from '@/http/axios';
  import rules from '@/utils/rules';
  export default {
    name: "TableSet",
    components: {XyjTable},
    data(){
      return {
        selectload: false,
        editForm:{},
        addDialogLoading: false,
        addDialogVisible: false,
        addDialogMany: false,
        addDialogManyLoading: false,
        rule: rules,
        gridData: [], //实体类查询数据
        selectData: null, //实体类选择数据
        manylable: "", //多级表头标题
        allEntityLoading: false, //全部实体类初始化按钮执行状态
        tableConfig: {
          hasDelete: true, //删除
          hasNew: false, //新增
          hasView: true, //查看
          hasEdit: true, //编辑
          //el-table设置
          tableAttrs:{
            height: "86%",
            width: "800px",
          },
          //el-table事件设置
          tableOns: {
            select: function (selection,row) {

            }
          },
          //表格中的操作列设置
          operationAttrs:{
            width: "200px"
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: baseUrl,
          //表初始化关键字，一般为表名
          tableName: "SysTableinfo",
          //查询时的排序字段
          sorter: [{direction: "ASC",property: "tablename"},{direction: "ASC",property: "arrange"}],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: baseUrl,
            url: "sysTableinfo/query",
            method: 'post',
            notCondition: true
          },
          //业务数据微服务,新增网址
          bNewUrl: {
            hostUrl: baseUrl,
            url: "sysTableinfo/save",
            method: 'post',
           // clear: true, //清除新增对话框输入的数据
            close: true, //关闭新增对话框
          },
          //业务数据微服务,修改网址
          bEditUrl: {
            hostUrl: baseUrl,
            url: "sysTableinfo/save",
            method: 'post',
            // clear: true, //清除新增对话框输入的数据
            close: true  //关闭修改对话框
          },
          //业务数据微服务,删除网址
          bDeleteUrl: {
            hostUrl: baseUrl,
            url: "/sysTableinfo/delete",
            method: 'post'
          },
          
          //根据字段名定义函数
          extraSelectData: {
            isallowsearch: function () {
              return [
                {label: '不可搜',value: 0},
                {label: '可搜可显示',value: 1},
                {label: '可搜不显示',value: 2}
              ];
            },
            rule: function () {
              return [
                {label: '验证QQ号',value: 'QQ'},
                {label: '非必填QQ号',value: 'notQQ'},
                {label: '验证手机号',value: 'phone'},
                {label: '非必填手机号',value: 'notPhone'},
                {label: '两位小数',value: 'checkMoney'},
                {label: '非必填两位小数',value: 'notCheckMoney'},
                {label: '身份证',value: 'checkId'},
                {label: '非必填身份证',value: 'notCheckId'},
                {label: '整数',value: 'checkInt'},
                {label: '非必填整数',value: 'notCheckInt'},
                {label: '数字',value: 'checkNum'},
                {label: '非必填数字',value: 'notcheckNum'},
                {label: '输入框必填项',value: 'checkMustFill'},
                {label: '选择框必填项',value: 'checkSelectValue'},
                {label: '复选框必选项',value: 'checkSelectArray'},
              ];
            },
            align: function () {
              return [
                {label: '左对齐',value: 'left'},
                {label: '右对齐',value: 'right'},
                {label: '居中',value: 'center'}
              ];
            },
            fieldtype: function () {
              return [
                {label: '文本框',value: 'input'},
                {label: '下拉框',value: 'select'},
                {label: '单选组',value: 'radio-group'},
                {label: '复选组',value: 'checkbox-group'},
                {label: '多文本',value: 'textarea'},
                //{label: '双日期',value: 'date-picker'},
                {label: '日期时间',value: 'single-date'},
                {label: '日期',value: 'date'},
                {label: '年月',value: 'month'},
                {label: '小时分钟',value: 'hourminute'},
                {label: '小时',value: 'hour'},
                {label: '开关',value: 'switch'},
                {label: '上传文件',value: 'upload'},
              ];
            },
            headeralign: function () {
              return [
                {label: '左对齐',value: 'left'},
                {label: '右对齐',value: 'right'},
                {label: '居中',value: 'center'}
              ];
            },
            sumtype: function () {
              return [
                {label: '文本',value: 'text'},
                {label: '求和',value: 'sum'},
                {label: '平均值',value: 'avg'},
                {label: '记录数',value: 'count'}
              ];
            },
            dicttype: function (column) {
              let tdata = [];
              let tmpUrl = baseUrl + '/dict/findByGroupType?delFlag=0';
              jqueryAjax({
                url: tmpUrl,
                xyjSuccess: function (data) {
                  data.data.forEach((column,idx) => {
                    tdata.push({label: column.type, value: column.type})
                  });
                }
              });
              return tdata;
            }
          }
        }
      }
    },
    methods:{
      getRowDatas(row){
        this.selectData = row;
      },
      addData(){
        if(this.selectData === null){
          this.$message.info("请选实体类信息");
          return
        }
        this.addDialogLoading = true;
        axios({
          hostUrl: baseUrl,
          url: "/sysTableinfo/saveEntityClass",
          method: "POST",
          params:{
            servceId: this.selectData.httpUrl,
            classFullName: this.selectData.entityName
          }
        }).then((res) => {
          this.addDialogVisible = false;
          this.$refs.otable.getList({0:{field0:"tablename",gx0:"EQUAL",value0:this.selectData.className,andor0:"AND"}});
        }).catch((err) =>{
          this.$message.error(err.message);
        }).finally((err) =>{
          this.addDialogLoading = false;
        })
      },
      addManyHeader(){
        this.$refs["fromMany"].validate(valid => {
          if (!valid) return false
          let tmpSelect = this.$refs.otable.selected;
          if(tmpSelect.length<=1){
            this.$message.warning("最少选择两行数据，才能创建多级表头");
            return;
          }
          this.addDialogManyLoading = true;
          axios({
            hostUrl: baseUrl,
            url: "/sysTableinfo/createHeader",
            method: "POST",
            params:{
              label: this.manylable
            },
            data: tmpSelect
          }).then((res) => {
            this.addDialogMany = false;
            this.$refs.otable.getList();
          }).catch((err) =>{
            this.$message.error(err.message);
          }).finally((err) =>{
            this.addDialogManyLoading = false;
          });
        });
/*        if(this.manylable.length<=0){
          this.$message.warning("多级表头标题不能为空")
          return;
        }*/

      },
      selectEnClick() {
        this.selectload = true
        axios({
          hostUrl: baseUrl,
          url: "/EureKaUtiles/getAllEntityClass",
          method: "GET"
        }).then((res) => {
          this.addDialogVisible = true;
          this.gridData = res;
          this.selectload = false;
        }).catch((err) =>{
          this.selectload = false;
        })
      },
      moreClick() {
        let state = false;
        let selected = this.$refs.otable.selected;
        if(selected.length>1){
          state = selected.some(value => {
            if(value.tablename !== selected[0].tablename) {
              return true;
            }
          });
        }else{
          this.$message.warning("最少选择两行数据，才能创建多级表头");
          return;
        }
        if(state){
          this.$message.warning("多级表头不能跨表头选择");
        }else{
          this.addDialogMany = true;
        }
      },
      entityClick(){
        this.$confirm('全部实体类初始化,会花费很长时间请耐心等待', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.allEntityLoading = true;
          axios({
            hostUrl: baseUrl,
            timeout: 1200000,
            url: "/sysTableinfo/saveAllEntityClass",
            method: "POST"
          }).then((res) => {
            this.$refs.otable.getList();
            this.$message.info("实体类初始化完成");
          }).catch((err) =>{
            this.$message.error(err.message);
          }).finally(err =>{
            this.allEntityLoading = false;
          });
        }).catch(() => {
          this.allEntityLoading = false;
        });
      }
    }
  }
</script>

<style scoped lang="scss">
</style>
