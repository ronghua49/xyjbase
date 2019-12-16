<template>
	<div style="height: 100%;">
		<el-row v-if="btnShow" style="height: 100%;">
			<el-col :span="11"><span style="display:inline-block;background:#ccc;height:1px;width:100%;vertical-align: middle;"></span></el-col>
			<el-col :span="1">
				<i @click="showTable" class="el-icon-arrow-down"></i>
			</el-col>
			<el-col :span="12"><span style="display:inline-block;background:#ccc;height:1px;width:100%;vertical-align: middle;"></span></el-col>
		</el-row>
		<div style="height: 100%;">
			<xyj-table ref="reftablamain" id="1" v-bind="tablemain" style="height: 100%;">
				<template slot="xyjcontainer">
					<!--          <el-button ref="btnr" class="btnclass" style="padding: 7px;" type="primary" size="mini" @click="openDetails(reject)"  >查看</el-button>-->
					<Xyj-button type="primary" :disabled="isDisabledCheck" size="mini" @click="openDetails" :label="$t('xyftf.view')" text="查看"></Xyj-button>
				</template>
			</xyj-table>
		</div>

		<el-dialog title="账单详情" :visible.sync="centerDialogVisible" width="50%" center>
			<template>
				<el-row style="line-height: 32px;padding-bottom: 10px;">
					<el-col :span="12">账单状态：{{form.status}}</el-col>
					<el-col :span="12">账单日期: {{form.ordertime}}</el-col>
					<el-col :span="12">账单金额：{{form.paymoney}}</el-col>
					<el-col :span="12">订单编号：{{form.ordercode}}</el-col>
					<el-col :span="12">用户昵称：{{form.nickname}}</el-col>
					<el-col :span="12">设备编码：{{form.deviceno}}</el-col>
					<el-col :span="12">手机号码：{{form.mobile}}</el-col>
					<el-col :span="12">设备位置：{{form.city+form.dist+form.address}}</el-col>
				</el-row>

				<el-table :data="tableData" border stripe size="mini" style="width: 100%">
					<el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
					<el-table-column prop="papername" label="纸张规格" min-width="120" align="center"></el-table-column>
					<el-table-column prop="color2side" label="类型" min-width="120" align="center"></el-table-column>
					<el-table-column prop="price" label="单价(张)" min-width="120" align="center"></el-table-column>
					<el-table-column prop="papernum" label="数量" min-width="120" align="center"></el-table-column>
				</el-table>
			</template>
			<!--<span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="centerDialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="centerDialogVisible = false">确 定</el-button>
      </span>-->
		</el-dialog>
		<!-- <div>
      <xyj-table ref="reftableitem" id="2" v-bind="tableitem">
        <template slot="xyjcontainer">
          <Xyj-button style="padding: 7px;" type="primary" @click="chooseType" :label="$t('action.chooseVehicleType')"
                      :disabled="isDisabled"/>
          <Xyj-button
            style="position:relative;margin-left: 0px; padding: 7px;"
            type="primary"
            @click="downLoadBaseData"
            :label="$t('action.export')"/>
          <Xyj-button style="margin-left: 0px;padding: 7px;" type="primary" @click="downloadBlank"
                      :label="$t('action.downloadTemplate')"/>
          <el-upload
            style="position:relative;display:inline-block;"
            action=""
            :http-request="importExcel"
            :show-file-list="false"
            :disabled="isDisabled"
          >
            <Xyj-button style="padding: 7px;" type="primary" :label="$t('action.Import')" :disabled="isDisabled"/>
            &lt;!&ndash; 上传进度条 &ndash;&gt;
            <el-progress
              id="progress"
              :text-inside="true"
              :stroke-width="32"
              color="#e8f3f5"
              v-if="progress.tableFlag == true"
              type="line"
              :percentage="progress.UploadPercent"
            ></el-progress>
          </el-upload>
          <Xyj-button style="padding: 7px;" type="primary" :label="$t('action.submit')" @click="submission"
                      :disabled="isDisabled"/>
        </template>
        <template slot="expandContent" slot-scope="scope">
          &lt;!&ndash; <xyj-table ref="reftablamain"  id="1" v-bind="tablemain" @update="findafter"> </xyj-table> &ndash;&gt;
        </template>
        <template slot="getInvestInfo">
          <el-row>
            <el-col
              :xl="4"
              :lg="5"
              :md="6"
              :sm="9"
              :xs="12"
              v-for="(item,index) in info"
              :key="index"
            >{{item.name}}：{{item.key}}
            </el-col>
          </el-row>
        </template>
      </xyj-table>
    </div>-->
		<!--选择车型dialog弹出框-->
		<!--    <choose-car-type ref="carshow" :paramsTrans="paramsTrans" :onOk="carSeledtOk"></choose-car-type>-->
		<!--    <approval-dialog ref="action"></approval-dialog>-->
	</div>
