<template>
  <div class="page-container">
    <div class="header">
      <div class="header-left">
        <span class="el-icon-tickets"></span>
        <span class="sx"></span>
        <span>支付信息表</span>
      </div>
      <div class="header-right">
        <el-button @click="getList" class="fa fa-refresh" size="mini" type="primary">
          {{$t('action.refresh')}}
        </el-button>
      </div>
    </div>
    <el-table
    	id="table"
      v-loading="loading"
      :data="tableData.rows"
      border size="mini"
      max-height="550"
      element-loading-text="加载中"
      style="width: 100%">
      <el-table-column
        align="center"
        type="index"
        label="序号"
        min-width="60">
      </el-table-column>
      <el-table-column
        sortable
        v-for="item in columns"
        align="center"
        :key="item.label"
        :prop="item.prop"
        :formatter="item.formatter"
        :label="item.label"
        :min-width="item.minWidth">
      </el-table-column>
    </el-table>


    <!--分页-->
    <div class="pagination">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageRequest.page"
        :page-sizes="[10, 20, 30, 40,50]"
        :page-size="pageRequest.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="tableData.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

  import { format } from "@/utils/datetime";
  export default {
    name: "PayRecordController",
    data() {
      return {
        pageRequest: {
          page: 1,
          pageSize: 10,
          showTotal: true
        },
        tableData: [],
        loading:false,
        columns: [
          {label: '订单号', prop: 'prepayid', minWidth: '160'},
          {label: '业务类型', prop: 'ywtype', minWidth: '120'},
          {label: '订单是否处理', prop: 'isdispose', minWidth: '120',formatter:(row, column, cellValue, index) => {
              if (cellValue == 0) {
                return '否'
              }else if (cellValue == 1) {
                return '是'
              }
            }},
          {label: '是否付款成功', prop: 'codestate', minWidth: '120',formatter:(row, column, cellValue, index) => {
              if (cellValue == 0) {
                return '否'
              }else if (cellValue == 1) {
                return '是'
              }
            }},
          {label: '购买时间', prop: 'czdate', minWidth: '160',
            formatter:(row, column, cellValue, index) => {
            return format(row[column.property]);
            }},
          {label: '购买金额（单位：分）', prop: 'czprice', minWidth: '180'},
          {label: '实际付款时间', prop: 'fkdate', minWidth: '160',formatter:(row, column, cellValue, index) => {
              return format(row[column.property]);
            }},
          {label: '最近一次退款日期', prop: 'lasttkdate', minWidth: '160',formatter:(row, column, cellValue, index) => {
            if (cellValue == ''||cellValue == null) {
              return '无'
            }
              return format(row[column.property]);
            }},
          {
            label: '支付方式', prop: 'payment', minWidth: '140', formatter: (row, column, cellValue, index) => {
              if (cellValue == 0) {
                return '支付宝APP支付'
              } else if (cellValue == 1) {
                return '微信APP支付'
              } else if (cellValue == 2) {
                return '支付宝H5支付'
              } else if (cellValue == 3) {
                return '微信H5支付'
              }
            }
          },
          {label: '第三方支付交易号', prop: 'thirdcode', minWidth: '240'},
          {label: '累计退款金额（单位：分）', prop: 'tkmoney', minWidth: '190'},
          {label: '备注', prop: 'memo', minWidth: '140'},
        ],
      }
    },
    mounted() {
      this.getList();
    },
    methods: {

      handleSizeChange(val) {
        this.pageRequest.pageSize = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.pageRequest.page = val;
        this.getList();
      },
      getList() {
        let _this = this;
        _this.loading = true;
        _this.$api.common.xyjPostPay({
          url: '/sysPayRecord/query',
          data: {
            "pageBean": _this.pageRequest,
            "params": {},
            "sorter": []
          }
        }).then(res => {
          if (res.code == 200) {
            _this.tableData = res.data;
          }
        }).catch(err =>{
          this.$message.error(err.message)
        }).finally(fn =>{
          _this.loading = false;
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .header {
    display: flex;
    height: 38px;
    font-size: 14px;
    background-color: #F3F3F3;
    justify-content: space-between;
    align-items: center;
  }

  .header-left {
    margin-left: 10px;
  }

  .header-right {
    margin-right: 10px;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  .page-container,#table{
		height: 100%;
	}
	#table{
		/deep/
		&>div:nth-of-type(1){
			height: 93%;
			max-height: initial!important;
		}
	}
	#table /deep/ .el-table__body-wrapper{height:93%!important;}
</style>
