<template>
  <div style="height: 100%;">
    <div class="header">
      <div class="header-left">
        <el-button size="mini" type="primary" @click="goBack">返回</el-button>
      </div>
      <b>业主基本信息</b>
    </div>
    <el-form :model="ruleForm" ref="ruleForm" label-width="100px" disabled size="small" class="demo-ruleForm">
      <el-form-item label="注册日期" prop="name">
        <el-input v-model="ruleForm.bindDate"></el-input>
      </el-form-item>
      <el-form-item label="业主姓名" prop="region">
        <el-input v-model="ruleForm.ownerName"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="region">
        <el-input v-model="ruleForm.sex"></el-input>
      </el-form-item>
      <el-form-item label="用户昵称" prop="name">
        <el-input v-model="ruleForm.nikiName"></el-input>
      </el-form-item>
      <el-form-item label="手机号码" prop="region">
        <el-input v-model="ruleForm.phone"></el-input>
      </el-form-item>
      <el-form-item label="住房信息" prop="region">
        <el-input v-model="ruleForm.liveInfo"></el-input>
      </el-form-item>
    </el-form>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="绑卡信息" name="first">
        <el-table
          stripe border
          size="mini"
          :data="tableData.rows"
          style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="60"
            align="center">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in columns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            width="120"
            align="center">
          </el-table-column>

          <el-table-column
            width="120"
            align="center"
            label="识别图片">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="viewImg(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="报警记录" name="second">
        <el-table
          stripe border
          size="mini"
          :data="tableData2.rows"
          style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="60"
            align="center">
          </el-table-column>
          <el-table-column
            v-for="(item,index) in columns2"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            width="120"
            align="center">
          </el-table-column>
        </el-table>
        <el-row type="flex" justify="end" style="padding: 20px;">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="tableData2.page"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="tableData2.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="Number(tableData2.total)">
          </el-pagination>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      title="查看图片"
      :visible.sync="dialogVisible"
      width="50%">
      <img style="width: 100%;" :src="flowPic" alt="">
      <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
    <el-button size="mini" type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
  import axios from "axios"
  import Cookies from "js-cookie"

  export default {
    name: "OwnerCardView",
    props: ['rowId'],
    data() {
      return {
        dialogVisible: false,
        activeName: "first",
        ruleForm: {
          bindDate: "",
          ownerName: "",
          sex: "",
          nikiName: "",
          phone: "",
          liveInfo: "",
        },
        pageBean2:{
          page:1,
          pageSize:10,
        },
        tableData: {rows: []},
        tableData2: {rows: []},
        columns: [
          {prop: "bindDate", label: "绑卡日期", width: "120"},
          {prop: "bindStatus", label: "绑卡状态", width: "120"},
          {prop: "cardNo", label: "报警卡号", width: "120"},
          {prop: "cardStatus", label: "正常挂失", width: "120"},
          {prop: "lcoakStatus", label: "锁开关状态", width: "120"},
        ],
        columns2: [
          {prop: "alarmDate", label: "报警日期", width: "120"},
          {prop: "deviceNo", label: "门口机", width: "120"},
          {prop: "cardNo", label: "报警卡号", width: "120"},
        ],
        flowPic: "",
      }
    },
    watch: {
      rowId(val) {
        if (val) {
          this.getDetails(val);
          this.getInfo(val);

        }
      }
    },
    mounted(){
      if (this.$route.name && this.tablemain.bExport) {
        this.tablemain.bExport.fileName = this.$route.name;
      }
    },
    methods: {
      goBack() {
        this.$emit("goBack", {})
      },
      handleClick(tab, event) {
        console.log(tab, event);
      },
      getDetails(id) {
        this.$api.wisdom.alarmGet({
          url: "/cardholderInfo/detail/" + id,
        }).then(res => {
          if (res.code == 200) {
            this.ruleForm = res.data;
            this.getHistory(res.data);
          }
        })
      },
      getInfo(id) {
        this.$api.wisdom.alarmPost({
          url: "/cardholderInfo/bindDetail",
          data: {
            querys: [
              {
                "hasInitValue": true,
                "operation": "EQUAL",
                "property": "id",
                "relation": "AND",
                "value": id
              }
            ]
          }
        }).then(res => {
          if (res.code == 200) {
            this.tableData = res.data;
          }
        })

      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageBean2.pageSize = val;
        this.getHistory();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageBean2.page = val;
        this.getHistory();
      },
      getHistory() {
        this.$api.wisdom.alarmPost({
          url: "/alarmRecord/findByOpenId",
          data: {
            pageBean:this.pageBean2,
            querys: [
              {
                "hasInitValue": true,
                "operation": "EQUAL",
                "property": "openid",
                "relation": "AND",
                "value": this.ruleForm.openid
              }
            ]
          }
        }).then(res => {
          if (res.code == 200) {
            this.tableData2 = res.data;
          }
        })

      },
      viewImg(row) {
        this.dialogVisible = true;
        axios({
          methods: 'get',
          url: this.global.alarmUrl + "/cardholderInfo/photo/" + row.bindId,
          headers: {
            'token': Cookies.get("token"),
            "content-type": "application/json;charset=UTF-8"
          },
          responseType: 'blob',
          params: {},
        }).then(response => {
          let blob = new Blob([response.data]);
          var reader = new FileReader();
          reader.readAsDataURL(blob);
          reader.onload = (e) => {
            this.flowPic = e.target.result;
          };
        }).catch(res => {
          this.$message({type: "error", message: res.message});
        });

      },



    }
  }
</script>

<style scoped lang="scss">
  .header {
    background-color: #f5f5f5;
    position: relative;
    text-align: center;
    height: 38px;

    .header-left {
      position: absolute;
      left: 0;
      top: 5px;
      margin-left: 10px;
    }

    b {
      line-height: 38px;
      text-align: center;
    }
  }

  .el-form {
    margin-top: 10px;
    display: flex;
    flex-wrap: wrap;

    .el-form-item {
      width: 33%;
    }
  }
</style>
