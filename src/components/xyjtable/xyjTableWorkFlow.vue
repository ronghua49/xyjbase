<script src="../../http/axios.js"></script>
<template>
  <div class="el-data-table xyj-table" @click.ctrl="showTable">
    <el-row class="xyj-tooltip" type="flex" align="middle" justify="space-between" id="aaa">
      <div class="xyj-operates">
        <slot name="xyjcontainer"></slot>
      </div>
      <el-row  type="flex" align="middle" justify="end" v-if="getQuickSearch.length > 0 || !!$slots.search" >
        <!--功能按钮-->
        <el-form >
          <el-form-item >
            <Xyj-button style="padding: 7px;" v-if="hasNew" type="primary" :disabled="disabledNew"
                       @click="onDefaultNew($event)" :tableName="tableName" :label="$t('action.add')" text="新增" />
            <Xyj-button style="padding: 7px;" v-show="hasSelect && hasDelete" type="danger" :label="$t('action.batchDelete')" text="批量删除"
                       @click="onDefaultDelete($event,'many')" :tableName="tableName"
                       :disabled="single ? (!selected.length || selected.length > 1) : !selected.length || disabledNew" />
          </el-form-item>
        </el-form>

        <!--搜索字段-->
        <el-form-renderer class="serach-style" v-if="hasSearch ? getQuickSearch.length > 0 || !!$slots.search : hasSearch" inline :content="getQuickSearch" ref="searchForm">
          <!--@slot 额外的搜索内容, 当searchForm不满足需求时可以使用-->
          <slot name="search"></slot>
        </el-form-renderer>
        <el-form v-if="hasSearch">
          <el-form-item>
            <el-button style="padding: 7px;" type="primary" @click="search" size="mini" icon="el-icon-search" circle title="查询"></el-button>
           <!-- <el-button @click="resetSearch" size="mini">重置</el-button> -->
            <el-button style="padding: 7px;" @click="searchTitledialogVisible = true" size="mini" icon="el-icon-zoom-in" type="primary" circle title="高级查询"></el-button>
          </el-form-item>
        </el-form>
      </el-row>
    </el-row>
    <div class="getInvestInfo">
      <slot name="getInvestInfo"></slot>
    </div>

    <!--表格-->
    <el-table ref="table" v-bind="getTableAttrs" :data="data" 
              :row-style="showRow"
              :span-method="arraySpanMethod"
              @sort-change = "fieldSort"
              element-loading-text="拼命加载中"
              v-loading="loading"
              size = "mini"
              v-on="tableOns"
              highlight-current-row
              :row-key="getRowKeys"
              :expand-row-keys="expands"
              @current-change="handleSelectionChange"
              @selection-change="mhandleSelectionChange"
              class="tableMain">
      <!--TODO 不用jsx写, 感觉template逻辑有点不清晰了-->
      <template v-if="isTree">
        <!--有多选-->
        <template v-if="hasSelect">
          <el-table-column key="selection-key" v-bind="getTableColumns[0]"> </el-table-column>
          <el-table-column key="tree-ctrl" v-bind="getTableColumns[1]" >
            <template slot-scope="scope">
              <span v-if="isTree" v-for="space in scope.row._level" class="ms-tree-space" :key="space"></span>
              <span v-if="isTree && iconShow(scope.$index, scope.row)" class="tree-ctrl" @click="toggleExpanded(scope.$index)">
                <i v-if="!scope.row._expanded" class="el-icon-plus"></i>
                <i v-else class="el-icon-minus"></i>
              </span>
              {{scope.row[columns[1].prop]}}
            </template>
          </el-table-column>

          <el-table-column
            v-for="(col) in TableColumns.filter((c, i) => i !== 0 && i !== 1)"
            :key="col.prop"
            v-bind="col" >
          </el-table-column>
        </template>
        <!--无选择-->
        <template v-else>
          <!--展开这列, 丢失 el-table-column属性-->
          <el-table-column key="tree-ctrl" v-bind="getTableColumns[0]" >
            <template slot-scope="scope">
              <span v-if="isTree" v-for="space in scope.row._level" class="ms-tree-space" :key="space"></span>
              <span v-if="isTree && iconShow(scope.$index, scope.row)" class="tree-ctrl" @click="toggleExpanded(scope.$index)">
                <i v-if="!scope.row._expanded" class="el-icon-plus"></i>
                <i v-else class="el-icon-minus"></i>
              </span>
              {{scope.row[TableColumns[0].prop]}}
            </template>
          </el-table-column>

          <el-table-column v-for="(col) in getTableColumns.filter((c, i) => i !== 0)" :key="col.prop" v-bind="col" ></el-table-column>
        </template>
      </template>
      <!--非树-->
      <template v-else>
        <!-- table展开行 -->
        <el-table-column v-if="isExpand" type="expand" label="" show-overflow-tooltip width="50" fixed="left" align="center">
          <template slot-scope="scope">
            <slot name="expandContent" :row="scope.row"></slot>
          </template>
        </el-table-column>
        <!--处理分页序号累加功能，宋建国，2019-06-14-->
        <el-table-column label="序号" show-overflow-tooltip width="50" fixed="left" align="center">
          <template slot-scope="scope"><span>{{(scope.$index + (page - 1) * size)+1}} </span></template>
        </el-table-column>

        <template v-for="item in getTableColumns">
          <!--处理多级表头 2019-04-04 宋建国-->
          <xyj-multilevel-header v-if="item.children && item.children.length" v-bind:key="item.id" v-bind:column="item"></xyj-multilevel-header>
          <el-table-column v-else :key=item.id v-bind="item" :sortable="true"></el-table-column>
        </template>
      </template>
      <!--默认操作列-->
      <el-table-column label="操作" v-if="hasOperation" v-bind="operationAttrs" fixed="left" align="center">
        <template slot-scope="scope">
          <el-row type="flex" justify="center">
            <Xyj-button style="padding: 7px;" v-if="isTree && hasNew" type="primary" :label="$t('action.add')" text="新增" :disabled="disabledNew"
                       @click="onDefaultNew($event,scope.row)" />
            <Xyj-button style="padding: 7px;" v-if="hasEdit" :label="$t('action.edit')" text="编辑" :disabled="disabledEdit || disabledNew" 
                       @click="onDefaultEdit($event,scope.row)" :tableName="tableName" />
            <Xyj-button style="padding: 7px;" v-if="hasView" type="info" :label="$t('action.search')" text="查询" :tableName="tableName" :disabled="disabledView"
                       @click="onDefaultView($event,scope.row)" />

            <Xyj-button style="padding: 7px;"
                       v-for="(btn, i) in extraButtons"
                       v-if="'show' in btn ? btn.show(scope.row) : true"
                       :type="scope.row.isSuspended ? btn.flag ? changeBtns.type : btn.type : btn.type"
                       v-bind="btn" :label="scope.row.isSuspended ? btn.flag ? $t(changeBtns.label) : $t(btn.label) : $t(btn.label)" @click="onCustomButtonsClick($event,btn.atClick, scope.row)" :key="i" size="mini"
                       :loading="customButtonsLoading" :text="btn.text"></Xyj-button>

            <Xyj-button style="padding: 7px;" v-if="!hasSelect && hasDelete && canDelete(scope.row)" type="danger" :label="$t('action.delete')" text="删除" :tableName="tableName" :disabled="scope.row.isShow?scope.row.isShow:disabledDelete || disabledNew"
                       @click="onDefaultDelete($event,scope.row,'single')" />
          </el-row>
        </template>
      </el-table-column>
      <!--@slot 自定义操作列, 当extraButtons不满足需求时可以使用 -->
      <slot></slot>
    </el-table>
    <!--分页-->
    <el-pagination v-if="hasPagination" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page" :page-sizes="paginationSizes"
      :page-size="size" :total="total" style="text-align: right; padding: 10px 0" :layout="paginationLayout"></el-pagination>
    <!--对话框-->
    <transition name="fade">
      <el-dialog v-dialogDrag :title="dialogTitle" :visible.sync="dialogVisible" v-if="hasDialog" :close-on-click-modal="false" @close="$refs.dialogForm.resetFields()" :width="dialogWidth" class="newDialog">
        <!--https://github.com/FEMessage/el-form-renderer-->
        <el-form-renderer style="text-align: left" :content="getdialogFrom" ref="dialogForm" v-bind="formAttrs" :disabled="isView" class="dialogForm">
          <!--@slot 额外的弹窗表单内容, 当form不满足需求时可以使用 -->
          <slot name="form"></slot>
        </el-form-renderer>

        <div slot="footer" v-show="!isView">
          <el-button style="padding: 7px;" @click="cancel" >{{$t('action.cancel')}}</el-button>
          <el-button style="padding: 7px;" type="primary" @click="confirm" :loading="confirmLoading" >{{$t('action.comfirm')}}</el-button>
        </div>
      </el-dialog>
    </transition>

    <!--高级查询-->
    <transition name="fade">
      <el-dialog id="searchDialog" v-dialogDrag :title=searchTitle :visible.sync="searchTitledialogVisible" v-if="searchDataForm.length > 0" v-on:open="searchOpen" :append-to-body="appendToBody">
        <!--https://github.com/FEMessage/el-form-renderer-->
        <el-form-renderer class="senior-serach-style" :content="searchDataForm" ref="searchDialogForm" v-bind="formAttrs" v-bind:dvalue="sValue">
          <!--@slot 额外的弹窗表单内容, 当form不满足需求时可以使用 -->
          <slot name="searchslot"></slot>

        </el-form-renderer>

        <div slot="footer">
          <el-button style="padding: 7px;" type="primary" @click="addHighFind" size="mini">{{$t('action.add')}}</el-button>
          <el-button style="padding: 7px;" @click="seniorResetSearch" size="mini">{{$t('action.reset')}}</el-button>
          <el-button style="padding: 7px;" type="primary" @click="seniorSearch" :loading="confirmLoading" size="mini">{{$t('action.search')}}</el-button>
        </div>
      </el-dialog>
    </transition>

  </div>