</template>

<script>
	import ChooseCarType from "../../../components/bcomponents/chooseCarType";
	import axios from "@/http/axios";
	import utilmp from "@/utils/other/mp/mputil";

	export default {
		name: "OrderDetails",
		components: {
			ChooseCarType
		},
		data() {
			return {
				isDisabledCheck: true,
				selectRow: "",
				btnShow: false,
				qmodify: utilmp.QMODIFY,
				approvaling: utilmp.APPROVALING,
				reject: utilmp.REJECT,
				complete: utilmp.COMPLETE,
				ticketcancel: utilmp.TICKETCANCEL,
				isDisabled: false,
				text: [],
				info: [],
				progress: {
					tableFlag: false,
					UploadPercent: 0
				},
				paramsTrans: {
					hostUrl: this.global.lyBaseUrl,
					url: "/tIcBuCarType/getCar", //查询车型
					method: "get",
					confirmParms: {
						basecar: "",
						basestage: "",
						baseCarVersionId: "",
						hostUrl: this.global.lyBudgetUrl,
						url: "/tIcBuStampingMaterial/baseCarConfirm", //确定选择车型
						method: "post",
						params: {
							versionId: "",
							baseVersionId: ""
						}
					}
				}, //参数传递
				tablemain: {
					//不显示表格操作列
					hasOperation: false,
					hasPermission: true,
					//el-table设置
					tableAttrs: {
						height: "86%",
						width: "800px"
					},
					paginationSize: 20,
					//el-table事件设置
					tableOns: {
						'select': (selection, row) => {
							if(selection.length == 1) {
								this.isDisabledCheck = false;
								this.selectRow = selection[0];
							} else {
								this.isDisabledCheck = true;
								//this.selectRow = "";
							}
						},
						'select-all': (selection) => {
							this.isDisabledCheck = true;
						},
					},
					//表格中的操作列设置
					operationAttrs: {
						width: "200px"
					},
					//格式化表格字段显示，按照类属性定义格式化函数
					formatters: {
						status: function(row, column, cellValue) {
							if(cellValue == 1) {
								return "已支付";
							} else {
								return "待支付"
							}
						}
					},
					//single: false //单选
					//admin微服务网址
					hostUrl: this.global.baseUrl,
					//表初始化关键字，一般为表名
					tableName: "AppOrderMain",
					//查询时的排序字段
					sorter: [],
					//业务数据微服务,查询与高级查询
					bQuery: {
						hostUrl: this.global.hostUrl86,
						url: "appOrderMain/query",
						method: "post",
						notCondition: true //没有查询条件则查询全部数据
					},
					InitQuery: {
						// 0: { field0: "state", gx0: "IN", value0: utilmp.QMODIFY, andor0: "AND" }
					} //表格初始化时查询条件
				},
				//      tableitem: {
				//        hasDelete: false, //删除
				//        hasNew: false, //新增
				//        hasView: true, //查看
				//        hasEdit: false, //编辑
				//        isExpand: false, // 表格展开行
				//        disabledNew: false, // 新增按钮是否禁用
				//        //el-table设置
				//        tableAttrs: {
				//          // height: "400px",
				//          width: "800px"
				//        },
				//        //el-table事件设置
				//        tableOns: {
				//          
				//        },
				//        //表格中的操作列设置
				//        operationAttrs: {
				//          width: "130px"
				//        },
				//        //admin微服务网址
				//        hostUrl: this.global.baseUrl,
				//        //表初始化关键字，一般为表名
				//        tableName: "TIcBuStampingMaterial",
				//        //查询时的排序字段
				//        sorter: [],
				//        //查询时固定查询条件，对象下标必须从100开始向上加
				//        customQuery: {
				//          100: {
				//            field100: "versionId",
				//            gx100: "EQUAL",
				//            value100: null,
				//            andor100: "AND"
				//          }
				//        },
				//        //业务数据微服务,查询与高级查询
				//        bQuery: {
				//          hostUrl: this.global.lyBudgetUrl,
				//          url: "tIcBuStampingMaterial/query",
				//          method: "post",
				//          notCondition: false //没有查询条件则不查询数据
				//        },
				//        //业务数据微服务,新增网址
				//        bNewUrl: {
				//          hostUrl: this.global.lyBudgetUrl,
				//          url: "tIcBuStampingMaterial/save",
				//          method: "post",
				//          clear: true, //清除新增对话框输入的数据
				//          close: true //关闭新增对话框
				//        },
				//        //业务数据微服务,修改网址
				//        bEditUrl: {
				//          hostUrl: this.global.lyBudgetUrl,
				//          url: "tIcBuStampingMaterial/save",
				//          method: "post",
				//          clear: true, //清除新增对话框输入的数据
				//          close: true //关闭修改对话框
				//        },
				//        //业务数据微服务,删除网址
				//        bDeleteUrl: {
				//          hostUrl: this.global.lyBudgetUrl,
				//          url: "/tIcBuStampingMaterial/delete",
				//          method: "delete"
				//        },
				//        //当点击修改时，默认修改调用前处理上传数据时
				//        onManualEdit: function (data, row) {
				//          if (data) {
				//            data.id = row.id;
				//            data.versionId = row.versionId;
				//          }
				//        },
				//        //当点击新增时，默认新增调用前处理上传数据时
				//        onManualNew: this.reftableitemManualNew,
				//        //当点击删除时，默认新增调用前处理上传数据时
				//        onManualDelete: function (data) {
				//        },
				//        //弹框中的Change事件
				//        extraCheng: {
				//          material: this.materialChange
				//        }
				//      },
				//      ruleForm: {
				//        name: "",
				//      },
				centerDialogVisible: false,
				tableData: [],
				form: {}
			};
		},
		methods: {
			//    showTable() {
			//      let row = {};
			//      if (this.text.length > 1) {
			//        this.text.forEach(element => {
			//          // 需要高亮的数据
			//          if (this.$refs.reftablamain.data[0].versionId === element.versionId) row = element;
			//        });
			//        this.$refs.reftablamain.data = this.text;
			//      } else {
			//        row = this.$refs.reftablamain.data[0]
			//      }
			//      this.$refs.reftablamain.$refs.table.setCurrentRow(row);
			//      this.$refs.reftablamain.$el.style.display = 'block';
			//      this.btnShow = false;
			//    },
			//    materialChange: function (prpo, trimVal) {
			//      this.$refs.reftableitem.$refs.dialogForm.setOptions("brandNo", [
			//        {label: "文本框", value: "input"}
			//      ]);
			//    },
			//    reftableitemManualNew: function (data) {
			//      if (data) {
			//        data.versionId = this.$refs.reftablamain.selected[0].versionId;
			//      }
			//    },
			//    
			//    findafter: function (data, res) {
			//      let _this = this;
			//
			//      function updata(data) {
			//        _this.tableitem.customQuery[100] = Object.assign(
			//          {},
			//          _this.tableitem.customQuery[100],
			//          {value100: data}
			//        );
			//        _this.$nextTick(() => {
			//          //刷新数据
			//          // _this.$refs.reftableitem.getList();
			//        });
			//      }
			//
			//      if (data.length <= 0) {
			//        updata(-2)
			//        return;
			//      }
			//      this.paramsTrans.confirmParms.basecar = data[0].basecar; //车型
			//      this.paramsTrans.confirmParms.basestage = data[0].basestage; //阶段
			//      this.paramsTrans.confirmParms.params.versionId = data[0].versionId; //主表版本id
			//      this.paramsTrans.confirmParms.baseCarVersionId = data[0].baseCarVersionId; //是否是基点车
			//      this.$refs.reftablamain.$refs.table.setCurrentRow(data[0]);
			//      updata(data[0].versionId);
			//      this.getinfo(data[0].versionId);
			//      utilmp.checkDisabled(data[0].state, (disabled) => {
			//        //处理按钮是否禁用，disabled=true代表按钮禁用，false代表按钮不禁用
			//        this.isDisabled = disabled;
			//        this.tableitem.disabledNew = disabled;
			//      });
			//    },
			//    //选择车型
			//    chooseType() {
			//      this.$refs.carshow.open(true);
			//    },
			//    carSeledtOk(valueId) {
			//      this.tableitem.customQuery[100] = Object.assign(
			//        {},
			//        this.tableitem.customQuery[100],
			//        {value100: valueId}
			//      );
			//      this.$nextTick(() => {
			//        //刷新数据
			//        // this.$refs.reftableitem.getList();
			//      });
			//    },
			//    downLoadBaseData() {
			//      // 导出
			//      if (this.$refs.reftableitem.$refs.table.data.length <= 0) {
			//        this.$message.warning("没有明细数据无需导出");
			//        return;
			//      }
			//      axios({
			//        // 用axios发送post请求
			//        hostUrl: this.global.lyBudgetUrl,
			//        method: "post",
			//        url:
			//          "/tIcBuStampingMaterial/downLoadBaseData?mid=" +
			//          this.$refs.reftableitem.$refs.table.data[0].materialId, // 请求地址
			//        responseType: "blob" // 表明返回服务器返回的数据类型
			//      })
			//        .then(res => {
			//          // 处理返回的文件流
			//          const blob = new Blob([res.data], {
			//            type: "application/vnd.ms-excel;charset=utf-8"
			//          });
			//          if ("download" in document.createElement("a")) {
			//            // 非IE下载
			//            const elink = document.createElement("a");
			//            elink.download = decodeURIComponent(res.filename);
			//            elink.style.display = "none";
			//            elink.href = URL.createObjectURL(blob);
			//            document.body.appendChild(elink);
			//            elink.click();
			//            URL.revokeObjectURL(elink.href); // 释放URL 对象
			//            document.body.removeChild(elink);
			//          } else {
			//            // IE10+下载
			//            navigator.msSaveBlob(blob, decodeURIComponent(res.filename));
			//          }
			//        })
			//        .catch(err => {
			//          this.$message.error(err.message);
			//        });
			//    },
			//    importExcel(file) {
			//      // 导入
			//      let _this = this;
			//      let fd = new FormData();
			//      fd.append("file", file.file);
			//      fd.append("versionId", this.paramsTrans.confirmParms.params.versionId);
			//      axios({
			//        hostUrl: this.global.lyBudgetUrl,
			//        url: "/tIcBuStampingMaterial/importExcel", //导入
			//        method: "post",
			//        data: fd,
			//        onUploadProgress: function (progressEvent) {
			//          // 进度条
			//          if (progressEvent.lengthComputable) {
			//            _this.progress.tableFlag = true;
			//            _this.$nextTick(() => {
			//              _this.progress.UploadPercent =
			//                (progressEvent.loaded / progressEvent.total) * 100;
			//              if (_this.progress.UploadPercent == 100)
			//                setTimeout(() => {
			//                  _this.progress.tableFlag = false;
			//                }, 1000);
			//            });
			//          }
			//        }
			//      })
			//        .then(res => {
			//          this.$nextTick(() => {
			//            // 导入成功提示
			//            this.$message({message: "导入成功", type: "success"});
			//            //刷新数据
			//            this.$refs.reftableitem.getList();
			//          });
			//        })
			//        .catch(err => {
			//          this.$message.error(err.message);
			//        });
			//    },
			//    downloadBlank() {
			//      // 下载模板
			//      let fd = new FormData();
			//      fd.append("versionId", this.$refs.reftablamain.selected[0].versionId);
			//      axios({
			//        // 用axios发送post请求
			//        hostUrl: this.global.lyBudgetUrl,
			//        method: "post",
			//        url: "/tIcBuStampingMaterial/downloadBlank", // 请求地址
			//        data: fd,
			//        responseType: "blob" // 表明返回服务器返回的数据类型
			//      })
			//        .then(res => {
			//          // 处理返回的文件流
			//          const blob = new Blob([res.data], {
			//            type: "application/vnd.ms-excel;charset=utf-8"
			//          });
			//          if ("download" in document.createElement("a")) {
			//            // 非IE下载
			//            const elink = document.createElement("a");
			//            elink.download = decodeURIComponent(res.filename);
			//            elink.style.display = "none";
			//            elink.href = URL.createObjectURL(blob);
			//            document.body.appendChild(elink);
			//            elink.click();
			//            URL.revokeObjectURL(elink.href); // 释放URL 对象
			//            document.body.removeChild(elink);
			//          } else {
			//            // IE10+下载
			//            navigator.msSaveBlob(blob, decodeURIComponent(res.filename));
			//          }
			//        })
			//        .catch(err => {
			//          this.$message.error(err.message);
			//        });
			//    },
			//    getinfo(versionId) {
			//      axios({
			//        hostUrl: this.global.lyBudgetUrl,
			//        method: "get",
			//        url: "/carVersion/getMaterialsInfo?versionId=" + versionId + "&type=1" // 请求地址
			//      })
			//        .then(res => {
			//          this.info = [];
			//          for (let arr in res) {
			//            this.info.push({'name': res[arr].key});
			//          }
			//          this.info = this.info.map((x, j) => {
			//            x.key = Object.values(res)[j].value;
			//            return x;
			//          });
			//        })
			//        .catch(err => {
			//          this.info = [];
			//          // this.$message.error(err.message);
			//        });
			//    },
			//    /**
			//     * 宋建国：2019-06-24 06:08:34
			//     * 描述：审批发起
			//     */
			//    submission() {
			//      if (this.$refs.reftableitem.data.length <= 0) {
			//        this.$message.warning('空数据无法提交');
			//        return;
			//      }
			//      let tname = this.tableitem.tableName;
			//      let carselect = this.$refs.reftablamain.selected[0];
			//      let business_no = this.$refs.reftableitem.data[0].materialId;
			//      utilmp.clickApproval.call(this, business_no, (rows) => {
			//        rows['business_no'] = business_no;
			//        let carinfo = {
			//          carversionId: carselect.versionId,
			//          versio: carselect.version,
			//          cartyp: carselect.cartype,//"G88A",
			//          stage: carselect.stage,//"阶段",
			//          platform: carselect.platform,//"平台",
			//          actoryName: carselect.factoryName,// "工厂名称",
			//          sopdate: carselect.sopdate,// "SOP日期",
			//          ncontractdate: carselect.contractdate,//"合同日期",
			//          basecar: carselect.basecar,// "基点车",
			//          basestage: carselect.basestage,// "节点车阶段",
			//          tablename: tname,
			//          bQuery: this.tableitem.bQuery,
			//          hostUrl: this.tableitem.hostUrl,
			//          fieldname: 'materialId'
			//        }
			//        this.$refs.action.open(rows, carinfo, () => {
			//          let findwhere = {
			//            0: {field0: "state", gx0: "IN", value0: this.qmodify, andor0: "AND"}
			//          }
			//          this.$refs.reftablamain.getList(findwhere);
			//        });
			//      });
			//    },
			//    /**
			//     * 宋建国：2019-06-28 11:52:41
			//     * 描述：快捷查询
			//     */
			//    findcardate(findvalue) {
			//      var obj = document.getElementsByClassName("btnclass");
			//      for (let i = 0; i < obj.length; i++) {
			//        obj[i].style.fontWeight = "normal"
			//      }
			//      if (findvalue == "-1") {
			//        this.$refs.btnq.$el.style.fontWeight = "bold";
			//      } else if (findvalue == "0,1,2") {
			//        this.$refs.btna.$el.style.fontWeight = "bold";
			//      } else if (findvalue == "6") {
			//        this.$refs.btnr.$el.style.fontWeight = "bold";
			//      } else if (findvalue == "9") {
			//        this.$refs.btnc.$el.style.fontWeight = "bold";
			//      } else if (findvalue == "7") {
			//        this.$refs.btnt.$el.style.fontWeight = "bold";
			//      }
			//      let findwhere = {
			//        0: {field0: "state", gx0: "IN", value0: findvalue, andor0: "AND"}
			//      }
			//      this.$refs.reftablamain.getList(findwhere);
			//    },
			openDetails() {
				this.form = JSON.parse(JSON.stringify(this.selectRow));
				if(this.form.status == "1") {
					this.form.status = "已支付";
				} else {
					this.form.status = "待支付";
				}
				this.centerDialogVisible = true;
				axios({
					hostUrl: this.global.hostUrl85,
					method: "POST",
					url: "/appOrderSub/query", // 请求地址
					data: {
						"pageBean": {
							"page": 0,
							"pageSize": 0,
							"showTotal": true
						},
						"querys": [{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "ordermainid",
							"relation": "AND",
							"value": this.form.id
						}]
					}
				}).then(res => {
					this.tableData = res.data.rows;
				})
			}
		}
	};
</script>

<style scoped>
	#progress {
		position: absolute;
		top: 0;
		width: 100%;
	}
	
	/deep/ .el-progress-bar__outer,
	/deep/ .el-progress-bar__inner {
		border-radius: 0 !important;
	}
</style>