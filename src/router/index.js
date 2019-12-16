import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login'
import SubsidiaryLogin from '@/views/SubsidiaryLogin'
import index from '@/views/Wisdom/home'
import NotFound from '@/views/Error/404'
import Home from '@/views/Home'
import store from '@/store'
import api from '@/http/api';
import { getIFramePath, getIFrameUrl } from '@/utils/iframe'
Vue.use(Router)
const router = new Router({
	mode: 'hash',
  routes: [
    {
      path: '/',
      name: '首页',
      component: Home,
      children: []
    },
    {
      path: '/Login',
      name: '登录',
      component: Login
    },
    {
      path: '/SubsidiaryLogin',
      name: '子公司登录',
      component: SubsidiaryLogin
    },
    {
      path: '/404',
      name: 'notFound',
      component: NotFound
    },
    {
      path: '/index',
      name: 'index',
      component: index
    }
  ],
})

let then = this;
router.beforeEach((to, from, next) => {
  // 登录界面登录成功之后，会把用户信息保存在会话
  // 存在时间为会话生命周期，页面关闭即失效。
  let userName = sessionStorage.getItem('user')
  let loginURL = window.location.href.split('#')[1] == '/Login' ? '/Login'
: window.location.href.split('#')[1] == '/SubsidiaryLogin' ? '/SubsidiaryLogin' : '/Login';
  if(sessionStorage.getItem('branchCode')) loginURL = '/SubsidiaryLogin';
  if (to.path === loginURL) {
    // 如果是访问登录界面，如果用户会话信息存在，代表已登录过，跳转到主页
    if(userName) {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    if (!userName) {
      // 如果访问非登录界面，且户会话信息不存在，代表未登录，则跳转到登录界面
      next({ path: loginURL })
    } else {
      // 加载动态菜单和路由
      addDynamicMenuAndRoutes(userName, to, from);
      next();

      /**
       *宋建国：2019/11/25 15:14
       *描述：消息服务器连接
       *版本：1.0
       */
      if(!window.$mqtt.getClient()){
        window.$mqtt.xyjConnect();
      }else{
        if(!window.$mqtt.getClient().isConnected){
          window.$mqtt.xyjConnect();
        }else if(window.$mqtt.getUserName() !== userName){
        	window.$mqtt.disconnect();
        	window.$mqtt.xyjConnect();
        }
      }
    }
  }

})

function selfaddRoutes (params){
  router.matcher = new Router().matcher;
  router.addRoutes(params)
}

/**
 * 加载动态菜单和路由
 */
function addDynamicMenuAndRoutes(userName, to, from) {
  // store.commit('updateKey', Math.ceil(Math.random()*10));
  // 处理IFrame嵌套页面
  handleIFrameUrl(to.path);
  if(store.state.app.menuRouteLoaded) {
    console.log('动态菜单和路由已经存在.')
    return
  }
  let axiosurl = sessionStorage.getItem('branchCode') ? api.menu.findBranchMenu : api.menu.findNavTree
  axiosurl({'userName':userName}).then(res => {
    // 添加动态路由
    let dynamicRoutes = addDynamicRoutes(res.data)
    router.options.routes[0].children = router.options.routes[0].children.concat(dynamicRoutes)
    selfaddRoutes(router.options.routes)
    // 保存加载状态
    store.commit('menuRouteLoaded', true)
    // 保存菜单树
    store.commit('setNavTree', res.data)
  }).then(res => {
	sessionStorage.getItem('branchCode')?
	api.user.findBranchPermissions({
      'name':userName,
    }).then(res => {
      // 保存用户权限标识集合
      store.commit('setPerms', res.data)
    })
    : api.user.findPermissions({
      'name':userName,
    }).then(res => {
      // 保存用户权限标识集合
      store.commit('setPerms', res.data)
    })
  }).catch(err => {
  })
}

/**
 * 处理IFrame嵌套页面
 */
function handleIFrameUrl(path) {
  // 嵌套页面，保存iframeUrl到store，供IFrame组件读取展示
  let url = path
  let length = store.state.iframe.iframeUrls.length
  for(let i=0; i<length; i++) {
    let iframe = store.state.iframe.iframeUrls[i]
    if(path != null && path.endsWith(iframe.path)) {
      url = iframe.url
      store.commit('setIFrameUrl', url)
      break
    }
  }
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function addDynamicRoutes (menuList = [], routes = []) {
  var temp = []
  let keepalive = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].children && menuList[i].children.length >= 1) {
      for(var j = 0;j<menuList[i].children.length;j++){
        menuList[i].children[j].parentName = menuList[i].name;
      }
      temp = temp.concat(menuList[i].children)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      // 获取所有组件的name值
      keepalive.push(menuList[i].url.split('/')[menuList[i].url.split('/').length-1]);
      // 创建路由配置
      var route = {
        path: menuList[i].url,
        component: null,
        name: menuList[i].name,
        meta: {
          icon: menuList[i].icon,
          parentName:menuList[i].parentName,
          processKey:menuList[i].cid,
          processName:menuList[i].processName
        }
      }
      let path = getIFramePath(menuList[i].url)
      if (path) {
        // 如果是嵌套页面, 通过iframe展示
        route['path'] = path
        route['component'] = resolve => require([`@/views/IFrame/IFrame`], resolve)
        // 存储嵌套页面路由路径和访问URL
        let url = getIFrameUrl(menuList[i].url)
        let iFrameUrl = {'path': path, 'url': url}
        store.commit('addIFrameUrl', iFrameUrl)
      } else {
        try {
          // 根据菜单URL动态加载vue组件，这里要求vue组件须按照url路径存储
          // 如url="sys/user"，则组件路径应是"@/views/sys/user.vue",否则组件加载不到
          let array = menuList[i].url.split('/')
          let url = ''
          for (let i = 0; i < array.length; i++) {
            url += array[i].substring(0, 1).toUpperCase() + array[i].substring(1) + '/'
          }
          url = url.substring(0, url.length - 1)
          route['component'] = resolve => require([`@/views/${url}`], resolve)
        } catch (e) {
        }
      }
      routes.push(route)
    }
  }
  // 存储需要缓存的组件
  store.commit('updateKey', keepalive)
  if (temp.length >= 1) {
    addDynamicRoutes(temp, routes)
  } else {
    console.log('动态路由加载完成.')
  }
  return routes;
}
export default router