</template>

<script>
  import Vue from 'vue'
  import _get from 'lodash.get'
  import qs from 'qs'
  import ElFormRenderer from '../../components/XyjRenderer'
  import XyjMultilevelHeader from './xyjMultilevelHeader'
  import rules from '@/utils/rules';
  import axios from '@/http/axios';
  import jquery from 'jquery';
  import Cookies from "js-cookie";

  // 赋予table发送请求的能力
  //Vue.prototype.$xyjaxios = tableAxios
  // 操作成功后需要用到消息提醒
  //Vue.prototype.$message = Vue.$me
  // 删除操作需要用到确认提示
 //// Vue.prototype.$confirm = MessageBox.confirm

  // 默认返回的数据格式如下
  //          {
  //            "code":0,
  //            "msg":"ok",
  //            "payload":{
  //              "content":[], // 数组
  //              "totalElements":2, // 总数
  //            }
  //          }
  // 可根据实际情况传入 data/total 两个字段的路径, 分别对应上面数据结构中的 content/totalElements
  // 如果接口不分页, 则传hasPagination=false, 此时数据取 payload, 当然也可以自定义, 设置dataPath即可

  /**
   *  宋建国：20190328
   *  分页请求默认第几页
   * */
  const defaultFirstPage = 1

  /**
   *  宋建国：20190328
   *  默认值,返回的数据中的路径, 嵌套对象使用.表示即可
   * */
  const dataPath = 'payload.content'

  const totalPath = 'payload.totalElements'

  const noPaginationDataPath = 'payload'

  /**
   *  宋建国：20190328
   *  默认值,子节点的字段名
   * */
  const treeChildKey = 'children'

  /**
   *  宋建国：20190328
   *  默认值,父节点的字段名
   * */
  const treeParentKey = 'parentId'

  /**
   *  宋建国：20190328
   *  默认值,父节点字段值的来源字段
   * */
  const treeParentValue = 'id'

  /**
   *  宋建国：20190328
   *  主键默认ID
   * */
  const defaultId = 'id'

  const dialogForm = 'dialogForm'

  const equal = '='

  const equalPattern = /=/g

  const valueSeparator = '~'

  const paramSeparator = ','

  const valueSeparatorPattern = new RegExp(valueSeparator, 'g')

  const queryFlag = 'q='

  const queryPattern = new RegExp('q=.*' + paramSeparator)

  //选择字段下拉框
  let fieldsDown = {
    $id: "",
    $type: "select",
    label: "字段",
    $default: "",
    $el: {
      placeholder: "请选择字段",
      style: "width: 150px", //组件宽度
      filterable: true, //所搜
    },
    $options: []
  };

  //关系运算符
  let relationship = {
    $id: "",
    $type: "select",
    label: "关系符",
    $default: "EQUAL",
    $el: {
      placeholder: "请选择关系符",
      style: 'width: 110px', //组件宽度
    },
    $options:[
      {label: '等于',value: 'EQUAL'},
      {label: '相似',value: 'LIKE'},
      ]
  };

  //并且或者关系
  let andOr =  {
    $id: "",
    $type: "select",
    width: '50px',
    label: "关系符",
    $el: {
      placeholder: "请选择",
      style: 'width: 80px', //组件宽度
    },
    $default: "AND",
    $options:[{label: '并且',value: 'AND'},{label: '或者',value: 'OR'}]
  };
  //输入值对象
  let inputValue = {
    $id: "", // 每一个原子都存在id，用于存储该原子的值，注意不能重复
    $type: "input", // 类型，element 提供的所有表单类型，即 el-xxx
    label: "数值", // el-form-item上的属性,
    $el: {
      placeholder: "请输入",
      style: 'width: 130px ',
    }
  }

  //删除对象
  let deleteButton = {
    $id: "",
    $groupid: 0,
    $type: "button",
    $el: {
      type: "button",
      icon: "el-icon-delete",
      circle: true,
      style:  "background-color: #FF0800",
      disabled: false
    }
  }
  /**
   *  宋建国：20190417
   *  深度克隆对象的方法	 -- > 深层拷贝，对象属性方法一致，单都是单独的个体。
   * */
  function clone(obj){
    var result = Array.isArray(obj) ? [] : {};
    for (var key in obj) {
      if (obj.hasOwnProperty(key)) {
        if (typeof obj[key] === 'object' && obj[key] != null) {
          result[key] = clone(obj[key]);   //递归复制
        } else {
          result[key] = obj[key];
        }
      }
    }
    return result;
  }

  /**
   *  宋建国：20190417
   *  深度克隆对象的方法	 -- > 深层拷贝，对象属性方法一致，单都是单独的个体。
   * */
  function columnClone(obj){
    var result = Array.isArray(obj) ? [] : {};
    for (var key in obj) {
      if (obj.hasOwnProperty(key)) {
        if (typeof obj[key] === 'object' && obj[key] != null) {
          result[key] = columnClone(obj[key]);   //递归复制
        } else {
          if(key.toLowerCase()==="resizable" || key.toLowerCase() === "fixed") {
            result[key] =  obj[key]===1 ? true : false;
          }else if(key.toLowerCase() === "showoerflowtooltip") {
            result["show-overflow-tooltip"] = obj[key] === 1 ? true : false;
          }else if(key.toLowerCase() === "fieldtype" && obj[key].toLowerCase() === "selection"){
              result["type"] = "selection"
              result[key] = obj[key];
          }else{
              result[key] = obj[key];
          }
        }
      }
    }
    return result;
  }

  /**
   *  宋建国：20190417
   *  整理初始化列数据
   * */
  function ArrangementColumns(columns) {
    let tmpArray = [];
    let tmpcolumns = columns.sort(function(a,b){
      return a.arrange-b.arrange;
    });
    tmpcolumns.forEach((column,index) => {
        tmpArray.push(column);
        if(column.children && column.children.length){
          let tmpchildren = column.children.sort(function(a,b){
            return a.arrange-b.arrange;
          });
          tmpArray = tmpArray.concat(ArrangementColumns(tmpchildren));
        }
      });
      return tmpArray;
  };
  function changeBoolean(source) {
    if(typeof source !== 'object'){
      if(typeof source[attr] === "boolean"){
        return source ? 1 : 0;
      }else{
        return source;
      }
    }
    var _obj = Array.isArray(source)? []:{};
    if (typeof source === 'object') {
      for (var attr in source) {
        if (typeof source[attr] === 'object') {
          if(source[attr] === null){
            _obj[attr] = null;
          }else{
            _obj[attr] = deepClone(source[attr]);
          }
        } else {
          if(typeof source[attr] === "boolean"){
            _obj[attr] = source[attr] ? 1 : 0;
          }else{
            _obj[attr] = source[attr];
          }
        }
      }
      return _obj;
    }
  }
  /**
   *  宋建国：20190417
   *  将表格列信息转换为输入组建
   * */
  function convertInput(column) {
    switch (column.fieldtype) {
      case "input":
         return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "input", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性,
          $el: {
            placeholder: "请输入"+column.label,
            style: 'width: 150px' ,
            maxlength: (column.datalength/2)-1, //原生属性，最大输入长度
            disabled: false,
            // "show-word-limit": true,
            clearable: true,
            size:'small',
          },
          $options: [],
          $default: "", //默认值
          rules: {}
        };
      case "select":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "select", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            placeholder: "请选择"+column.label,
            style: 'width: 150px',
            filterable: true, //所搜
            disabled: false,
            clearable: true, //清空选择
            size:'small',
          },
          $options:[], //格式{label: '显示内容',value: '值'}
          $default: "", //默认值
          rules: {} // el-form-item上的属性
        };
      case "radio-group":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $el:{
            disabled: true
          },
          $type: "radio-group", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性,,
          $options:[],  //格式{label: '显示内容',value: '值'}
          $default: "", //默认值
          rules: column.rule // el-form-item上的属性
        };
      case "radio-button":
        return {};
      case "checkbox-group":
        return {
          $el: {
            disabled: false
          },
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "checkbox-group", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性,
          $default: [], //默认值
          $options:[], //格式{label: '显示内容',value: '值'}
          rules: {} // el-form-item上的属性
        };
      case "checkbox-button":
        return {};
      case "textarea":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "input", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "textarea",
            rows: "3",
            placeholder: "请输入"+column.label,
            style: 'width: 150px',
            maxlength: column.maxlength, //原生属性，最大输入长度
            disabled: false
          },
          $options:[],
          $default: "", //默认值
          rules: {}
        };
      case "date-picker":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "date-picker", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "datetimerange",
            clearable: true,
            startPlaceholder: "请选择开始日期" + column.label,
            endPlaceholder: "请选择结束日期"+ column.label,
            style: 'width: 150px',
            valueFormat: "yyyy-MM-dd HH:mm",
            format: "yyyy-MM-dd HH:mm",
            disabled: false
          },
          $options:[],
          $default: "", //默认值
          outputFormat: function outputFormat(val){
            return {
              startDate: val[0],
              endDate: val[1]
            };
          },
          rules: {}
        };
      case "single-date":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "date-picker", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "datetime",
            clearable: true,
            placeholder: "请选择" + column.label,
            style: 'width: 150px',
            valueFormat: "yyyy-MM-dd HH:mm:ss LZT",
            format: "yyyy-MM-dd HH:mm:ss",
            disabled: false
          },
          $options:[],
          $default: "", //默认值
