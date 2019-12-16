/**
 *宋建国：2019/8/9 18:53
 *描述：工具函数
 */

export default {
  /**
   *宋建国：2019/8/9 18:57
   *描述：判断变量是否为空
   *版本：1.0
   */
  isBlank: function(value) {
    if (value == "" || value == null || value == undefined) { // "",null,undefined
      return true;
    } else {
      return false;
    }
  },
  /**
   *宋建国：2019/8/14 12:33
   *描述：判断Object是否为空
   *版本：1.0
   */
  isObjectBlank: function(value){
    for(var item in value){
      return false;
    }
    return true;
  },
  /**
   *宋建国：2019/8/11 23:26
   *描述：通用处理下载文件函数
   *版本：1.0
   */
  downloadfile: function (res) {
    // 处理返回的文件流
    const blob = new Blob([res.data], {
      type: "application/vnd.ms-excel;charset=utf-8"
    });
    if ("download" in document.createElement("a")) {
      // 非IE下载
      const elink = document.createElement("a");
      elink.download = decodeURIComponent(res.filename);
      elink.style.display = "none";
      elink.href = URL.createObjectURL(blob);
      document.body.appendChild(elink);
      elink.click();
      URL.revokeObjectURL(elink.href); // 释放URL 对象
      document.body.removeChild(elink);
    } else {
      // IE10+下载
      navigator.msSaveBlob(blob, decodeURIComponent(res.filename));
    }
  },
  /**
   *王伟东：2019/8/14 17:16
   *描述：验证金额是否正确函数
   *版本：1.0
   * @params value 传递进来的金额
   * @params num 限制的金额长度 无限制请传递 +号
   */
  validateMoney(value, num) {
  		let ele = require("element-ui");
  		if ( !ele ) {
  			console.error(new Error("没有element-ui依赖"));
  			return false;
  		}
		let moneyMax = num ? num : 5
		if ( num == "+" ) {
			moneyMax = Infinity;
		}
		let val = value.toString();
		if(val === "") {
			ele.Message.error("输入金额内容不能为空");
			return false;
		}
		if ( val.indexOf(" ") != -1 ) {
			ele.Message.error("请输入正确的金额类型");
			return false;
		}
		if(isNaN(val)) {
			ele.Message.error("请输入正确的金额类型");
			return false;
		}
		if(val.slice(0, 1) == "0" && val.length != 1) {
			if(val.slice(1, 2) != ".") {
				ele.Message.error("请输入正确的金额类型");
				return false;
			} else if (val.slice(1, 2) == "." && val.length == 2) {
				ele.Message.error("请输入正确的金额类型");
				return false;
			}
		}
		if(val.indexOf(".") != -1) {
			var left = val.split(".")[0];
			var right = val.split(".")[1];
			if(right.length > 2) {
				ele.Message.error("请输入正确的金额类型");
				return false;
			}
			if(left.length > moneyMax) {
				ele.Message.error("金额最大不能超过" + moneyMax + "位");
				return false;
			}
		} else if(val.length > moneyMax) {
			ele.Message.error("金额最大不能超过" + moneyMax + "位");
			return false;
		}
		return true;
	},
	validatePhone(val) { // 验证手机号
		let ele = require("element-ui");
		if ( !ele ) {
			console.error(new Error("没有element-ui依赖"));
			return false;
		}
		if(val == "") {
			ele.Message.error("手机号码不能为空");
			return false;
		}
		if(!/^1(3|5|6|7|8|9)[0-9]{9}$/.test(val)) {
			ele.Message.error("手机号码格式不正确");
			return false;
		}
		return true;
	},
  /**
   *宋建国：2019/11/7 11:35
   *描述：审批状态转换
   *版本：1.0
   */
  applyStateChine: function (row, column, cellValue) {
    switch (cellValue) {
      case '0':
        return "编辑中";
        break;
      case '1':
        return "审批中";
        break;
      case '2':
        return "选择驳回";
        break;
      case '3':
        return "审批完成";
        break;
      case '4':
        return "终止";
        break;
      case '5':
        return "挂起";
        break;
      case '6':
        return "驳回发起人";
        break;
      case '7':
        return "驳回上一级";
        break;
      case '8':
        return "转办";
        break;
      case '9':
        return "选签";
        break;
      default:
        return cellValue;
    }
  },
  /**
   *张俊娜：2019/12/12
   *描述：生成13位唯一随机数
   *版本：1.0
   */
  createRandom:function (){
    return Number(Math.random() + Date.now()).toString().substr(0,13)
  }
}
