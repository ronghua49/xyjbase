import MD5 from "@/utils/md5-0.4";
import ErrorInfo from "@/utils/other/mp/errorinfo";

//编辑中
export const QMODIFY = '-1';
//审批中
export const APPROVALING = '0,1,2';
//驳回
export const REJECT = '6';
//流程结束
export const COMPLETE = '9';
//作废
export const TICKETCANCEL = '7';

//MD5加密
export function encryption(str){
  return MD5(str);
}

export default {
  encript: function (pws,userid) {
    let tmpstr = encryption(pws); //1
    tmpstr = encryption(tmpstr.toLowerCase()); //2
    tmpstr = encryption(tmpstr.toLowerCase());//3
    tmpstr = userid + tmpstr.toLowerCase();
    tmpstr = encryption(tmpstr);//4
    tmpstr = encryption(tmpstr.toLowerCase());//5
    tmpstr = encryption(tmpstr.toLowerCase());//6
    tmpstr = encryption(tmpstr.toLowerCase());//7
    let tmpCode = Math.random().toString().substring(2);
    tmpstr = tmpstr.toLowerCase() + tmpCode;
    tmpstr = encryption(tmpstr);//8
    tmpstr = tmpstr.toLowerCase();
    return {randomCode: tmpCode,password:tmpstr};
  },
  /**
  * 宋建国：2019-06-24 06:01:29
  * 描述：审批发起
  */
  clickApproval:function (businessno,callback) {
    let url = this.$route.path;
    let workid = this.$workConfig.getItem(url) ? this.$workConfig.getItem(url).workid : '';
    if (workid.length<=0){
      this.$message.warning('没有找到审批流模板，请联系系统管理员');
      return;
    }
    /**
    * 宋建国：2019-07-02 11:23:36
    * 描述：查询审批数据
    */
    this.$api.expapi.pending({
      pagesize: 10,
      pageindex: 1,
      sort: 'desc',
      sortfield: 'psrocessStartTime',
      qmode: 1,
      businessno: businessno
    }).then(pendres => {
      if(pendres.result !== '1'){
        let message = ErrorInfo.ERRORINFO[pendres.msg]
        this.$message.error(message);
        return;
      }
      let isstart = true;
      let nodeid = '';
      let pending_type = 0;
      if(pendres.rows.length>0){
        isstart = 0;
        nodeid = pendres.rows[0].NODE_GUID;
        pending_type = pendres.rows[0].PENDING_TYPE
      }
      this.$api.expapi.stephandler({
        isstart: isstart,  //是否发起流程,true表示发起流程，0：查找审批流设置
        wfid: workid,  //对应工作流菜单ID
        nodeid: nodeid,//流程实例节点id,  对应代办数据 "NODE_GUID"
        formname: '',//流程表单名,
        pending_type: pending_type
      }).then(res => {
        if(res.result !== '1'){
          let message = ErrorInfo.ERRORINFO[res.msg]
          this.$message.error(message);
          return;
        }
        if(pendres.rows.length>0){
          res.rows['wfid'] = pendres.rows[0].NODE_GUID;
        }else{
          res.rows['wfid'] = workid;
        }
        callback(res.rows);
      }).catch(err => {
        this.$message.error(err.message);
      })

    }).catch(err => {
      this.$message.error(err.message);
    })
  },
  /**
  * 宋建国：2019-06-26 11:39:59
  * 描述：审批状态转换为中文
  */
  approvalStateChine: function (row, column, cellValue) {
    switch (cellValue) {
      case -1:
        return "编辑中";
        break;
      case 0:
        return "已发起审批";
        break;
      case 1:
        return "审批中";
        break;
      case 2:
        return "重新发起";
        break;
      case 4:
        return "沟通";
        break;
      case 5:
        return "签提交";
        break;
      case 6:
        return "驳回";
        break;
      case 7:
        return "作废";
        break;
      case 8:
        return "转办";
        break;
      case 9:
        return "流程结束";
        break;
      case 10:
        return "重设责任人";
        break;
      case 11:
        return "发起人指定责任人";
        break;
      case 12:
        return "提醒处理";
        break;
      case 13:
        return "草稿";
        break;
      case 14:
        return "暂存";
        break;
      case 15:
        return "已办中追加";
        break;
      case 18:
        return "回收";
        break;
      case 19:
        return "唤醒";
        break;
      case 20:
        return "删除";
        break;
      case 21:
        return "已办抄送";
        break;
      case 22:
        return "跳转";
        break;
      case 24:
        return "带条件审批";
        break;
      case 25:
        return "查看抄送待办";
        break;
      default:
        return cellValue;
    }
  },
  QMODIFY,
  APPROVALING,
  REJECT,
  COMPLETE,
  TICKETCANCEL,
  /**
  * 宋建国：2019-06-28 01:47:38
  * 描述：检测状态是否可以编辑
   * disabled=true代表按钮禁用，false代表按钮不禁用
  */
  checkDisabled(state,callback){
    let disabled = false;
    switch (state) {
      case -1: //"编辑中";
        disabled = false;
        break;
      case 0: //"已发起审批";
        disabled = true;
        break;
      case 1: //"审批中";
        disabled = true;
        break;
      case 2: //"重新发起";
        disabled = true;
        break;
      case 4: //"沟通"
        disabled = true;
        break;
      case 5: //"签提交";
        disabled = true;
        break;
      case 6: //"驳回";
        disabled = false;
        break;
      case 7: //"作废";
        disabled = true;
        break;
      case 8: //"转办"
        disabled = true;
        break;
      case 9: //"流程结束"
        disabled = true;
        break;
      case 10: //"重设责任人"
        disabled = true;
        break;
      case 11: //"发起人指定责任人"
        disabled = true;
        break;
      case 12: //"提醒处理";
        disabled = true;
        break;
      case 13: //"草稿";
        disabled = false;
        break;
      case 14:  // "暂存";
        disabled = false;
        break;
      case 15: //"已办中追加";
        disabled = true;
        break;
      case 18: //"回收";
        disabled = true;
        break;
      case 19: //"唤醒";
        disabled = true;
        break;
      case 20: //"删除";
        disabled = true;
        break;
      case 21:  //"已办抄送";
        disabled = true;
        break;
      case 22: // "跳转";
        disabled = true;
        break;
      case 24: //"带条件审批";
        disabled = true;
        break;
      case 25:  //"查看抄送待办";
        disabled = true;
        break;
      default:
        disabled = true;
    }
    callback(disabled);
  }
}
