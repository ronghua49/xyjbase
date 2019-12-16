import service, {zfSetService} from '../../utils/service'
import config from "@/utils/global.js"
import axios from "../axios";
import Axios from "axios";

/**
 *宋建国：2019/9/6 21:34
 *描述：导出excel接口
 *版本：1.0
 */
export const exportExcel = (data) => {
  let pvar = Object.assign({}, {
      hostUrl:  "",
      url: '/excel/exportExcel',
      method: 'POST',
      responseType: "blob"},
    data);
  pvar["hostUrl"] = data.data.selfDate.hostUrl;
  return axios(pvar);
}
/**
 *宋建国：2019/9/25 13:40
 *描述：根据类型获取单据号
 *版本：1.0
 */
export const createRuleNoByType = (data) =>{
  return axios({
    hostUrl: config.baseUrl,
    url: '/billsRules/createRuleNoByType',
    method: 'get',
    params: data
  });
}
/**
 *宋建国：2019/9/25 13:44
 *描述：根据单据规则ID获取单据号
 *版本：1.0
 */
export const createRuleNo = (data) =>{
  return axios({
    hostUrl: config.baseUrl,
    url: '/billsRules/createRuleNo',
    method: 'get',
    params: data
  });
}


/*基础模块*/
export const xyjPost = data => {
    return service.zwlService.post(data.url,data.data).then((res) => {
        return res.data
    }).catch((error) => {
        return error
    })
}
export const xyjGet = data => {
    return service.zwlService.get(data.url,{params:data.data}).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
export const xyjDel = data => {
    return service.zwlService.delete(data.url,{params:data.data}).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
/*业务模块(订单)*/
export const xyjPostyw = data => {
    return service.yhService.post(data.url,data.data).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
export const xyjGetyw = data => {
    return service.yhService.get(data.url,{params:data.data}).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
export const xyjDelyw = data => {
    return service.yhService.delete(data.url,{params:data.data}).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
/*公共支付模块*/
export const xyjPostzf = data => {
    return service.common.post(data.url,data.data).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
export const xyjGetzf = data => {
    return service.common.get(data.url,{params:data.data}).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}
export const xyjDelzf = data => {
    return service.common.delete(data.url,{params:data.data}).then((res) => {
        return res.data
    }).catch((error) => {
        return error.response.data
    })
}


//支付设置、支付信息表、支付宝微信支付设置表
export const xyjPostPay = data => {
  return service.zfSetService.post(data.url,data.data).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
export const xyjGetPay = data => {
  return service.zfSetService.get(data.url,{params:data.data}).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
export const xyjDelPay = data => {
  return service.zfSetService.delete(data.url,{params:data.data}).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
/*活动品牌*/
export const xyjActivePost = data => {
  return service.activeService.post(data.url,data.data).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
export const xyjActiveGet = data => {
  return service.activeService.get(data.url,{params:data.data}).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
export const xyjActiveDel = data => {
  return service.activeService.delete(data.url,{params:data.data}).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
/*查询*/
export const xyjQueryPost = data => {
  return service.queryService.post(data.url,data.data).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
export const xyjQueryGet = data => {
  return service.queryService.get(data.url,{params:data.data}).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
/*查询2*/
export const xyjClonePost = data => {
  return service.cloneService.post(data.url,data.data).then((res) => {
    return res.data
  }).catch((error) => {
    return error.response.data
  })
}