/*          outputFormat: function outputFormat(val){
            return {
              sDate: val[0],
            };
          },*/
          rules: {}
        };
      case "date":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "date-picker", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "date",
            clearable: true,
            placeholder: "请选择" + column.label,
            style: 'width: 150px',
            valueFormat: "yyyy-MM-dd LZ",
            format: "yyyy-MM-dd",
            disabled: false,
          },
          $options:[],
          $default: "", //默认值
          rules: {}
        };
      case "month":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "date-picker", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "month",
            clearable: true,
            placeholder: "请选择" + column.label,
            style: 'width: 150px',
            valueFormat: "yyyy-MM LNY",
            format: "yyyy-MM",
            disabled: false,
            size:'small'
          },
          $options:[],
          $default: "", //默认值
          rules: {}
      	};
      case "hourminute":  //小时分钟
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "time-picker", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "datetime",
            clearable: true,
            placeholder: "请选择" + column.label,
            style: 'width: 150px',
            valueFormat: "HH:mm LTXF",
            format: "HH:mm",
            disabled: false,
          },
          $options:[],
          $default: "", //默认值
          rules: {}
        };
      case "hour":  //小时分钟
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $type: "time-picker", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $el: {
            type: "datetime",
            clearable: true,
            placeholder: "请选择" + column.label,
            style: 'width: 150px',
            valueFormat: "HH LTX",
            format: "HH",
            disabled: false,
          },
          $options:[],
          $default: "", //默认值
          rules: {}
        };
      case "switch":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $el: {
            disabled: false
          },
          $type: "switch", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $options:[],
          $default: true,
          rules: {}
        };
      case "upload":
        return {
          $id: column.prop, // 每一个原子都存在id，用于存储该原子的值，注意不能重复
          $el: {
            disabled: false,
            action:"",
            showFileList:false,
            httpRequest:function(file){console.log(file)}
            // drag:true,
          },
          $type: "upload", // 类型，element 提供的所有表单类型，即 el-xxx
          label: column.label, // el-form-item上的属性
          $options:[],
          $default: "",
          rules: {},
          // $style:{
          //   tagName:"el-button",
          //   props:{
          //     type:"primary",
          //     size:"small"
          //   },
          //   text:"上传文件"
          // },
          $style:{
            tagName:"el-input",
            props:{
              size:"small",
              placeholder:"点击选择上传文件"
            },
            text:""
          }
        };
        break;
      default:
        return {};
    }
  }

  export default {
    name: 'xyjTableWorkFlow',
    components:{
      XyjMultilevelHeader,
      ElFormRenderer
    },
    props: {
       /*  宋建国：20190328
        *   请求url, 如果为空, 则不会发送请求; 改变url, 则table会重新发送请求
       */
      url: {
        type: String,
        default: ''
      },
      /*  宋建国：20190328
        *   业务微服务服务器URL地址, 如果为空, 则不会发送请求; bQuery
      */
      bQuery:{
        type: Object,
        default(){
          return {
            notCondition: true, //true:空条件查询，false：空条件不查询
            hostUrl: "",
            url: "/qurey",
            method: "post"
          }
        }
      },
      /*  宋建国：20190328
        *   业务微服务服务器URL地址,新增
      */
      bNewUrl:{
        type: Object,
        default(){
          return {
            hostUrl: "",
            url: "",
            method: "post"
          }
        }
      },
      /*  宋建国：20190328
        *   业务微服务服务器URL地址,修改
      */
      bEditUrl:{
        type: Object,
        default(){
          return {
            hostUrl: "",
            url: "",
            method: "post"
          }
        }
      },
      /*  宋建国：20190328
       *   业务微服务服务器URL地址,删除
      */
      bDeleteUrl:{
        type: Object,
        default(){
          return {
            hostUrl: "",
            url: "",
            method: "post"
          }
        }
      },
      /*  宋建国：20190328
       *   服务器URL地址, 如果为空, 则不会发送请求; 改变hostUrl, 则table会重新发送请求
      */
      hostUrl:{
        type: String,
        default: ''
      },
      /*  宋建国：20190328
      *   表格初始化的关键字，一般是表名，对应系统表（sys_tableinfo）的tablename字段
      */
      tableName:{
        type: String,
        default: ''
      },
      /**
       *  宋建国：20190328
       *  主键，默认值 id
       *  修改/删除时会用到,请求会根据定义的属性值获取主键,即row[this.id]
       */
      id: {
          type: String,
          default: defaultId
      },
      /**
       * 宋建国：20190328
       * 分页请求的第一页的值(有的接口0是第一页)
       */
      firstPath: {
        type: Number,
        default: defaultFirstPage
      },
      /**
       * 宋建国：20190328
       * 渲染组件的分页数据在接口返回的数据中的路径, 嵌套对象使用.表示即可
       */
      dataPath: {
        type: String,
        default: dataPath
      },
      /**
       * 宋建国：20190328
       * 分页数据的总数在接口返回的数据中的路径, 嵌套对象使用.表示即可
       */
      totalPath:{
        type: String,
        default: totalPath
      },
      /**
       * 宋建国：20190328
       * 列属性设置, 详情见element-ui官网
       */
      columns: {
        type: Array,
        default(){
          return []
        }
      },
      /**
       * 宋建国：20190328
       * 列属性设置, 详情见element-ui官网
       */
      searchForm: {
        type: Array,
        default () {
          return []
        }
      },
      /**
       * 宋建国：20190328
       * 点击查询按钮, 查询前执行的函数, 需要返回Promise
       */
      beforeSearch: {
        type: Function,
        default(){
          return Promise.resolve()
        }
      },
      /**
       * 宋建国：20190328
       * 路由模式, hash | history || '', 决定了查询参数存放的形式, 设置为空则不存储查询参数
       */
      routerMode: {
        type: String,
        default: 'hash'
      },
      /**
       * 宋建国：20190328
       * 单选, 适用场景: 不可以批量删除
       */
      single: {
        type: Boolean,
        default: false
      },
      /**
       * 宋建国：20190328
       * 是否有操作列
       */
      hasOperation:{
        type: Boolean,
        default: true
      },
      /**
       * 宋建国：20190328
       * 操作列的自定义按钮, 渲染的是element-ui的button, 支持包括style在内的以下属性:
       * {type: '', text: '', atClick: row => Promise.resolve(), show: row => return true时显示 }
       * 点击事件 row参数 表示当前行数据, 需要返回Promise, 默认点击后会刷新table, resolve(false) 则不刷新
       */
      extraButtons: {
        type: Array,
        default(){
          return []
        }
      },
      /**
       * 宋建国：20190328
       * 操作列的自定义按钮需要替换的文字和方法
       * 点击事件 row参数 表示当前行数据, 需要返回Promise, 默认点击后会刷新table, resolve(false) 则不刷新
       */
      changeBtns:{
      	type:Object,
      	default(){
      		return {}
      	}
      },
      /**
       * 宋建国：20190328
       * 是否有新增按钮
       */
      hasNew: {
        type: Boolean,
        default: false
      },
      /**
       * 宋建国：20190328
       * 是否有快速查询
       */
      hasSearch: {
      	type: Boolean,
        default: true
      },
      /**
       * 宋建国：20190328
       * 是否有编辑按钮
       */
      hasEdit: {
        type: Boolean,
        default: true
      },
      /**
       * 宋建国：20190328
       * 是否有查看按钮
       */
      hasView: {
        type: Boolean,
        default: false
      },
      /**
       * 宋建国：20190328
       * table头部是否有删除按钮(该按钮要多选时才会出现)
       */
      hasDelete: {
        type: Boolean,
        default: false
      },
      /**
       * table表格是否有展开行
       */
      isExpand: {
        type: Boolean,
        default: false
      },
      dialogWidth:{
        type:String,
        default:"40%"
      },
      /**
       * 宋建国：20190328
       *  某行数据是否可以删除, 返回true表示可以, 控制的是单选时单行的删除按钮
       */
      canDelete: {
        type: Function,
        default(){
          return true
        }
      },
      /**
       * 宋建国：20190328
       * 点击新增按钮时的方法, 当默认新增方法不满足需求时使用, 需要返回promise
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据, 只有isTree为true时, 点击操作列的新增按钮才会有值
       */
      onNew:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击新增按钮执行成功之后的方法
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据, 只有isTree为true时, 点击操作列的新增按钮才会有值
       */
      onAfterSuccessNew:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击新增按钮执行失败之后的方法
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据, 只有isTree为true时, 点击操作列的新增按钮才会有值
       */
      onAfterFailNew:{
        type: Function
      },

      /**
       * 宋建国：20190328
       * 点击删除按钮执行成功之后的方法
       * 参数(selected) selected 是选择的数据
       */
      onAfterSuccessDelete:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击删除按钮执行失败之后的方法
       * 参数(selected, errinfo) selected 是选择的数据，errinfo：错误提示信息
       */
      onAfterFailDelete:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击修改按钮时的方法, 当默认修改方法不满足需求时使用, 需要返回promise
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据
       */
      onEdit: {
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击修改按钮时的方法, 当修改默认方法上传数据时，使用,
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据
       */
      onManualEdit:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击新增按钮时的方法, 当修改默认方法上传数据时，使用,
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据
       */
      onManualNew:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击删除按钮时的方法, 当修改默认方法上传数据时，使用,
       * 参数(data, row) data 是form表单的数据, row 是当前行的数据
       */
      onManualDelete:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 点击删除按钮时的方法, 当默认删除方法不满足需求时使用, 需要返回promise
       * 多选时, 参数为selected, 代表选中的行组成的数组; 非多选时参数为row, 代表单行的数据
       */
      onDelete: {
        type: Function
      },
       /**
       * 新增时，处理有默认数据的表单
       */
      onDialogDefault:{
        type: Function
      },
      /**
       * 点击新增按钮前，需要做的判断
       */
      onBeforeNew:{
        type: Function
      },
      /**
       * 宋建国：20190328
       * 是否分页
       */
      hasPagination:{
        type: Boolean,
        default: true
      },
      /**
       * 宋建国：20190328
       * 分页组件的子组件布局，子组件名用逗号分隔，对应element-ui pagination的layout属性
       */
      paginationLayout: {
        type: String,
        default: 'total,sizes,prev,pager,next,jumper'
      },
      /**
       * 宋建国：20190328
       * 分页组件的每页显示个数选择器的选项设置，对应element-ui pagination的page-sizes属性
       */
      paginationSizes: {
        type: Array,
        default: () => [10,20,30,40,50]
      },
      /**
       * 宋建国：20190328
       * 分页组件的每页显示个数选择器默认选项，对应element-ui pagination的page-size属性
       */
      paginationSize: {
        type: Number,
        default: 10
      },
      /**
       * 宋建国：20190328
       * 不分页时的size的大小
       */
      noPaginationSize: {
        type: Number,
        default: 999
      },
      /**
       * 宋建国：20190328
       * 要渲染的数据是否是树形结构
       */
      isTree: {
        type: Boolean,
        default: false
      },
      /**
       * 宋建国：20190328
       * 树形结构相关: 子节点的字段名
       */
      treeChildKey:{
        type: String,
        default: treeChildKey
      },
      /**
       * 宋建国：20190328
       * 树形结构相关: 父节点的字段名
       */
      treeParentKey:{
        type: String,
        default: treeParentKey
      },
      /**
       * 宋建国：20190328
       * 树形结构相关: 父节点字段值的来源字段。
       * 新增/修改时会用到, 例如, 在id为2的节点新增子节点, 则子节点的parentId为2, 也即parentId的值来源于字段id, 故treeParentValue为id
       */
      treeParentValue:{
        type: String,
        default: treeParentValue
      },
      /**
       * 宋建国：20190328
       * 树形结构相关: 是否展开所有节点
       */
      expandAll:{
        type: Boolean,
        default: false
      },
      /**
       * 宋建国：20190328
       * element table 属性设置, 详情配置参考element-ui官网
       */
      tableAttrs:{
        type: Object,
        default(){
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * element table 设置事件, 详情配置参考element-ui官网
       */
      tableOns:{
        type: Object,
        default(){
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * 操作列属性
       */
      operationAttrs:{
        type: Object,
        default(){
          return {
            width: '',
            fixed: 'right',
            headerAlign: 'center'//表头对齐方式，若不设置该项，则使用表格的对齐方式
          }
        }
      },
      /**
       * 宋建国：20190328
       * 是否有弹窗, 用于不需要弹窗时想减少DOM渲染的场景
       */
      hasDialog:{
        type: Boolean,
        default: true
      },
      /**
       * 宋建国：20190328
       * 高级查询对话框标题
       */
      searchTitle:{
        type: String,
        default: '高级查询对话框'
      },
      /**
       * 宋建国：20190328
       * 新增弹窗的标题
       */
      dialogNewTitle: {
        type: String,
        default: '新增'
      },
      /**
       * 宋建国：20190328
       * 修改弹窗的标题
       */
      dialogEditTitle:{
        type: String,
        default: '修改'
      },
      /**
       * 宋建国：20190328
       * 查看弹窗的标题
       */
      dialogViewTitle: {
        type: String,
        default: '查看'
      },
      /**
       * 宋建国：20190328
       * 弹窗表单, 用于新增与修改, 详情配置参考el-form-renderer
       */
      form: {
        type: Array,
        default() {
          return []
        }
      },
      /**
       * 宋建国：20190328
       * 弹窗表单属性设置, 详情配置参考element-ui官网
       */
      formAttrs: {
        type: Object,
        default() {
          return {
            labelPosition: "right",
            labelWidth: "7em"
          }
        }
      },
      /**
       * 宋建国：20190328
       * 在新增/修改弹窗 点击确认时调用，返回Promise, 如果reject, 则不会发送新增/修改请求
       * 参数: (data, isNew) data为表单数据, isNew true 表示是新增弹窗, false 为 编辑弹窗
       */
      beforeConfirm: {
        type: Function,
        default() {
          return Promise.resolve()
        }
      },
      /**
       * 宋建国：20190328
       * 外部的注入额外的查询参数, 键值对形式
       */
      customQuery: {
        type: Object,
        default() {
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * 表格初始化时查询条件
       */
      InitQuery: {
        type: Object,
        default() {
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * 外部注入选择数据
       * */
      extraSelectData:{
        type: Object,
        default(){
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * 扩展cheng事件
       * */
      extraCheng:{
        type: Object,
        default(){
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * 前端手动修改控件属性
       * */
      extraAttribute:{
        type: Object,
        default(){
          return {}
        }
      },
      /**
       * 宋建国：20190328
       * 表格列格式化
       * */
      formatters:{
        type: Object,
        default(){
          return {}
        }
      },
      /**
       * 宋建国：20190505
       * 外部传送排序字段
       * */
      sorter: {
        type: Array,
        default: () => []
      },
      editAxios: {
        type: Object,
        default(){
          return {}
        }
      },
    /**
     * @description: 新增按钮是否禁用
     * @param {type} 
     * @return: 
     */  
      disabledNew: {
        type: Boolean,
        default(){
          return false
        }
      },
      image: {
        type: Array,
        default(){
          return []
        }
      },
      // 添加流程树中的code查询条件
      codeVaule:{
      	type:Function
      },
     //  排序时的查询条件
      sortVaule:{
      	type:Function
      },
      // 查询字段显示的值
      searchList:{
      	type: Array
      },
      // 高级查询弹框是否是嵌套子弹框
      appendToBody:{
      	type:Boolean,
      	default:false
      },
    },
    data(){
      return {
        expands:[],
        getRowKeys(row){ return row.id},// 表格展开行id
        // disabledNew: false, //控制新增按钮是否禁用 false:启用，true：禁用
        disabledEdit: false, //控制修改按钮是否禁用 false:启用，true：禁用
        disabledView: false, //控制查看按钮是否禁用 false:启用，true：禁用
        disabledDelete: false, //控制删除按钮是否禁用 false:启用，true：禁用
        disabled:false, // 新增修改时确定按钮只能点击一次
        data: [],
        hasSelect: false,
        mSelect: false, //是否有多选
        size: this.paginationSize || this.paginationSizes[0],
        page: defaultFirstPage,
        total: null,
        summary:[], //汇总条件
        summaryData: {},//汇总数据
        loading: false,
        selected: [],
        customButtonsLoading: false,
        dialogTitle: this.dialogNewTitle,
        dialogVisible: false,
        buttonState: false,
        searchTitledialogVisible: false,
        isNew: true,
        isEdit: false,
        isView: false,
        confirmLoading: false,
        ///当前修改的那一行
        row: {},
        // 初始的customQuery值, 重置查询时, 会用到
        // JSON.stringify是为了后面深拷贝作准备
        initCustomQuery: JSON.stringify(this.customQuery),
        rule: rules, //校验对象
        searchDataForm: [], //高级查询框动态数据
        getQuickSearchs:[],// 普通查询数据
        tableColumns: this.columns, //父组件传入默认值
        dialogFrom: this.form, //新增，修改，查看数据组件
        allTableColumns: [], //多级表头数据合成一级表头数据
        fieldSelectValue: [], //字段选择数据
        sValue: {},//快速查询值,
        inputSelect: {},//用户手动输入数据，防止多次请求,
        xyjSorter: clone(this.sorter),//排序
        xyjformAttrs: this.formAttrs,
      }
    },
    watch:{
      url: function (val,old) {
        this.page = defaultFirstPage
        this.getList()
      },
      dialogVisible: function (val,old) {
        if(!val){
          this.isNew = false
          this.isEdit = false
          this.isView = false
          this.confirmLoading = false
          this.$refs[dialogForm].resetFields();
        }
      },
      searchTitledialogVisible: function (val,old) {
        if(!val){
          this.confirmLoading = false;
        }
      }
    },
    mounted(){
/*      //搜索条件
      let searchForm = this.$refs.searchForm
      if(searchForm){
        //阻止表单提交的默认行为
        searchForm.$el.setAttribute('action','javascript:;')
        //恢复查询条件
        let matches = location.href.match(queryPattern)
        if(matches){
          let query = matches[0].substr(2).replace(valueSeparatorPattern,equal)
          let params = qs.parse(query,{delimiter: paramSeparator})
          //page,size 处理
          this.page = params.page * 1
          this.size = params.size * 1
          //对slot=search无效处理
          searchForm.updateForm(
            Object.keys(params).reduce((acc,k)=>{
              if(k !=='page' && k!=='size'){
                acc[k] = params[k]
              }
              return acc
            },{})
          )
        }
      }
      this.$nextTick(() => {
        this.getList()
      })*/
    },
    methods: {
      // 车程里程碑图片
      arraySpanMethod({ row, column, rowIndex, columnIndex }) {
        let _col = 1;
        if (rowIndex === 0 && !row.id && typeof(row.id) != "undefined" && row.id !=0) {
          for(let i = 0,l=this.getImg.length;i<l;i++){
            if(this.getImg[i].startYear === column.parentName && this.getImg[i].startMonth === column.label){
              _col = this.getImg[i].endMonth - this.getImg[i].startMonth + 1
              row[column.property] = 'start';
              row.img = this.getImg[i].urlImage;
              if (columnIndex === columnIndex) {
                return [1, _col];
              } else {
                return [0, 0];
              }
            }
          }
        }
      },
      // 获取表格实体类名字
      showTable(){
        this.$message.info(this.tableName)
      },
      //处理查询条件
      getQuery(findWhere){
        let query = {};
        if(!this.bQuery.hostUrl || !this.bQuery.url){
          console.warn("服务器网址为空，不能发送请求")
          return query;
        }
        let tQuery;
        if(!findWhere){
          let searchForm = this.searchTitledialogVisible ? this.$refs.searchDialogForm :this.$refs.searchForm;
          let formQuery = searchForm ? searchForm.getFormValue() : {}
          // TODO Object.assign IE不支持, 所以后面Object.keys的保守其实是没有必要的。。。
          tQuery = Object.assign({},formQuery,this.customQuery)
        }else{
          tQuery =  Object.assign({},findWhere,this.customQuery);
        }
        let size = this.hasPagination ? this.size : this.noPaginationSize;
        // 根据偏移值计算接口正确的页数
        let pageOffset = this.firstPath - defaultFirstPage
        let page = this.page + pageOffset
        let querys = [];
        let data,Ndata;
        for(let key in tQuery){
          // tQuery[key].hasOwnProperty("gx"+key) && tQuery[key].hasOwnProperty("field"+key) && tQuery[key].hasOwnProperty("value"+key) && tQuery[key]["andor"+key]
          if(tQuery[key].hasOwnProperty("gx"+key) && tQuery[key].hasOwnProperty("field"+key) && tQuery[key].hasOwnProperty("value"+key)){
            let tmpValue = tQuery[key]["value"+key];
            if(tQuery[key]["andor"+key]){
              data = {hasInitValue: false,operation: tQuery[key]["gx"+key],property: tQuery[key]["field"+key],relation: tQuery[key]["andor"+key],value: tmpValue ? 1 : 0};
              Ndata = {hasInitValue: false,operation: tQuery[key]["gx"+key],property: tQuery[key]["field"+key],relation: tQuery[key]["andor"+key],value: tmpValue};
            }else{
              data = {hasInitValue: false,operation: tQuery[key]["gx"+key],property: tQuery[key]["field"+key],relation: "AND",value: tmpValue ? 1 : 0};
              Ndata = {hasInitValue: false,operation: tQuery[key]["gx"+key],property: tQuery[key]["field"+key],relation: "AND",value: tmpValue};
            }
            if(typeof tmpValue === "boolean"){
              querys.push(data);
            }else if(typeof tmpValue === "number"){
              querys.push(Ndata)
            }else if(typeof tmpValue ==="string"){
              if(tmpValue.length>0){
                querys.push(Ndata)
              }
            }
          }
        }
        query = {
          pageBean:{
            page: page,
            pageSize: size,
            showTotal: true,
            summary: this.summary
          },
          params: {},
          querys: querys,
          selfDate: {},
          sorter: this.xyjSorter
        };
        return query;
      },
      getList(findWhere){
        let query = this.getQuery(findWhere)
        if(!this.bQuery.notCondition && query.querys.length<=0){
          return;
        }
//      查询code下的流程数据
				if(this.codeVaule){
					this.codeVaule(query)
				}
				console.log(query)
        //请求开始
        this.loading = true;
        axios(
          Object.assign({},this.bQuery,{data:query})
        ).then((res) => {
          if(!res.data){
            this.data = res.rows;
            if(this.data[0]){
              if(!this.data[0].id){
                !this.data[0].id && typeof(this.data[0].id) != "undefined" && this.data[0].id !=0 ? this.data[0].isShow = true : '';
              }
            }
//            this.total = res.total;
            this.total = typeof (res.total) === "string" ? Number(res.total) : res.total;
            this.summaryData = res.map;
          }else{
            this.data = res.data.rows;
           // this.total = res.data.total;
            this.total = typeof (res.data.total) === "string" ? Number(res.data.total) : res.data.total;
            this.summaryData = res.data.map;
          }
          if(this.isTree){
            this.data = this.tree2Array(this.data,this.expandAll)
          }
          this.$nextTick(() => {
			     	this.$refs.table.doLayout()
			    })
          /**
          * 请求返回, 数据更新后触发, 返回(data, resp) data是渲染table的数据, resp是请求返回的完整response
          * @event update
          */
          this.$emit('update', this.data, res);
        }).catch((err) =>{
          this.$message.error(err.message);
          this.$emit('error',err);
        }).finally(err =>{
          this.loading = false;
        });

 /*      this.$xyjaxios.get(url + params + `&page=${page}`).then(
          resp => {
            let res = resp.data
            let totalData = res.totalData;
            let data = []
            if(!this.hasPagination){
              data = _get(res, this.dataPath) || _get(res,noPaginationDataPath) || []
            } else {
              data = _get(res, this.dataPath) || []
              this.total = _get(res,this.totalPath)
            }
            this.data = data
            this.totalshow(totalData);
            if(this.isTree){
              this.data = this.tree2Array(data,this.expandAll)
            }
            this.loading = false
            /!**
             * 请求返回, 数据更新后触发, 返回(data, resp) data是渲染table的数据, resp是请求返回的完整response
             * @event update
             *!/
            this.$emit('update',data,res)
          }
        ).catch(err => {
          this.$emit('error',err)
          this.loading = false
        })**/

        ///存储query记录, 便于后面恢复
/*        if(this.routerMode && shouldStoreQuery > 0) {
          let newUrl = ''
          let searchQuery = queryFlag + (params + `&page=${this.page}`).replace(/&/g, paramSeparator).replace(equalPattern,valueSeparator)+paramSeparator
          // 非第一次查询
          if(location.href.indexOf(queryFlag) > -1){
            newUrl = location.href.replace(queryPattern,searchQuery)
          }else if (this.routerMode == 'hash'){
            let search = location.hash.indexOf('?') > -1 ? `&${searchQuery}` :  `?${searchQuery}`
            newUrl = location.origin + location.pathname + location.search + location.hash + search
          }else{
            let search = location.search ?  `&${searchQuery}` : `?${searchQuery}`
            newUrl = location.origin + location.pathname + location.search + search + location.hash
          }
          history.pushState(history.state,'xyjTable search',newUrl)
        }*/
      },
      search(){
        this.$refs.searchForm.validate(valid => {
          if(!valid) return
          this.beforeSearch().then(() => {
            this.page = defaultFirstPage
            this.codeValue = true;
            this.getList()
          }).catch(err => {
            this.$emit('error',err)
          })
        })
      },
      resetSearch(){
        // reset后, form里的值会变成 undefined, 在下一次查询会赋值给query
        this.$refs.searchForm.resetFields()
        this.page = defaultFirstPage
        //重置
        history.replaceState(history.state,'',location.href.replace(queryPattern,''))
        this.$nextTick(() => {
          this.getList()
        })
        this.$emit('reset')
        this.$emit('update:customQuery',Object.assign(this.customQuery,JSON.parse(this.initCustomQuery)))
      },
      //高级查询重置
      seniorResetSearch(){
        // reset后, form里的值会变成 undefined, 在下一次查询会赋值给query
        this.$refs.searchDialogForm.resetFields();
        this.page = defaultFirstPage
        //重置
        history.replaceState(history.state,'',location.href.replace(queryPattern,''))
        this.$nextTick(() => {
          this.getList()
        })
        this.$emit('reset')
        this.$emit('update:customQuery',Object.assign(this.customQuery,JSON.parse(this.initCustomQuery)))
      },
      //高级查询查询
      seniorSearch(){
        this.$refs.searchDialogForm.validate(valid => {
          if(!valid) return
          this.beforeSearch().then(() => {
            this.page = defaultFirstPage
            this.getList()
          }).catch(err => {
            this.$emit('error',err)
          })
        })
      },
      handleSizeChange(val){
        if(this.size === val) return
        this.page = defaultFirstPage
        this.size = val
        this.getList()
      },
      handleCurrentChange(val) {
        if (this.page === val) return
        this.page = val
        this.getList()
      },
      handleSelectionChange(val, oldCurrentRow){
        //如果是单选则进入
        if(!this.mSelect){
          this.selected.length = 0;
          this.selected.push(val);
          this.$emit('selection-change',this.selected);
        }
      },
      mhandleSelectionChange(val){
        //如果是否选则处理
        if(this.mSelect){
          this.selected = val;
          this.hasSelect = val.length>1 ? true : false;
          /**
           * 多选启用时生效, 返回(selected)已选中行的数组
           * @event selection-change
           */
          this.$emit('selection-change',val)
        }
      },
      // 弹窗相关
      // 除非树形结构在操作列点击新增, 否则 row 都是 undefined
      onDefaultNew(cab,row = {}){
        this.row = row
        this.buttonState = true;
        this.isNew = true
        this.isEdit = false
        this.isView = false
        this.dialogTitle = this.dialogNewTitle
        this.dialogVisible = true
        if(this.onBeforeNew){
        	this.onBeforeNew()
        }
        	// 如果有默认数据，填充进表单
	        if(this.onDialogDefault){
	          this.onDialogDefault(row);
	          // 给表单填充值
	          this.$nextTick(() => {
	            this.$refs[dialogForm].updateForm(row)
	          })
	        }
        
        cab()
      },
      onDefaultView(cab,row){
        this.row = row
        this.buttonState = false;
        this.isView = true
        this.isNew = false
        this.isEdit = false
        this.dialogTitle = this.dialogViewTitle
        this.dialogVisible = true
        // 给表单填充值
        this.$nextTick(() => {
          this.$refs[dialogForm].updateForm(row)
        })
        cab()
      },
      onDefaultEdit(cab,row){
        this.row = Object.assign({},row)
        this.buttonState = true;
        this.isEdit = true
        this.isNew = false
        this.isView = false
        this.dialogTitle = this.dialogEditTitle
        this.dialogVisible = true
        if(this.onDialogDefault){
          this.onDialogDefault(this.row);
        }
        // 给表单填充值
        this.$nextTick(() => {
          this.$refs[dialogForm].updateForm(this.row)
        })
        cab()
      },
      cancel() {
        this.dialogVisible = false;
        this.$refs[dialogForm].resetFields();
      },
      //新增按钮事件
      confirm(){
        if (this.isView) {
          this.cancel()
          return
        }
        this.$refs[dialogForm].validate(valid => {
          if (!valid) return false
          let tdata = Object.assign({},this.$refs[dialogForm].getFormValue());
          if(this.isTree) {
            if (this.isNew) {
              tdata[this.treeParentKey] = this.row[this.treeParentValue]
            } else {
              tdata[this.treeParentKey] = this.row[this.treeParentKey]
            }
          }
          let data = changeBoolean(tdata);
          this.beforeConfirm(data,this.isNew).then(resp => {
            this.confirmLoading = true
            let condiction = 'isNew'
            let customMethod = 'onNew'
            if(this.isEdit){
              condiction = 'isEdit'
              customMethod = 'onEdit'
            }
            //外部处理钩子
            if(this[condiction] && this[customMethod]){
              this[customMethod](data,this.row).then(resp => {
                this.getList()
                if(resp.clear){
                  this.$refs[dialogForm].resetFields();
                }
                if(resp.close){
                  this.cancel()
                }
                this.showMessage(true,resp.successMessage)
                //onNew事件执行成功之后的回调方法 宋建国
                if(this.onAfterSuccessNew){
                  this.onAfterSuccessNew(data,this.row);
                }
              }).catch(e => {
                this.showMessage(false,e.message);
                if(this.onAfterFailNew){
                  this.onAfterFailNew(data,e.message);
                }
              })
              this.confirmLoading = false;
              return
            }

            // 默认新增/修改逻辑
            let url = Object.assign({},this.isEdit ? this.bEditUrl : this.bNewUrl);
            if(url.hostUrl === "" || url.url === ""){
              console.warn("没有填写接口地址，请核实");
              this.confirmLoading = false;
              return;
            }
            //外部处理上传数据
            if(this.isEdit){
              if(this.onManualEdit){
                this.onManualEdit(data,this.row);
              }
            }else if(this.isNew){
              if(this.onManualNew){
                this.onManualNew(data);
              }
            }
            axios(
                Object.assign({},url,{data:data})
            ).then((res) => {
              //onNew事件执行成功之后的回调方法 宋建国
              if(this.onAfterSuccessNew){
                this.onAfterSuccessNew(data,this.row);
              }
              if(url.clear){
                this.$refs[dialogForm].resetFields();
              }
              if(url.close){
                this.cancel()
              }
              this.getList();
              this.showMessage(true,this.isEdit ? "修改成功" : "新增成功");
              this.confirmLoading = false;
            }).catch((err) => {
              this.showMessage(false,err.message);
              if(this.onAfterFailNew){
                this.onAfterFailNew(data,err.message);
              }
              this.confirmLoading = false;
            })
          }).catch(e => {
            this.showMessage(false,e.message);
            if(this.onAfterFailNew){
              this.onAfterFailNew(data,e.message);
            }
          })
        })
      },
      onDefaultDelete(cab,row,state){
        let then = this;
        this.$confirm('确定删除吗？','提示',{
          type: 'warning',
          beforeClose: (action,instance,done) => {
            if(action == 'confirm'){
              let odata = [];
              if(state.toLowerCase()==="many"){
                for(let i=0; i<then.selected.length; i++){
                  odata.push(then.selected[i].id);
                }
              }else{
                odata.push(row.id);
              }
              instance.confirmButtonLoading = true
              if (then.onDelete){
                then.onDelete(
                  odata
                ).then(resp => {
                  then.showMessage(true,"删除成功")
                  cab()
                  done()
                  then.getList()
                  //删除后成功事件
                  if(then.onAfterSuccessDelete){
                    then.onAfterSuccessDelete(odata);
                  }
                }).catch(e => {
                	cab()
                  this.showMessage(true,e.message);
                  //删除后失败事件
                  if(this.onAfterFailDelete){
                    this.onAfterFailDelete(odata,e.message);
                  }
                }).finally(e => {
                	cab()
                  instance.confirmButtonLoading = false
                })
                return
              }
              //默认删除
              if(!then.bDeleteUrl.hostUrl || !then.bDeleteUrl.url){
                console.warn("服务器网址为空，不能发送请求");
                instance.confirmButtonLoading = false
                cab()
                return;
              }
              //外部处理上传数据
              if(this.onManualDelete){
                this.onManualDelete(odata);
              }
              odata = odata.filter(item => item)
              axios(
                Object.assign({},then.bDeleteUrl,{data:odata})
              ).then(res => {
                done()
                //删除后成功事件
                if(then.onAfterSuccessDelete){
                  then.onAfterSuccessDelete(odata);
                }
                then.getList()
                then.showMessage(true,"删除成功")
                instance.confirmButtonLoading = false
                cab()
              }).catch(err => {
                //删除后失败事件
                if(then.onAfterFailDelete){
                  then.onAfterFailDelete(odata,err.message);
                }
                then.showMessage(false,err.message)
                instance.confirmButtonLoading = false
                cab()
              });
            }else{
              done();
            }
          }
        }).catch(er => {
          /*取消*/
        })
      },
      onCustomButtonsClick(cab,fn, parameter) {
        if (!fn) return
        //this.customButtonsLoading = true
        fn(parameter).then(flag => {
        	  cab()
            if (flag === false) return
            this.getList()
          }).catch(e => {}).finally(e => {
            //this.customButtonsLoading = false
          })
      },
      // 树形table相关
      // https://github.com/PanJiaChen/vue-element-admin/tree/master/src/components/TreeTable
      tree2Array(data, expandAll, parent = null, level = null) {
        let tmp = []
        data.forEach(record => {
          if (record._expanded === undefined) {
            this.$set(record, '_expanded', expandAll)
          }
          let _level = 0
          if (level !== undefined && level !== null) {
            _level = level + 1
          }
          this.$set(record, '_level', _level)
          // 如果有父元素
          if (parent) {
            this.$set(record, 'parent', parent)
          }
          tmp.push(record)

          if (record[this.treeChildKey] && record[this.treeChildKey].length > 0) {
            const children = this.tree2Array(
              record[this.treeChildKey],
              expandAll,
              record,
              _level
            )
            tmp = tmp.concat(children)
          }
        })
        return tmp
      },
      showRow(row) {
        const show = row.row.parent
          ? row.row.parent._expanded && row.row.parent._show
          : true
        row.row._show = show
        return show
          ? 'animation:treeTableShow 1s-webkit-animation:treeTableShow 1s'
          : 'display:none'
      },
      // 切换下级是否展开
      toggleExpanded(trIndex) {
        const record = this.data[trIndex]
        record._expanded = !record._expanded
      },
      // 图标显示
      iconShow(index, record) {
        //      return index ===0 && record.children && record.children.length > 0;
        return record[this.treeChildKey] && record[this.treeChildKey].length > 0
      },
      showMessage(isSuccess = true, message="message没有传入提示信息") {
        if (isSuccess) {
          this.$message({
            type: 'success',
            message: message
          })
        } else {
          this.$message({
            type: 'error',
            message: message
          })
        }
      },
      //添加查询
      addHighFind(){
        let tIndex = this.searchDataForm.length;
        //克隆数据
        // 字段下拉框
        let tmpFieldsDown = clone(fieldsDown);
        tmpFieldsDown.$id = "field" + tIndex;
        tmpFieldsDown.$groupid = tIndex + '';
        tmpFieldsDown.$default = "";
        tmpFieldsDown.$el.size = "small";
        Object.assign(tmpFieldsDown.$options, this.fieldSelectValue);

        tmpFieldsDown.atChange = function (id, value) {
          //根据类型改变输查询值的输入组件方式
          then.searchDataForm.some((cvalue) => {
            if (cvalue.$id == this.$groupid) {
              then.allTableColumns.some((column) => {
                if (column.prop) {
                  if (value.toLocaleLowerCase() === column.prop.toLocaleLowerCase()) {
                    let tmpInputValue = clone(convertInput(column));
                    tmpInputValue.$id = "value" + cvalue.$id;
                    tmpInputValue.rules = null;
                    tmpInputValue.label = "数值";
                    then.iniInput(tmpInputValue,column);
                    Object.assign(cvalue.$items[2],tmpInputValue);
                    return true;
                  }
                }
              });
              return true;
            }
          });
        };

        //关系符
        let tmpRelationship = clone(relationship);
        tmpRelationship.$id = "gx" + tIndex;
        tmpRelationship.$el.size = "small";

        //并且或者
        let tmpAndOr = clone(andOr);
        tmpAndOr.$id = "andor" + tIndex;

        //查询值
        let inputWhere = clone(inputValue);
        inputWhere.$id = "value" + tIndex;
        inputWhere.rules = null;
        inputWhere.$groupid = tIndex + '';
        inputWhere.label = "数值";
        inputWhere.$el.size = "small";

        //删除按钮
        let deleteB = clone(deleteButton);
        deleteB.$id = "del" + tIndex;
        deleteB.$groupid = tIndex + "";
        deleteB.click = function(event){
          let idx = -1;
          for(let j=0; j < then.searchDataForm.length; j++) {
            if(then.searchDataForm[j].$id === this.$groupid){
              idx = j;
              break;
            }
          }
          if (idx>-1){
            then.searchDataForm.splice(idx,1);
          }
        };
        let then = this;

        this.searchDataForm.push({
          $id: tIndex + "",
          $type: "group",
          $quick: "0",
          $allTableColumns: this.allTableColumns,
          // $items:[tmpFieldsDown,tmpRelationship,inputWhere,tmpAndOr,deleteB]
          $items:[tmpFieldsDown,tmpRelationship,inputWhere,deleteB]
        });
      },
      searchOpen(){
        this.sValue= this.$refs.searchForm.getFormValue();
      },
      //选择数据暂存
      fInputSelect(prop,dicttype,value = []){
        if(this.inputSelect[prop]){
          return this.inputSelect[prop];
        }else {
          let tmpUrl = this.hostUrl + '/dict/findByType?type='+dicttype;
          let token = Cookies.get('token');
          let then = this;
          jquery.ajax({
            headers: {"token": token, "Content-Type": "application/json;charset=UTF-8"},
            url: tmpUrl,
            type: 'GET', //GET
            async: false,    //或false,是否异步
            data: {},
            timeout: 10000,    //超时时间
            dataType: 'json',    //返回的数据格式：
            beforeSend: function (xhr) {
            },
            success: function (data, textStatus, jqXHR) {
              then.inputSelect[prop] = [];
              data.data.forEach((value, idx) => {
                then.inputSelect[prop].push({key: value.id, label: value.label, value: value.value});
              });
            },
            error: function (xhr, textStatus) {
              return [];
              console.error(textStatus);
            },
            complete: function () {
            }
          });
          return then.inputSelect[prop];
        }
      },
      //初始化
      iniInput(inputWhere,column){
        if(inputWhere.$type === "select" || inputWhere.$type  === "radio-group" || inputWhere.$type  === "checkbox-group"){
          inputWhere.$options.length = 0;
          if(this.extraSelectData[column.prop.toLocaleLowerCase()]){
            if(this.inputSelect[column.prop.toLocaleLowerCase()]){
              inputWhere.$options = inputWhere.$options.concat(this.inputSelect[column.prop.toLocaleLowerCase()]);
              return ;
            }
            if(typeof this.extraSelectData[column.prop.toLocaleLowerCase()] === 'function'){
              let tmpdata = this.extraSelectData[column.prop.toLocaleLowerCase()](column);
              if(tmpdata){
                inputWhere.$options = inputWhere.$options.concat(tmpdata);
                this.inputSelect[column.prop.toLocaleLowerCase()] = tmpdata;
              }else{
                console.warn("外部函数没有返回值");
              }
            }
          }else{
            //系统根据设置请求字典数据
            if(column.dicttype){
              let tmpData = this.fInputSelect(column.prop.toLocaleLowerCase(),column.dicttype);
              inputWhere.$options = inputWhere.$options.concat(tmpData);
            }
          }
        }
      },
      //字段排序
      fieldSort(column){
        this.xyjSorter.length = 0;
        if(column.prop){
          let direc = column.order === "ascending" ? "ASC" : "DESC";
          this.xyjSorter.push({direction: direc,property: column.prop});
        }else{
          this.xyjSorter = this.xyjSorter.concat(this.sorter);
        }
        let query = {};
        if(this.sortVaule){
        	query = this.sortVaule()
        }
        this.getList(query);
      }
    },
    computed: {
      /**
       *  宋建国：20190329
       *  表格默认属性
       * */
      getTableAttrs(){
          let then = this;
          let tmp = {
          border: true,
          // headerCellStyle:{background:'#FCFCFC',color:'#606266',textalign:'center'},
          headerCellStyle:function ({row, column, rowIndex, columnIndex}){
            then.tableColumns.forEach((x,i) => {
              if(x.label === column.label){
                column.columncolor = x.columncolor
              }
            })  
            row.forEach(x => {
              if(x.children && x.children.length) {
                for(let i = 0,l = x.children.length;i<l;i++){
                  x.children[i].columncolor = x.columncolor
                  x.children[i].parentName = x.label
                }
              } 
            }) 
            if(column.label === "序号" || column.label === "操作") column.columncolor = '#FCFCFC'
            return 'background-color:'+column.columncolor
          },
          showSummary: true,
          summaryMethod: function (objData) {
            const tmpcolumns = objData.columns;
            let sums = [];
            tmpcolumns.forEach((column,index) => {
              if(column.property){
                let fieldname = column.property.toUpperCase();
                let tmpValue = then.summaryData == null? null:then.summaryData[fieldname];
                sums[index] = tmpValue ? tmpValue : "";
               }
            })
            return sums;
          }
        };
        return Object.assign(tmp,this.tableAttrs)
      },
      /**
       *  宋建国：20190329
       *  获取排序列
       * */
      getTableColumns(){
        return this.tableColumns.sort(function(a,b){
          return a.arrange-b.arrange
        })
      },
      /**
       *  宋建国：20190329
       *  获取快捷查询字段
       * */
      getQuickSearch(){
        let tmpQuick = this.getQuickSearchs.filter(function (column) {
          if(!column.$quick) return false;
          if(column.$quick === 1) {
            return true;
          } else {
            return false;
          }
        });
        return tmpQuick;
      },
      getdialogFrom(){
        let then = this;
        let tmpWidth = 0;
        let tmpdialogFrom = this.dialogFrom.filter(function (column) {
          let labelLength = column.label.replace(/[\u0391-\uFFE5]/g,"aa").length/2;
          if(tmpWidth<labelLength){
            tmpWidth = labelLength;
          }
          if(then.buttonState){
            if(column.$editshow === 1){
              return true;
            }else{
              return false;
            }
          }else{
            return true;
          }
        });
        then.xyjformAttrs.labelWidth = tmpWidth+2+"em";
        return tmpdialogFrom;
      },
      getImg(){
        let then = this;
        return then.image
      }
    },
    created () {
      var then = this;
      let tIndex = -1;
      // let loadingInstance = this.$loading({
      //   text:"玩命初始化窗体"
      // });
      // 判断是否需要修改表头数据
      let data = {};
      data = Object.keys(this.editAxios).length == 0 
      ? {
        hostUrl: this.hostUrl,
        url: '/sysTableinfo/findByTableName?tablename=' + this.tableName,
        method: 'get'
      } : this.editAxios
      //查询表格初始化数据
      axios(data).then((res) => {
        if (this.tableColumns.length === 0) {
          this.tableColumns = columnClone(res.data);
          //处理外部字段格式化
          this.tableColumns.forEach((column,index) =>{
            //设置是否有多选状态
            if(column.fieldtype.toLowerCase() === "selection"){
              this.mSelect = true;
            }
            if(this.formatters.hasOwnProperty(column.prop)){
              column.formatter = this.formatters[column.prop]
            }
          });
        }
        //
        this.allTableColumns = ArrangementColumns(this.tableColumns);
        //字段选择下拉框数据
        this.allTableColumns.forEach((column, index) => {
          if (!column.label && !column.prop) return;
          if (column.fieldtype === "selection") return;
          if(this.searchList && this.searchList.length>0){
          	this.searchList.forEach(response => {
	          	if(column.prop == response) {
	          		this.fieldSelectValue.push({key: column.prop, label: column.label, value: column.prop});
	          	}
	          })
          }else{
          	this.fieldSelectValue.push({key: column.prop, label: column.label, value: column.prop});
          }
          //整理汇总条件
          if(column.sumtype){
             this.summary.push({fieldName:column.prop,type:column.sumtype});
          }
        });

        //新增、删除、查看对话框输入组件信息
        if (this.dialogFrom.length <= 0) {
          this.allTableColumns.forEach((column, idx) => {
            if (column.fieldtype === "selection") return;
            let tmpInput = convertInput(column);
            this.iniInput(tmpInput,column);
            if(rules[column.rule]){
              tmpInput.rules = rules[column.rule];
            }
            tmpInput.$el.disabled =  column.edit == 1 ? false : true;
            if(tmpInput.$el.disabled){
              tmpInput.$el.placeholder = "系统自动生成"
            }
            tmpInput.$el.style = 'width: 100%';
            tmpInput.$editshow = column.editshow;

            //弹框中的控件格式化显示
            if(this.extraAttribute[column.prop]){
              tmpInput.$el = Object.assign({},tmpInput.$el,this.extraAttribute[column.prop]);
            }
            if(this.extraCheng[column.prop]){
              if(typeof this.extraCheng[column.prop] === 'function'){
                tmpInput.atChange = this.extraCheng[column.prop];
              }
            }
            this.dialogFrom.push(tmpInput);
          });
        }
        //所搜
        this.allTableColumns.forEach((column, index) => {
          if (!column.quick) return;
          if (column.quick !== 1) return;
          if (column.fieldtype === "selection") return;
          tIndex = tIndex + 1;

          // 字段下拉框
          let tmpFieldsDown = clone(fieldsDown);
          tmpFieldsDown.$id = "field" + tIndex;
          tmpFieldsDown.$groupid = tIndex + '';
          tmpFieldsDown.$default = column.prop;
          tmpFieldsDown.$el.size = "small";
          Object.assign(tmpFieldsDown.$options, this.fieldSelectValue);

          tmpFieldsDown.atChange = function (id, value) {
            //根据类型改变输查询值的输入组件方式
            then.searchDataForm.some((cvalue) => {
              if (cvalue.$id == this.$groupid) {
                then.allTableColumns.some((column) => {
                  if (column.prop) {
                    if (value.toLocaleLowerCase() === column.prop.toLocaleLowerCase()) {
                      let tmpInputValue = clone(convertInput(column));
                      tmpInputValue.$id = "value" + cvalue.$id;
                      tmpInputValue.rules = null;
                      tmpInputValue.label = "数值";
                      then.iniInput(tmpInputValue,column);
                      Object.assign(cvalue.$items[2],tmpInputValue);
                      return true;
                    }
                  }
                });
                return true;
              }
            });
          };

          //关系符
          let tmpRelationship = clone(relationship);
          tmpRelationship.$id = "gx" + tIndex;
          tmpRelationship.$el.size = "small";

          //并且或者
          let tmpAndOr = clone(andOr);
          tmpAndOr.$id = "andor" + tIndex;

          //查询值
          let inputWhere = convertInput(column);
          inputWhere.$id = "value" + tIndex;
          inputWhere.rules = null;
          inputWhere.$groupid = tIndex + '';
          inputWhere.label = "数值";
          this.iniInput(inputWhere,column);
          this.searchDataForm.push({
            $id: tIndex + '',
            $type: "group",
            $quick: column.quick,
            $allTableColumns: this.allTableColumns,
            // $items: [tmpFieldsDown, tmpRelationship, inputWhere, tmpAndOr]
            $items: [tmpFieldsDown, tmpRelationship, inputWhere]
          });
          this.getQuickSearchs.push({
            $id: tIndex + '',
            $type: "group",
            $quick: column.quick,
            $allTableColumns: this.allTableColumns,
            $items: [tmpFieldsDown, tmpRelationship,inputWhere]
          });
        });
        this.$nextTick(() => {
          this.getList(this.InitQuery);
        })
      }).catch((err) => {
        console.error(err);
      }).finally(err =>{
        // this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
        //   loadingInstance.close();
        // });
      })
    }
  }
</script>

<style lang="scss">
  .xyj-table{
    height: 100%;
  }
  .fade-enter-active, .fade-leave-active {
    transition: opacity 0.5s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }
  .xyj-tooltip{
    padding: 0px;
    .el-form-item{
      margin-bottom: 0;
    }
  }
  .el-data-table {
    color-blue : #2196F3;
    space-width : 18px;

    .serach-style {
      text-align:left;
      position: relative;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: flex-start;
      margin-left: 20px;
      .el-form-item{
        margin-bottom: 0!important;
        /*min-width: 282px;*/
        display: flex;
        align-items: center;
        justify-content: flex-end;
        .el-form-item__content>div{
          max-width: 150px;
          input{
            max-width: 150px;
          }
        }
      }
      .el-select{
        width: 100%!important;
      }
      &>div:nth-of-type(n+2){
        margin-top: 8px;
      }
      &>div>div{
        display: flex;
        justify-content: flex-end;
        &>div{
          display: flex;
          align-items: center;
          label{
            white-space: nowrap;
            text-align: right;
          }
        }
        &>div:nth-of-type(1){
          /*width: 200px;*/
        }
        &>div:nth-of-type(2){
          width: 185px;
        }
        &>div:nth-last-of-type(1){
          // width: 140px;
        }
      }
      &>div:nth-last-of-type(1){
        /*position: absolute;
        top: calc(50% - 16px);
        left: 1300px;*/
      }
    }
    .senior-serach-style {
      text-align:left;
      .el-form-item{
        margin-bottom: 0!important;
        //min-width: 282px;
        display: flex;
        align-items: center;
      }
      &>div>div{
        display: flex;
        //min-width: 750px;
        &>div{
          display: flex;
          align-items: center;
          label{
            white-space: nowrap;
          }
        }
        &>div:nth-last-of-type(1)>div{
          min-width: 40px!important;
        }
      }
    }
    .el-dialog{
      width: auto;
      display: inline-block;
      form>div{
        display: block !important;
        margin-bottom: 15px;
        &>div{
          &>div:nth-last-of-type(1) .el-form-item__content{
            margin: 0 7px!important;
          }
          &>div .el-form-item__label{
            width: auto!important;
            display: inline-block;
            margin-left: 10px;
            //min-width: 62px;
          }
          &>div .el-form-item__content{
            margin: 0!important;
            .el-select{
             // width: 100%!important;
            }
            &>div{
             // max-width: 200px;
            }
          }
        }
      }
      .el-dialog__body{
        padding: 0px 2px;
      }
    }
    .ms-tree-space {
      position: relative;
      top: 1px;
      display: inline-block;
      font-style: normal;
      font-weight: 400;
      line-height: 1;
      width: 400px;
      height: 14px;

      &::before {
        content: '';
      }
    }

    .tree-ctrl {
      position: relative;
      cursor: pointer;
      color: #FF4500;
    }

    .getInvestInfo{
      text-align: left;
      margin-bottom: 10px;
    }

    @keyframes treeTableShow {
      from {
        opacity: 0;
      }

      to {
        opacity: 1;
      }
    }
  }
  .el-input .el-input__count .el-input__count-inner{
    background: transparent!important;
  }
  .newDialog .el-dialog__body>form{
    padding-left: 0px;
    padding-right: 20px;
  }
.el-table--scrollable-x .el-table__body-wrapper{z-index: 2;}
.yellow{background-color: #FFFF93!important;}
#searchDialog{
	.senior-serach-style {
	  text-align:left;
	  &>div>div{
	    display: flex;
	  }
	}
	.el-dialog{
	  width: auto;
	  display: inline-block;
	  form>div{
      display: block !important;
      &>div{
        &>div:nth-last-of-type(1) .el-form-item__content{
          margin: 0 7px!important;
        }
        &>div .el-form-item__label{
          width: auto!important;
          display: inline-block;
          margin-left: 10px;
        }
        &>div .el-form-item__content{
          margin: 0!important;
          display: inline-block;
        }
      }
    }
    .el-dialog__body{
    	padding: 0;
    }
	}
}
</style>
