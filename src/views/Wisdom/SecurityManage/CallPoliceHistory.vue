<template>
  <div style="height: 100%;">
    <xyj-table ref="reftablamain" id="1" @update="getData" v-bind="tablemain">

    </xyj-table>
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
  import axios from "axios";
  import Cookies from "js-cookie";
  export default {
    name: "CallPoliceHistory",
    data() {
      return {
        flowPic: "",
        dialogVisible: false,
        orgOptions: {},
        tablemain: {
          //不显示表格操作列
          hasOperation: true,
          hasExport: true,//导出
          hasEdit:false,
          //el-table设置
          tableAttrs: {
            height: "86%",
            width: "800px",
          },
          //el-table事件设置
          tableOns: {},
          //表格中的操作列设置
          operationAttrs: {
            width: "200px"
          },
          extraButtons: [
            {
              label: "查看", text: "查看", atClick: (row) => {
                return new Promise((resolve,reject)=>{
                  this.openView(row);
                  resolve(false);
                })
              }
            }
          ],
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            status: function (row, column, cellValue, index) {
              switch (cellValue) {
                case '0':
                  return "在线";
                  break;
                case '1':
                  return "离线";
                  break;
                default:
                  return cellValue;
              }
            }
          },
          //single: false //单选
          //admin微服务网址
          hostUrl: this.global.baseUrl,
          //表初始化关键字，一般为表名
          tableName: "AlarmRecordVo",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.alarmUrl,
            url: "/alarmRecord/alarm",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: " AlarmRecordVo",//如果为空系统默认为，表初始化关键字
            fileName: ""//自定义文件名,如果为空则根据实体类名称定义下载文件名
          },
        },
      }
    },
    mounted(){
      if (this.$route.name && this.tablemain.bExport) {
        this.tablemain.bExport.fileName = this.$route.name;
      }
    },
    methods: {
      getData(data, res) {
        console.log("data", data);
        console.log("res", res);
        // this.drawLine(res.data["flow_percent"]);
        // this.drawLine2(res.data["monthflow_trends"]);
      },
      openView(row){
        console.log(row);
        this.dialogVisible = true;
        axios({
          methods: 'get',
          url: this.global.alarmUrl + "/cardholderInfo/photo/" + row.id,
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
      }

    }
  }
</script>

<style scoped>

</style>
