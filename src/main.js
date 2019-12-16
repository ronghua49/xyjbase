import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import router from './router'
import api from './http'
import i18n from './i18n'
import store from './store'
import global from '@/utils/global'
import TIPSINFO from '@/utils/tipsInfo'
import toolutil from '@/utils/toolutil'
import ElementUI from 'element-ui'
import XyjTable from '@/components/xyjtable/xyjTable';
import XyjTableWorkFlow from '@/components/xyjtable/xyjTableWorkFlow';
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'
import '@/assets/iconfont/iconfont.css'
import XyjButton from "@/components/XyjButton/XyjButton"
import XyjUpload from "@/components/XyjUpload/XyjUpload"
import ApprovalDialog from "@/components/bcomponents/approvalDialog";
import WorkFlowClient from "@/components/WorkFlow/WorkFlowClient";
import Echarts from 'echarts'
import '@/utils/directives'
import '@/utils/drag'
import 'echarts/lib/chart/line'
import upload from "@/views/upload/upload";
import download from "@/views/upload/download";
//import FormMaking from '@/components/vueformmaking/index.js'
//Vue.use(FormMaking)
import mqtt from '@/mqtt/mqtt';
import topic from "@/mqtt/topics";

Vue.use(ElementUI)
Vue.use(api);
Vue.component("XyjTable", XyjTable);
Vue.component("XyjTableWorkFlow", XyjTableWorkFlow);
Vue.component('XyjButton', XyjButton);
Vue.component('XyjUpload', XyjUpload);
Vue.component('ApprovalDialog', ApprovalDialog);
Vue.component("WorkFlowClient", WorkFlowClient); //工作流客户端
Vue.component("upload",upload);//导入模板
Vue.component("download",download);//下载模板


Vue.prototype.global = global;
Vue.prototype.$echarts = Echarts;
Vue.prototype.$TINFO = TIPSINFO;
Vue.prototype.$tu = toolutil;
/**
 *宋建国：2019/11/28 16:58
 *描述：消息通行自定义主题协议
 *版本：1.0
 */
Vue.prototype.$topic = topic;
Vue.prototype.$bus = new Vue(); // event Bus 用于无关系组件间的通信。
let tmpmqtt = new mqtt.createMqtt(global.websocket.hostIp,global.websocket.Port);
Vue.prototype.$mqtt = tmpmqtt;
window.$mqtt = tmpmqtt;
new Vue({
  el: '#app',
  i18n,
  router,
  store,
  render: h => h(App)
});

