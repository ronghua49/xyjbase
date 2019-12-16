/**
 * 全局常量、方法封装模块
 * 通过原型挂载到Vue属性
 * 通过 this.Global 调用
 */

import Vue from 'vue'
import jquery from 'jquery';
jquery.ajax({
  url: location.origin+'/static/config.json',
  type: 'GET', //GET
  async: false,    //或false,是否异步
  timeout: 10000,    //超时时间
  success: function (data, textStatus, jqXHR) {
    console.log(data)
    for(let key in data){
      Vue.prototype[key] = data[key]
    }
  },
  error: function (xhr, textStatus) {
    console.error(textStatus);
  }
});

export const baseUrl = Vue.prototype.baseUrl
export const hostUrl = Vue.prototype.hostUrl
export const hostUrl85 = Vue.prototype.hostUrl85
export const hostUrl86 = Vue.prototype.hostUrl86

/*审批流*/
export const workflow = Vue.prototype.workflow;
/*门禁*/
export const doorUrl = Vue.prototype.doorUrl;
/*防盗管理系统*/
export const alarmUrl = Vue.prototype.alarmUrl;
export const partplatz = Vue.prototype.partplatz;
/* 项目名称 */
export const appName = Vue.prototype.appName;
/**
 *宋建国：2019/11/24 0:45
 *描述：websocket配置
 *版本：1.0
 */
export const websocket = Vue.prototype.websocket;
/**
 *宋建国：2019/12/6 17:36
 *描述：消息聊天
 *版本：1.0
 */
export const xyjim = Vue.prototype.xyjim;

export default {
  baseUrl,
  hostUrl85,
  hostUrl86,
  workflow,
  doorUrl,
  alarmUrl,
  partplatz,
  websocket,
  xyjim
}
