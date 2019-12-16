<template>
  <div>
    <xyj-table ref="reftablamain" id="1" v-bind="tablemain"></xyj-table>
    <el-row type="flex" justify="end">
      <img style="height: 300px;" :src="flowPic" alt="">
    </el-row>
  </div>
</template>

<script>
  import axios from "axios";
  import Cookies from "js-cookie";
  export default {
    name: "OpendoorHistory",
    data() {
      return {
        flowPic:"",
        tablemain: {
          //不显示表格操作列
          hasOperation: false,
          hasExport: true,//导出
          //el-table设置
          tableAttrs: {
            height: "400px",
            width: "800px",
          },
          //el-table事件设置
          tableOns: {
            "row-click": this.rowclick,
            // "row-dblclick": (row, column, event) => {
            //   //  双击隐藏其他行
            //   this.$nextTick(() => {
            //     //  复制所有的数据
            //     this.text = [];
            //     if(this.$refs.reftablamain.data.length>1) this.text = JSON.parse(JSON.stringify(this.$refs.reftablamain.data));
            //     //  只展示当前选中行
            //     this.$refs.reftablamain.data.length = 0;
            //     this.$refs.reftablamain.data.push(row);
            //     this.$refs.reftablamain.$el.style.display = 'none';
            //     this.btnShow = true;
            //   });
            // },
          },
          //表格中的操作列设置
          operationAttrs: {
            width: "200px"
          },
          //格式化表格字段显示，按照类属性定义格式化函数
          formatters: {
            state: function (row, column, cellValue, index) {
              switch (cellValue) {
                case 0:
                  return "编辑中";
                  break;
                case 1:
                  return "审批中";
                  break;
                case 2:
                  return "完成";
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
          tableName: "DfGateopen2",
          //查询时的排序字段
          sorter: [],
          //业务数据微服务,查询与高级查询
          bQuery: {
            hostUrl: this.global.doorUrl,
            url: "dfGateopen2/openDoor",
            method: 'post',
            notCondition: true //没有查询条件则查询全部数据
          },
          InitQuery: {}, //表格初始化时查询条件
          bExport: {
            // url: baseUrl+"/sysTableinfo/query", //查询接口，如果不填写默认为表格查询网址bQuery
            tableName: "DfGateopen2",//如果为空系统默认为，表初始化关键字
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
      rowclick(row) {
        console.log(row);
        let photo = escape(row.photo);
        axios({
          methods: 'get',
          url: this.global.doorUrl + "/dfGateopen2/download/" + '?photo=' + photo,
          headers: {
            'token': Cookies.get("token"),
            "content-type": "application/json;charset=UTF-8"
          },
          data:{},
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
